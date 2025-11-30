import { emit } from '../utils/bus.js';
import {
  apiListActive,
  apiListArchived,
  apiCreateNote,
  apiArchiveNote,
  apiUnarchiveNote,
  apiDeleteNote,
} from './api.js';

const KEY = 'notes';
const QUEUE_KEY = 'notesQueue';
const DEFAULT_COLOR = 'orange';

function isOnline() {
  return typeof navigator !== 'undefined' ? navigator.onLine : true;
}
function readQueue() {
  try {
    return JSON.parse(localStorage.getItem(QUEUE_KEY) || '[]');
  } catch {
    return [];
  }
}
function writeQueue(q) {
  localStorage.setItem(QUEUE_KEY, JSON.stringify(q));
}
function enqueue(op) {
  const q = readQueue();
  q.push(op);
  writeQueue(q);
}

export function normalizeNote(raw = {}) {
  const id = (raw.id ?? String(Date.now())).toString();
  const title = raw.title ?? '';
  const content = raw.content ?? raw.body ?? '';
  let createdAt = raw.createdAt ?? raw.date ?? new Date().toISOString();
  if (typeof createdAt !== 'string' || createdAt.length < 10)
    createdAt = new Date(createdAt).toISOString();
  const archived = !!raw.archived;
  const color = raw.color || raw.noteColor || DEFAULT_COLOR;
  return { id, title, content, createdAt, archived, color };
}

export function getNotes() {
  try {
    return JSON.parse(localStorage.getItem(KEY) || '[]');
  } catch {
    return [];
  }
}
export function setNotes(notes = []) {
  localStorage.setItem(KEY, JSON.stringify(notes.map(normalizeNote)));
  emit('storage:updated');
}

function mergeServerNotes(serverActive = [], serverArchived = []) {
  const map = new Map();
  [...serverActive, ...serverArchived]
    .map(normalizeNote)
    .forEach((n) => map.set(n.id, n));

  getNotes().forEach((n) => {
    if (!map.has(n.id)) map.set(n.id, n);
  });

  setNotes(
    [...map.values()].sort(
      (a, b) => new Date(b.createdAt) - new Date(a.createdAt)
    )
  );
}

export function syncFromAPI() {
  if (!isOnline()) return Promise.resolve();
  return Promise.all([apiListActive(), apiListArchived()])
    .then(([active, archived]) => mergeServerNotes(active, archived))
    .catch((e) => {
      const msg = e?.message || 'Gagal menyinkronkan catatan dari server.';
      emit('toast:error', { message: msg });
    });
}

export function processQueue() {
  if (!isOnline()) return Promise.resolve();
  const q = readQueue();
  if (!q.length) return Promise.resolve();

  const run = (i) => {
    if (i >= q.length) {
      writeQueue([]);
      return syncFromAPI();
    }
    const op = q[i];
    let p;
    if (op.type === 'create')
      p = apiCreateNote({ title: op.title, body: op.body });
    else if (op.type === 'archive') p = apiArchiveNote(op.id);
    else if (op.type === 'unarchive') p = apiUnarchiveNote(op.id);
    else if (op.type === 'delete') p = apiDeleteNote(op.id);
    else p = Promise.resolve();

    return p
      .then(() => run(i + 1))
      .catch((e) => {
        emit('toast:error', {
          message:
            e?.message || `Gagal memproses antrian (${op?.type || 'op'}).`,
        });
        return run(i + 1);
      });
  };
  return run(0);
}

export function addNote(input = {}) {
  const temp = normalizeNote(input);
  const notes = getNotes();
  notes.unshift(temp);
  setNotes(notes);

  const payload = { title: temp.title, body: temp.content };
  if (isOnline()) {
    return apiCreateNote(payload)
      .then((serverNote) => {
        const merged = getNotes().map((n) =>
          n.id === temp.id &&
          n.title === temp.title &&
          n.content === temp.content
            ? normalizeNote(serverNote)
            : n
        );
        setNotes(merged);
        return serverNote;
      })
      .catch((e) => {
        emit('toast:error', {
          message: e?.message || 'Gagal membuat catatan di server.',
        });
      });
  } else {
    enqueue({ type: 'create', title: temp.title, body: temp.content });
    return Promise.resolve(temp);
  }
}

export function updateNote(id, patch = {}) {
  const notes = getNotes();
  const idx = notes.findIndex((n) => n.id === String(id));
  if (idx < 0) return;

  const current = notes[idx];
  const next = { ...current, ...patch };
  notes[idx] = next;
  setNotes(notes);

  if (typeof patch.archived === 'boolean') {
    const op = patch.archived ? 'archive' : 'unarchive';
    if (isOnline()) {
      const api = patch.archived ? apiArchiveNote : apiUnarchiveNote;
      return api(id)
        .then(() => syncFromAPI())
        .catch((e) => {
          emit('toast:error', {
            message: e?.message || 'Gagal mengubah status arsip.',
          });
        });
    } else {
      enqueue({ type: op, id });
      return Promise.resolve();
    }
  }
}

export function deleteNote(id) {
  setNotes(getNotes().filter((n) => n.id !== String(id)));

  if (isOnline()) {
    return apiDeleteNote(id)
      .then(() => syncFromAPI())
      .catch((e) => {
        emit('toast:error', {
          message: e?.message || 'Gagal menghapus catatan di server.',
        });
      });
  } else {
    enqueue({ type: 'delete', id });
    return Promise.resolve();
  }
}

export function bootstrapFromSeed(seed = []) {
  try {
    const existing = JSON.parse(localStorage.getItem(KEY) || '[]');
    if (Array.isArray(existing) && existing.length > 0) return;
  } catch {}
  const normalized = (Array.isArray(seed) ? seed : []).map(normalizeNote);
  localStorage.setItem(KEY, JSON.stringify(normalized));
  emit('storage:updated');
}

if (typeof window !== 'undefined') {
  window.addEventListener('online', () => {
    processQueue().then(() => syncFromAPI());
  });
}
