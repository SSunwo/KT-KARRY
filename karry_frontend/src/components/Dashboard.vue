<!-- eslint-disable vue/multi-word-component-names -->
<script>
import { mapGetters } from 'vuex'
import getListAPI from '@/service/getListAPI'

export default {
  data() {
    return {
      isLoading: true,
      error: null,
      carrierShipments: [],
      shipperShipments: [],
      unsettledAmount: 0,
      settledAmount: 0,
      totalPayment: 0,
      recentShipments: [],
    }
  },

  computed: {
    ...mapGetters({
      userId: 'getUserId',
      userRole: 'getUserRole',
    }),

    isCarrier() {
      return this.userRole === 'carrier'
    },

    isShipper() {
      return this.userRole === 'shipper'
    },

    totalShipments() {
      return this.isCarrier ? this.carrierShipments.length : this.shipperShipments.length
    },

    pendingShipments() {
      const shipments = this.isCarrier ? this.carrierShipments : this.shipperShipments
      return shipments.filter((s) => s.shipmentStatus === 'Pending').length
    },

    completedShipments() {
      const shipments = this.isCarrier ? this.carrierShipments : this.shipperShipments
      return shipments.filter((s) => s.shipmentStatus === 'Completed').length
    },
  },

  methods: {
    async fetchDashboardData() {
      this.isLoading = true
      this.error = null

      try {
        console.log('📢 Fetching Dashboard Data...')
        console.log('User Role:', this.userRole)
        console.log('User ID:', this.userId)

        const response = await getListAPI.getDashboardData(this.userId)
        console.log('📦 Dashboard Response:', response)

        if (this.isCarrier) {
          this.carrierShipments = response.carrierShipments || []
          this.unsettledAmount = response.unsettledAmount || 0
          this.settledAmount = response.settledAmount || 0

          // 날짜 기준으로 정렬 후 상위 5개 선택
          this.recentShipments = [...this.carrierShipments]
            .sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt))
            .slice(0, 5)
        } else if (this.isShipper) {
          this.shipperShipments = response.shipperShipments || []
          this.totalPayment = response.totalPayment || 0

          // 날짜 기준으로 정렬 후 상위 5개 선택
          this.recentShipments = [...this.shipperShipments]
            .sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt))
            .slice(0, 5)
        }
      } catch (error) {
        console.error('📌 대시보드 데이터 불러오기 실패:', error)
        this.error = '데이터를 불러오는 중 오류가 발생했습니다.'
      } finally {
        this.isLoading = false
      }
    },

    formatPrice(value) {
      return `₩${Number(value).toLocaleString()}`
    },

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

    truncateText(text, length = 20) {
      if (!text) return ''
      return text.length > length ? text.substring(0, length) + '...' : text
    },
  },

  mounted() {
    this.fetchDashboardData()
  },
}
</script>

<template>
  <div>
    <!-- Loading State -->
    <div v-if="isLoading" class="flex justify-center items-center h-64">
      <div
        class="inline-block animate-spin rounded-full h-12 w-12 border-4 border-gray-300 border-t-blue-600"
      ></div>
      <span class="ml-3 text-gray-600">데이터를 불러오는 중...</span>
    </div>

    <!-- Error State -->
    <div v-else-if="error" class="bg-red-50 p-4 rounded-lg text-center">
      <div class="text-red-500 mb-2">{{ error }}</div>
      <button
        @click="fetchDashboardData"
        class="px-4 py-2 bg-blue-500 text-white rounded-lg hover:bg-blue-600"
      >
        다시 시도
      </button>
    </div>

    <!-- Dashboard Content -->
    <div v-else>
      <!-- Welcome Message -->
      <div class="mb-6">
        <h1 class="text-2xl font-bold text-gray-900">안녕하세요, {{ userId }}님!</h1>
        <p class="text-gray-600">
          {{ isCarrier ? '운송사' : '화주' }} 대시보드에 오신 것을 환영합니다.
        </p>
      </div>

      <!-- Stats Cards -->
      <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4 mb-6">
        <!-- Total Shipments -->
        <div class="bg-white rounded-lg shadow p-4 flex items-center">
          <div class="rounded-full bg-blue-100 p-3 mr-4">
            <svg
              class="w-6 h-6 text-blue-600"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
              xmlns="http://www.w3.org/2000/svg"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2"
              ></path>
            </svg>
          </div>
          <div>
            <p class="text-sm font-medium text-gray-500">총 배송</p>
            <p class="text-xl font-semibold">{{ totalShipments }}</p>
          </div>
        </div>

        <!-- Pending Shipments -->
        <div class="bg-white rounded-lg shadow p-4 flex items-center">
          <div class="rounded-full bg-yellow-100 p-3 mr-4">
            <svg
              class="w-6 h-6 text-yellow-600"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
              xmlns="http://www.w3.org/2000/svg"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"
              ></path>
            </svg>
          </div>
          <div>
            <p class="text-sm font-medium text-gray-500">대기 중</p>
            <p class="text-xl font-semibold">{{ pendingShipments }}</p>
          </div>
        </div>

        <!-- Completed Shipments -->
        <div class="bg-white rounded-lg shadow p-4 flex items-center">
          <div class="rounded-full bg-green-100 p-3 mr-4">
            <svg
              class="w-6 h-6 text-green-600"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
              xmlns="http://www.w3.org/2000/svg"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="M5 13l4 4L19 7"
              ></path>
            </svg>
          </div>
          <div>
            <p class="text-sm font-medium text-gray-500">완료</p>
            <p class="text-xl font-semibold">{{ completedShipments }}</p>
          </div>
        </div>

        <!-- Financial Info (Different based on role) -->
        <div class="bg-white rounded-lg shadow p-4 flex items-center">
          <div class="rounded-full bg-indigo-100 p-3 mr-4">
            <svg
              class="w-6 h-6 text-indigo-600"
              fill="none"
              stroke="currentColor"
              viewBox="0 0 24 24"
              xmlns="http://www.w3.org/2000/svg"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z"
              ></path>
            </svg>
          </div>
          <div v-if="isCarrier">
            <p class="text-sm font-medium text-gray-500">미정산 금액</p>
            <p class="text-xl font-semibold">{{ formatPrice(unsettledAmount) }}</p>
          </div>
          <div v-else>
            <p class="text-sm font-medium text-gray-500">총 지불액</p>
            <p class="text-xl font-semibold">{{ formatPrice(totalPayment) }}</p>
          </div>
        </div>
      </div>

      <!-- Additional Stats for Carrier -->
      <div v-if="isCarrier" class="grid grid-cols-1 md:grid-cols-2 gap-4 mb-6">
        <div class="bg-white rounded-lg shadow p-4">
          <h2 class="text-lg font-semibold mb-2">정산 현황</h2>
          <div class="flex items-center justify-between">
            <div>
              <p class="text-sm text-gray-500">정산 완료</p>
              <p class="text-xl font-bold text-green-600">{{ formatPrice(settledAmount) }}</p>
            </div>
            <div>
              <p class="text-sm text-gray-500">미정산</p>
              <p class="text-xl font-bold text-yellow-600">{{ formatPrice(unsettledAmount) }}</p>
            </div>
            <div>
              <p class="text-sm text-gray-500">총액</p>
              <p class="text-xl font-bold">{{ formatPrice(settledAmount + unsettledAmount) }}</p>
            </div>
          </div>
        </div>

        <div class="bg-white rounded-lg shadow p-4">
          <h2 class="text-lg font-semibold mb-2">배송 현황</h2>
          <div class="relative pt-1">
            <div class="flex mb-2 items-center justify-between">
              <div>
                <span
                  class="text-xs font-semibold inline-block py-1 px-2 uppercase rounded-full text-green-600 bg-green-200"
                >
                  완료율
                </span>
              </div>
              <div class="text-right">
                <span class="text-xs font-semibold inline-block text-green-600">
                  {{
                    totalShipments ? Math.round((completedShipments / totalShipments) * 100) : 0
                  }}%
                </span>
              </div>
            </div>
            <div class="overflow-hidden h-2 mb-4 text-xs flex rounded bg-green-200">
              <div
                :style="`width: ${totalShipments ? (completedShipments / totalShipments) * 100 : 0}%`"
                class="shadow-none flex flex-col text-center whitespace-nowrap text-white justify-center bg-green-500"
              ></div>
            </div>
          </div>
        </div>
      </div>

      <!-- Recent Shipments Table -->
      <div class="bg-white rounded-lg shadow overflow-hidden">
        <div class="px-4 py-5 border-b border-gray-200 sm:px-6 flex justify-between items-center">
          <h3 class="text-lg leading-6 font-medium text-gray-900">최근 배송 내역</h3>
          <router-link
            :to="isCarrier ? '/matching' : '/shipment'"
            class="px-4 py-2 bg-blue-600 text-white text-sm rounded-md hover:bg-blue-700"
          >
            전체 보기
          </router-link>
        </div>

        <div class="overflow-x-auto">
          <table class="min-w-full divide-y divide-gray-200">
            <thead class="bg-gray-50">
              <tr>
                <th
                  scope="col"
                  class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                >
                  ID
                </th>
                <th
                  scope="col"
                  class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                >
                  출발지
                </th>
                <th
                  scope="col"
                  class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                >
                  도착지
                </th>
                <th
                  scope="col"
                  class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                >
                  금액
                </th>
                <th
                  scope="col"
                  class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                >
                  상태
                </th>
                <th
                  scope="col"
                  class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                >
                  날짜
                </th>
              </tr>
            </thead>
            <tbody class="bg-white divide-y divide-gray-200">
              <tr v-if="recentShipments.length === 0">
                <td colspan="6" class="px-6 py-4 text-center text-sm text-gray-500">
                  배송 내역이 없습니다.
                </td>
              </tr>
              <tr
                v-for="shipment in recentShipments"
                :key="shipment.shipmentId"
                class="hover:bg-gray-50"
              >
                <td class="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                  {{ shipment.shipmentId }}
                </td>
                <td class="px-6 py-4 text-sm text-gray-500">
                  {{ truncateText(shipment.shipmentOrigin) }}
                </td>
                <td class="px-6 py-4 text-sm text-gray-500">
                  {{ truncateText(shipment.shipmentDestination) }}
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                  {{ formatPrice(shipment.amount || shipment.calculatedPrice) }}
                </td>
                <td class="px-6 py-4 whitespace-nowrap">
                  <span
                    :class="getStatusClass(shipment.shipmentStatus)"
                    class="px-2 py-1 inline-flex text-xs leading-5 font-semibold rounded-full"
                  >
                    {{ shipment.shipmentStatus }}
                  </span>
                </td>
                <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                  {{ formatDate(shipment.createdAt) }}
                </td>
              </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
/* Add any component-specific styles here */
</style>
