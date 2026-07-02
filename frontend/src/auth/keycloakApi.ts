import axios from 'axios'
import { config } from '@/config'

const realmBase = `${config.keycloak.url}/realms/${config.keycloak.realm}/protocol/openid-connect`
const tokenUrl = `${realmBase}/token`
const logoutUrl = `${realmBase}/logout`

export interface TokenResponse {
  access_token: string
  refresh_token: string
  expires_in: number
  refresh_expires_in: number
  token_type: string
}

const formHeaders = {
  headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
}

/** client_id (+ client_secret when the Keycloak client is confidential). */
function clientParams(): Record<string, string> {
  const params: Record<string, string> = { client_id: config.keycloak.clientId }
  if (config.keycloak.clientSecret) {
    params.client_secret = config.keycloak.clientSecret
  }
  return params
}

/**
 * Resource Owner Password Credentials grant: exchange username/password for
 * tokens directly, so the login form lives in our own UI (no Keycloak redirect).
 * Requires "Direct access grants" enabled on the Keycloak client.
 */
export async function passwordLogin(
  username: string,
  password: string,
): Promise<TokenResponse> {
  const body = new URLSearchParams({
    ...clientParams(),
    grant_type: 'password',
    username,
    password,
    scope: 'openid',
  })
  const { data } = await axios.post<TokenResponse>(tokenUrl, body, formHeaders)
  return data
}

export async function refreshLogin(refreshToken: string): Promise<TokenResponse> {
  const body = new URLSearchParams({
    ...clientParams(),
    grant_type: 'refresh_token',
    refresh_token: refreshToken,
  })
  const { data } = await axios.post<TokenResponse>(tokenUrl, body, formHeaders)
  return data
}

/** Revokes the session on Keycloak. Best-effort; ignores failures. */
export async function keycloakLogout(refreshToken: string): Promise<void> {
  const body = new URLSearchParams({
    ...clientParams(),
    refresh_token: refreshToken,
  })
  try {
    await axios.post(logoutUrl, body, formHeaders)
  } catch {
    // Session is cleared locally regardless.
  }
}
