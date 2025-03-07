<script>
import registAPI from '@/service/registAPI'
import ShipmentTable from '@/components/ShipmentsTable.vue'

export default {
  data() {
    return {
      shipments: [],
    }
  },
  components: {
    ShipmentTable,
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
  <ShipmentTable />
</template>

<!--  예전...
  <div class="p-4">
    <h2 class="text-lg font-semibold">Shipment List</h2>
    <ul class="mt-4 space-y-2">
      <li v-for="(shipment, index) in shipments" :key="index" class="p-2 border rounded">
        {{ shipment.userNum }} - {{ shipment.origin }} → {{ shipment.destination }}
      </li>
    </ul>
  </div> -->
