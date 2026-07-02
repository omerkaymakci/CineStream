# CineStream — Frontend

React 19 + TypeScript + Vite + Tailwind CSS v4 tabanlı streaming arayüzü.
Backend mikroservislerine (api-gateway → movie-service) bağlanır ve kimlik
doğrulama için **Keycloak** kullanır.

## Özellikler

- 🎬 **Keşfet / Katalog** — yayındaki filmleri poster kartlarıyla listeler, başlığa göre arama
- 🔎 **Film detay** — açıklama, süre, durum ve "İzle" aksiyonu
- ▶️ **Video oynatıcı** — `videoUrl` üzerinden HTML5 player
- 🛠️ **Admin paneli** — film ve tür (genre) için CRUD (yalnızca `ADMIN` rolü)
- 🔐 **Keycloak auth** — `check-sso` + PKCE, JWT gateway'e `Bearer` olarak iletilir
- 🎨 Netflix esintili koyu tema (Tailwind v4)

## Gereksinim: Node 20+

> ⚠️ Vite 7 ve Tailwind v4, **Node.js 20.19+** (veya 22.12+) gerektirir.
> Sistemde nvm ile `v20.19.0` kurulu. Kullanmak için:
>
> ```bash
> nvm use 20.19.0
> ```
>
> Node 16/18 ile `npm run dev` / `npm run build` **çalışmaz** (native binding hatası).

## Kurulum & çalıştırma

```bash
npm install
npm run dev      # http://localhost:3000
npm run build    # tsc + production build (dist/)
npm run lint
```

## Ortam değişkenleri

`.env` (örnek için `.env.example`):

```
VITE_API_BASE_URL=http://localhost:8080/movie-service
VITE_KEYCLOAK_URL=http://localhost:9000
VITE_KEYCLOAK_REALM=cinestream
VITE_KEYCLOAK_CLIENT_ID=cinestream-frontend
```

## Keycloak kurulumu (tek seferlik)

Backend `docker-compose` Keycloak'ı `start-dev` ile ayağa kaldırır ama realm/client
import etmez. Admin konsolundan (`http://localhost:9000`, admin/admin):

1. **Realm** oluştur: `cinestream`
2. **Client** oluştur:
   - Client ID: `cinestream-frontend`
   - Client type: **OpenID Connect**, **Public** (client authentication kapalı)
   - Valid redirect URIs: `http://localhost:3000/*`
   - Web origins: `http://localhost:3000`
3. **Realm roles** ekle: `USER`, `ADMIN`
4. Kullanıcı oluştur, şifre ata ve rol ata (izleyici için `USER`, yönetici için `ADMIN`).

> Roller JWT'nin `realm_access.roles` claim'inden okunur; gateway `GET` için
> `USER`/`ADMIN`, yazma işlemleri için `ADMIN` bekler.

## Mimari

```
src/
  api/        axios client (JWT interceptor) + movie/genre servisleri
  auth/       keycloak singleton, AuthProvider, useAuth, ProtectedRoute
  hooks/      React Query hook'ları (useMovies, useGenres)
  components/ layout (Navbar), MovieCard, Modal, durum bileşenleri
  pages/      Home, MovieDetail, Watch, admin/ (Movies, Genres)
  lib/        yardımcılar (süre formatı, poster gradyanı)
  config.ts   env tabanlı runtime config
```

Veri katmanı **@tanstack/react-query**, HTTP **axios**, routing **react-router-dom**.
