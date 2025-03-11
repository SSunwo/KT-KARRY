<script>
export default {
  props: {
    modelValue: String,
    label: {
      type: String,
      default: '주소',
    },
    placeholder: {
      type: String,
      default: '주소를 선택하세요',
    },
  },
  emits: ['update:modelValue', 'address-selected'],
  data() {
    return {
      address: this.modelValue || '',
      detailAddress: '',
      isLoading: false,
      isApiLoaded: false,
      isPopupOpen: false,
      addressHistory: [],
      showHistory: false,
      error: null,
      addressData: null,
      animation: {
        pulse: false,
        shake: false,
      },
    }
  },
  computed: {
    combinedAddress() {
      if (!this.address) return ''
      return this.detailAddress ? `${this.address} ${this.detailAddress}` : this.address
    },
    hasAddress() {
      return !!this.address
    },
    formattedAddress() {
      if (!this.addressData) return null
      return {
        zonecode: this.addressData.zonecode,
        roadAddress: this.addressData.roadAddress,
        jibunAddress: this.addressData.jibunAddress,
        buildingName: this.addressData.buildingName,
      }
    },
  },
  methods: {
    checkApiLoaded() {
      if (window.daum && window.daum.Postcode) {
        this.isApiLoaded = true
        return true
      }
      return false
    },

    loadPostcodeApi() {
      if (this.checkApiLoaded()) return Promise.resolve()

      return new Promise((resolve, reject) => {
        const script = document.createElement('script')
        script.src = '//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js'
        script.onload = () => {
          this.isApiLoaded = true
          resolve()
        }
        script.onerror = () => {
          reject(new Error('Daum Postcode API 로드 실패'))
        }
        document.head.appendChild(script)
      })
    },

    async openPostcodePopup() {
      this.error = null
      this.isLoading = true
      this.animation.pulse = true

      try {
        if (!this.isApiLoaded) {
          await this.loadPostcodeApi()
        }

        this.isPopupOpen = true

        new window.daum.Postcode({
          oncomplete: (data) => {
            this.handleAddressSelection(data)
          },
          onclose: () => {
            this.isPopupOpen = false
          },
          width: '100%',
          height: '100%',
        }).open({
          popupName: 'KT-KARRY 주소검색',
          left: window.screen.width / 2 - window.innerWidth / 2,
          top: window.screen.height / 2 - window.innerHeight / 2,
        })
      } catch (err) {
        this.error = err.message || '주소 검색 중 오류가 발생했습니다.'
        this.animation.shake = true
        setTimeout(() => {
          this.animation.shake = false
        }, 500)
      } finally {
        this.isLoading = false
        setTimeout(() => {
          this.animation.pulse = false
        }, 500)
      }
    },

    handleAddressSelection(data) {
      this.addressData = data
      const roadAddr = data.roadAddress || data.jibunAddress

      // 주소 설정
      this.address = roadAddr
      this.detailAddress = ''

      // 부모 컴포넌트로 데이터 전송
      this.$emit('update:modelValue', this.combinedAddress)
      this.$emit('address-selected', this.formattedAddress)

      // 주소 기록에 추가 (최대 5개)
      if (!this.addressHistory.includes(roadAddr)) {
        this.addressHistory.unshift(roadAddr)
        if (this.addressHistory.length > 5) {
          this.addressHistory.pop()
        }
        // 로컬 스토리지에 저장
        try {
          localStorage.setItem('kt-karry-address-history', JSON.stringify(this.addressHistory))
        } catch (e) {
          console.error('로컬 스토리지 저장 실패:', e)
        }
      }

      // 팝업 닫기
      this.isPopupOpen = false

      // 상세주소 입력란에 포커스
      this.$nextTick(() => {
        const detailInput = this.$refs.detailInput
        if (detailInput) {
          detailInput.focus()
        }
      })
    },

    updateDetailAddress() {
      this.$emit('update:modelValue', this.combinedAddress)
    },

    selectHistoryAddress(address) {
      this.address = address
      this.detailAddress = ''
      this.$emit('update:modelValue', address)
      this.showHistory = false
    },

    clearAddress() {
      this.address = ''
      this.detailAddress = ''
      this.addressData = null
      this.$emit('update:modelValue', '')
    },
  },
  watch: {
    modelValue(newValue) {
      if (newValue !== this.combinedAddress) {
        this.address = newValue || ''
        this.detailAddress = ''
      }
    },
  },
  mounted() {
    // 주소 기록 불러오기
    try {
      const savedHistory = localStorage.getItem('kt-karry-address-history')
      if (savedHistory) {
        this.addressHistory = JSON.parse(savedHistory)
      }
    } catch (e) {
      console.error('주소 기록 불러오기 실패:', e)
    }

    // API 미리 로드
    this.loadPostcodeApi().catch((err) => {
      console.warn('Daum Postcode API 사전 로드 실패:', err)
    })
  },
}
</script>

<template>
  <div class="address-selector">
    <label v-if="label" class="block text-sm font-medium text-gray-700 mb-1">{{ label }}</label>

    <div class="relative">
      <!-- 주소 입력 필드 -->
      <div
        :class="[
          'transition-all duration-300 ease-in-out border rounded-lg overflow-hidden shadow-sm hover:shadow focus-within:shadow-md',
          hasAddress ? 'bg-white border-gray-300' : 'bg-gray-50 border-gray-200',
          animation.shake ? 'animate-shake' : '',
          animation.pulse ? 'animate-pulse' : '',
          error ? 'border-red-300' : 'focus-within:border-blue-500',
        ]"
      >
        <!-- 주소 표시 영역 -->
        <div class="flex flex-col sm:flex-row">
          <div class="relative flex-grow">
            <input
              v-model="address"
              type="text"
              :placeholder="placeholder"
              class="w-full p-3 pr-10 bg-transparent outline-none"
              readonly
              @click="openPostcodePopup"
            />
            <div v-if="hasAddress" class="absolute right-2 top-1/2 -translate-y-1/2 flex space-x-1">
              <button
                @click.stop="clearAddress"
                type="button"
                class="p-1 text-gray-400 hover:text-gray-600 rounded-full hover:bg-gray-100 transition-colors"
                title="주소 지우기"
              >
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  class="h-4 w-4"
                  fill="none"
                  viewBox="0 0 24 24"
                  stroke="currentColor"
                >
                  <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="2"
                    d="M6 18L18 6M6 6l12 12"
                  />
                </svg>
              </button>
            </div>
          </div>

          <div class="border-t sm:border-t-0 sm:border-l border-gray-200 flex-shrink-0">
            <button
              @click="openPostcodePopup"
              type="button"
              :disabled="isLoading"
              class="w-full sm:w-auto px-4 py-3 bg-blue-500 hover:bg-blue-600 active:bg-blue-700 text-white font-medium transition-colors flex items-center justify-center"
            >
              <span v-if="isLoading" class="flex items-center">
                <svg
                  class="animate-spin -ml-1 mr-2 h-4 w-4 text-white"
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
                검색 중...
              </span>
              <span v-else class="flex items-center">
                <svg
                  xmlns="http://www.w3.org/2000/svg"
                  class="h-4 w-4 mr-1"
                  fill="none"
                  viewBox="0 0 24 24"
                  stroke="currentColor"
                >
                  <path
                    stroke-linecap="round"
                    stroke-linejoin="round"
                    stroke-width="2"
                    d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z"
                  />
                </svg>
                주소 검색
              </span>
            </button>
          </div>
        </div>

        <!-- 상세 주소 입력 필드 (주소가 선택된 경우에만 표시) -->
        <div v-if="hasAddress" class="border-t border-gray-200">
          <input
            v-model="detailAddress"
            ref="detailInput"
            type="text"
            placeholder="상세 주소를 입력하세요"
            class="w-full p-3 bg-transparent outline-none"
            @input="updateDetailAddress"
          />
        </div>
      </div>

      <!-- 주소 기록 드롭다운 -->
      <div v-if="addressHistory.length > 0" class="relative">
        <button
          v-if="!showHistory"
          @click="showHistory = true"
          type="button"
          class="mt-1 text-xs text-gray-500 hover:text-gray-700 flex items-center"
        >
          <svg
            xmlns="http://www.w3.org/2000/svg"
            class="h-3 w-3 mr-1"
            fill="none"
            viewBox="0 0 24 24"
            stroke="currentColor"
          >
            <path
              stroke-linecap="round"
              stroke-linejoin="round"
              stroke-width="2"
              d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z"
            />
          </svg>
          최근 주소 기록
        </button>

        <div
          v-if="showHistory"
          class="absolute z-10 mt-1 w-full bg-white border border-gray-200 rounded-md shadow-lg py-1 max-h-60 overflow-auto"
        >
          <div
            class="px-3 py-2 text-xs font-medium text-gray-500 border-b border-gray-100 flex justify-between items-center"
          >
            <span>최근 사용한 주소</span>
            <button
              @click="showHistory = false"
              type="button"
              class="text-gray-400 hover:text-gray-600"
            >
              <svg
                xmlns="http://www.w3.org/2000/svg"
                class="h-4 w-4"
                fill="none"
                viewBox="0 0 24 24"
                stroke="currentColor"
              >
                <path
                  stroke-linecap="round"
                  stroke-linejoin="round"
                  stroke-width="2"
                  d="M6 18L18 6M6 6l12 12"
                />
              </svg>
            </button>
          </div>
          <div
            v-for="(historyAddress, index) in addressHistory"
            :key="index"
            class="px-3 py-2 hover:bg-gray-50"
          >
            <button
              @click="selectHistoryAddress(historyAddress)"
              type="button"
              class="w-full text-left text-sm text-gray-700 truncate"
            >
              {{ historyAddress }}
            </button>
          </div>
        </div>
      </div>

      <!-- 에러 메시지 -->
      <div v-if="error" class="mt-1 text-sm text-red-600">
        {{ error }}
      </div>

      <!-- 주소 상세 정보 (선택된 경우) -->
      <div v-if="addressData && hasAddress" class="mt-2 text-xs text-gray-500 space-y-1">
        <div v-if="addressData.zonecode" class="flex">
          <span class="font-medium w-20">우편번호:</span>
          <span>{{ addressData.zonecode }}</span>
        </div>
        <div v-if="addressData.jibunAddress" class="flex">
          <span class="font-medium w-20">지번주소:</span>
          <span>{{ addressData.jibunAddress }}</span>
        </div>
        <div v-if="addressData.buildingName" class="flex">
          <span class="font-medium w-20">건물명:</span>
          <span>{{ addressData.buildingName }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.animate-shake {
  animation: shake 0.5s cubic-bezier(0.36, 0.07, 0.19, 0.97) both;
}

@keyframes shake {
  0%,
  100% {
    transform: translateX(0);
  }
  10%,
  30%,
  50%,
  70%,
  90% {
    transform: translateX(-5px);
  }
  20%,
  40%,
  60%,
  80% {
    transform: translateX(5px);
  }
}

/* 트랜지션 효과 */
.address-selector {
  transition: all 0.3s ease;
}

input {
  transition:
    background-color 0.3s ease,
    border-color 0.3s ease,
    box-shadow 0.3s ease;
}

button {
  transition:
    background-color 0.2s ease,
    transform 0.1s ease,
    opacity 0.2s ease;
}

button:active:not(:disabled) {
  transform: scale(0.98);
}
</style>
