import './assets/main.css'

import { createApp } from 'vue'

import App from './App.vue'
import router from './router'
import store from '@/vuex/store'
import axios from 'axios'

const app = createApp(App)

app.config.globalProperties.$axios = axios
app.config.globalProperties.$store = store

app.use(router).use(store).mount('#app')
