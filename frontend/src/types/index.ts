export type MovieStatus = 'DRAFT' | 'PUBLISHED'

/** Mirrors GenreResponse from movie-service. */
export interface Genre {
  id: number
  name: string
}

/** Mirrors MovieResponse from movie-service. */
export interface Movie {
  id: number
  title: string
  description: string | null
  durationMinutes: number | null
  status: MovieStatus
  videoUrl: string | null
}

/** Mirrors MovieRequest expected by movie-service. */
export interface MovieRequest {
  title: string
  description?: string
  durationMinutes?: number
  status: MovieStatus
  active?: boolean
  releaseDate?: string // ISO yyyy-MM-dd
  videoUrl?: string
  genreIds?: number[]
}

/** Mirrors GenreRequest expected by movie-service. */
export interface GenreRequest {
  name: string
}
