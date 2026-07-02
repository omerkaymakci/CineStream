/**
 * Central runtime configuration, sourced from Vite env vars (see .env.example).
 * Falls back to sensible local-dev defaults so the app runs out of the box.
 */
export const config = {
  apiBaseUrl:
    import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080/movie-service',
  keycloak: {
    url: import.meta.env.VITE_KEYCLOAK_URL ?? 'http://localhost:9000',
    realm: import.meta.env.VITE_KEYCLOAK_REALM ?? 'cinestream',
    clientId: import.meta.env.VITE_KEYCLOAK_CLIENT_ID ?? 'cinestream-frontend',
    // Only needed if the Keycloak client is "confidential" (Client
    // authentication ON). Leave empty for a public client (recommended).
    clientSecret: import.meta.env.VITE_KEYCLOAK_CLIENT_SECRET ?? '',
  },
} as const
