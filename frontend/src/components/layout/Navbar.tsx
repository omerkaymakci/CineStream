import { useState, type ReactNode } from 'react'
import { Link, NavLink, useNavigate, useSearchParams } from 'react-router-dom'
import { useAuth } from '@/auth/useAuth'

function navClass({ isActive }: { isActive: boolean }) {
  return `flex items-center gap-1.5 border-b-2 pb-0.5 text-sm transition ${
    isActive
      ? 'border-brand font-semibold text-brand'
      : 'border-transparent font-medium text-zinc-400 hover:text-white'
  }`
}

function BrandMark({ className = '' }: { className?: string }) {
  return (
    <svg viewBox="0 0 40 40" className={className} fill="none" aria-hidden>
      <defs>
        <linearGradient
          id="brandGrad"
          x1="4"
          y1="4"
          x2="36"
          y2="36"
          gradientUnits="userSpaceOnUse"
        >
          <stop stopColor="#a855f7" />
          <stop offset="1" stopColor="#3b82f6" />
        </linearGradient>
      </defs>
      <path
        d="M30 9.5 A 13.5 13.5 0 1 0 30 30.5"
        stroke="url(#brandGrad)"
        strokeWidth="6"
        strokeLinecap="round"
      />
      <path d="M17 13.5 L29 20 L17 26.5 Z" fill="url(#brandGrad)" />
    </svg>
  )
}

function NavItem({ to, icon, label, end }: {
  to: string
  icon: ReactNode
  label: string
  end?: boolean
}) {
  return (
    <NavLink to={to} className={navClass} end={end}>
      {icon}
      {label}
    </NavLink>
  )
}

export function Navbar() {
  const { authenticated, isAdmin, username, logout } = useAuth()
  const navigate = useNavigate()
  const [searchParams] = useSearchParams()
  const [query, setQuery] = useState(searchParams.get('q') ?? '')
  const [menuOpen, setMenuOpen] = useState(false)

  const onSearch = (value: string) => {
    setQuery(value)
    navigate(value ? `/?q=${encodeURIComponent(value)}` : '/', { replace: true })
  }

  return (
    <header className="sticky top-0 z-40 border-b border-border/70 bg-bg/85 backdrop-blur-md">
      <div className="mx-auto flex h-16 max-w-7xl items-center gap-8 px-4 sm:px-6">
        <Link to="/" className="flex shrink-0 items-center gap-2">
          <BrandMark className="size-9" />
          <span className="text-xl font-bold tracking-tight text-white">
            Cine
            <span className="bg-gradient-to-r from-violet-400 to-blue-500 bg-clip-text text-transparent">
              Stream
            </span>
          </span>
        </Link>

        {authenticated && (
          <nav className="hidden items-center gap-7 md:flex">
            <NavItem to="/" end label="Keşfet" icon={<IconHome />} />
            <NavItem to="/admin/movies" label="Filmler" icon={<IconFilm />} />
            <NavItem to="/listem" label="Listem" icon={<IconHeart />} />
          </nav>
        )}

        <div className="ml-auto flex items-center gap-3">
          {authenticated && (
            <div className="relative hidden lg:block">
              <IconSearch className="pointer-events-none absolute left-3.5 top-1/2 size-4 -translate-y-1/2 text-muted" />
              <input
                value={query}
                onChange={(e) => onSearch(e.target.value)}
                placeholder="Film ara…"
                className="w-72 rounded-full border border-border bg-surface py-2 pl-10 pr-4 text-sm outline-none transition focus:border-brand"
              />
            </div>
          )}

          {authenticated ? (
            <>
              <div className="relative">
                <button
                  onClick={() => setMenuOpen((o) => !o)}
                  className="flex items-center gap-2 rounded-full border border-border px-3 py-1.5 text-sm font-medium transition hover:border-brand"
                >
                  <IconUser />
                  <span className="hidden sm:inline">{username}</span>
                  <IconChevron
                    className={`size-4 text-muted transition ${menuOpen ? 'rotate-180' : ''}`}
                  />
                </button>

                {menuOpen && (
                  <>
                    <button
                      className="fixed inset-0 z-10 cursor-default"
                      onClick={() => setMenuOpen(false)}
                      aria-hidden
                    />
                    <div className="absolute right-0 z-20 mt-2 w-52 overflow-hidden rounded-xl border border-border bg-surface shadow-2xl">
                      <div className="border-b border-border px-4 py-3">
                        <p className="text-sm font-semibold">{username}</p>
                        <p className="mt-0.5 text-xs text-muted">
                          {isAdmin ? 'Yönetici' : 'Üye'}
                        </p>
                      </div>
                      {isAdmin && (
                        <div className="py-1">
                          <MenuLink to="/admin/movies" onClick={() => setMenuOpen(false)}>
                            Film Yönetimi
                          </MenuLink>
                          <MenuLink to="/admin/genres" onClick={() => setMenuOpen(false)}>
                            Tür Yönetimi
                          </MenuLink>
                        </div>
                      )}
                    </div>
                  </>
                )}
              </div>

              <button
                onClick={logout}
                className="rounded-full border border-border px-4 py-1.5 text-sm font-medium transition hover:border-brand hover:text-white"
              >
                Çıkış
              </button>
            </>
          ) : (
            <Link
              to="/login"
              className="rounded-full bg-gradient-to-r from-violet-500 to-blue-600 px-5 py-1.5 text-sm font-semibold text-white transition hover:opacity-90"
            >
              Giriş yap
            </Link>
          )}
        </div>
      </div>
    </header>
  )
}

function MenuLink({ to, onClick, children }: {
  to: string
  onClick: () => void
  children: ReactNode
}) {
  return (
    <Link
      to={to}
      onClick={onClick}
      className="block px-4 py-2 text-sm text-zinc-200 transition hover:bg-surface-2 hover:text-white"
    >
      {children}
    </Link>
  )
}

/* ── Icons ─────────────────────────────────────────────── */
function IconHome() {
  return (
    <svg viewBox="0 0 24 24" className="size-4" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <path d="M3 10.5 12 3l9 7.5" />
      <path d="M5 9.5V21h14V9.5" />
    </svg>
  )
}
function IconFilm() {
  return (
    <svg viewBox="0 0 24 24" className="size-4" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <rect x="3" y="4" width="18" height="16" rx="2" />
      <path d="M7 4v16M17 4v16M3 9h4M17 9h4M3 15h4M17 15h4" />
    </svg>
  )
}
function IconHeart() {
  return (
    <svg viewBox="0 0 24 24" className="size-4" fill="currentColor">
      <path d="M12 21s-7.5-4.7-10-9.3C.6 8.4 2 5 5.2 5c2 0 3.3 1.1 4.8 3 1.5-1.9 2.8-3 4.8-3C18 5 19.4 8.4 22 11.7 19.5 16.3 12 21 12 21z" />
    </svg>
  )
}
function IconSearch({ className = '' }: { className?: string }) {
  return (
    <svg viewBox="0 0 24 24" className={className} fill="none" stroke="currentColor" strokeWidth="2">
      <circle cx="11" cy="11" r="7" />
      <path d="m21 21-4.3-4.3" />
    </svg>
  )
}
function IconUser() {
  return (
    <svg viewBox="0 0 24 24" className="size-4" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <circle cx="12" cy="8" r="4" />
      <path d="M4 21c0-4 4-6 8-6s8 2 8 6" />
    </svg>
  )
}
function IconChevron({ className = '' }: { className?: string }) {
  return (
    <svg viewBox="0 0 24 24" className={className} fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <path d="m6 9 6 6 6-6" />
    </svg>
  )
}
