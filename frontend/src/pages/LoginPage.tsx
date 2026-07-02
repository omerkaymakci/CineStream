import { useState } from 'react'
import { Navigate, useLocation, useNavigate } from 'react-router-dom'
import axios from 'axios'
import { useAuth } from '@/auth/useAuth'
import { Spinner } from '@/components/Spinner'

interface LocationState {
  from?: { pathname?: string }
}

export function LoginPage() {
  const { authenticated, initialized, login } = useAuth()
  const navigate = useNavigate()
  const location = useLocation()
  const from = (location.state as LocationState | null)?.from?.pathname ?? '/'

  const [username, setUsername] = useState('')
  const [password, setPassword] = useState('')
  const [showPassword, setShowPassword] = useState(false)
  const [error, setError] = useState<string | null>(null)
  const [loading, setLoading] = useState(false)

  if (initialized && authenticated) {
    return <Navigate to={from} replace />
  }

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault()
    if (!username.trim() || !password) return
    setError(null)
    setLoading(true)
    try {
      await login(username.trim(), password)
      navigate(from, { replace: true })
    } catch (err) {
      if (axios.isAxiosError(err)) {
        if (err.response) {
          setError('Kullanıcı adı veya şifre hatalı.')
        } else {
          setError(
            'Keycloak sunucusuna ulaşılamadı. Backend ve CORS ayarlarını kontrol et.',
          )
        }
      } else {
        setError('Beklenmeyen bir hata oluştu.')
      }
    } finally {
      setLoading(false)
    }
  }

  const inputClass =
    'w-full rounded-lg border border-border bg-surface-2 px-3 py-2.5 text-sm outline-none transition focus:border-brand'

  return (
    <div className="relative flex min-h-[calc(100vh-4rem)] items-center justify-center px-4">
      <div
        className="absolute inset-0 -z-10 opacity-70"
        style={{
          background:
            'radial-gradient(55% 50% at 50% 0%, rgba(37,99,235,0.18), transparent)',
        }}
      />

      <div className="w-full max-w-md">
        <div className="mb-8 text-center">
          <div className="mx-auto mb-4 flex size-12 items-center justify-center rounded-full bg-white text-bg">
            <svg viewBox="0 0 24 24" className="ml-0.5 size-6" fill="currentColor">
              <path d="M8 5v14l11-7z" />
            </svg>
          </div>
          <h1 className="text-2xl font-bold">CineStream'e giriş yap</h1>
          <p className="mt-1 text-sm text-muted">Devam etmek için hesabına giriş yap</p>
        </div>

        <form
          onSubmit={handleSubmit}
          autoComplete="off"
          className="space-y-4 rounded-2xl border border-border bg-surface p-6 shadow-xl"
        >
          {error && (
            <div className="rounded-lg border border-red-500/40 bg-red-500/10 px-3 py-2 text-sm text-red-300">
              {error}
            </div>
          )}

          <div>
            <label className="mb-1 block text-sm font-medium text-zinc-300">
              Kullanıcı adı
            </label>
            <input
              className={inputClass}
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              placeholder="kullanıcı adın"
              autoFocus
              autoComplete="off"
            />
          </div>

          <div>
            <label className="mb-1 block text-sm font-medium text-zinc-300">
              Şifre
            </label>
            <div className="relative">
              <input
                className={`${inputClass} pr-10`}
                type={showPassword ? 'text' : 'password'}
                value={password}
                onChange={(e) => setPassword(e.target.value)}
                placeholder="••••••••"
                autoComplete="off"
              />
              <button
                type="button"
                onClick={() => setShowPassword((s) => !s)}
                className="absolute right-3 top-1/2 -translate-y-1/2 text-muted transition hover:text-zinc-200"
                aria-label={showPassword ? 'Şifreyi gizle' : 'Şifreyi göster'}
              >
                {showPassword ? (
                  <svg
                    viewBox="0 0 24 24"
                    className="size-5"
                    fill="none"
                    stroke="currentColor"
                    strokeWidth="1.7"
                    strokeLinecap="round"
                    strokeLinejoin="round"
                  >
                    <path d="M9.9 4.24A9.1 9.1 0 0 1 12 4c6.5 0 10 8 10 8a13.2 13.2 0 0 1-1.67 2.68" />
                    <path d="M6.6 6.6A13.5 13.5 0 0 0 2 12s3.5 8 10 8a9.7 9.7 0 0 0 5.4-1.6" />
                    <path d="m2 2 20 20" />
                  </svg>
                ) : (
                  <svg
                    viewBox="0 0 24 24"
                    className="size-5"
                    fill="none"
                    stroke="currentColor"
                    strokeWidth="1.7"
                    strokeLinecap="round"
                    strokeLinejoin="round"
                  >
                    <path d="M2 12s3.5-8 10-8 10 8 10 8-3.5 8-10 8-10-8-10-8Z" />
                    <circle cx="12" cy="12" r="3" />
                  </svg>
                )}
              </button>
            </div>
          </div>

          <button
            type="submit"
            disabled={loading}
            className="flex w-full items-center justify-center gap-2 rounded-lg bg-brand px-4 py-2.5 font-semibold text-white transition hover:bg-brand-hover disabled:opacity-60"
          >
            {loading && <Spinner className="size-4" />}
            Giriş yap
          </button>
        </form>
      </div>
    </div>
  )
}
