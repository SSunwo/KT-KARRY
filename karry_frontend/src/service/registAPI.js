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
const assignMatching = (shipmentId) => {
  return axios.post(`${serverURL}/matching/${shipmentId}`, {
    headers: {
      'Content-Type': 'application/json',
    },
  })
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
      console.log(`🛠️ API 호출: ${serverURL}/pricelog/${shipmentId}`) // 요청 URL 확인
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

  async createMatching(shipmentId) {
    try {
      const response = await assignMatching(shipmentId)

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
        throw new Error('🚨 shipmentId가 유효하지 않습니다!')
      }

      const response = await patchShipmentStatus(shipmentId, newStatus)
      console.log('patchShipmentStatus response로그입니다' + response)
      console.log('patchShipmentStatus response.data로그입니다' + response.data)
      return response.data
    } catch (err) {
      console.error(`❌ Shipment 상태 변경 실패: ${err}`)
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
}
