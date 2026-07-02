import { Link, useNavigate, useParams } from 'react-router-dom'
import { useMovie } from '@/hooks/useMovies'
import { MoviePoster } from '@/components/MoviePoster'
import { StatusBadge } from '@/components/StatusBadge'
import { Spinner } from '@/components/Spinner'
import { ErrorState } from '@/components/ErrorState'
import { formatDuration } from '@/lib/format'

export function MovieDetailPage() {
  const { id } = useParams<{ id: string }>()
  const movieId = Number(id)
  const navigate = useNavigate()
  const { data: movie, isLoading, isError, refetch } = useMovie(movieId)

  if (isLoading) {
    return (
      <div className="flex justify-center py-32">
        <Spinner className="size-8 text-brand" />
      </div>
    )
  }

  if (isError || !movie) {
    return (
      <div className="mx-auto max-w-3xl px-4 py-16">
        <ErrorState title="Film bulunamadı" onRetry={() => refetch()} />
      </div>
    )
  }

  const canWatch = !!movie.videoUrl

  return (
    <div className="relative">
      {/* Backdrop */}
      <MoviePoster
        movie={movie}
        className="absolute inset-x-0 top-0 h-80 w-full blur-2xl"
      />
      <div className="absolute inset-x-0 top-0 h-80 bg-gradient-to-b from-transparent to-bg" />

      <div className="relative mx-auto max-w-5xl px-4 py-10 sm:px-6">
        <button
          onClick={() => navigate(-1)}
          className="mb-6 inline-flex items-center gap-1 text-sm text-muted transition hover:text-white"
        >
          ← Geri
        </button>

        <div className="flex flex-col gap-8 sm:flex-row">
          <MoviePoster
            movie={movie}
            className="aspect-[2/3] w-48 shrink-0 rounded-xl border border-border shadow-xl"
          />

          <div className="flex-1">
            <div className="mb-3 flex items-center gap-3">
              <StatusBadge status={movie.status} />
              <span className="text-sm text-muted">
                {formatDuration(movie.durationMinutes)}
              </span>
            </div>

            <h1 className="text-4xl font-bold">{movie.title}</h1>

            <p className="mt-4 max-w-2xl leading-relaxed text-zinc-300">
              {movie.description || 'Bu film için henüz açıklama eklenmemiş.'}
            </p>

            <div className="mt-8 flex flex-wrap gap-3">
              {canWatch ? (
                <Link
                  to={`/watch/${movie.id}`}
                  className="inline-flex items-center gap-2 rounded-xl bg-brand px-6 py-3 font-semibold text-white transition hover:bg-brand-hover"
                >
                  <svg viewBox="0 0 24 24" className="size-5" fill="currentColor">
                    <path d="M8 5v14l11-7z" />
                  </svg>
                  Şimdi izle
                </Link>
              ) : (
                <span className="inline-flex items-center gap-2 rounded-xl border border-border bg-surface px-6 py-3 font-medium text-muted">
                  Video henüz hazır değil
                </span>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  )
}
