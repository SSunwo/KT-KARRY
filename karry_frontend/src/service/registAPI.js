import axios from 'axios'

const serverURL = 'http://localhost:8080'

// 배차 등록 API 호출
const registShipment = (user_id, origin, destination, weight, size, price, status) => {
  const reqData = {
    userId: user_id,
    origin: origin,
    destination: destination,
    weight: weight,
    size: size,
    price: price,
    status: status,
  }

  return axios.post(serverURL + '/shipment', reqData, {
    headers: {
      'Content-Type': 'application/json',
    },
  })
}

// 배차 목록 전부 가져오기 API 호출
const getShipments = () => {
  return axios.get(serverURL + '/shipment', {
    headers: {
      'Content-Type': 'application/json',
    },
  })
}
// PriceLog 가져오기 API 호출
const getPricelog = (shipmentId) => {
  return axios.get(serverURL + '/pricelog/' + shipmentId, {
    headers: {
      'Content-Type': 'application/json',
    },
  })
}

/// 운송 매칭 API 호출
const assignMatching = (shipmentId, createdBy, acceptedBy) => {
  return axios.post(
    `${serverURL}/matching/create`,
    { shipmentId: shipmentId, createdBy: createdBy, acceptedBy: acceptedBy },
    {
      headers: {
        'Content-Type': 'application/json',
      },
    },
  )
}

// Shipment 상태 변경 API
const patchShipmentStatus = (shipmentId, newStatus) => {
  console.log(`🔄 PATCH 요청: shipmentId=${shipmentId}, newStatus=${newStatus}`) // DEBUG 추가

  return axios.patch(
    `http://localhost:8080/shipment/${shipmentId}`,
    { status: newStatus },
    {
      headers: { 'Content-Type': 'application/json' },
    },
  )
}

// Matching 상태 변경 API
const updateMatchingStatus = (matchingId, newStatus) => {
  return axios.patch(
    `${serverURL}/matching/${matchingId}`,
    { status: newStatus },
    { headers: { 'Content-Type': 'application/json' } },
  )
}

// 거래 내역 생성 API (배송 수락 시 호출)
const createTransaction = (matchingId, pricelogId, amount, createdBy, acceptedBy) => {
  return axios.post(
    `${serverURL}/transaction/create`,
    {
      matchingId: matchingId,
      pricelogId: pricelogId,
      amount: amount,
      createdBy: createdBy,
      acceptedBy: acceptedBy,
    },
    {
      headers: { 'Content-Type': 'application/json' },
    },
  )
}

// 거래 상태 업데이트 API (배송 완료 시 호출)
const updateTransactionStatus = (matchingId, newStatus) => {
  return axios.patch(
    `${serverURL}/transaction/status/${matchingId}`,
    {
      status: newStatus,
    },
    {
      headers: { 'Content-Type': 'application/json' },
    },
  )
}

// // 거래 정산 API
// const completeTransaction = (transactionId) => {
//   return axios.patch(
//     `${serverURL}/transaction/complete/${transactionId}`,
//     {},
//     {
//       headers: { 'Content-Type': 'application/json' },
//     },
//   )
// }

// 매칭 ID를 이용하여 transactionId 가져오는 API
const getTransactionIdByMatchingId = async (matchingId) => {
  try {
    console.log(`거래 ID 조회 요청: matchingId=${matchingId}`)
    const response = await axios.get(`${serverURL}/transaction/matching/${matchingId}`)

    if (!response.data || !response.data.transactionId) {
      console.error('거래 ID를 찾을 수 없습니다!', response.data)
      return null
    }

    console.log(`거래 ID 조회 성공: ${response.data.transactionId}`)
    return response.data.transactionId
  } catch (error) {
    console.error(`거래 ID 조회 실패: ${error.response?.data || error.message}`)
    return null
  }
}

// 🚀 ShipmentId로 MatchingId 조회 API
const getMatchingIdByShipmentId = async (shipmentId) => {
  try {
    console.log(`📌 매칭 ID 조회 요청: shipmentId=${shipmentId}`)
    const response = await axios.get(`${serverURL}/matching/shipment/${shipmentId}`)

    if (!response.data || !response.data.matchingId) {
      console.error('🚨 매칭 ID를 찾을 수 없습니다!', response.data)
      return null
    }

    return response.data.matchingId
  } catch (error) {
    console.error(`🚨 매칭 ID 조회 실패: ${error}`)
    return null
  }
}

export default {
  async doRegist(user_id, origin, destination, weight, size, price, status) {
    try {
      const registPromise = registShipment(
        user_id,
        origin,
        destination,
        weight,
        size,
        price,
        status,
      )
      const [registResponse] = await Promise.all([registPromise])
      if (registResponse.data.length === 0) {
        return 'notFound'
      } else {
        return registResponse
      }
    } catch (err) {
      console.error(err)
      throw new Error('등록 실패....')
    }
  },

  async getShipmentsList() {
    try {
      const shipmentPromise = getShipments()
      const [shipmentResponse] = await Promise.all([shipmentPromise])
      if (shipmentResponse.data.length === 0) {
        return 'list not found'
      } else {
        return shipmentResponse
      }
    } catch (err) {
      console.error(err)
      throw new Error('배차 목록 불러오기 실패...')
    }
  },

  // 특정 배송 요청의 PriceLog 정보 가져오기
  // 백엔드에서 데이터를 반환하도록 수정
  async getPricelog(shipmentId) {
    try {
      console.log(`API 호출: ${serverURL}/pricelog/${shipmentId}`) // 요청 URL 확인
      const priceLogResponse = await getPricelog(shipmentId) // API 호출

      console.log('API 응답:', priceLogResponse) // 응답 객체 전체 확인

      if (!priceLogResponse || !priceLogResponse.data) {
        console.error(`PriceLog 데이터가 존재하지 않습니다. shipmentId: ${shipmentId}`)
        return null // `undefined`가 아니라 `null` 반환
      }

      return priceLogResponse.data // ✅ `data`만 반환하도록 수정
    } catch (err) {
      console.error(`PriceLog 불러오기 실패 (shipmentId: ${shipmentId}):`, err)
      throw new Error('PriceLog 정보를 불러오는데 실패했습니다.')
    }
  },

  async createMatching(shipmentId, createdBy, acceptedBy) {
    try {
      const response = await assignMatching(shipmentId, createdBy, acceptedBy)

      if (!(response.status === 201 || response.status === 200)) {
        console.error('예상치 못한 응답:', response)
        throw new Error('운송 매칭 실패: 서버 응답이 올바르지 않습니다.')
      }
    } catch (err) {
      console.error('운송 매칭 실패:', err)
      throw new Error('서버 오류: 운송 매칭 중 문제가 발생했습니다.')
    }
  },

  // shipment 상태 변경
  async updateShipmentStatus(shipmentId, newStatus) {
    try {
      // DEBUG : shipmentId와 newStatus 값 확인
      console.log(`📦 상태 업데이트 요청: shipmentId=${shipmentId}, newStatus=${newStatus}`)

      if (!shipmentId) {
        throw new Error('shipmentId가 유효하지 않습니다!')
      }

      const response = await patchShipmentStatus(shipmentId, newStatus)
      console.log('patchShipmentStatus response로그입니다' + response)
      console.log('patchShipmentStatus response.data로그입니다' + response.data)
      return response.data
    } catch (err) {
      console.error(`Shipment 상태 변경 실패: ${err}`)
      throw new Error('Shipment 상태 변경 실패!')
    }
  },

  // matching 상태 변경
  async updateMatchingStatus(matchingId, newStatus) {
    try {
      const response = await updateMatchingStatus(matchingId, newStatus)
      return response.data
    } catch (err) {
      console.error(`Matching 상태 변경 실패: ${err}`)
      throw new Error('Matching 상태 변경 실패!')
    }
  },

  // ✅ 거래 내역 생성 (배송 수락 시)
  async createTransaction(matchingId, pricelogId, amount, createdBy, acceptedBy) {
    try {
      const response = await createTransaction(
        matchingId,
        pricelogId,
        amount,
        createdBy,
        acceptedBy,
      )
      return response.data
    } catch (err) {
      console.error(`🚨 거래 생성 실패: ${err}`)
      throw new Error('거래 생성 실패!')
    }
  },

  // 거래 상태 업데이트 (배송 완료 시)
  async updateTransactionStatus(matchingId, newStatus) {
    try {
      const response = await updateTransactionStatus(matchingId, newStatus)
      return response.data
    } catch (err) {
      console.error(`🚨 거래 상태 업데이트 실패: ${err}`)
      throw new Error('거래 상태 업데이트 실패!')
    }
  },

  // 거래 정산
  async completeTransaction(transactionId) {
    try {
      console.log(`API 요청: 거래 정산 transactionId=${transactionId}`) // 🛠️ 디버깅 추가
      const response = await axios.patch(
        `${serverURL}/transaction/complete/${transactionId}`,
        {},
        { headers: { 'Content-Type': 'application/json' } },
      )
      return response.data
    } catch (err) {
      console.error(`거래 정산 실패: ${err}`)
      throw new Error('거래 정산 실패!')
    }
  },

  getTransactionIdByMatchingId,
  getMatchingIdByShipmentId,
}
