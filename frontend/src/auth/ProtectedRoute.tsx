import { type ReactNode } from 'react'
import { Navigate, useLocation } from 'react-router-dom'
import { useAuth } from './useAuth'
import { FullPageLoader } from '@/components/FullPageLoader'
import { AccessDenied } from '@/components/AccessDenied'

interface ProtectedRouteProps {
  children: ReactNode
  /** When set, the user must additionally hold this realm role. */
  requireRole?: string
}

export function ProtectedRoute({ children, requireRole }: ProtectedRouteProps) {
  const { initialized, authenticated, roles } = useAuth()
  const location = useLocation()

  if (!initialized) {
    return <FullPageLoader label="Oturum kontrol ediliyor…" />
  }

  if (!authenticated) {
    return <Navigate to="/login" state={{ from: location }} replace />
  }

  if (requireRole && !roles.includes(requireRole)) {
    return <AccessDenied requiredRole={requireRole} />
  }

  return <>{children}</>
}
