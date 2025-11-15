document.addEventListener("DOMContentLoaded", () => {
  lucide.createIcons();
  
  const notesContainer = document.getElementById("notesContainer");
  const archivedContainer = document.getElementById("archivedContainer");

  const noteModal = document.getElementById("noteModal");
  const archiveModal = document.getElementById("archiveModal");

  const searchInput = document.getElementById("searchInput");
  const noteTitle = document.getElementById("noteTitle");
  const noteContent = document.getElementById("noteContent");
  const noteColor = document.getElementById("noteColor");

  const colorPicker = document.getElementById("colorPicker");

  // Event binding
  document.getElementById("addNoteBtnMobile").addEventListener("click", openNoteModal);
  document.getElementById("toggleArchiveBtnMobile").addEventListener("click", toggleArchiveModal);
  document.getElementById("addNoteBtnSidebar").addEventListener("click", openNoteModal);
  document.getElementById("toggleArchiveBtnSidebar").addEventListener("click", toggleArchiveModal);
  document.getElementById("saveNoteBtn").addEventListener("click", saveNote);
  document.getElementById("cancelNoteBtn").addEventListener("click", closeNoteModal);
  document.getElementById("closeArchiveBtn").addEventListener("click", toggleArchiveModal);
  searchInput.addEventListener("input", renderNotes);

  // Color picker event
  colorPicker.querySelectorAll("span").forEach(span => {
    span.addEventListener("click", () => {
      const color = span.dataset.color;
      noteColor.value = color;
      colorPicker.querySelectorAll("span").forEach(el => el.classList.remove("ringed"));
      span.classList.add("ringed");
    });
  });

  function openNoteModal() {
    noteModal.classList.add("active");
  }

  function closeNoteModal() {
    noteModal.classList.remove("active");
    noteTitle.value = "";
    noteContent.value = "";
  }

  function toggleArchiveModal() {
    archiveModal.classList.toggle("active");
    renderNotes();
  }

  function getNotes() {
    return JSON.parse(localStorage.getItem("notes") || "[]");
  }

  function saveNote() {
    const note = {
      id: Date.now(),
      title: noteTitle.value,
      content: noteContent.value,
      color: noteColor.value,
      date: new Date().toLocaleDateString(),
      archived: false
    };
    const notes = getNotes();
    notes.push(note);
    localStorage.setItem("notes", JSON.stringify(notes));
    closeNoteModal();
    renderNotes();
  }

  function deleteNote(id) {
    const notes = getNotes().filter(note => note.id !== id);
    localStorage.setItem("notes", JSON.stringify(notes));
    renderNotes();
  }

  function archiveNote(id) {
    const notes = getNotes().map(note =>
      note.id === id ? { ...note, archived: true } : note
    );
    localStorage.setItem("notes", JSON.stringify(notes));
    renderNotes();
  }

  function unarchiveNote(id) {
    const notes = getNotes().map(note =>
      note.id === id ? { ...note, archived: false } : note
    );
    localStorage.setItem("notes", JSON.stringify(notes));
    renderNotes();
    archiveModal.classList.remove("active");
  }

  function renderNotes() {
    const notes = getNotes();
    const query = searchInput.value.toLowerCase();
    notesContainer.innerHTML = "";
    archivedContainer.innerHTML = "";

    notes.forEach(note => {
      const match =
        note.title.toLowerCase().includes(query) ||
        note.content.toLowerCase().includes(query);

      const noteEl = document.createElement("div");
      noteEl.className = `note note-color-${note.color}`;
      noteEl.innerHTML = `
        <h4>${note.title}</h4>
        <p>${note.content}</p>
        <p class="text-xs mt-1">${note.date}</p>
        <div class="note-actions">
          ${
            note.archived
              ? `<button class="unarchive">Unarchive</button>`
              : `<button class="archive">Archive</button>`
          }
          <button class="delete">Delete</button>
        </div>
      `;

      const archiveBtn = noteEl.querySelector(".archive");
      const unarchiveBtn = noteEl.querySelector(".unarchive");
      const deleteBtn = noteEl.querySelector(".delete");

      if (archiveBtn) archiveBtn.addEventListener("click", () => archiveNote(note.id));
      if (unarchiveBtn) unarchiveBtn.addEventListener("click", () => unarchiveNote(note.id));
      if (deleteBtn) deleteBtn.addEventListener("click", () => deleteNote(note.id));

      if (match) {
        (note.archived ? archivedContainer : notesContainer).appendChild(noteEl);
      }
    });
  }

  // Init
  renderNotes();
  noteColor.value = "orange";
  colorPicker.querySelector("[data-color='orange']").classList.add("ringed");
});
