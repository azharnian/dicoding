import { emit } from '../utils/bus.js';

class MobileFooter extends HTMLElement {
  connectedCallback() {
    this.innerHTML = `
      <footer class="mobile-footer">
        <button class="footer-btn" title="Add Note" id="add">
          <i data-lucide="plus-circle" class="icon"></i>
        </button>
        <button class="footer-btn" title="Archive" id="archive">
          <i data-lucide="archive" class="icon"></i>
        </button>
      </footer>
    `;
    this.querySelector('#add').addEventListener('click', () => emit('open-note-modal'));
    this.querySelector('#archive').addEventListener('click', () => emit('toggle-archive'));
  }
}
customElements.define('mobile-footer', MobileFooter);
