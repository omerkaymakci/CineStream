import {
  keycloakLogout,
  refreshLogin,
  type TokenResponse,
} from './keycloakApi'
import { decodeJwt, type JwtPayload } from '@/lib/jwt'

const STORAGE_KEY = 'cinestream.session'

interface StoredSession {
  accessToken: string
  refreshToken: string
  expiresAt: number // ms epoch — access token expiry
  refreshExpiresAt: number // ms epoch — refresh token expiry
}

let session: StoredSession | null = load()
let refreshInFlight: Promise<string | null> | null = null
let version = 0
const listeners = new Set<() => void>()

function load(): StoredSession | null {
  try {
    const raw = localStorage.getItem(STORAGE_KEY)
    return raw ? (JSON.parse(raw) as StoredSession) : null
  } catch {
    return null
  }
}

function persist() {
  if (session) localStorage.setItem(STORAGE_KEY, JSON.stringify(session))
  else localStorage.removeItem(STORAGE_KEY)
}

function notify() {
  version++
  listeners.forEach((fn) => fn())
}

/** React (or anything) can subscribe to session changes. */
export function subscribe(fn: () => void): () => void {
  listeners.add(fn)
  return () => {
    listeners.delete(fn)
  }
}

/** Monotonic counter for useSyncExternalStore snapshots. */
export function getVersion(): number {
  return version
}

export function setSessionFromToken(token: TokenResponse) {
  const now = Date.now()
  session = {
    accessToken: token.access_token,
    refreshToken: token.refresh_token,
    expiresAt: now + token.expires_in * 1000,
    refreshExpiresAt: now + token.refresh_expires_in * 1000,
  }
  persist()
  notify()
}

export function clearSession() {
  session = null
  persist()
  notify()
}

export function getPayload(): JwtPayload | null {
  return session ? decodeJwt(session.accessToken) : null
}

/** Authenticated as long as the refresh token is still alive. */
export function isAuthenticated(): boolean {
  return !!session && Date.now() < session.refreshExpiresAt
}

/**
 * Returns a non-expired access token, transparently refreshing when it is
 * within 30s of expiry. Concurrent callers share a single refresh request.
 */
export async function getValidAccessToken(): Promise<string | null> {
  if (!session) return null

  if (Date.now() < session.expiresAt - 30_000) {
    return session.accessToken
  }

  if (refreshInFlight) return refreshInFlight

  if (Date.now() >= session.refreshExpiresAt) {
    clearSession()
    return null
  }

  const refreshToken = session.refreshToken
  refreshInFlight = (async () => {
    try {
      const token = await refreshLogin(refreshToken)
      setSessionFromToken(token)
      return token.access_token
    } catch {
      clearSession()
      return null
    } finally {
      refreshInFlight = null
    }
  })()

  return refreshInFlight
}

/** Clears the local session and revokes it on Keycloak (best-effort). */
export function endSession() {
  const refreshToken = session?.refreshToken
  clearSession()
  if (refreshToken) void keycloakLogout(refreshToken)
}
