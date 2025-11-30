class AppToaster extends HTMLElement {
  connectedCallback() {
    this.innerHTML = `<div class="app-toaster" id="toast"></div>`;
    this.toast = this.querySelector('#toast');
  }

  show(message, type = 'success') {
    this.toast.textContent = message;
    this.toast.className = `app-toaster ${type} show`;
    clearTimeout(this._timer);
    this._timer = setTimeout(() => {
      this.toast.classList.remove('show');
    }, 3000);
  }
}
customElements.define('app-toaster', AppToaster);
