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
  // ✅ 백엔드에서 데이터를 반환하도록 수정
  async getPricelog(shipmentId) {
    try {
      console.log(`🛠️ API 호출: ${serverURL}/pricelog/${shipmentId}`) // 요청 URL 확인
      const priceLogResponse = await getPricelog(shipmentId) // API 호출

      console.log('📥 API 응답:', priceLogResponse) // 응답 객체 전체 확인

      if (!priceLogResponse || !priceLogResponse.data) {
        console.error(`❌ PriceLog 데이터가 존재하지 않습니다. shipmentId: ${shipmentId}`)
        return null // `undefined`가 아니라 `null` 반환
      }

      return priceLogResponse.data // ✅ `data`만 반환하도록 수정
    } catch (err) {
      console.error(`PriceLog 불러오기 실패 (shipmentId: ${shipmentId}):`, err)
      throw new Error('PriceLog 정보를 불러오는데 실패했습니다.')
    }
  },
}
