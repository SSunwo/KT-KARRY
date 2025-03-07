import { USER_ID, IS_AUTH, ERROR_STATE } from './mutation_types'
import loginAPI from '../service/loginAPI'
import router from '@/router'

let setUserId = ({ commit }, data) => {
  commit(USER_ID, data)
  localStorage.setItem('userId', data) // Local Storage 저장
}

let setErrorState = ({ commit }, data) => {
  commit(ERROR_STATE, data)
}

let setIsAuth = ({ commit }, data) => {
  commit(IS_AUTH, data)
  localStorage.setItem('isAuth', data) // Local Storage 저장
}

// 백엔드에서 반환한 결과값을 가지고 로그인 성공 실패 여부를 vuex에 넣어준다.
let processResponse = (store, loginResponse) => {
  console.log('processResponse에서 loginResponse:', loginResponse) // ✅ 응답 데이터 확인

  if (loginResponse === 'notFound') {
    setErrorState(store, 'Wrong ID or Password')
    setIsAuth(store, false)
    localStorage.removeItem('userId') // 로그인 실패 시 제거
    localStorage.removeItem('isAuth')
  } else {
    setUserId(store, loginResponse.data.user_id)
    setErrorState(store, '')
    setIsAuth(store, true)

    console.log('✅ 저장된 user_id:', store.getters.getUserId) // ✅ Vuex에 user_id가 정상 저장되는지 확인
    router.push('/')
  }
}

export default {
  async login(store, { user_id, user_pw }) {
    let loginResponse = await loginAPI.doLogin(user_id, user_pw)

    console.log('백엔드 로그인 응답:', loginResponse.data) // ✅ 응답 데이터 확인

    processResponse(store, loginResponse)
    return store.getters.getIsAuth // 로그인 결과를 리턴
  },

  logout({ commit }) {
    commit(USER_ID, null)
    commit(IS_AUTH, false)
    localStorage.removeItem('userId') // Local Storage 제거
    localStorage.removeItem('isAuth')
    router.push('/signin')
  },
}
