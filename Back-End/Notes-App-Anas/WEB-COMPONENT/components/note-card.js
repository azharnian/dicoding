import { emit } from '../utils/bus.js';

class NoteCard extends HTMLElement {
  static get observedAttributes() { return ['data-id','data-title','data-content','data-color','data-date','data-archived']; }
  attributeChangedCallback() { this.render(); }
  connectedCallback() { this.render(); }

  get data() {
    return {
      id: this.getAttribute('data-id'),
      title: this.getAttribute('data-title') || '',
      content: this.getAttribute('data-content') || '',
      color: this.getAttribute('data-color') || 'orange',
      date: this.getAttribute('data-date') || '',
      archived: this.getAttribute('data-archived') === 'true',
    };
  }

  render() {
    const { id, title, content, color, date, archived } = this.data;
    this.innerHTML = `
      <div class="note note-color-${color}">
        <h4>${title || '(untitled)'}</h4>
        <p>${content || ''}</p>
        <p style="font-size:.75rem;margin-top:.25rem">${date}</p>
        <div class="note-actions">
          ${archived
            ? `<button class="unarchive">Unarchive</button>`
            : `<button class="archive">Archive</button>`}
          <button class="delete">Delete</button>
        </div>
      </div>
    `;
    this.querySelector('.delete')?.addEventListener('click', () => emit('note:delete', { id }));
    this.querySelector('.archive')?.addEventListener('click', () => emit('note:archive', { id }));
    this.querySelector('.unarchive')?.addEventListener('click', () => emit('note:unarchive', { id }));
  }
}
customElements.define('note-card', NoteCard);
