<script setup>
import { computed } from 'vue'
import { useStore } from 'vuex'

const store = useStore()

// Vuex의 상태를 computed로 가져오기
const isAuth = computed(() => store.getters.getIsAuth)
// 로그아웃
const logout = () => {
  store.dispatch('logout')
}
</script>

<template>
  <button v-if="isAuth" @click="logout" class="text-sm/6 font-semibold text-gray-900">
    Logout
  </button>
</template>
