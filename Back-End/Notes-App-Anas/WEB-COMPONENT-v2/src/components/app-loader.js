class AppLoader extends HTMLElement {
  connectedCallback() {
    this.innerHTML = `
      <div class="loader-backdrop">
        <div class="lds-spinner" aria-label="Loading..." role="status">
          <div></div><div></div><div></div><div></div><div></div><div></div>
          <div></div><div></div><div></div><div></div><div></div><div></div>
        </div>
      </div>
    `;
    this.backdrop = this.querySelector('.loader-backdrop');
  }
  show() {
    this.backdrop?.removeAttribute('hidden');
  }
  hide() {
    this.backdrop?.setAttribute('hidden', '');
  }
}
customElements.define('app-loader', AppLoader);
