import { Link } from 'react-router-dom'

export function AccessDenied({ requiredRole }: { requiredRole?: string }) {
  return (
    <div className="flex min-h-[60vh] flex-col items-center justify-center gap-3 px-6 text-center">
      <div className="text-5xl">🔒</div>
      <h1 className="text-2xl font-semibold">Erişim reddedildi</h1>
      <p className="max-w-md text-muted">
        Bu sayfayı görüntülemek için yetkin yok
        {requiredRole ? ` (${requiredRole} rolü gerekli).` : '.'}
      </p>
      <Link
        to="/"
        className="mt-2 rounded-lg bg-brand px-4 py-2 font-medium text-white transition hover:bg-brand-hover"
      >
        Ana sayfaya dön
      </Link>
    </div>
  )
}
