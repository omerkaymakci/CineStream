import { type ReactNode } from 'react'

interface EmptyStateProps {
  icon?: string
  title: string
  description?: string
  action?: ReactNode
}

export function EmptyState({
  icon = '🎬',
  title,
  description,
  action,
}: EmptyStateProps) {
  return (
    <div className="flex flex-col items-center justify-center gap-3 rounded-2xl border border-dashed border-border bg-surface/50 px-6 py-16 text-center">
      <div className="text-5xl">{icon}</div>
      <h2 className="text-lg font-semibold">{title}</h2>
      {description && <p className="max-w-sm text-sm text-muted">{description}</p>}
      {action}
    </div>
  )
}
