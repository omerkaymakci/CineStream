import type { MovieStatus } from '@/types'

export function StatusBadge({ status }: { status: MovieStatus }) {
  const isPublished = status === 'PUBLISHED'
  return (
    <span
      className={`inline-flex items-center gap-1.5 rounded-full px-2.5 py-0.5 text-xs font-medium ${
        isPublished
          ? 'bg-emerald-500/15 text-emerald-400'
          : 'bg-amber-500/15 text-amber-400'
      }`}
    >
      <span
        className={`size-1.5 rounded-full ${
          isPublished ? 'bg-emerald-400' : 'bg-amber-400'
        }`}
      />
      {isPublished ? 'Yayında' : 'Taslak'}
    </span>
  )
}
