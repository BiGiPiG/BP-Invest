<template>
  <aside>
    <div class="tracked_menu">
      <div class="tracked_list_header">Tracked shares</div>
      <div v-for="share in trackedShares">
        <div class="trackedShares">{{share}}</div>
      </div>
    </div>
    <div class="profile-wrapper">
      <button id="profile" type="button" @click="toggleMenu" ref="btnRef" :aria-expanded="isOpen.toString()">
        <img src="/images/Profile.png" width="40px" />
        <span>Your profile</span>
      </button>
      <div v-if="isOpen" class="profile-menu" ref="menuRef" :style="{ width: menuWidth + 'px' }">
        <ul>
          <li @click="selectItem('profile')">Profile</li>
          <li @click="selectItem('settings')">Settings</li>
          <li @click="selectItem('logout')">Logout</li>
        </ul>
      </div>
    </div>
  </aside>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount, watch } from 'vue';

const isOpen = ref(false);
const btnRef = ref(null);
const menuRef = ref(null);
const menuWidth = ref(200);

const props = defineProps({
  trackedShares: { type: Array }
});

function toggleMenu() {
  isOpen.value = !isOpen.value;
}

watch(btnRef, (newValue) => {
  if (newValue) {
    menuWidth.value = newValue.offsetWidth;
  }
});

function selectItem(action) {
  isOpen.value = false;
  switch(action) {
    case 'logout':
      logout();
      break;
    case "settings":
      openSettings();
      break;
    case 'profile':
      openProfile();
      break;
  }
}

function handleClickOutside(event) {
  const buttonEl = btnRef.value;
  const menuEl = menuRef.value;
  if (!buttonEl || !menuEl) return;
  if (!buttonEl.contains(event.target) && !menuEl.contains(event.target)) {
    isOpen.value = false;
  }
}

function logout() {
  localStorage.removeItem('token');
  window.location.href = '/login'
}

function openSettings() {
  console.log('Opening settings...');
}

function openProfile() {
  console.log('Opening profile...');
}

onMounted(() => {
  document.addEventListener('click', handleClickOutside);
  if (btnRef.value) {
    menuWidth.value = btnRef.value.offsetWidth;
  }
});

onBeforeUnmount(() => {
  document.removeEventListener('click', handleClickOutside);
});
</script>

<style scoped>
@import url('https://fonts.googleapis.com/css2?family=Alfa+Slab+One&family=Inter:ital,opsz,wght@0,14..32,100..900;1,14..32,100..900&display=swap');

.tracked_menu {
  width: 100%;
  display: flex;
  flex-direction: column;
}

.tracked_list_header {
  display: flex;
  justify-content: center;
  font-family: "Inter", sans-serif;
  font-weight: 600;
  font-size: 1.5rem;
  color: #fff;
  margin-bottom: 3%;
}

.trackedShares {
  font-family: "Inter", sans-serif;
  font-weight: 400;
  font-size: 1rem;
  color: #fff;
  height:40px;
  text-align: center;
  line-height: 40px;
}

.trackedShares:hover {
  background: linear-gradient(145deg, #32394f, #2a3044);
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.25);
}

aside {
  position: relative;
  display: flex;
  flex-direction: column;
  min-height: 100%;
}

.profile-wrapper {
  position: absolute;
  bottom: 20px;
  left: 0;
  right: 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 0 20px;
}

#profile {
  all: unset;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  font-family: "Inter", sans-serif;
  font-weight: 600;
  font-size: 1rem;
  color: #fff;
  cursor: pointer;
  outline: none;
  border-radius: 12px;
  background: linear-gradient(145deg, #2a3044, #232837);
  height: 60px;
  padding: 0 20px;
  width: 100%;
  max-width: 280px;
  transition: all 0.3s ease;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
  border: 1px solid rgba(255, 255, 255, 0.1);
}

#profile:hover {
  background: linear-gradient(145deg, #32394f, #2a3044);
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.25);
}

#profile:active {
  transform: translateY(0);
}

#profile:focus {
  outline: 2px solid #4a72ff;
  outline-offset: 2px;
}

.profile-menu {
  position: absolute;
  bottom: calc(100% + 10px);
  left: 50%;
  transform: translateX(-50%);
  background: #1f2331;
  border-radius: 12px;
  color: #e5e7eb;
  z-index: 50;
  font-family: "Inter", sans-serif;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.3);
  border: 1px solid rgba(255, 255, 255, 0.1);
  overflow: hidden;
  animation: slideDown 0.2s ease-out;
}

@keyframes slideDown {
  from {
    opacity: 0;
    transform: translate(-50%, 10px);
  }
  to {
    opacity: 1;
    transform: translate(-50%, 0);
  }
}

.profile-menu ul {
  list-style: none;
  margin: 0;
  padding: 8px;
  width: 100%;
}

.profile-menu li {
  padding: 12px 16px;
  cursor: pointer;
  border-radius: 8px;
  transition: all 0.2s ease;
  font-size: 0.95rem;
  display: flex;
  align-items: center;
  gap: 10px;
}

.profile-menu li:hover {
  background: #2a3044;
  color: #fff;
}

.profile-menu li:active {
  background: #32394f;
  transform: scale(0.98);
}

/* Адаптивность */
@media (max-width: 768px) {
  .profile-wrapper {
    padding: 0 15px;
  }

  #profile {
    max-width: 240px;
    font-size: 0.9rem;
    height: 55px;
  }

  .profile-menu li {
    padding: 10px 14px;
    font-size: 0.9rem;
  }
}

@media (max-width: 480px) {
  .profile-wrapper {
    padding: 0 10px;
  }

  #profile {
    max-width: 220px;
    font-size: 0.85rem;
    height: 50px;
    gap: 8px;
  }

  #profile img {
    width: 32px;
  }

  .profile-menu li {
    padding: 8px 12px;
    font-size: 0.85rem;
  }
}
</style>