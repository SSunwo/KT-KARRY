<script>
import { mapGetters } from 'vuex'
import registAPI from '@/service/registAPI'

export default {
  data() {
    return {
      origin: '',
      destination: '',
      weight: '',
      size: '',
      price: '',
      status: 'Pending',
    }
  },
  computed: {
    ...mapGetters(['getUserId']), // Vuex에서 현재 로그인된 사용자 ID 가져오기
    user_id() {
      console.log('Vuex에서 가져온 user_id:', this.getUserId) // ✅ 콘솔에서 확인
      return this.getUserId || '' // 로그인 상태에서 user_id 자동 설정
    },
  },
  methods: {
    async fnRegist() {
      if (
        !this.user_id ||
        !this.origin ||
        !this.destination ||
        !this.weight ||
        !this.size ||
        !this.price ||
        !this.status
      ) {
        alert('모든 정보를 입력하세요.')
        return
      }

      try {
        let registResult = await registAPI.doRegist(
          this.user_id,
          this.origin,
          this.destination,
          this.weight,
          this.size,
          this.price,
          this.status,
        )
        if (registResult) {
          alert('등록 성공! 🎉' + registResult)
          //   this.$router.push('/shipment/')
        }
      } catch (err) {
        if (err.message.indexOf('Network Error') > -1) {
          alert('서버에 접속할 수 없습니다. 상태를 확인해주세요.')
        } else {
          alert('등록 정보를 확인할 수 없습니다.')
        }
      }
    },
  },
}
</script>
<template>
  <div class="flex flex-col items-center justify-center min-h-screen">
    <h2 class="text-xl font-semibold">Shipment Registration</h2>
    <form class="mt-6 space-y-4" @submit.prevent="fnRegist">
      <div>
        <label class="block text-sm font-medium">Origin</label>
        <input v-model="origin" type="text" required class="w-full p-2 border rounded" />
      </div>
      <div>
        <label class="block text-sm font-medium">Destination</label>
        <input v-model="destination" type="text" required class="w-full p-2 border rounded" />
      </div>

      <div>
        <label class="block text-sm font-medium">Weight</label>
        <input v-model="weight" type="number" required class="w-full p-2 border rounded" />
      </div>
      <div>
        <label class="block text-sm font-medium">Size</label>
        <input v-model="size" type="number" required class="w-full p-2 border rounded" />
      </div>
      <div>
        <label class="block text-sm font-medium">Price</label>
        <input v-model="price" type="number" required class="w-full p-2 border rounded" />
      </div>
      <!-- <div>
        <label class="block text-sm font-medium">Status</label>
        <input v-model="status" type="text" required class="w-full p-2 border rounded" />
      </div> -->
      <button
        type="submit"
        class="flex w-full justify-center rounded-md bg-indigo-600 px-3 py-1.5 text-sm/6 font-semibold text-white shadow-xs hover:bg-indigo-500 focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"
      >
        Register Shipment
      </button>
    </form>
  </div>
</template>
