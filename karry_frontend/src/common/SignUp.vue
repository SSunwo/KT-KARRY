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
        <input v-model="user_id" type="text" required class="w-full p-2 border rounded" />
      </div>
      <div>
        <label class="block text-sm font-medium">Password</label>
        <input v-model="user_pw" type="password" required class="w-full p-2 border rounded" />
      </div>
      <div>
        <label class="block text-sm font-medium">Name</label>
        <input v-model="user_name" type="text" required class="w-full p-2 border rounded" />
      </div>
      <div>
        <label class="block text-sm font-medium">Role</label>
        <input v-model="role" type="text" required class="w-full p-2 border rounded" />
      </div>
      <button type="submit" class="w-full p-2 bg-blue-500 text-white rounded">Sign Up</button>
    </form>
  </div>
</template>
