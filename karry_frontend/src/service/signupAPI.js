import axios from 'axios'

const signupUser = (user_id, user_pw, user_name, role) => {
  const reqData = {
    userId: user_id,
    userPw: user_pw,
    userName: user_name,
    role: role,
  }

  let serverURL = 'http://localhost:8080'

  return axios.post(serverURL + '/signup', reqData, {
    headers: {
      'Content-Type': 'application/json',
    },
  })
}

export default {
  async doSignup(user_id, user_pw, user_name, role) {
    try {
      const getsignupPromise = signupUser(user_id, user_pw, user_name, role)
      const [signupResponse] = await Promise.all([getsignupPromise])
      if (signupResponse.data.length === 0) {
        return 'notFound'
      } else {
        return signupResponse
      }
    } catch (err) {
      console.error(err)
      throw new Error('회원가입 실패')
    }
  },
}
