export const bus = new EventTarget();

export function emit(type, detail = {}) {
  bus.dispatchEvent(new CustomEvent(type, { detail, bubbles: false }));
}

export function on(type, handler) {
  const listener = (e) => handler(e.detail, e);
  bus.addEventListener(type, listener);
  return () => bus.removeEventListener(type, listener);
}
