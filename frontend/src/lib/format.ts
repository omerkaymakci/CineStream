/** "125" -> "2s 5dk" */
export function formatDuration(minutes?: number | null): string {
  if (!minutes || minutes <= 0) return '—'
  const h = Math.floor(minutes / 60)
  const m = minutes % 60
  if (h === 0) return `${m}dk`
  if (m === 0) return `${h}s`
  return `${h}s ${m}dk`
}

/** Deterministic gradient so each movie gets a stable "poster" color. */
export function gradientFor(seed: string | number): string {
  const str = String(seed)
  let hash = 0
  for (let i = 0; i < str.length; i++) {
    hash = str.charCodeAt(i) + ((hash << 5) - hash)
  }
  const h1 = Math.abs(hash) % 360
  const h2 = (h1 + 55) % 360
  return `linear-gradient(135deg, hsl(${h1} 55% 22%), hsl(${h2} 60% 12%))`
}

/**
 * Deterministic placeholder rating (7.0–9.6) derived from the movie id.
 * NOTE: the backend has no rating field; this is display-only mock data.
 */
export function mockRating(seed: string | number): string {
  const str = String(seed)
  let hash = 0
  for (let i = 0; i < str.length; i++) {
    hash = str.charCodeAt(i) + ((hash << 5) - hash)
  }
  const rating = 7 + (Math.abs(hash) % 27) / 10
  return rating.toFixed(1)
}

export function initials(title: string): string {
  return title
    .split(/\s+/)
    .filter(Boolean)
    .slice(0, 2)
    .map((w) => w[0]?.toUpperCase() ?? '')
    .join('')
}
