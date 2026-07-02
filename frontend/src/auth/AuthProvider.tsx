import {
  useEffect,
  useState,
  useSyncExternalStore,
  type ReactNode,
} from 'react'
import { passwordLogin } from './keycloakApi'
import {
  clearSession,
  endSession,
  getPayload,
  getValidAccessToken,
  getVersion,
  isAuthenticated,
  setSessionFromToken,
  subscribe,
} from './session'
import { AuthContext, type AuthContextValue } from './AuthContext'

export function AuthProvider({ children }: { children: ReactNode }) {
  const [initialized, setInitialized] = useState(false)

  // Re-render whenever the session store changes (login / logout / refresh).
  useSyncExternalStore(subscribe, getVersion)

  useEffect(() => {
    // Restore any persisted session on load, refreshing if the access token
    // has already expired.
    void (async () => {
      if (isAuthenticated()) {
        await getValidAccessToken()
      } else {
        clearSession()
      }
      setInitialized(true)
    })()
  }, [])

  const payload = getPayload()
  const roles = payload?.realm_access?.roles ?? []

  const value: AuthContextValue = {
    initialized,
    authenticated: isAuthenticated(),
    username: payload?.preferred_username ?? payload?.name,
    roles,
    isAdmin: roles.includes('ADMIN'),
    login: async (username: string, password: string) => {
      const token = await passwordLogin(username, password)
      setSessionFromToken(token)
    },
    logout: () => endSession(),
  }

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>
}
