<script>
import { mapGetters } from 'vuex'
import getListAPI from '@/service/getListAPI'
import registAPI from '@/service/registAPI'

export default {
  data() {
    return {
      matchings: [],
      isLoading: true,
      error: null,
    }
  },
  computed: {
    ...mapGetters(['getUserId']), // Vuex에서 로그인된 사용자 ID 가져오기
    userId() {
      return this.getUserId || ''
    },
    hasMatchings() {
      return this.matchings && this.matchings.length > 0
    },
  },
  methods: {
    formatDate(dateString) {
      if (!dateString) return ''
      const date = new Date(dateString)
      return date.toLocaleDateString('ko-KR', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit',
      })
    },

    formatText(text, maxLength = 20) {
      if (!text) return ''
      return text.length > maxLength ? text.substring(0, maxLength) + '...' : text
    },

    getStatusClass(status) {
      switch (status) {
        case 'Pending':
          return 'bg-yellow-100 text-yellow-800'
        case 'Shipping':
          return 'bg-blue-100 text-blue-800'
        case 'Completed':
          return 'bg-green-100 text-green-800'
        case 'Cancelled':
          return 'bg-red-100 text-red-800'
        default:
          return 'bg-gray-100 text-gray-800'
      }
    },

    async fetchUserMatchings() {
      this.isLoading = true
      this.error = null

      try {
        if (!this.userId) {
          this.error = '로그인이 필요합니다!'
          return
        }

        const res = await getListAPI.getUserMatchingList(this.userId)
        console.log('매칭된 운송 목록:', res)
        this.matchings = res || []
      } catch (error) {
        console.error('매칭 목록 불러오기 실패:', error)
        this.error = '매칭된 운송 목록을 불러오는 데 실패했습니다.'
      } finally {
        this.isLoading = false
      }
    },

    // 배송 완료 처리
    async completeMatching(matching) {
      try {
        await registAPI.updateMatchingStatus(matching.matchingId, 'Completed')
        await registAPI.updateShipmentStatus(matching.shipmentId, 'Completed')

        const transactionId = await registAPI.getTransactionIdByMatchingId(matching.matchingId)
        if (!transactionId) {
          throw new Error('🚨 거래 ID를 찾을 수 없습니다!')
        }

        console.log(`거래 정산 요청: transactionId=${transactionId}`)
        await registAPI.completeTransaction(transactionId)

        alert('배송 완료 처리되었습니다!')
        this.fetchUserMatchings()
      } catch (error) {
        alert('배송 완료 처리 실패!')
        console.error(error)
      }
    },

    // 배송 취소 처리
    async cancelMatching(matching) {
      try {
        await registAPI.updateMatchingStatus(matching.matchingId, 'Cancelled')
        await registAPI.updateShipmentStatus(matching.shipmentId, 'Cancelled')
        alert('배송이 취소되었습니다.')
        this.fetchUserMatchings()
      } catch (error) {
        alert('배송 취소 실패!')
        console.error(error)
      }
    },
  },
  mounted() {
    this.fetchUserMatchings()
  },
}
</script>

<template>
  <div class="relative overflow-hidden shadow-md sm:rounded-lg">
    <!-- Header section -->
    <div class="p-4 bg-white dark:bg-gray-900">
      <div class="flex justify-between items-center">
        <h2 class="text-xl font-semibold text-gray-900 dark:text-white">🚚 나의 운송 매칭 내역</h2>
        <button
          @click="fetchUserMatchings"
          class="px-3 py-1.5 bg-blue-600 text-white rounded-lg hover:bg-blue-700 text-sm"
        >
          새로고침
        </button>
      </div>
    </div>

    <!-- Loading state -->
    <div v-if="isLoading" class="p-8 text-center">
      <div
        class="inline-block animate-spin rounded-full h-8 w-8 border-4 border-gray-300 border-t-blue-600"
      ></div>
      <p class="mt-2 text-gray-600">데이터를 불러오는 중...</p>
    </div>

    <!-- Error state -->
    <div v-else-if="error" class="p-8 text-center">
      <div
        class="inline-flex items-center justify-center w-16 h-16 rounded-full bg-red-100 text-red-500 mb-4"
      >
        <svg
          class="w-8 h-8"
          fill="none"
          stroke="currentColor"
          viewBox="0 0 24 24"
          xmlns="http://www.w3.org/2000/svg"
        >
          <path
            stroke-linecap="round"
            stroke-linejoin="round"
            stroke-width="2"
            d="M12 8v4m0 4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"
          ></path>
        </svg>
      </div>
      <p class="text-gray-600">{{ error }}</p>
    </div>

    <!-- Empty state -->
    <div v-else-if="!hasMatchings" class="p-8 text-center">
      <div
        class="inline-flex items-center justify-center w-16 h-16 rounded-full bg-gray-100 text-gray-500 mb-4"
      >
        <svg
          class="w-8 h-8"
          fill="none"
          stroke="currentColor"
          viewBox="0 0 24 24"
          xmlns="http://www.w3.org/2000/svg"
        >
          <path
            stroke-linecap="round"
            stroke-linejoin="round"
            stroke-width="2"
            d="M20 13V6a2 2 0 00-2-2H6a2 2 0 00-2 2v7m16 0v5a2 2 0 01-2 2H6a2 2 0 01-2-2v-5m16 0h-2.586a1 1 0 00-.707.293l-2.414 2.414a1 1 0 01-.707.293h-3.172a1 1 0 01-.707-.293l-2.414-2.414A1 1 0 006.586 13H4"
          ></path>
        </svg>
      </div>
      <p class="text-gray-600">매칭된 운송 내역이 없습니다.</p>
      <p class="text-gray-500 text-sm mt-2">배송 매칭이 완료되면 이곳에 표시됩니다.</p>
    </div>

    <!-- Table with data -->
    <div v-else class="overflow-x-auto">
      <table class="w-full text-sm text-left text-gray-500 dark:text-gray-400">
        <thead
          class="text-xs text-gray-700 uppercase bg-gray-50 dark:bg-gray-700 dark:text-gray-400"
        >
          <tr>
            <th scope="col" class="px-4 py-3 whitespace-nowrap">매칭 ID</th>
            <th scope="col" class="px-4 py-3">출발지</th>
            <th scope="col" class="px-4 py-3">도착지</th>
            <th scope="col" class="px-4 py-3 whitespace-nowrap">거리 (km)</th>
            <th scope="col" class="px-4 py-3 whitespace-nowrap">금액 (원)</th>
            <th scope="col" class="px-4 py-3 whitespace-nowrap">상태</th>
            <th scope="col" class="px-4 py-3 whitespace-nowrap">매칭일</th>
            <th scope="col" class="px-4 py-3 whitespace-nowrap">업데이트일</th>
            <th scope="col" class="px-4 py-3 whitespace-nowrap">액션</th>
          </tr>
        </thead>
        <tbody>
          <tr
            v-for="matching in matchings"
            :key="matching.matchingId"
            class="bg-white border-b dark:bg-gray-800 dark:border-gray-700 hover:bg-gray-50 dark:hover:bg-gray-600"
          >
            <td class="px-4 py-3 font-medium text-gray-900 whitespace-nowrap dark:text-white">
              {{ matching.matchingId }}
            </td>
            <td class="px-4 py-3">
              {{ formatText(matching.origin) }}
            </td>
            <td class="px-4 py-3">
              {{ formatText(matching.destination) }}
            </td>
            <td class="px-4 py-3 whitespace-nowrap">{{ matching.distance }} km</td>
            <td class="px-4 py-3 whitespace-nowrap">₩{{ matching.amount?.toLocaleString() }}</td>
            <td class="px-4 py-3 whitespace-nowrap">
              <span
                :class="getStatusClass(matching.status)"
                class="px-2 py-1 rounded-full text-xs font-medium"
              >
                {{ matching.status }}
              </span>
            </td>
            <td class="px-4 py-3 whitespace-nowrap">
              {{ formatDate(matching.acceptedAt) }}
            </td>
            <td class="px-4 py-3 whitespace-nowrap">
              {{ formatDate(matching.updatedAt) }}
            </td>
            <td class="px-4 py-3 whitespace-nowrap">
              <div v-if="matching.status === 'Shipping'" class="flex space-x-2">
                <button
                  @click="completeMatching(matching)"
                  class="px-3 py-1 bg-green-500 text-white rounded text-xs hover:bg-green-600"
                >
                  배송 완료
                </button>
                <button
                  @click="cancelMatching(matching)"
                  class="px-3 py-1 bg-red-500 text-white rounded text-xs hover:bg-red-600"
                >
                  취소
                </button>
              </div>
              <div v-else-if="matching.status === 'Completed'" class="text-green-600 text-xs">
                완료됨
              </div>
              <div v-else-if="matching.status === 'Cancelled'" class="text-red-600 text-xs">
                취소됨
              </div>
              <div v-else class="text-gray-500 text-xs">
                {{ matching.status }}
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Pagination (optional) -->
    <nav
      class="flex items-center justify-between p-4 bg-white dark:bg-gray-900"
      aria-label="Table navigation"
    >
      <span class="text-sm font-normal text-gray-500 dark:text-gray-400">
        총
        <span class="font-semibold text-gray-900 dark:text-white">{{ matchings.length }}</span> 건
      </span>
      <ul class="inline-flex -space-x-px text-sm h-8">
        <li>
          <a
            href="#"
            class="flex items-center justify-center px-3 h-8 ml-0 leading-tight text-gray-500 bg-white border border-gray-300 rounded-l-lg hover:bg-gray-100 hover:text-gray-700 dark:bg-gray-800 dark:border-gray-700 dark:text-gray-400 dark:hover:bg-gray-700 dark:hover:text-white"
            >이전</a
          >
        </li>
        <li>
          <a
            href="#"
            class="flex items-center justify-center px-3 h-8 leading-tight text-gray-500 bg-white border border-gray-300 hover:bg-gray-100 hover:text-gray-700 dark:bg-gray-800 dark:border-gray-700 dark:text-gray-400 dark:hover:bg-gray-700 dark:hover:text-white"
            >1</a
          >
        </li>
        <li>
          <a
            href="#"
            aria-current="page"
            class="flex items-center justify-center px-3 h-8 text-blue-600 border border-gray-300 bg-blue-50 hover:bg-blue-100 hover:text-blue-700 dark:border-gray-700 dark:bg-gray-700 dark:text-white"
            >2</a
          >
        </li>
        <li>
          <a
            href="#"
            class="flex items-center justify-center px-3 h-8 leading-tight text-gray-500 bg-white border border-gray-300 rounded-r-lg hover:bg-gray-100 hover:text-gray-700 dark:bg-gray-800 dark:border-gray-700 dark:text-gray-400 dark:hover:bg-gray-700 dark:hover:text-white"
            >다음</a
          >
        </li>
      </ul>
    </nav>
  </div>
</template>

<style scoped>
/* Add responsive styles */
@media (max-width: 768px) {
  .overflow-x-auto {
    -webkit-overflow-scrolling: touch;
  }
}
</style>
