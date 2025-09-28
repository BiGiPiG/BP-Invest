
import { createApp } from 'vue';
import App from './App.vue';
import router from './router/index.js';
import {setupAuthInterceptor} from "./authInterceptor.js";

const app = createApp(App);
app.use(router);
app.mount('#app');

setupAuthInterceptor(router)

