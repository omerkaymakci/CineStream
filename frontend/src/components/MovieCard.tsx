import { Link } from 'react-router-dom'
import { MoviePoster } from './MoviePoster'
import type { Movie } from '@/types'

export function MovieCard({ movie }: { movie: Movie }) {
  return (
    <Link to={`/movies/${movie.id}`} className="group block">
      <div className="relative overflow-hidden rounded-lg shadow-md transition duration-200 group-hover:-translate-y-1 group-hover:shadow-xl group-hover:shadow-black/40">
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

      <h3
        className="mt-2 truncate text-center text-sm font-medium text-zinc-200 group-hover:text-white"
        title={movie.title}
      >
        {movie.title}
      </h3>
    </Link>
  )
}
