export default {
  getUserId: (state) => {
    console.log('Vuex userId:', state.userId) // Vuex에서 값이 제대로 있는지 확인
    return state.userId
  },
  getUserRole: (state) => state.userRole,
  getErrorState: (state) => state.errorState,
  getIsAuth: (state) => state.isAuth,
  loggedIn(state) {
    return !!state.user
  },
}
