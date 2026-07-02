import { useMemo, useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '@/auth/useAuth'
import { useMovies } from '@/hooks/useMovies'
import { MovieCard } from '@/components/MovieCard'
import { EmptyState } from '@/components/EmptyState'
import { ErrorState } from '@/components/ErrorState'
import { Spinner } from '@/components/Spinner'

export function HomePage() {
  const { authenticated, initialized } = useAuth()
  const navigate = useNavigate()

  const scrollToCatalog = () => {
    document
      .getElementById('catalog')
      ?.scrollIntoView({ behavior: 'smooth', block: 'start' })
  }

  return (
    <div>
      {/* ── Hero ──────────────────────────────────────────── */}
      <section className="relative overflow-hidden">
        <div
          className="absolute inset-0 -z-10 opacity-70"
          style={{
            background:
              'radial-gradient(60% 55% at 50% 0%, rgba(37,99,235,0.20), transparent)',
          }}
        />
        <div className="mx-auto max-w-7xl px-4 py-24 text-center sm:px-6 sm:py-28">
          <h1 className="text-5xl font-extrabold tracking-tight sm:text-6xl">
            CineStream'e Hoş Geldin
          </h1>
          <p className="mx-auto mt-4 max-w-xl text-lg text-muted">
            Favori filmlerini çevrimiçi izle
          </p>
          <button
            onClick={
              initialized && !authenticated
                ? () => navigate('/login')
                : scrollToCatalog
            }
            className="mt-8 rounded-lg bg-brand px-7 py-3 text-base font-semibold text-white shadow-lg shadow-brand/25 transition hover:bg-brand-hover"
          >
            {initialized && !authenticated ? 'Giriş yap ve izle' : 'Filmlere Göz At'}
          </button>
        </div>
      </section>

      {/* ── Popular Movies ────────────────────────────────── */}
      <section id="catalog" className="mx-auto max-w-7xl px-4 pb-16 sm:px-6">
        {authenticated ? (
          <Catalog />
        ) : (
          initialized && <LoggedOutPrompt onLogin={() => navigate('/login')} />
        )}
      </section>
    </div>
  )
}

function LoggedOutPrompt({ onLogin }: { onLogin: () => void }) {
  return (
    <>
      <h2 className="mb-6 text-2xl font-bold">Popüler Filmler</h2>
      <EmptyState
        icon="🍿"
        title="Filmleri görmek için giriş yap"
        description="Kütüphanedeki tüm filmleri keşfetmek için hesabınla giriş yapman gerekiyor."
        action={
          <button
            onClick={onLogin}
            className="mt-2 rounded-lg bg-brand px-5 py-2 font-semibold text-white transition hover:bg-brand-hover"
          >
            Giriş yap
          </button>
        }
      />
    </>
  )
}

function Catalog() {
  const { data: movies, isLoading, isError, refetch } = useMovies()
  const [query, setQuery] = useState('')

  const filtered = useMemo(() => {
    if (!movies) return []
    const q = query.trim().toLowerCase()
    if (!q) return movies
    return movies.filter((m) => m.title.toLowerCase().includes(q))
  }, [movies, query])

  return (
    <>
      <div className="mb-6 flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
        <h2 className="text-2xl font-bold">Popüler Filmler</h2>
        <div className="relative w-full sm:w-72">
          <svg
            className="pointer-events-none absolute left-3 top-1/2 size-4 -translate-y-1/2 text-muted"
            viewBox="0 0 24 24"
            fill="none"
            stroke="currentColor"
            strokeWidth="2"
          >
            <circle cx="11" cy="11" r="7" />
            <path d="m21 21-4.3-4.3" />
          </svg>
          <input
            value={query}
            onChange={(e) => setQuery(e.target.value)}
            placeholder="Film ara…"
            className="w-full rounded-lg border border-border bg-surface py-2 pl-9 pr-3 text-sm outline-none transition focus:border-brand"
          />
        </div>
      </div>

      {isLoading && (
        <div className="flex justify-center py-24">
          <Spinner className="size-8 text-brand" />
        </div>
      )}

      {isError && (
        <ErrorState
          message="Filmler yüklenemedi. Oturumun geçerli ve backend'in ayakta olduğundan emin ol."
          onRetry={() => refetch()}
        />
      )}

      {!isLoading && !isError && filtered.length === 0 && (
        <EmptyState
          title={query ? 'Sonuç bulunamadı' : 'Henüz film yok'}
          description={
            query
              ? 'Farklı bir arama terimi dene.'
              : 'Bir yönetici film eklediğinde burada görünecek.'
          }
        />
      )}

      {!isLoading && !isError && filtered.length > 0 && (
        <div className="grid grid-cols-2 gap-4 sm:grid-cols-3 md:grid-cols-4 lg:grid-cols-5 xl:grid-cols-6">
          {filtered.map((movie) => (
            <MovieCard key={movie.id} movie={movie} />
          ))}
        </div>
      )}
    </>
  )
}
