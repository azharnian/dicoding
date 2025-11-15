import { emit, on } from '../utils/bus.js';
import {
  getNotes,
  addNote,
  updateNote,
  deleteNote,
  syncFromAPI,
} from '../services/storage.js';
import './app-loader.js';
import './app-toaster.js';

function formatDate(iso) {
  const d = new Date(iso);
  return isNaN(d.getTime()) ? '' : d.toLocaleDateString();
}

class NotesApp extends HTMLElement {
  connectedCallback() {
    this.innerHTML = `
      <app-toaster id="toaster"></app-toaster>
      <app-loader id="appLoader"></app-loader>
      <mobile-footer></mobile-footer>
      <div class="app-container">
        <app-sidebar></app-sidebar>
        <main class="main-content">
          <input id="search" type="text" placeholder="Cari catatan..." class="search-input"/>
          <div id="grid" class="notes-grid"></div>
        </main>
      </div>

      <note-modal></note-modal>
      <archive-modal></archive-modal>
    `;
    this.grid = this.querySelector('#grid');
    this.search = this.querySelector('#search');
    this.loader = this.querySelector('#appLoader');

    this.search.addEventListener('input', () => this.render());

    on('note:create', (note) => {
      addNote(note);
    });
    on('note:delete', ({ id }) => {
      deleteNote(id);
    });
    on('note:archive', ({ id }) => {
      updateNote(id, { archived: true });
      emit('toggle-archive');
    });
    on('note:unarchive', ({ id }) => {
      updateNote(id, { archived: false });
      emit('toggle-archive');
    });

    on('storage:updated', () => this.render());

    this.loader?.show();
    this.render();
    const minDelay = new Promise((resolve) => setTimeout(resolve, 5000));
    const sync = syncFromAPI();
    Promise.all([minDelay, sync]).then(() => {
      this.loader?.hide();
    });

    this.toaster = this.querySelector('#toaster');

    on('toast:error', ({ message }) => {
      this.toaster?.show(message || 'Terjadi kesalahan.', 'error');
    });

    window.addEventListener('online', () => {
      this.toaster.show('Koneksi kembali online', 'success');
    });
    window.addEventListener('offline', () => {
      this.toaster.show('Anda sedang offline', 'error');
    });
  }

  render() {
    const q = (this.search.value || '').toLowerCase();
    const notes = getNotes();

    this.grid.innerHTML = '';

    const activeNodes = [];
    const archivedNodes = [];

    notes.forEach((n) => {
      const match =
        (n.title || '').toLowerCase().includes(q) ||
        (n.content || '').toLowerCase().includes(q);
      if (!match) return;

      const el = document.createElement('note-card');
      el.setAttribute('data-id', n.id);
      el.setAttribute('data-title', n.title || '');
      el.setAttribute('data-content', n.content || '');
      el.setAttribute('data-color', n.color || 'orange');
      el.setAttribute('data-date', formatDate(n.createdAt));
      el.setAttribute('data-archived', String(n.archived));

      (n.archived ? archivedNodes : activeNodes).push(el);
    });

    activeNodes.forEach((n) => this.grid.appendChild(n));
    emit('archive:render', { nodes: archivedNodes });
  }
}
customElements.define('notes-app', NotesApp);
