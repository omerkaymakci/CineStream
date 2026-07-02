import { useEffect, useState } from 'react'
import { Modal } from '@/components/Modal'
import { Spinner } from '@/components/Spinner'
import { useGenres } from '@/hooks/useGenres'
import type { Movie, MovieRequest, MovieStatus } from '@/types'

interface MovieFormModalProps {
  open: boolean
  initial?: Movie | null
  submitting?: boolean
  onClose: () => void
  onSubmit: (payload: MovieRequest) => void
}

const inputClass =
  'w-full rounded-lg border border-border bg-surface-2 px-3 py-2 text-sm outline-none transition focus:border-brand'
const labelClass = 'mb-1 block text-sm font-medium text-zinc-300'

export function MovieFormModal({
  open,
  initial,
  submitting,
  onClose,
  onSubmit,
}: MovieFormModalProps) {
  const { data: genres } = useGenres()

  const [title, setTitle] = useState('')
  const [description, setDescription] = useState('')
  const [durationMinutes, setDurationMinutes] = useState('')
  const [status, setStatus] = useState<MovieStatus>('DRAFT')
  const [videoUrl, setVideoUrl] = useState('')
  const [genreIds, setGenreIds] = useState<number[]>([])

  // Reset fields whenever the modal opens (for a fresh create or edit).
  useEffect(() => {
    if (!open) return
    setTitle(initial?.title ?? '')
    setDescription(initial?.description ?? '')
    setDurationMinutes(
      initial?.durationMinutes ? String(initial.durationMinutes) : '',
    )
    setStatus(initial?.status ?? 'DRAFT')
    setVideoUrl(initial?.videoUrl ?? '')
    setGenreIds([])
  }, [open, initial])

  const toggleGenre = (id: number) => {
    setGenreIds((prev) =>
      prev.includes(id) ? prev.filter((g) => g !== id) : [...prev, id],
    )
  }

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault()
    if (!title.trim()) return
    onSubmit({
      title: title.trim(),
      description: description.trim() || undefined,
      durationMinutes: durationMinutes ? Number(durationMinutes) : undefined,
      status,
      videoUrl: videoUrl.trim() || undefined,
      genreIds: genreIds.length ? genreIds : undefined,
    })
  }

  return (
    <Modal
      open={open}
      title={initial ? 'Filmi düzenle' : 'Yeni film ekle'}
      onClose={onClose}
    >
      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label className={labelClass}>Başlık *</label>
          <input
            className={inputClass}
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            placeholder="Film adı"
            autoFocus
            required
          />
        </div>

        <div>
          <label className={labelClass}>Açıklama</label>
          <textarea
            className={`${inputClass} min-h-24 resize-y`}
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            placeholder="Kısa özet"
          />
        </div>

        <div className="grid grid-cols-2 gap-4">
          <div>
            <label className={labelClass}>Süre (dakika)</label>
            <input
              type="number"
              min={0}
              className={inputClass}
              value={durationMinutes}
              onChange={(e) => setDurationMinutes(e.target.value)}
              placeholder="120"
            />
          </div>
          <div>
            <label className={labelClass}>Durum</label>
            <select
              className={inputClass}
              value={status}
              onChange={(e) => setStatus(e.target.value as MovieStatus)}
            >
              <option value="DRAFT">Taslak</option>
              <option value="PUBLISHED">Yayında</option>
            </select>
          </div>
        </div>

        <div>
          <label className={labelClass}>Video URL</label>
          <input
            className={inputClass}
            value={videoUrl}
            onChange={(e) => setVideoUrl(e.target.value)}
            placeholder="https://…/movie.mp4"
          />
        </div>

        {genres && genres.length > 0 && (
          <div>
            <label className={labelClass}>Türler</label>
            <div className="flex flex-wrap gap-2">
              {genres.map((g) => {
                const active = genreIds.includes(g.id)
                return (
                  <button
                    type="button"
                    key={g.id}
                    onClick={() => toggleGenre(g.id)}
                    className={`rounded-full border px-3 py-1 text-sm transition ${
                      active
                        ? 'border-brand bg-brand/15 text-white'
                        : 'border-border text-muted hover:border-brand/60'
                    }`}
                  >
                    {g.name}
                  </button>
                )
              })}
            </div>
          </div>
        )}

        <div className="flex justify-end gap-3 pt-2">
          <button
            type="button"
            onClick={onClose}
            className="rounded-lg border border-border px-4 py-2 text-sm font-medium transition hover:border-muted"
          >
            İptal
          </button>
          <button
            type="submit"
            disabled={submitting}
            className="inline-flex items-center gap-2 rounded-lg bg-brand px-5 py-2 text-sm font-semibold text-white transition hover:bg-brand-hover disabled:opacity-60"
          >
            {submitting && <Spinner className="size-4" />}
            {initial ? 'Kaydet' : 'Ekle'}
          </button>
        </div>
      </form>
    </Modal>
  )
}
