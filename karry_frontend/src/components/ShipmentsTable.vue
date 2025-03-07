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
          userNum: shipment.userNum, // 사용자 번호
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
        <!-- <tr v-for="(shipment, idx) in shipments" :key="idx" class="px-6 py-4">
          <th
            scope="row"
            class="px-6 py-4 font-medium text-gray-900 whitespace-nowrap dark:text-white"
          >
            {{ shipment.shipmentId }}
          </th>
          <td
            scope="row"
            class="px-6 py-4 font-medium text-gray-900 whitespace-nowrap dark:text-white"
          >
            {{ shipment.userNum }}
          </td>
          <td
            scope="row"
            class="px-6 py-4 font-medium text-gray-900 whitespace-nowrap dark:text-white"
          >
            {{ shipment.origin }}
          </td>
          <td
            scope="row"
            class="px-6 py-4 font-medium text-gray-900 whitespace-nowrap dark:text-white"
          >
            {{ shipment.destination }}
          </td>
          <td
            scope="row"
            class="px-6 py-4 font-medium text-gray-900 whitespace-nowrap dark:text-white"
          >
            {{ shipment.weight }}
          </td>
        </tr> -->
      </tbody>
    </table>
  </div>
</template>

<!-- <td v-for="(content, idx) in shipment" :key="idx" class="px-6 py-4"></td> -->
<!-- <tr
          class="odd:bg-white odd:dark:bg-gray-900 even:bg-gray-50 even:dark:bg-gray-800 border-b dark:border-gray-700 border-gray-200"
        >
          <th
            scope="row"
            class="px-6 py-4 font-medium text-gray-900 whitespace-nowrap dark:text-white"
          >
            Apple MacBook Pro 17"
          </th>
          <td class="px-6 py-4">Silver</td>
          <td class="px-6 py-4">Laptop</td>
          <td class="px-6 py-4">$2999</td>
          <td class="px-6 py-4">
            <a href="#" class="font-medium text-blue-600 dark:text-blue-500 hover:underline"
              >Edit</a
            >
          </td>
        </tr>
        <tr
          class="odd:bg-white odd:dark:bg-gray-900 even:bg-gray-50 even:dark:bg-gray-800 border-b dark:border-gray-700 border-gray-200"
        >
          <th
            scope="row"
            class="px-6 py-4 font-medium text-gray-900 whitespace-nowrap dark:text-white"
          >
            Apple MacBook Pro 17"
          </th>
          <td class="px-6 py-4">Silver</td>
          <td class="px-6 py-4">Laptop</td>
          <td class="px-6 py-4">$2999</td>
          <td class="px-6 py-4">
            <a href="#" class="font-medium text-blue-600 dark:text-blue-500 hover:underline"
              >Edit</a
            >
          </td>
        </tr>
        <tr
          class="odd:bg-white odd:dark:bg-gray-900 even:bg-gray-50 even:dark:bg-gray-800 border-b dark:border-gray-700 border-gray-200"
        >
          <th
            scope="row"
            class="px-6 py-4 font-medium text-gray-900 whitespace-nowrap dark:text-white"
          >
            Microsoft Surface Pro
          </th>
          <td class="px-6 py-4">White</td>
          <td class="px-6 py-4">Laptop PC</td>
          <td class="px-6 py-4">$1999</td>
          <td class="px-6 py-4">
            <a href="#" class="font-medium text-blue-600 dark:text-blue-500 hover:underline"
              >Edit</a
            >
          </td>
        </tr>
        <tr
          class="odd:bg-white odd:dark:bg-gray-900 even:bg-gray-50 even:dark:bg-gray-800 border-b dark:border-gray-700 border-gray-200"
        >
          <th
            scope="row"
            class="px-6 py-4 font-medium text-gray-900 whitespace-nowrap dark:text-white"
          >
            Magic Mouse 2
          </th>
          <td class="px-6 py-4">Black</td>
          <td class="px-6 py-4">Accessories</td>
          <td class="px-6 py-4">$99</td>
          <td class="px-6 py-4">
            <a href="#" class="font-medium text-blue-600 dark:text-blue-500 hover:underline"
              >Edit</a
            >
          </td>
        </tr>
        <tr
          class="odd:bg-white odd:dark:bg-gray-900 even:bg-gray-50 even:dark:bg-gray-800 border-b dark:border-gray-700 border-gray-200"
        >
          <th
            scope="row"
            class="px-6 py-4 font-medium text-gray-900 whitespace-nowrap dark:text-white"
          >
            Google Pixel Phone
          </th>
          <td class="px-6 py-4">Gray</td>
          <td class="px-6 py-4">Phone</td>
          <td class="px-6 py-4">$799</td>
          <td class="px-6 py-4">
            <a href="#" class="font-medium text-blue-600 dark:text-blue-500 hover:underline"
              >Edit</a
            >
          </td>
        </tr>
        <tr>
          <th
            scope="row"
            class="px-6 py-4 font-medium text-gray-900 whitespace-nowrap dark:text-white"
          >
            Apple Watch 5
          </th>
          <td class="px-6 py-4">Red</td>
          <td class="px-6 py-4">Wearables</td>
          <td class="px-6 py-4">$999</td>
          <td class="px-6 py-4">
            <a href="#" class="font-medium text-blue-600 dark:text-blue-500 hover:underline"
              >Edit</a
            >
          </td>
        </tr> -->
