<script>
import { mapGetters } from 'vuex'
import registAPI from '@/service/registAPI'
import DaumPostcode from '@/components/DaumPostcode.vue'

export default {
  data() {
    return {
      origin: '',
      destination: '',
      weight: '',
      size: '',
      price: '',
      status: 'Pending',
      isSubmitting: false,
    }
  },
  components: { DaumPostcode },
  computed: {
    ...mapGetters(['getUserId']), // Vuex에서 현재 로그인된 사용자 ID 가져오기
    user_id() {
      console.log('Vuex에서 가져온 user_id:', this.getUserId) // 콘솔에서 확인
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

      this.isSubmitting = true

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
          alert('등록 성공! 🎉')
          this.$router.push('/shipment/')
        }
      } catch (err) {
        if (err.message.indexOf('Network Error') > -1) {
          alert('서버에 접속할 수 없습니다. 상태를 확인해주세요.')
        } else {
          alert('등록 정보를 확인할 수 없습니다.')
        }
      } finally {
        this.isSubmitting = false
      }
    },
  },
}
</script>

<template>
  <div class="flex justify-center items-start">
    <div class="w-full max-w-2xl bg-white p-8 rounded-lg shadow-md">
      <h1 class="text-2xl font-bold text-center mb-8">📦 Shipment Registration</h1>

      <form class="space-y-6" @submit.prevent="fnRegist">
        <div>
          <label class="block text-sm font-medium mb-2">Origin Address</label>
          <DaumPostcode v-model="origin" />
        </div>

        <div>
          <label class="block text-sm font-medium mb-2">Destination Address</label>
          <DaumPostcode v-model="destination" />
        </div>

        <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
          <div>
            <label class="block text-sm font-medium mb-2">Weight (kg)</label>
            <input
              v-model="weight"
              type="number"
              required
              class="w-full p-2 border border-gray-300 rounded"
              placeholder="Enter weight in kg"
            />
          </div>

          <div>
            <label class="block text-sm font-medium mb-2">Size (cm³)</label>
            <input
              v-model="size"
              type="number"
              required
              class="w-full p-2 border border-gray-300 rounded"
              placeholder="Enter size in cm³"
            />
          </div>

          <div>
            <label class="block text-sm font-medium mb-2">Price (₩)</label>
            <input
              v-model="price"
              type="number"
              required
              class="w-full p-2 border border-gray-300 rounded"
              placeholder="Enter price in KRW"
            />
          </div>
        </div>

        <div class="pt-4">
          <button
            type="submit"
            :disabled="isSubmitting"
            class="w-full p-3 bg-indigo-600 text-white rounded-md hover:bg-indigo-700 disabled:opacity-50 transition-colors"
          >
            <span v-if="isSubmitting" class="flex items-center justify-center">
              <svg
                class="animate-spin -ml-1 mr-3 h-5 w-5 text-white"
                xmlns="http://www.w3.org/2000/svg"
                fill="none"
                viewBox="0 0 24 24"
              >
                <circle
                  class="opacity-25"
                  cx="12"
                  cy="12"
                  r="10"
                  stroke="currentColor"
                  stroke-width="4"
                ></circle>
                <path
                  class="opacity-75"
                  fill="currentColor"
                  d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"
                ></path>
              </svg>
              Registering...
            </span>
            <span v-else>Register Shipment</span>
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<style scoped>
/* Remove spinner from number inputs */
input[type='number']::-webkit-inner-spin-button,
input[type='number']::-webkit-outer-spin-button {
  -webkit-appearance: none;
  margin: 0;
}
input[type='number'] {
  -moz-appearance: textfield;
}
</style>
