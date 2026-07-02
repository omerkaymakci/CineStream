import axios from 'axios'
import { config } from '@/config'
import { clearSession, getValidAccessToken } from '@/auth/session'

export const api = axios.create({
  baseURL: config.apiBaseUrl,
})

/** Attach a fresh access token (auto-refreshed) to every request. */
api.interceptors.request.use(async (request) => {
  const token = await getValidAccessToken()
  if (token) {
    request.headers.Authorization = `Bearer ${token}`
  }
  return request
})

api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      // Token rejected → drop the session; ProtectedRoute will send the user
      // back to the login page on the next render.
      clearSession()
    }
    return Promise.reject(error)
  },
)
