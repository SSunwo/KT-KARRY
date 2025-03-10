import axios from 'axios'

const serverURL = 'http://localhost:8080'

// 로그인된 사용자의 Matching 목록 가져오기
const getUserMatchings = (userId) => {
  return axios.get(`${serverURL}/matching/user/${userId}`, {
    headers: { 'Content-Type': 'application/json' },
  })
}

export default {
  async getUserMatchingList(userId) {
    try {
      const response = await getUserMatchings(userId)
      return response.data
    } catch (err) {
      console.error(`Matching 목록 불러오기 실패: ${err}`)
      throw new Error('Matching 목록 불러오기 실패!')
    }
  },
}
