import { Link, NavLink } from 'react-router-dom'
import { useAuth } from '@/auth/useAuth'

function navClass({ isActive }: { isActive: boolean }) {
  return `text-sm font-medium transition hover:text-white ${
    isActive ? 'text-white' : 'text-muted'
  }`
}

export function Navbar() {
  const { authenticated, isAdmin, username, logout } = useAuth()

  return (
    <header className="sticky top-0 z-40 border-b border-border/70 bg-bg/80 backdrop-blur-md">
      <div className="mx-auto flex h-16 max-w-7xl items-center gap-6 px-4 sm:px-6">
        <Link to="/" className="flex items-center gap-2.5">
          <span className="flex size-8 items-center justify-center rounded-full bg-white text-bg">
            <svg viewBox="0 0 24 24" className="ml-0.5 size-4" fill="currentColor">
              <path d="M8 5v14l11-7z" />
            </svg>
          </span>
          <span className="text-xl font-bold tracking-tight">CineStream</span>
        </Link>

        <nav className="flex items-center gap-5">
          <NavLink to="/" className={navClass} end>
            Keşfet
          </NavLink>
          {isAdmin && (
            <NavLink to="/admin/movies" className={navClass}>
              Filmler
            </NavLink>
          )}
          {isAdmin && (
            <NavLink to="/admin/genres" className={navClass}>
              Türler
            </NavLink>
          )}
        </nav>

        <div className="ml-auto flex items-center gap-3">
          {authenticated ? (
            <>
              <span className="hidden text-sm text-muted sm:inline">
                {username}
                {isAdmin && (
                  <span className="ml-2 rounded bg-brand/15 px-1.5 py-0.5 text-xs font-semibold text-brand">
                    ADMIN
                  </span>
                )}
              </span>
              <button
                onClick={logout}
                className="rounded-lg border border-border px-3 py-1.5 text-sm font-medium transition hover:border-brand hover:text-white"
              >
                Çıkış
              </button>
            </>
          ) : (
            <Link
              to="/login"
              className="rounded-lg bg-brand px-4 py-1.5 text-sm font-semibold text-white transition hover:bg-brand-hover"
            >
              Giriş yap
            </Link>
          )}
        </div>
      </div>
    </header>
  )
}
