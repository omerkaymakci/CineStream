import { gradientFor, initials } from '@/lib/format'
import type { Movie } from '@/types'

interface MoviePosterProps {
  movie: Movie
  className?: string
}

/**
 * Shows the movie's uploaded poster when present; otherwise falls back to a
 * stable gradient tile with the movie's initials.
 */
export function MoviePoster({ movie, className = '' }: MoviePosterProps) {
  if (movie.posterImage) {
    return (
      <div className={`relative overflow-hidden ${className}`}>
        <img
          src={movie.posterImage}
          alt={movie.title}
          className="h-full w-full object-cover"
          loading="lazy"
        />
        <div className="pointer-events-none absolute inset-0 bg-gradient-to-t from-black/50 to-transparent" />
      </div>
    )
  }

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
