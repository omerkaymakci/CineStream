import { Link } from 'react-router-dom'
import { MoviePoster } from './MoviePoster'
import { mockRating } from '@/lib/format'
import type { Movie } from '@/types'

export function MovieCard({ movie }: { movie: Movie }) {
  return (
    <Link
      to={`/movies/${movie.id}`}
      className="group block overflow-hidden rounded-xl border border-border bg-surface transition duration-200 hover:-translate-y-1 hover:border-brand/50 hover:shadow-xl hover:shadow-black/40"
    >
      <div className="relative overflow-hidden">
        <MoviePoster movie={movie} className="aspect-[2/3] w-full" />

        {/* Hover play affordance */}
        <div className="absolute inset-0 flex items-center justify-center bg-black/45 opacity-0 transition group-hover:opacity-100">
          <span className="flex size-12 items-center justify-center rounded-full bg-brand text-white shadow-lg">
            <svg viewBox="0 0 24 24" className="ml-0.5 size-6" fill="currentColor">
              <path d="M8 5v14l11-7z" />
            </svg>
          </span>
        </div>
      </div>

      <div className="flex items-center justify-between gap-2 p-3">
        <h3
          className="truncate text-sm font-semibold text-zinc-100"
          title={movie.title}
        >
          {movie.title}
        </h3>
        <span className="flex shrink-0 items-center gap-1 rounded-md bg-brand/15 px-1.5 py-0.5 text-xs font-semibold text-brand">
          <svg viewBox="0 0 24 24" className="size-3" fill="currentColor">
            <path d="M12 2l2.9 6.3 6.9.7-5.2 4.6 1.5 6.8L12 17.8 5.9 20.4l1.5-6.8L2.2 9l6.9-.7L12 2z" />
          </svg>
          {mockRating(movie.id)}
        </span>
      </div>
    </Link>
  )
}
