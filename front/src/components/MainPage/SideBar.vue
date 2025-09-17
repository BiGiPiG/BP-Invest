<template>
  <aside>
    
    
    <div class="profile-wrapper">
      <button id="profile" type="button" @click="toggleMenu" ref="btnRef" :aria-expanded="isOpen.toString()">
        <img src="/images/Профиль.png" width="40px" />
        <span>Your profile</span>
      </button>
      <div v-if="isOpen" class="profile-menu" ref="menuRef">
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
import { ref, onMounted, onBeforeUnmount } from 'vue';

const isOpen = ref(false);
const btnRef = ref(null);
const menuRef = ref(null);

function toggleMenu() {
  isOpen.value = !isOpen.value;
}

function selectItem(action) {
  isOpen.value = false;
  switch(action) {
    case 'logout':
      logout();
      break;
    case "Settings":
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
  
}

function openProfile() {

}

onMounted(() => {
  document.addEventListener('click', handleClickOutside);
});

onBeforeUnmount(() => {
  document.removeEventListener('click', handleClickOutside);
});
</script>

<style scoped> 
@import url('https://fonts.googleapis.com/css2?family=Alfa+Slab+One&family=Inter:ital,opsz,wght@0,14..32,100..900;1,14..32,100..900&display=swap');



.profile-wrapper {

  position: relative;
  width: calc(100% - 20px);
  display: flex;

  height: 60px;
  
  margin-top: auto;
  margin-left: 20px;
  margin-bottom: 20px;

  gap: 5px;
  justify-content: center;
  align-items: center;
}

#profile {
  all: unset;
  width: 100%;
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  font-family: "Inter";
  font-weight: 600;
  font-size: 1.2em;
  color: #fff;
  cursor: pointer;
  outline: none;
  border-radius: 10px;
}

#profile:hover {
  border-color: antiquewhite;
  background-color: #2a3044;
}

#profile:focus {
  outline: none;
}

.profile-menu {
  position: absolute;
  bottom: 100%;
  left: 0;
  right: 0;
  margin-bottom: 8px;
  background: #1f2331;
  border-radius: 5px;
  color: #e5e7eb;
  z-index: 10;
  width: 100%;
  font-family: "Inter";
}

.profile-menu ul {
  list-style: none;
  width: 100%;
  margin: 0;
  padding: 0;
}

.profile-menu li {
  padding: 10px 14px;
  cursor: pointer;
  border-radius: 5px;
}

.profile-menu li:hover {
  background: #2a3044;
}
</style>

