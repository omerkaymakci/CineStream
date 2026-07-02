import { Link, useParams } from 'react-router-dom'
import { useMovie } from '@/hooks/useMovies'
import { Spinner } from '@/components/Spinner'
import { ErrorState } from '@/components/ErrorState'

export function WatchPage() {
  const { id } = useParams<{ id: string }>()
  const movieId = Number(id)
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

  return (
    <div className="mx-auto max-w-6xl px-4 py-6 sm:px-6">
      <Link
        to={`/movies/${movie.id}`}
        className="mb-4 inline-flex items-center gap-1 text-sm text-muted transition hover:text-white"
      >
        ← {movie.title}
      </Link>

      <div className="overflow-hidden rounded-2xl border border-border bg-black">
        {movie.videoUrl ? (
          <video
            key={movie.videoUrl}
            src={movie.videoUrl}
            controls
            autoPlay
            className="aspect-video w-full bg-black"
          >
            Tarayıcın video oynatmayı desteklemiyor.
          </video>
        ) : (
          <div className="flex aspect-video w-full items-center justify-center text-muted">
            Bu film için video kaynağı bulunmuyor.
          </div>
        )}
      </div>

      <h1 className="mt-5 text-2xl font-bold">{movie.title}</h1>
      {movie.description && (
        <p className="mt-2 max-w-3xl leading-relaxed text-zinc-300">
          {movie.description}
        </p>
      )}
    </div>
  )
}
