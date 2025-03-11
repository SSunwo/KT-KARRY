<!-- eslint-disable vue/multi-word-component-names -->

<!-- eslint-disable vue/multi-word-component-names -->
<template>
  <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4 mb-4">
    <div class="flex items-center justify-center h-24 rounded bg-gray-50 dark:bg-gray-800">
      <p class="text-2xl text-gray-400 dark:text-gray-500">
        <svg
          class="w-8 h-8"
          aria-hidden="true"
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
      </p>
      <div class="ml-4">
        <div class="text-sm font-medium text-gray-500">Pending</div>
        <div class="text-xl font-semibold text-gray-900">{{ stats.pending }}</div>
      </div>
    </div>
    <div class="flex items-center justify-center h-24 rounded bg-gray-50 dark:bg-gray-800">
      <p class="text-2xl text-gray-400 dark:text-gray-500">
        <svg
          class="w-8 h-8"
          aria-hidden="true"
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
      </p>
      <div class="ml-4">
        <div class="text-sm font-medium text-gray-500">Completed</div>
        <div class="text-xl font-semibold text-gray-900">{{ stats.completed }}</div>
      </div>
    </div>
    <div class="flex items-center justify-center h-24 rounded bg-gray-50 dark:bg-gray-800">
      <p class="text-2xl text-gray-400 dark:text-gray-500">
        <svg
          class="w-8 h-8"
          aria-hidden="true"
          fill="none"
          stroke="currentColor"
          viewBox="0 0 24 24"
          xmlns="http://www.w3.org/2000/svg"
        >
          <path
            stroke-linecap="round"
            stroke-linejoin="round"
            stroke-width="2"
            d="M9 5H7a2 2 0 00-2 2v12a2 2 0 002 2h10a2 2 0 002-2V7a2 2 0 00-2-2h-2M9 5a2 2 0 002 2h2a2 2 0 002-2M9 5a2 2 0 012-2h2a2 2 0 012 2m-3 7h3m-3 4h3m-6-4h.01M9 16h.01"
          ></path>
        </svg>
      </p>
      <div class="ml-4">
        <div class="text-sm font-medium text-gray-500">Total Shipments</div>
        <div class="text-xl font-semibold text-gray-900">{{ stats.total }}</div>
      </div>
    </div>
    <div class="flex items-center justify-center h-24 rounded bg-gray-50 dark:bg-gray-800">
      <p class="text-2xl text-gray-400 dark:text-gray-500">
        <svg
          class="w-8 h-8"
          aria-hidden="true"
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
      </p>
      <div class="ml-4">
        <div class="text-sm font-medium text-gray-500">Revenue</div>
        <div class="text-xl font-semibold text-gray-900">₩{{ formatNumber(stats.revenue) }}</div>
      </div>
    </div>
  </div>

  <div class="grid grid-cols-1 gap-4 mb-4">
    <div class="relative overflow-x-auto shadow-md sm:rounded-lg">
      <div class="flex items-center justify-between p-4 bg-white dark:bg-gray-800">
        <div>
          <h2 class="text-xl font-semibold">Recent Shipments</h2>
        </div>
        <div v-if="isAuth" class="flex space-x-2">
          <router-link
            to="/shipment/register"
            class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700"
          >
            Register New Shipment
          </router-link>
          <router-link
            to="/shipment"
            class="px-4 py-2 bg-gray-200 text-gray-800 rounded-lg hover:bg-gray-300"
          >
            View All Shipments
          </router-link>
        </div>
      </div>

      <div v-if="isAuth">
        <ShipmentsTable />
      </div>
      <div v-else class="p-8 text-center">
        <h3 class="text-lg font-medium text-gray-700 mb-2">Welcome to KT-KARRY</h3>
        <p class="text-gray-500 mb-4">Please sign in to view and manage shipments</p>
        <div class="flex justify-center space-x-4">
          <router-link
            to="/signin"
            class="px-4 py-2 bg-blue-600 text-white rounded-lg hover:bg-blue-700"
          >
            Sign In
          </router-link>
          <router-link
            to="/signup"
            class="px-4 py-2 bg-gray-200 text-gray-800 rounded-lg hover:bg-gray-300"
          >
            Sign Up
          </router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { computed } from 'vue'
import { useStore } from 'vuex'
import ShipmentsTable from '@/components/ShipmentsTable.vue'

export default {
  components: {
    ShipmentsTable,
  },
  data() {
    return {
      stats: {
        pending: 12,
        completed: 42,
        total: 78,
        revenue: 12500000,
      },
    }
  },
  setup() {
    const store = useStore()

    // Vuex의 상태를 computed로 가져오기
    const isAuth = computed(() => store.getters.getIsAuth)

    return {
      isAuth,
    }
  },
  methods: {
    formatNumber(number) {
      return number.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',')
    },
  },
}
</script>
