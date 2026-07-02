import { Link } from 'react-router-dom'

export function NotFoundPage() {
  return (
    <div className="flex min-h-[60vh] flex-col items-center justify-center gap-3 text-center">
      <div className="text-6xl font-black text-brand">404</div>
      <h1 className="text-2xl font-semibold">Sayfa bulunamadı</h1>
      <Link
        to="/"
        className="mt-2 rounded-lg bg-brand px-4 py-2 font-medium text-white transition hover:bg-brand-hover"
      >
        Ana sayfaya dön
      </Link>
    </div>
  )
}
