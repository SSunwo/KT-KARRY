import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import AboutView from '../views/AboutView.vue'
import SignIn from '@/common/SignIn.vue'
import SignUp from '@/common/SignUp.vue'
import Matching from '@/views/MatchingView.vue'
import store from '@/vuex/store'

// TODO: 없애기
import MainPage from '@/views/ShipmentListView.vue'

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView,
  },
  {
    path: '/about',
    name: 'about',
    component: AboutView,
  },
  {
    path: '/signin',
    name: 'signin',
    component: SignIn,
    meta: { guestOnly: true },
  },
  {
    path: '/signup',
    name: 'signup',
    component: SignUp,
    meta: { guestOnly: true },
  },
  {
    path: '/shipment',
    name: 'shipment',
    component: MainPage,
    meta: { requiresAuth: true },
  },
  {
    path: '/shipment/register',
    name: 'ShipmentRegister',
    component: () => import('@/views/RegistView.vue'),
  },
  {
    path: '/matching',
    name: 'matching',
    component: Matching,
    meta: { requiresAuth: true },
  },
  {
    path: '/dashboard',
    name: 'dashboard',
    component: () => import('@/views/DashboardView.vue'),
    meta: { requiresAuth: true },
  },
]

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
})

// 전역 가드 설정
router.beforeEach((to, from, next) => {
  const isAuth = store.getters.getIsAuth // Vuex에서 로그인 상태 가져오기

  // 로그인 필수 페이지 보호 (requiresAuth)
  if (to.meta.requiresAuth && !isAuth) {
    next('/signin') // 로그인 안 했으면 signin 페이지로 이동
  }
  // 비회원 전용 페이지 보호 (guestOnly)
  else if (to.meta.guestOnly && isAuth) {
    next('/') // 로그인한 사용자가 signin/signup 접근 시 홈으로 이동
  } else {
    next() // 그 외의 경우는 정상적으로 이동
  }
})

export default router
