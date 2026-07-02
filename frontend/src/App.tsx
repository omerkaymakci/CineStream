import { BrowserRouter, Navigate, Route, Routes } from 'react-router-dom'
import { AuthProvider } from '@/auth/AuthProvider'
import { ProtectedRoute } from '@/auth/ProtectedRoute'
import { Layout } from '@/components/layout/Layout'
import { HomePage } from '@/pages/HomePage'
import { LoginPage } from '@/pages/LoginPage'
import { MovieDetailPage } from '@/pages/MovieDetailPage'
import { WatchPage } from '@/pages/WatchPage'
import { ListemPage } from '@/pages/ListemPage'
import { AdminMoviesPage } from '@/pages/admin/AdminMoviesPage'
import { AdminGenresPage } from '@/pages/admin/AdminGenresPage'
import { NotFoundPage } from '@/pages/NotFoundPage'

function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route element={<Layout />}>
            <Route path="/" element={<HomePage />} />
            <Route path="/login" element={<LoginPage />} />

            <Route
              path="/movies/:id"
              element={
                <ProtectedRoute>
                  <MovieDetailPage />
                </ProtectedRoute>
              }
            />
            <Route
              path="/watch/:id"
              element={
                <ProtectedRoute>
                  <WatchPage />
                </ProtectedRoute>
              }
            />

            <Route
              path="/listem"
              element={
                <ProtectedRoute>
                  <ListemPage />
                </ProtectedRoute>
              }
            />

            <Route
              path="/admin"
              element={<Navigate to="/admin/movies" replace />}
            />
            <Route
              path="/admin/movies"
              element={
                <ProtectedRoute requireRole="ADMIN">
                  <AdminMoviesPage />
                </ProtectedRoute>
              }
            />
            <Route
              path="/admin/genres"
              element={
                <ProtectedRoute requireRole="ADMIN">
                  <AdminGenresPage />
                </ProtectedRoute>
              }
            />

            <Route path="*" element={<NotFoundPage />} />
          </Route>
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  )
}

export default App
