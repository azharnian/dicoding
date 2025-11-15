import { emit } from '../utils/bus.js';

class AppSidebar extends HTMLElement {
  connectedCallback() {
    this.innerHTML = `
      <aside class="sidebar">
        <h1>Notes</h1>
        <button class="primary" id="new">+ New Note</button>
        <button class="link" id="archive">View Archive</button>
      </aside>
    `;
    this.querySelector('#new').addEventListener('click', () =>
      emit('open-note-modal')
    );
    this.querySelector('#archive').addEventListener('click', () =>
      emit('toggle-archive')
    );
  }
}
customElements.define('app-sidebar', AppSidebar);
