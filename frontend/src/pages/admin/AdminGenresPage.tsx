import { useState } from 'react'
import {
  useCreateGenre,
  useDeleteGenre,
  useGenres,
  useUpdateGenre,
} from '@/hooks/useGenres'
import { Spinner } from '@/components/Spinner'
import { ErrorState } from '@/components/ErrorState'
import { EmptyState } from '@/components/EmptyState'
import type { Genre } from '@/types'

export function AdminGenresPage() {
  const { data: genres, isLoading, isError, refetch } = useGenres()
  const createMutation = useCreateGenre()
  const updateMutation = useUpdateGenre()
  const deleteMutation = useDeleteGenre()

  const [newName, setNewName] = useState('')
  const [editingId, setEditingId] = useState<number | null>(null)
  const [editName, setEditName] = useState('')

  const handleCreate = async (e: React.FormEvent) => {
    e.preventDefault()
    const name = newName.trim()
    if (!name) return
    try {
      await createMutation.mutateAsync({ name })
      setNewName('')
    } catch {
      alert('Tür eklenemedi. Yetkini (ADMIN) kontrol et.')
    }
  }

  const startEdit = (genre: Genre) => {
    setEditingId(genre.id)
    setEditName(genre.name)
  }

  const handleUpdate = async (id: number) => {
    const name = editName.trim()
    if (!name) return
    try {
      await updateMutation.mutateAsync({ id, payload: { name } })
      setEditingId(null)
    } catch {
      alert('Tür güncellenemedi.')
    }
  }

  const handleDelete = async (genre: Genre) => {
    if (!confirm(`"${genre.name}" türünü silmek istediğine emin misin?`)) return
    try {
      await deleteMutation.mutateAsync(genre.id)
    } catch {
      alert('Tür silinemedi.')
    }
  }

  return (
    <div className="mx-auto max-w-3xl px-4 py-8 sm:px-6">
      <h1 className="text-3xl font-bold">Tür Yönetimi</h1>
      <p className="mt-1 text-muted">Film türlerini yönet</p>

      <form onSubmit={handleCreate} className="mt-6 flex gap-3">
        <input
          value={newName}
          onChange={(e) => setNewName(e.target.value)}
          placeholder="Yeni tür adı (örn. Aksiyon)"
          className="flex-1 rounded-lg border border-border bg-surface px-3 py-2 text-sm outline-none transition focus:border-brand"
        />
        <button
          type="submit"
          disabled={createMutation.isPending}
          className="inline-flex items-center gap-2 rounded-lg bg-brand px-5 py-2 text-sm font-semibold text-white transition hover:bg-brand-hover disabled:opacity-60"
        >
          {createMutation.isPending && <Spinner className="size-4" />}
          Ekle
        </button>
      </form>

      <div className="mt-8">
        {isLoading && (
          <div className="flex justify-center py-16">
            <Spinner className="size-8 text-brand" />
          </div>
        )}

        {isError && <ErrorState onRetry={() => refetch()} />}

        {!isLoading && !isError && genres && genres.length === 0 && (
          <EmptyState
            icon="🏷️"
            title="Henüz tür yok"
            description="Yukarıdaki formu kullanarak ilk türü ekle."
          />
        )}

        {!isLoading && !isError && genres && genres.length > 0 && (
          <ul className="divide-y divide-border overflow-hidden rounded-2xl border border-border">
            {genres.map((genre) => (
              <li
                key={genre.id}
                className="flex items-center gap-3 bg-surface px-4 py-3"
              >
                {editingId === genre.id ? (
                  <>
                    <input
                      value={editName}
                      onChange={(e) => setEditName(e.target.value)}
                      className="flex-1 rounded-lg border border-border bg-surface-2 px-3 py-1.5 text-sm outline-none focus:border-brand"
                      autoFocus
                    />
                    <button
                      onClick={() => handleUpdate(genre.id)}
                      disabled={updateMutation.isPending}
                      className="rounded-md bg-brand px-3 py-1 text-xs font-semibold text-white transition hover:bg-brand-hover disabled:opacity-50"
                    >
                      Kaydet
                    </button>
                    <button
                      onClick={() => setEditingId(null)}
                      className="rounded-md border border-border px-3 py-1 text-xs font-medium transition hover:border-muted"
                    >
                      İptal
                    </button>
                  </>
                ) : (
                  <>
                    <span className="flex-1 font-medium">{genre.name}</span>
                    <button
                      onClick={() => startEdit(genre)}
                      className="rounded-md border border-border px-3 py-1 text-xs font-medium transition hover:border-brand"
                    >
                      Düzenle
                    </button>
                    <button
                      onClick={() => handleDelete(genre)}
                      disabled={deleteMutation.isPending}
                      className="rounded-md border border-red-500/40 px-3 py-1 text-xs font-medium text-red-400 transition hover:bg-red-500/10 disabled:opacity-50"
                    >
                      Sil
                    </button>
                  </>
                )}
              </li>
            ))}
          </ul>
        )}
      </div>
    </div>
  )
}
