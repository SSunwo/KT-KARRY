<!-- eslint-disable vue/multi-word-component-names -->
<script setup>
import { computed } from 'vue'
import { useStore } from 'vuex'

import SignOutButton from '@/components/SignOutButton.vue'

const store = useStore()

// Vuex의 상태를 computed로 가져오기
const isAuth = computed(() => store.getters.getIsAuth)
const loggedInUserId = computed(() => store.getters.getUserId) // 로그인된 유저 ID 가져오기
</script>

<template>
  <nav class="mx-auto flex max-w-7xl items-center justify-between p-6 lg:px-8">
    <div class="flex lg:flex-1">
      <a href="#" class="-m-1.5 p-1.5">
        <span class="sr-only">Your Company</span>
        <img class="h-8 w-auto" src="../assets/muco.png" alt="" />
      </a>
    </div>
    <div class="hidden lg:flex lg:gap-x-12">
      <RouterLink to="/" class="text-sm/6 font-semibold text-gray-900">Home</RouterLink>
      <RouterLink to="/about" class="text-sm/6 font-semibold text-gray-900">About</RouterLink>
      <RouterLink to="/shipment" v-if="isAuth" class="text-sm/6 font-semibold text-gray-900"
        >ShipmentList</RouterLink
      >
      <RouterLink to="/regist" v-if="isAuth" class="text-sm/6 font-semibold text-gray-900"
        >Regist</RouterLink
      >
      <RouterLink to="/matching" v-if="isAuth" class="text-sm/6 font-semibold text-gray-900"
        >MyMatching</RouterLink
      >
      <RouterLink to="/signup" v-if="!isAuth" class="text-sm/6 font-semibold text-gray-900"
        >Sign Up</RouterLink
      >
      <RouterLink to="/signin" v-if="!isAuth" class="text-sm/6 font-semibold text-gray-900"
        >Sign in</RouterLink
      >
      <SignOutButton />
      <!-- 로그인된 유저 정보 표시 -->
      <div v-if="isAuth" class="text-sm/6 font-semibold text-gray-900">
        <span class="text-blue-600">👤 {{ loggedInUserId }}</span>
      </div>
    </div>
  </nav>
</template>
