import { createContext } from 'react'

export interface AuthContextValue {
  /** Initial session restore/validation has finished. */
  initialized: boolean
  authenticated: boolean
  username?: string
  roles: string[]
  isAdmin: boolean
  /** Authenticates via Keycloak password grant. Throws on failure. */
  login: (username: string, password: string) => Promise<void>
  logout: () => void
}

export const AuthContext = createContext<AuthContextValue | null>(null)
