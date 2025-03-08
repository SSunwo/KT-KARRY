<script>
export default {
  props: {
    modelValue: String,
  },
  emits: ['update:modelValue'], // ✅ 부모 컴포넌트에 데이터 전달
  data() {
    return {
      addressData: {}, // 우편번호 검색 데이터를 저장할 객체
      postcode: this.modelValue?.postcode || '',
      roadAddress: this.modelValue?.roadAddress || '',
      jibunAddress: this.modelValue?.jibunAddress || '',
      detailAddress: this.modelValue?.detailAddress || '',
      extraAddress: this.modelValue?.extraAddress || '',
    }
  },
  methods: {
    openPostcodePopup() {
      if (!window.daum || !window.daum.Postcode) {
        alert('주소 검색 API를 불러오지 못했습니다. 페이지를 새로고침 해주세요.')
        return
      }

      new window.daum.Postcode({
        oncomplete: (data) => {
          let roadAddr = data.roadAddress // 도로명 주소
          let extraRoadAddr = '' // 참고항목

          if (data.bname !== '' && /[동|로|가]$/g.test(data.bname)) {
            extraRoadAddr += data.bname
          }
          if (data.buildingName !== '' && data.apartment === 'Y') {
            extraRoadAddr += extraRoadAddr !== '' ? ', ' + data.buildingName : data.buildingName
          }
          if (extraRoadAddr !== '') {
            extraRoadAddr = ' (' + extraRoadAddr + ')'
          }

          // ✅ 데이터 업데이트
          this.postcode = data.zonecode
          this.roadAddress = roadAddr
          this.jibunAddress = data.jibunAddress
          this.extraAddress = extraRoadAddr

          // ✅ 부모 컴포넌트에 데이터 전송
          this.$emit('update:modelValue', {
            postcode: this.postcode,
            roadAddress: this.roadAddress,
            jibunAddress: this.jibunAddress,
            detailAddress: this.detailAddress,
            extraAddress: this.extraAddress,
          })
        },
      }).open()
    },
  },
  watch: {
    modelValue: {
      deep: true,
      handler(newValue) {
        this.postcode = newValue?.postcode || ''
        this.roadAddress = newValue?.roadAddress || ''
        this.jibunAddress = newValue?.jibunAddress || ''
        this.detailAddress = newValue?.detailAddress || ''
        this.extraAddress = newValue?.extraAddress || ''
      },
    },
  },
}
</script>

<template>
  <div class="flex flex-col items-center justify-center min-h-screen">
    <h2 class="text-xl font-semibold">주 소 검 색</h2>
    <form class="mt-6 space-y-4" @submit.prevent="submitForm">
      <div>
        <div class="flex">
          <input
            v-model="postcode"
            type="text"
            placeholder="우편번호"
            class="p-2 border rounded w-full"
            readonly
          />
          <button @click="openPostcodePopup" class="ml-2 p-2 bg-blue-500 text-white rounded">
            우편번호 찾기
          </button>
        </div>
        <input
          v-model="roadAddress"
          type="text"
          placeholder="도로명 주소"
          class="p-2 border rounded w-full mt-2"
          readonly
        />
        <!-- <input
      v-model="jibunAddress"
      type="text"
      placeholder="지번 주소"
      class="p-2 border rounded w-full mt-2"
      readonly
    /> -->
        <input
          v-model="detailAddress"
          type="text"
          placeholder="상세 주소"
          class="p-2 border rounded w-full mt-2"
        />
        <!-- <input
      v-model="extraAddress"
      type="text"
      placeholder="참고 항목"
      class="p-2 border rounded w-full mt-2"
      readonly
    /> -->
      </div>
      <button type="submit" class="w-full p-2 bg-indigo-600 text-white rounded">
        Register Shipment
      </button>
    </form>
  </div>
</template>

<style scoped>
button {
  cursor: pointer;
}
</style>
