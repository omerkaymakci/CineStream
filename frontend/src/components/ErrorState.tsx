interface ErrorStateProps {
  title?: string
  message?: string
  onRetry?: () => void
}

export function ErrorState({
  title = 'Bir şeyler ters gitti',
  message = 'Veriler yüklenemedi. Lütfen tekrar deneyin.',
  onRetry,
}: ErrorStateProps) {
  return (
    <div className="flex flex-col items-center justify-center gap-3 rounded-2xl border border-red-500/30 bg-red-500/5 px-6 py-16 text-center">
      <div className="text-5xl">⚠️</div>
      <h2 className="text-lg font-semibold">{title}</h2>
      <p className="max-w-sm text-sm text-muted">{message}</p>
      {onRetry && (
        <button
          onClick={onRetry}
          className="mt-2 rounded-lg border border-border bg-surface-2 px-4 py-2 text-sm font-medium transition hover:border-brand"
        >
          Tekrar dene
        </button>
      )}
    </div>
  )
}
