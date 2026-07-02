import { gradientFor, initials } from '@/lib/format'
import type { Movie } from '@/types'

interface MoviePosterProps {
  movie: Movie
  className?: string
}

/**
 * The backend has no artwork, so we render a stable gradient tile with the
 * movie's initials as a stand-in poster.
 */
export function MoviePoster({ movie, className = '' }: MoviePosterProps) {
  return (
    <div
      className={`relative flex items-center justify-center overflow-hidden ${className}`}
      style={{ background: gradientFor(movie.id || movie.title) }}
    >
      <span className="text-4xl font-black tracking-tight text-white/85 drop-shadow">
        {initials(movie.title)}
      </span>
      <div className="pointer-events-none absolute inset-0 bg-gradient-to-t from-black/50 to-transparent" />
    </div>
  )
}
