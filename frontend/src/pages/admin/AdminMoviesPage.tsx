import { useState } from 'react'
import { Link } from 'react-router-dom'
import {
  useCreateMovie,
  useDeleteMovie,
  useMovies,
  useUpdateMovie,
} from '@/hooks/useMovies'
import { StatusBadge } from '@/components/StatusBadge'
import { Spinner } from '@/components/Spinner'
import { ErrorState } from '@/components/ErrorState'
import { EmptyState } from '@/components/EmptyState'
import { formatDuration } from '@/lib/format'
import { MovieFormModal } from './MovieFormModal'
import type { Movie, MovieRequest } from '@/types'

export function AdminMoviesPage() {
  const { data: movies, isLoading, isError, refetch } = useMovies()
  const createMutation = useCreateMovie()
  const updateMutation = useUpdateMovie()
  const deleteMutation = useDeleteMovie()

  const [modalOpen, setModalOpen] = useState(false)
  const [editing, setEditing] = useState<Movie | null>(null)

  const openCreate = () => {
    setEditing(null)
    setModalOpen(true)
  }
  const openEdit = (movie: Movie) => {
    setEditing(movie)
    setModalOpen(true)
  }

  const handleSubmit = async (payload: MovieRequest) => {
    try {
      if (editing) {
        await updateMutation.mutateAsync({ id: editing.id, payload })
      } else {
        await createMutation.mutateAsync(payload)
      }
      setModalOpen(false)
    } catch {
      alert('İşlem başarısız oldu. Yetkini (ADMIN) ve backend bağlantısını kontrol et.')
    }
  }

  const handleDelete = async (movie: Movie) => {
    if (!confirm(`"${movie.title}" filmini silmek istediğine emin misin?`)) return
    try {
      await deleteMutation.mutateAsync(movie.id)
    } catch {
      alert('Silme işlemi başarısız oldu.')
    }
  }

  return (
    <div className="mx-auto max-w-6xl px-4 py-8 sm:px-6">
      <div className="mb-6 flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold">Film Yönetimi</h1>
          <p className="mt-1 text-muted">Film ekle, düzenle veya sil</p>
        </div>
        <button
          onClick={openCreate}
          className="inline-flex items-center gap-2 rounded-lg bg-brand px-4 py-2 font-semibold text-white transition hover:bg-brand-hover"
        >
          + Yeni film
        </button>
      </div>

      {isLoading && (
        <div className="flex justify-center py-24">
          <Spinner className="size-8 text-brand" />
        </div>
      )}

      {isError && <ErrorState onRetry={() => refetch()} />}

      {!isLoading && !isError && movies && movies.length === 0 && (
        <EmptyState
          title="Henüz film eklenmemiş"
          description="İlk filmini eklemek için sağ üstteki butonu kullan."
        />
      )}

      {!isLoading && !isError && movies && movies.length > 0 && (
        <div className="overflow-hidden rounded-2xl border border-border">
          <table className="w-full text-left text-sm">
            <thead className="bg-surface-2 text-muted">
              <tr>
                <th className="px-4 py-3 font-medium">Başlık</th>
                <th className="px-4 py-3 font-medium">Süre</th>
                <th className="px-4 py-3 font-medium">Durum</th>
                <th className="px-4 py-3 font-medium">Video</th>
                <th className="px-4 py-3 text-right font-medium">İşlemler</th>
              </tr>
            </thead>
            <tbody className="divide-y divide-border">
              {movies.map((movie) => (
                <tr key={movie.id} className="bg-surface transition hover:bg-surface-2/60">
                  <td className="px-4 py-3">
                    <Link
                      to={`/movies/${movie.id}`}
                      className="font-medium hover:text-brand"
                    >
                      {movie.title}
                    </Link>
                  </td>
                  <td className="px-4 py-3 text-muted">
                    {formatDuration(movie.durationMinutes)}
                  </td>
                  <td className="px-4 py-3">
                    <StatusBadge status={movie.status} />
                  </td>
                  <td className="px-4 py-3">
                    {movie.videoUrl ? (
                      <span className="text-emerald-400">✓ var</span>
                    ) : (
                      <span className="text-muted">—</span>
                    )}
                  </td>
                  <td className="px-4 py-3">
                    <div className="flex justify-end gap-2">
                      <button
                        onClick={() => openEdit(movie)}
                        className="rounded-md border border-border px-3 py-1 text-xs font-medium transition hover:border-brand"
                      >
                        Düzenle
                      </button>
                      <button
                        onClick={() => handleDelete(movie)}
                        disabled={deleteMutation.isPending}
                        className="rounded-md border border-red-500/40 px-3 py-1 text-xs font-medium text-red-400 transition hover:bg-red-500/10 disabled:opacity-50"
                      >
                        Sil
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      <MovieFormModal
        open={modalOpen}
        initial={editing}
        submitting={createMutation.isPending || updateMutation.isPending}
        onClose={() => setModalOpen(false)}
        onSubmit={handleSubmit}
      />
    </div>
  )
}
