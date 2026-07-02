import { useMemo } from 'react'
import { Link, useNavigate, useSearchParams } from 'react-router-dom'
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

  const loggedOut = initialized && !authenticated

  return (
    <div className="mx-auto max-w-7xl px-4 py-6 sm:px-6">
      {/* ── Hero ──────────────────────────────────────────── */}
      <section className="relative overflow-hidden rounded-2xl border border-border">
        {/* Abstract backdrop (real movie posters are copyrighted). */}
        <div
          className="absolute inset-0"
          style={{
            background:
              'radial-gradient(120% 120% at 100% 0%, rgba(139,92,246,0.28), transparent 55%), radial-gradient(120% 120% at 90% 100%, rgba(37,99,235,0.30), transparent 55%), #0b1120',
          }}
        />
        <div className="absolute inset-0 bg-gradient-to-r from-bg via-bg/85 to-transparent" />

        <div className="relative max-w-xl px-8 py-16 sm:px-12 sm:py-20">
          <h1 className="text-4xl font-extrabold leading-tight tracking-tight sm:text-5xl">
            CineStream'e
            <br />
            <span className="bg-gradient-to-r from-violet-400 to-blue-500 bg-clip-text text-transparent">
              Hoş Geldin
            </span>
          </h1>
          <p className="mt-4 text-lg text-zinc-300">
            Favori filmlerini çevrimiçi izle, keyfini çıkar.
          </p>
          <button
            onClick={loggedOut ? () => navigate('/login') : scrollToCatalog}
            className="mt-7 inline-flex items-center gap-2 rounded-lg bg-gradient-to-r from-violet-500 to-blue-600 px-6 py-3 font-semibold text-white shadow-lg shadow-blue-600/25 transition hover:opacity-90"
          >
            <svg viewBox="0 0 24 24" className="size-5" fill="currentColor">
              <path d="M8 5v14l11-7z" />
            </svg>
            {loggedOut ? 'Giriş yap ve izle' : 'Filmlere Göz At'}
          </button>
        </div>
      </section>

      {/* ── Popular Movies ────────────────────────────────── */}
      <section id="catalog" className="mt-10">
        {authenticated ? (
          <Catalog />
        ) : (
          loggedOut && <LoggedOutPrompt onLogin={() => navigate('/login')} />
        )}
      </section>
    </div>
  )
}

function SectionHeader({
  query,
  showSeeAll,
}: {
  query?: string
  showSeeAll?: boolean
}) {
  return (
    <div className="mb-6 flex items-end justify-between gap-4">
      <div>
        <h2 className="flex items-center gap-2 text-2xl font-bold">
          <svg viewBox="0 0 24 24" className="size-5 text-brand" fill="currentColor">
            <path d="M12 2l2.9 6.3 6.9.7-5.2 4.6 1.5 6.8L12 17.8 5.9 20.4l1.5-6.8L2.2 9l6.9-.7L12 2z" />
          </svg>
          {query ? `“${query}” için sonuçlar` : 'Popüler Filmler'}
        </h2>
        <p className="mt-1 text-sm text-muted">
          {query ? 'Aramanla eşleşen filmler' : 'Sizin için seçilmiş en beğenilen filmler'}
        </p>
      </div>

      {showSeeAll && (
        <Link
          to="/"
          className="inline-flex shrink-0 items-center gap-1 rounded-full border border-border px-4 py-1.5 text-sm font-medium text-zinc-200 transition hover:border-brand hover:text-white"
        >
          Tümünü Gör
          <svg viewBox="0 0 24 24" className="size-4" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
            <path d="m9 6 6 6-6 6" />
          </svg>
        </Link>
      )}
    </div>
  )
}

function LoggedOutPrompt({ onLogin }: { onLogin: () => void }) {
  return (
    <>
      <SectionHeader />
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
  const [searchParams] = useSearchParams()
  const query = searchParams.get('q') ?? ''

  const filtered = useMemo(() => {
    if (!movies) return []
    const q = query.trim().toLowerCase()
    if (!q) return movies
    return movies.filter((m) => m.title.toLowerCase().includes(q))
  }, [movies, query])

  return (
    <>
      <SectionHeader
        query={query || undefined}
        showSeeAll={!query && filtered.length > 0}
      />

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
