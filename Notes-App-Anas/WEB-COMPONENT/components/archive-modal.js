import { on } from '../utils/bus.js';

class ArchiveModal extends HTMLElement {
  connectedCallback() {
    this.innerHTML = `
      <div class="modal" id="backdrop">
        <div class="modal-content">
          <h3>Archived Notes</h3>
          <div class="notes-grid" id="list"></div>
          <div class="modal-actions">
            <button class="modal-btn" id="close">Close</button>
          </div>
        </div>
      </div>
    `;
    this.backdrop = this.querySelector('#backdrop');
    this.list = this.querySelector('#list');
    this.querySelector('#close').addEventListener('click', () => this.toggle());

    on('toggle-archive', () => this.toggle());
    on('archive:render', ({ nodes }) => {
      this.list.innerHTML = '';
      nodes.forEach(n => this.list.appendChild(n));
    });
  }
  toggle(){ this.backdrop.classList.toggle('active'); }
}
customElements.define('archive-modal', ArchiveModal);
