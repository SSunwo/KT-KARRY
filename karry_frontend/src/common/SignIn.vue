<script>
import { mapActions, mapGetters } from 'vuex'

export default {
  data() {
    return {
      user_id: '',
      user_pw: '',
    }
  },
  methods: {
    ...mapActions(['login']),

    async fnLogin() {
      if (this.user_id === '') {
        alert('ID를 입력하세요.')
        return
      }

      if (this.user_pw === '') {
        alert('비밀번호를 입력하세요.')
        return
      }

      try {
        let loginResult = await this.login({ user_id: this.user_id, user_pw: this.user_pw })
        if (loginResult) alert('로그인 결과 : ' + loginResult)
      } catch (err) {
        if (err.message.indexOf('Network Error') > -1) {
          alert('서버에 접속할 수 없습니다. 상태를 확인해주세요.')
        } else {
          alert('로그인 정보를 확인할 수 없습니다.')
        }
      }
    },
    computed: {
      ...mapGetters({
        errorState: 'getErrorState',
      }),
    },
  },
}
</script>

<template>
  <div class="flex flex-col items-center justify-center">
    <h2 class="text-xl font-semibold">Sign In</h2>
    <form class="mt-6 space-y-4" @submit.prevent="fnLogin">
      <div>
        <label for="id" class="block text-sm/6 font-medium text-gray-900">User ID</label>
        <input
          v-model="user_id"
          type="text"
          name="id"
          id="id"
          autocomplete="username"
          required
          class="block w-full rounded-md bg-white px-3 py-1.5 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 placeholder:text-gray-400 focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600 sm:text-sm/6"
        />
      </div>
      <div>
        <label for="password" class="block text-sm/6 font-medium text-gray-900">Password</label>
        <input
          v-model="user_pw"
          type="password"
          name="password"
          id="password"
          autocomplete="current-password"
          required
          class="block w-full rounded-md bg-white px-3 py-1.5 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 placeholder:text-gray-400 focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600 sm:text-sm/6"
        />
      </div>
      <div>
        <button
          type="submit"
          class="flex w-full justify-center rounded-md bg-indigo-600 px-3 py-1.5 text-sm/6 font-semibold text-white shadow-xs hover:bg-indigo-500 focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"
        >
          Sign in
        </button>
      </div>
    </form>
  </div>
</template>

<style></style>
