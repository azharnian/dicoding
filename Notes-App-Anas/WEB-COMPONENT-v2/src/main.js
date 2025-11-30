import './styles/app.css';
import { createIcons, icons } from 'lucide';
import './utils/bus.js';
import './services/storage.js';

// Components
import './components/app-loader.js';
import './components/app-toaster.js';
import './components/app-sidebar.js';
import './components/color-picker.js';
import './components/mobile-footer.js';
import './components/note-card.js';
import './components/note-modal.js';
import './components/archive-modal.js';
import './components/notes-app.js';

// import { bootstrapFromSeed } from './services/storage.js';
// import { notesData } from './data/seed.js';
// bootstrapFromSeed(notesData);

document.addEventListener('DOMContentLoaded', () => {
  createIcons({ icons });
});
