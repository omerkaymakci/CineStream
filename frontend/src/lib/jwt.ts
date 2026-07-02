export interface JwtPayload {
  exp: number
  iat?: number
  preferred_username?: string
  name?: string
  email?: string
  realm_access?: { roles?: string[] }
  [key: string]: unknown
}

function base64UrlDecode(input: string): string {
  const pad = input.length % 4 === 0 ? '' : '='.repeat(4 - (input.length % 4))
  const base64 = (input + pad).replace(/-/g, '+').replace(/_/g, '/')
  const binary = atob(base64)
  const bytes = Uint8Array.from(binary, (c) => c.charCodeAt(0))
  return new TextDecoder().decode(bytes)
}

/** Decodes a JWT payload without verifying the signature (that's Keycloak's job). */
export function decodeJwt(token: string): JwtPayload | null {
  try {
    const payload = token.split('.')[1]
    if (!payload) return null
    return JSON.parse(base64UrlDecode(payload)) as JwtPayload
  } catch {
    return null
  }
}
