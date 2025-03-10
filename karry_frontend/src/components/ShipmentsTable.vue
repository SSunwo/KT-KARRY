<script>
import { mapGetters } from 'vuex'
import registAPI from '@/service/registAPI'

export default {
  data() {
    return {
      shipments: [],
      selectedPriceLog: null,
      isModalOpen: false,
    }
  },
  computed: {
    ...mapGetters({
      loggedInUserId: 'getUserId', // Vuex에서 로그인된 `userId` 가져오기
    }),
    // Check if shipments array has data
    hasShipments() {
      return this.shipments && this.shipments.length > 0
    },
    // Get column headers dynamically from the first shipment
    columnHeaders() {
      if (!this.hasShipments) return []
      return Object.keys(this.shipments[0])
    },
  },
  methods: {
    async findAllShipmentsList() {
      try {
        const res = await registAPI.getShipmentsList()
        console.log('response data : ', res.data)
        // 응답 구조 확인 후 list 또는 data로 할당
        this.shipments = res.data.map((shipment) => ({
          shipmentId: shipment.shipmentId, // 배송 ID
          userId: shipment.userId,
          origin: shipment.origin, // 출발지
          destination: shipment.destination, // 도착지
          weight: shipment.weight, // 화물 무게
          size: shipment.size, // 화물 크기
          price: shipment.price, // 운송 요금
          status: shipment.status, // 현재 상태
          createdAt: shipment.createdAt, // 생성 일자
        }))
      } catch (error) {
        console.error('배차 목록 불러오기 실패:', error)
      }
    },

    // Format long text to prevent table overflow
    formatText(text, maxLength = 20) {
      if (!text) return ''
      return text.length > maxLength ? text.substring(0, maxLength) + '...' : text
    },

    // Format date for better display
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

    // PriceLog 정보 불러오기
    async fetchPriceLog(shipmentId) {
      try {
        console.log(`🔍 PriceLog 요청: shipmentId=${shipmentId}`)

        const res = await registAPI.getPricelog(shipmentId)

        if (!res) {
          console.error('PriceLog 응답이 없습니다!')
          alert('해당 배송 요청의 PriceLog 데이터가 존재하지 않습니다.')
          return
        }

        console.log('PriceLog 데이터:', res)
        this.selectedPriceLog = res
        this.isModalOpen = true // 모달 열기
      } catch (error) {
        console.error('PriceLog 불러오기 실패:', error)
        alert('가격 정보를 불러오는 데 실패했습니다.')
      }
    },

    closeModal() {
      this.isModalOpen = false
      this.selectedPriceLog = null
    },

    async acceptShipment() {
      try {
        // selectedPriceLog가 정상적으로 로드되었는지 확인
        if (!this.selectedPriceLog || !this.selectedPriceLog.shipmentId) {
          alert('잘못된 요청: PriceLog 데이터가 없습니다!')
          return
        }

        const shipmentId = this.selectedPriceLog?.shipmentId
        const pricelogId = this.selectedPriceLog?.priceLogId // 요금 로그 ID
        const amount = this.selectedPriceLog?.calculatedPrice // 거래 금액
        const createdBy = this.selectedPriceLog?.userId // 등록한 사용자 ID 가져오기
        const acceptedBy = this.loggedInUserId // 현재 로그인한 사용자 ID

        // 1. 배차 생성 요청 (Matching 생성)
        await registAPI.createMatching(shipmentId, createdBy, acceptedBy)

        // 2. Shipment 테이블의 status를 "Shipping"으로 변경
        console.log(`상태 변경 요청: shipmentId=${shipmentId}, status=Shipping`)
        await registAPI.updateShipmentStatus(shipmentId, 'Shipping')

        const matchingId = await registAPI.getMatchingIdByShipmentId(shipmentId)
        // 3. **거래 내역 생성**
        console.log(
          `거래 생성: matchingId=${matchingId}, pricelogId=${pricelogId}, amount=${amount}, createdBy=${createdBy}, acceptedBy=${acceptedBy}`,
        )
        await registAPI.createTransaction(matchingId, pricelogId, amount, createdBy, acceptedBy)

        alert('운송 배차 성공!')

        // 모달 닫기 + 배차 목록 새로고침
        this.closeModal()
        this.findAllShipmentsList()
      } catch (error) {
        alert('운송 배차 실패! 서버 오류 발생')
        console.error('오류 메시지:', error)
      }
    },
  },

  mounted() {
    // 컴포넌트가 마운트될 때 데이터 불러오기
    this.findAllShipmentsList()
  },
}
</script>

<template>
  <div class="relative overflow-hidden shadow-md sm:rounded-lg">
    <!-- Search bar -->
    <div class="p-4 bg-white dark:bg-gray-900">
      <div class="flex justify-between items-center">
        <h2 class="text-xl font-semibold text-gray-900 dark:text-white">Shipments</h2>
        <div class="relative">
          <div class="absolute inset-y-0 left-0 flex items-center pl-3 pointer-events-none">
            <svg
              class="w-4 h-4 text-gray-500 dark:text-gray-400"
              aria-hidden="true"
              xmlns="http://www.w3.org/2000/svg"
              fill="none"
              viewBox="0 0 20 20"
            >
              <path
                stroke="currentColor"
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="m19 19-4-4m0-7A7 7 0 1 1 1 8a7 7 0 0 1 14 0Z"
              />
            </svg>
          </div>
          <input
            type="text"
            id="table-search"
            class="block p-2 pl-10 text-sm text-gray-900 border border-gray-300 rounded-lg w-80 bg-gray-50 focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
            placeholder="Search for shipments"
          />
        </div>
      </div>
    </div>

    <!-- Table with horizontal scroll for small screens -->
    <div class="overflow-x-auto">
      <table class="w-full text-sm text-left text-gray-500 dark:text-gray-400">
        <thead
          class="text-xs text-gray-700 uppercase bg-gray-50 dark:bg-gray-700 dark:text-gray-400"
        >
          <tr>
            <th scope="col" class="px-4 py-3 whitespace-nowrap">ID</th>
            <th scope="col" class="px-4 py-3 whitespace-nowrap">User</th>
            <th scope="col" class="px-4 py-3 whitespace-nowrap">Origin</th>
            <th scope="col" class="px-4 py-3 whitespace-nowrap">Destination</th>
            <th scope="col" class="px-4 py-3 whitespace-nowrap">Weight</th>
            <th scope="col" class="px-4 py-3 whitespace-nowrap">Size</th>
            <th scope="col" class="px-4 py-3 whitespace-nowrap">Price</th>
            <th scope="col" class="px-4 py-3 whitespace-nowrap">Status</th>
            <th scope="col" class="px-4 py-3 whitespace-nowrap">Created</th>
            <th scope="col" class="px-4 py-3 whitespace-nowrap">Action</th>
          </tr>
        </thead>
        <tbody>
          <tr v-if="!hasShipments" class="bg-white border-b dark:bg-gray-800 dark:border-gray-700">
            <td colspan="10" class="px-4 py-4 text-center text-gray-500 dark:text-gray-400">
              No shipments found
            </td>
          </tr>
          <tr
            v-for="(shipment, idx) in shipments"
            :key="idx"
            class="bg-white border-b dark:bg-gray-800 dark:border-gray-700 hover:bg-gray-50 dark:hover:bg-gray-600"
          >
            <td class="px-4 py-3 font-medium text-gray-900 whitespace-nowrap dark:text-white">
              {{ shipment.shipmentId }}
            </td>
            <td class="px-4 py-3 whitespace-nowrap">
              {{ shipment.userId }}
            </td>
            <td class="px-4 py-3">
              {{ formatText(shipment.origin) }}
            </td>
            <td class="px-4 py-3">
              {{ formatText(shipment.destination) }}
            </td>
            <td class="px-4 py-3 whitespace-nowrap">{{ shipment.weight }} kg</td>
            <td class="px-4 py-3 whitespace-nowrap">{{ shipment.size }} cm³</td>
            <td class="px-4 py-3 whitespace-nowrap">₩{{ shipment.price?.toLocaleString() }}</td>
            <td class="px-4 py-3 whitespace-nowrap">
              <span
                :class="{
                  'bg-yellow-100 text-yellow-800': shipment.status === 'Pending',
                  'bg-blue-100 text-blue-800': shipment.status === 'Shipping',
                  'bg-green-100 text-green-800': shipment.status === 'Completed',
                  'bg-red-100 text-red-800': shipment.status === 'Cancelled',
                  'bg-gray-100 text-gray-800': ![
                    'Pending',
                    'Shipping',
                    'Completed',
                    'Cancelled',
                  ].includes(shipment.status),
                }"
                class="px-2 py-1 rounded-full text-xs font-medium"
              >
                {{ shipment.status }}
              </span>
            </td>
            <td class="px-4 py-3 whitespace-nowrap">
              {{ formatDate(shipment.createdAt) }}
            </td>
            <td class="px-4 py-3 whitespace-nowrap">
              <button
                v-if="shipment.status === 'Pending'"
                @click="fetchPriceLog(shipment.shipmentId)"
                class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-1 px-3 rounded text-xs"
              >
                수락
              </button>
              <span v-else class="text-gray-500 text-xs">배차 완료</span>
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
        Showing
        <span class="font-semibold text-gray-900 dark:text-white">{{ shipments.length }}</span>
        items
      </span>
      <ul class="inline-flex -space-x-px text-sm h-8">
        <li>
          <a
            href="#"
            class="flex items-center justify-center px-3 h-8 ml-0 leading-tight text-gray-500 bg-white border border-gray-300 rounded-l-lg hover:bg-gray-100 hover:text-gray-700 dark:bg-gray-800 dark:border-gray-700 dark:text-gray-400 dark:hover:bg-gray-700 dark:hover:text-white"
            >Previous</a
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
            >Next</a
          >
        </li>
      </ul>
    </nav>
  </div>

  <!-- 가격 정보 모달 -->
  <div
    v-if="isModalOpen"
    class="fixed inset-0 flex items-center justify-center bg-gray-900 bg-opacity-50 z-50"
  >
    <div class="bg-white p-6 rounded-lg shadow-lg max-w-md w-full">
      <div class="flex justify-between items-center mb-4">
        <h2 class="text-xl font-bold text-gray-900">Price Log 정보</h2>
        <button @click="closeModal" class="text-gray-500 hover:text-gray-700">
          <svg
            class="w-6 h-6"
            fill="none"
            stroke="currentColor"
            viewBox="0 0 24 24"
            xmlns="http://www.w3.org/2000/svg"
          >
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              stroke-width="2"
              d="M6 18L18 6M6 6l12 12"
            ></path>
          </svg>
        </button>
      </div>

      <div class="space-y-3 mb-6">
        <div class="grid grid-cols-3 gap-2">
          <span class="text-gray-600 font-medium">아이디:</span>
          <span class="col-span-2">{{ selectedPriceLog?.userId }}</span>
        </div>
        <div class="grid grid-cols-3 gap-2">
          <span class="text-gray-600 font-medium">출발지:</span>
          <span class="col-span-2">{{ selectedPriceLog?.origin }}</span>
        </div>
        <div class="grid grid-cols-3 gap-2">
          <span class="text-gray-600 font-medium">도착지:</span>
          <span class="col-span-2">{{ selectedPriceLog?.destination }}</span>
        </div>
        <div class="grid grid-cols-3 gap-2">
          <span class="text-gray-600 font-medium">거리:</span>
          <span class="col-span-2">{{ selectedPriceLog?.distance }} km</span>
        </div>
        <div class="grid grid-cols-3 gap-2">
          <span class="text-gray-600 font-medium">거래 금액:</span>
          <span class="col-span-2 font-semibold text-blue-600">
            {{ selectedPriceLog?.calculatedPrice?.toLocaleString() }} 원
          </span>
        </div>
      </div>

      <div class="flex justify-end space-x-3">
        <button
          @click="closeModal()"
          class="px-4 py-2 bg-gray-200 text-gray-800 rounded-lg hover:bg-gray-300 transition-colors"
        >
          닫기
        </button>
        <button
          @click="acceptShipment(selectedPriceLog?.shipmentId)"
          class="px-4 py-2 bg-yellow-500 text-white rounded-lg hover:bg-yellow-600 transition-colors"
        >
          수락
        </button>
      </div>
    </div>
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
