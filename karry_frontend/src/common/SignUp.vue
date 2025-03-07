<script>
import signupAPI from '@/service/signupAPI'

export default {
  data() {
    return {
      user_id: '',
      user_pw: '',
      user_name: '',
      role: '',
    }
  },
  methods: {
    async fnSignup() {
      if (!this.user_id || !this.user_pw || !this.user_name || !this.role) {
        alert('모든 정보를 입력하세요.')
        return
      }

      try {
        let signupResult = await signupAPI.doSignup(
          this.user_id,
          this.user_pw,
          this.user_name,
          this.role,
        )
        if (signupResult) {
          alert('회원가입 성공! 🎉' + signupResult)
          this.$router.push('/signin') // 회원가입 후 로그인 페이지로 이동
        }
      } catch (err) {
        if (err.message.indexOf('Network Error') > -1) {
          alert('서버에 접속할 수 없습니다. 상태를 확인해주세요.')
        } else {
          alert('회원가입 정보를 확인할 수 없습니다.')
        }
      }
    },
  },
  //   computed: {
  //     ...mapGetters({
  //       errorState: 'getErrorState',
  //     }),
  //   },
}
</script>

<template>
  <div class="flex flex-col items-center justify-center min-h-screen">
    <h2 class="text-xl font-semibold">Sign Up</h2>
    <form class="mt-6 space-y-4" @submit.prevent="fnSignup">
      <div>
        <label class="block text-sm font-medium">User ID</label>
        <input
          v-model="user_id"
          type="text"
          required
          class="block w-full rounded-md bg-white px-3 py-1.5 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 placeholder:text-gray-400 focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600 sm:text-sm/6"
        />
      </div>
      <div>
        <label class="block text-sm font-medium">Password</label>
        <input
          v-model="user_pw"
          type="password"
          required
          class="block w-full rounded-md bg-white px-3 py-1.5 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 placeholder:text-gray-400 focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600 sm:text-sm/6"
        />
      </div>
      <div>
        <label class="block text-sm font-medium">Name</label>
        <input
          v-model="user_name"
          type="text"
          required
          class="block w-full rounded-md bg-white px-3 py-1.5 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 placeholder:text-gray-400 focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600 sm:text-sm/6"
        />
      </div>
      <div>
        <label class="block text-sm font-medium">Role</label>
        <select
          id="role"
          v-model="role"
          required
          class="block w-full rounded-md bg-white px-3 py-1.5 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 placeholder:text-gray-400 focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600 sm:text-sm/6"
        >
          <option value="" disabled selected>사용자 유형을 선택하세요</option>
          <option value="shipper">화주 (Shipper)</option>
          <option value="carrier">차주 (Carrier)</option>
        </select>
      </div>
      <button
        type="submit"
        class="flex w-full justify-center rounded-md bg-indigo-600 px-3 py-1.5 text-sm/6 font-semibold text-white shadow-xs hover:bg-indigo-500 focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"
      >
        Sign Up
      </button>
    </form>
  </div>
</template>
