<script>
export default {
  props: {
    modelValue: String, // ✅ v-model을 통해 부모에서 받은 값 (origin 또는 destination)
  },
  emits: ['update:modelValue'], // ✅ 부모 컴포넌트에 데이터 전달
  data() {
    return {
      address: this.modelValue || '', // ✅ 초기 데이터
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

          // ✅ 부모 컴포넌트로 데이터 전송
          this.address = roadAddr
          this.$emit('update:modelValue', this.address)
        },
      }).open()
    },
  },
  watch: {
    modelValue(newValue) {
      this.address = newValue
    },
  },
}
</script>

<template>
  <div>
    <div class="flex">
      <input
        v-model="address"
        type="text"
        placeholder="주소를 선택하세요"
        class="p-2 border rounded w-full"
        readonly
      />
      <button @click="openPostcodePopup" class="ml-2 p-2 bg-blue-500 text-white rounded">
        주소 검색
      </button>
    </div>
  </div>
</template>

<style scoped>
button {
  cursor: pointer;
}
</style>
