<script>
import registAPI from '@/service/registAPI'

export default {
  data() {
    return {
      shipments: [],
      selectedPriceLog: null,
      isModalOpen: false,
    }
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
  },

  mounted() {
    // 컴포넌트가 마운트될 때 데이터 불러오기
    this.findAllShipmentsList()
  },
}
</script>

<template>
  <div class="relative overflow-x-auto shadow-md sm:rounded-lg">
    <!-- TODO : search 기능 추가 -->
    <div class="pb-4 bg-white dark:bg-gray-900">
      <label for="table-search" class="sr-only">Search</label>
      <div class="relative mt-1">
        <div
          class="absolute inset-y-0 rtl:inset-r-0 start-0 flex items-center ps-3 pointer-events-none"
        >
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
          class="block pt-2 ps-10 text-sm text-gray-900 border border-gray-300 rounded-lg w-80 bg-gray-50 focus:ring-blue-500 focus:border-blue-500 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
          placeholder="Search for items"
        />
      </div>
    </div>
    <table class="w-full text-sm text-left rtl:text-right text-gray-500 dark:text-gray-400">
      <thead class="text-xs text-gray-700 uppercase bg-gray-50 dark:bg-gray-700 dark:text-gray-400">
        <tr>
          <th
            v-for="(key, index) in Object.keys(shipments[0] || {})"
            :key="index"
            class="px-6 py-3"
          >
            {{ key }}
          </th>
          <th class="px-6 py-3">수락</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="(shipment, idx) in shipments" :key="idx" class="px-6 py-4">
          <td
            v-for="(value, key) in Object.values(shipment)"
            :key="key"
            class="px-6 py-4 font-medium text-gray-900 whitespace-nowrap dark:text-white"
          >
            {{ value }}
          </td>
          <td class="px-6 py-4">
            <button
              @click="fetchPriceLog(shipment.shipmentId)"
              class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
            >
              수락
            </button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>

  <!-- 가격 정보 모달 -->
  <div
    v-if="isModalOpen"
    class="fixed inset-0 flex items-center justify-center bg-gray-900 bg-opacity-50"
  >
    <div class="bg-white p-6 rounded shadow-lg">
      <h2 class="text-lg font-bold">Price Log 정보</h2>
      <p><strong>출발지:</strong> {{ selectedPriceLog?.origin }}</p>
      <p><strong>도착지:</strong> {{ selectedPriceLog?.destination }}</p>
      <p><strong>거리:</strong> {{ selectedPriceLog?.distance }} km</p>
      <p><strong>기본 요금:</strong> {{ selectedPriceLog?.simplePrice }} 원</p>
      <p><strong>계산된 요금:</strong> {{ selectedPriceLog?.calculatedPrice }} 원</p>
      <button @click="closeModal()" class="mt-4 px-4 py-2 bg-red-500 text-white rounded">
        닫기
      </button>
    </div>
  </div>
</template>
