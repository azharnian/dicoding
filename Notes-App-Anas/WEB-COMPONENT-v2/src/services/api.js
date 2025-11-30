const API_BASE = 'https://notes-api.dicoding.dev/v2';

function toJson(res) {
  return res
    .json()
    .catch(() => ({}))
    .then((body) => ({ res, body }));
}

function http(method, path, payload) {
  const opts = { method, headers: { 'Content-Type': 'application/json' } };
  if (payload != null) opts.body = JSON.stringify(payload);

  return fetch(`${API_BASE}${path}`, opts)
    .then(toJson)
    .then(({ res, body }) => {
      if (res.ok) return body;
      const err = new Error(
        body?.message || body?.error || `Request failed (${res.status})`
      );
      err.status = res.status;
      err.code = body?.code || body?.status || 'ERROR';
      err.body = body;
      throw err;
    })
    .catch((e) => {
      if (!(e instanceof Error)) {
        const err = new Error('Network error');
        err.code = 'NETWORK_ERROR';
        throw err;
      }
      if (!e.code) e.code = 'REQUEST_ERROR';
      throw e;
    });
}

// ===== Notes API =====
export function apiListActive() {
  return http('GET', '/notes').then((r) => r?.data || []);
}
export function apiListArchived() {
  return http('GET', '/notes/archived').then((r) => r?.data || []);
}
export function apiCreateNote({ title, body }) {
  return http('POST', '/notes', { title, body }).then((r) => r?.data);
}
export function apiDeleteNote(id) {
  return http('DELETE', `/notes/${id}`);
}
export function apiArchiveNote(id) {
  return http('POST', `/notes/${id}/archive`);
}
export function apiUnarchiveNote(id) {
  return http('POST', `/notes/${id}/unarchive`);
}
