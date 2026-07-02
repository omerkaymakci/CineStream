import { api } from './client'
import type { Genre, GenreRequest } from '@/types'

export async function fetchGenres(): Promise<Genre[]> {
  const { data } = await api.get<Genre[]>('/genres')
  return data
}

export async function createGenre(payload: GenreRequest): Promise<Genre> {
  const { data } = await api.post<Genre>('/genres', payload)
  return data
}

export async function updateGenre(
  id: number,
  payload: GenreRequest,
): Promise<Genre> {
  const { data } = await api.put<Genre>(`/genres/${id}`, payload)
  return data
}

export async function deleteGenre(id: number): Promise<void> {
  await api.delete(`/genres/${id}`)
}
