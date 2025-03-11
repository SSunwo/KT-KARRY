import axios from 'axios'

const serverURL = 'http://localhost:8080'

// 로그인된 사용자의 Matching 목록 가져오기
const getUserMatchings = (userId) => {
  return axios.get(`${serverURL}/matching/accepted/${userId}`, {
    headers: { 'Content-Type': 'application/json' },
  })
}

// dashboard용 api
const getCarrierDashboardData = async (userId) => {
  try {
    const [transactionsRes, unsettledRes, settledRes] = await Promise.all([
      axios.get(`${serverURL}/transaction/dashboard/carrier/${userId}`),
      axios.get(`${serverURL}/transaction/dashboard/carrier/${userId}/unsettled`),
      axios.get(`${serverURL}/transaction/dashboard/carrier/${userId}/settled`),
    ])

    return {
      transactions: transactionsRes.data,
      unsettledAmount: unsettledRes.data.unsettledAmount,
      settledAmount: settledRes.data.settledAmount,
    }
  } catch (error) {
    console.error('🚨 차주 대시보드 데이터 가져오기 실패:', error)
    throw new Error('차주 대시보드 데이터를 불러오는 중 오류가 발생했습니다.')
  }
}

const getShipperDashboardData = async (userId) => {
  try {
    const [pendingRes, paymentRes] = await Promise.all([
      axios.get(`${serverURL}/transaction/dashboard/shipper/${userId}/pending`),
      axios.get(`${serverURL}/transaction/dashboard/shipper/${userId}/payment`),
    ])

    return {
      pendingShipments: pendingRes.data,
      totalPayment: paymentRes.data.totalPayment,
    }
  } catch (error) {
    console.error('🚨 화주 대시보드 데이터 가져오기 실패:', error)
    throw new Error('화주 대시보드 데이터를 불러오는 중 오류가 발생했습니다.')
  }
}

// Fact Table 기반 대시보드 데이터 가져오기
const getDashboardData = async (userId) => {
  try {
    const response = await axios.get(`${serverURL}/fact/dashboard/${userId}`)
    return response.data
  } catch (error) {
    console.error('🚨 대시보드 데이터 가져오기 실패:', error)
    throw new Error('대시보드 데이터를 불러오는 중 오류가 발생했습니다.')
  }
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

  getCarrierDashboardData,
  getShipperDashboardData,
  getDashboardData,
}
