const KEY = 'notes';
const DEFAULT_COLOR = 'orange';

function toISODateFromLocaleString(s) {
  const d = new Date(s);
  return isNaN(d.getTime()) ? new Date().toISOString() : d.toISOString();
}

export function normalizeNote(raw = {}) {
  const id = (raw.id ?? String(Date.now())).toString();
  const title = raw.title ?? '';
  const content = raw.content ?? raw.body ?? '';
  let createdAt = raw.createdAt ?? raw.date;
  if (!createdAt) createdAt = new Date().toISOString();
  if (typeof createdAt !== 'string' || createdAt.length < 10) {
    createdAt = toISODateFromLocaleString(createdAt);
  }
  const archived = Boolean(raw.archived ?? false);
  const color = raw.color ?? DEFAULT_COLOR;

  return { id, title, content, createdAt, archived, color };
}

export function getNotes() {
  let data = [];
  try {
    data = JSON.parse(localStorage.getItem(KEY) || '[]');
  } catch {
    data = [];
  }
  const normalized = (Array.isArray(data) ? data : []).map(normalizeNote);

  localStorage.setItem(KEY, JSON.stringify(normalized));
  return normalized;
}

export function setNotes(notes) {
  const normalized = (Array.isArray(notes) ? notes : []).map(normalizeNote);
  localStorage.setItem(KEY, JSON.stringify(normalized));
}

export function addNote(note) {
  const n = normalizeNote({
    ...note,
    createdAt: note?.createdAt ?? new Date().toISOString(),
    color: note?.color ?? DEFAULT_COLOR,
  });
  const notes = getNotes();
  notes.push(n);
  setNotes(notes);
  return n;
}

export function updateNote(id, patch) {
  const notes = getNotes().map(n => n.id === String(id) ? normalizeNote({ ...n, ...patch, id: String(id) }) : n);
  setNotes(notes);
}

export function deleteNote(id) {
  const notes = getNotes().filter(n => n.id !== String(id));
  setNotes(notes);
}

export function bootstrapFromSeed(seed = []) {
  try {
    const existing = JSON.parse(localStorage.getItem(KEY) || '[]');
    if (Array.isArray(existing) && existing.length > 0) return;
  } catch {}

  const normalized = (Array.isArray(seed) ? seed : []).map(normalizeNote);
  localStorage.setItem(KEY, JSON.stringify(normalized));
}

