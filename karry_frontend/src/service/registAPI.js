import axios from 'axios'

const registShipment = (user_num, origin, destination, weight, size, price, status) => {
  const reqData = {
    userNum: user_num,
    origin: origin,
    destination: destination,
    weight: weight,
    size: size,
    price: price,
    status: status,
  }

  let serverURL = 'http://localhost:8080'

  return axios.post(serverURL + '/shipment', reqData, {
    headers: {
      'Content-Type': 'application/json',
    },
  })
}

const getShipments = () => {
  let serverURL = 'http://localhost:8080'

  return axios.get(serverURL + '/shipment', {
    headers: {
      'Content-Type': 'application/json',
    },
  })
}

export default {
  async doRegist(user_num, origin, destination, weight, size, price, status) {
    try {
      const registPromise = registShipment(
        user_num,
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
}
