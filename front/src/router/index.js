import { createRouter, createWebHistory } from 'vue-router';
import MainPage from '../views/MainPage.vue';
import LoginPage from '../views/LoginPage.vue';

const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/home',
    name: 'home',
    meta: { requiresAuth: true },
    component: MainPage
  },
  {
    path: '/login',
    name: 'login',
    component: LoginPage
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

// Глобальный навигационный guard
router.beforeEach((to, from, next) => {
  const isAuthenticated = Boolean(localStorage.getItem('token'));

  if (to.meta.requiresAuth && !isAuthenticated) {
    // Если маршрут защищён, а пользователь не залогинен — редирект на /login
    next('/login')
  } else if (to.name === 'login' && isAuthenticated) {
    // Если залогинен и пытается зайти на /login — редирект на главную
    next('/home')
  } else {
    next() // разрешить переход
  }
})

export default router;