import { Spinner } from './Spinner'

export function FullPageLoader({ label }: { label?: string }) {
  return (
    <div className="flex min-h-screen flex-col items-center justify-center gap-4 bg-bg">
      <Spinner className="size-8 text-brand" />
      {label && <p className="text-sm text-muted">{label}</p>}
    </div>
  )
}
