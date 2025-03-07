<script>
import registAPI from '@/service/registAPI'

export default {
  data() {
    return {
      shipments: [],
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
        </tr>
      </tbody>
    </table>
  </div>
</template>
