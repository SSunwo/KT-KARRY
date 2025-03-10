<script>
import getListAPI from '@/service/getListAPI'
import registAPI from '@/service/registAPI'

export default {
  data() {
    return {
      userId: null, // 로그인된 유저의 ID
      matchings: [],
    }
  },
  methods: {
    async fetchUserMatchings() {
      try {
        if (!this.userId) {
          alert('로그인이 필요합니다!')
          return
        }

        const res = await getListAPI.getUserMatchingList(this.userId)
        console.log('매칭된 운송 목록:', res)
        this.matchings = res
      } catch (error) {
        console.error('매칭 목록 불러오기 실패:', error)
        alert('매칭된 운송 목록을 불러오는 데 실패했습니다.')
      }
    },
    // 배송 완료 처리
    async completeMatching(matching) {
      try {
        await registAPI.updateMatchingStatus(matching.matchingId, 'Completed')
        await registAPI.updateShipmentStatus(matching.shipmentId, 'Completed')
        alert('✅ 배송 완료 처리되었습니다!')
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
    // 로그인된 유저의 userId를 가져와서 조회 (예시: 로컬 스토리지에서 가져오기)
    this.userId = localStorage.getItem('userId') // 예시: 로그인된 userId 저장 방식에 따라 다름

    if (this.userId) {
      this.fetchUserMatchings()
    } else {
      alert('로그인이 필요합니다!')
    }
  },
}
</script>

<template>
  <div class="container mx-auto p-6">
    <h1 class="text-2xl font-bold">🚚 나의 운송 매칭 내역</h1>

    <div v-if="matchings.length === 0" class="text-gray-500 mt-4">매칭된 운송 내역이 없습니다.</div>

    <table v-else class="w-full mt-6 border-collapse border border-gray-300">
      <thead>
        <tr class="bg-gray-100">
          <th class="border p-2">매칭 ID</th>
          <th class="border p-2">출발지</th>
          <th class="border p-2">도착지</th>
          <th class="border p-2">거리 (km)</th>
          <th class="border p-2">금액 (원)</th>
          <th class="border p-2">상태</th>
          <th class="border p-2">매칭일</th>
          <th class="border p-2">버튼..</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="matching in matchings" :key="matching.matchingId" class="border">
          <td class="border p-2">{{ matching.matchingId }}</td>
          <td class="border p-2">{{ matching.origin }}</td>
          <td class="border p-2">{{ matching.destination }}</td>
          <td class="border p-2">{{ matching.distance }}</td>
          <td class="border p-2">{{ matching.amount }}</td>
          <td class="border p-2">{{ matching.status }}</td>
          <td class="border p-2">{{ new Date(matching.acceptedAt).toLocaleString() }}</td>
          <td class="border p-2">
            <button
              v-if="matching.status === 'shipping'"
              @click="completeMatching(matching)"
              class="mt-4 px-4 py-2 bg-green-500 text-white rounded"
            >
              배송 완료
            </button>
            <button
              v-if="matching.status === 'shipping'"
              @click="cancelMatching(matching)"
              class="mt-4 px-4 py-2 bg-red-500 text-white rounded"
            >
              매칭 취소
            </button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>
