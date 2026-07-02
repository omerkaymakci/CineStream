import { Outlet } from 'react-router-dom'
import { Navbar } from './Navbar'

export function Layout() {
  return (
    <div className="flex min-h-screen flex-col">
      <Navbar />
      <main className="flex-1">
        <Outlet />
      </main>
      <footer className="border-t border-border/70 py-6 text-center text-xs text-muted">
        CineStream · Demo streaming platformu
      </footer>
    </div>
  )
}
