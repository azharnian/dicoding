import { emit, on } from '../utils/bus.js';

class NoteModal extends HTMLElement {
  connectedCallback() {
    this.innerHTML = `
      <div class="modal" id="backdrop">
        <div class="modal-content">
          <h3>New Note</h3>
          <input id="title" type="text" placeholder="Title"/>
          <div id="title-error" class="input-error"></div>

          <textarea id="content" placeholder="Write something..."></textarea>
          <div id="content-error" class="input-error"></div>

          <div>
            <label>Pilih Warna</label>
            <color-picker id="picker" value="orange"></color-picker>
          </div>
          <div class="modal-actions">
            <button class="modal-btn primary" id="save">
              <i data-lucide="save" class="icon"></i><span>Save</span>
            </button>
            <button class="modal-btn" id="cancel">
              <i data-lucide="x-circle" class="icon"></i><span>Cancel</span>
            </button>
          </div>
        </div>
      </div>
    `;

    this.backdrop = this.querySelector('#backdrop');
    this.titleEl = this.querySelector('#title');
    this.contentEl = this.querySelector('#content');
    this.picker = this.querySelector('#picker');

    this.titleError = this.querySelector('#title-error');
    this.contentError = this.querySelector('#content-error');

    this.titleEl.addEventListener('input', () => {
      this.validateField(this.titleEl, this.titleError, 'Title is required.');
    });

    this.contentEl.addEventListener('input', () => {
      this.validateField(this.contentEl, this.contentError, 'Content is required.');
    });

    this.querySelector('#save').addEventListener('click', () => {
      const isTitleValid = this.validateField(this.titleEl, this.titleError, 'Title is required.');
      const isContentValid = this.validateField(this.contentEl, this.contentError, 'Content is required.');

      if (!isTitleValid || !isContentValid) return;

      const payload = {
        id: Date.now(),
        title: this.titleEl.value.trim(),
        content: this.contentEl.value.trim(),
        color: this.picker.value,
        date: new Date().toLocaleDateString(),
        archived: false
      };
      emit('note:create', payload);
      this.close();
    });

    this.querySelector('#cancel').addEventListener('click', () => this.close());

    on('open-note-modal', () => this.open());
    const ensureIcons = () => window.lucide?.createIcons && lucide.createIcons();
    this.addEventListener('open', ensureIcons);
  }

  validateField(inputEl, errorEl, message) {
    if (inputEl.value.trim() === '') {
      errorEl.textContent = message;
      return false;
    } else {
      errorEl.textContent = '';
      return true;
    }
  }

  open() {
    this.backdrop.classList.add('active');
    this.dispatchEvent(new Event('open'));
  }

  close() {
    this.backdrop.classList.remove('active');
    this.titleEl.value = '';
    this.contentEl.value = '';
    this.picker.value = 'orange';
    this.picker.render();
    this.titleError.textContent = '';
    this.contentError.textContent = '';
  }
}

customElements.define('note-modal', NoteModal);
