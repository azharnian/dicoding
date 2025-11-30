const COLORS = ['orange','yellow','purple','blue','green'];

class ColorPicker extends HTMLElement {
  connectedCallback() {
    this.value = this.getAttribute('value') || 'orange';
    this.render();
  }
  setValue(v) {
    this.value = v;
    this.dispatchEvent(new CustomEvent('change', { detail: { value: v }}));
    this.render();
  }
  render() {
    this.innerHTML = `
      <div class="color-picker">
        ${COLORS.map(c => `<span data-color="${c}" class="note-color-${c} ${this.value===c?'ringed':''}"></span>`).join('')}
      </div>
      <input type="hidden" name="color" value="${this.value}"/>
    `;
    this.querySelectorAll('span').forEach(el=>{
      el.addEventListener('click',()=>{
        this.setValue(el.dataset.color);
      });
    });
  }
}
customElements.define('color-picker', ColorPicker);
