import { emit, on } from '../utils/bus.js';
import { getNotes, addNote, updateNote, deleteNote } from '../services/storage.js';

function formatDate(iso) {
  const d = new Date(iso);
  return isNaN(d.getTime()) ? '' : d.toLocaleDateString();
}

class NotesApp extends HTMLElement {
  connectedCallback() {
    this.innerHTML = `
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

    this.search.addEventListener('input', () => this.render());

    on('note:create', (note) => { addNote(note); this.render(); });
    on('note:delete', ({ id }) => { deleteNote(id); this.render(); });
    on('note:archive', ({ id }) => { updateNote(id, { archived: true }); this.render(); emit('toggle-archive'); });
    on('note:unarchive', ({ id }) => { updateNote(id, { archived: false }); this.render(); emit('toggle-archive'); });

    this.render();
  }

  render() {
    const q = (this.search.value || '').toLowerCase();
    const notes = getNotes();

    this.grid.innerHTML = '';

    const activeNodes = [];
    const archivedNodes = [];

    notes.forEach(n => {
      const match = (n.title || '').toLowerCase().includes(q) || (n.content || '').toLowerCase().includes(q);
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

    activeNodes.forEach(n => this.grid.appendChild(n));
    emit('archive:render', { nodes: archivedNodes });
  }
}
customElements.define('notes-app', NotesApp);
