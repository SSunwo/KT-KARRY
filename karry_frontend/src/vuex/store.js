import { createStore } from 'vuex'
import getters from '@/vuex/getters'
import mutations from '@/vuex/mutation'
import actions from '@/vuex/actions'

export default createStore({
  state: {
    userId: localStorage.getItem('userId') || '', // Local Storage에서 불러오기
    userRole: localStorage.getItem('userRole') || '', // 사용자의 역할을 저장
    isAuth: localStorage.getItem('isAuth') === 'true', // 문자열 'true' 변환
    errorState: '',
  },
  mutations,
  getters,
  actions,
})
