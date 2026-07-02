import { api } from './client'
import type { Movie, MovieRequest } from '@/types'

export async function fetchMovies(): Promise<Movie[]> {
  const { data } = await api.get<Movie[]>('/movies')
  return data
}

export async function fetchMovie(id: number): Promise<Movie> {
  const { data } = await api.get<Movie>(`/movies/${id}`)
  return data
}

export async function createMovie(payload: MovieRequest): Promise<Movie> {
  const { data } = await api.post<Movie>('/movies', payload)
  return data
}

export async function updateMovie(
  id: number,
  payload: MovieRequest,
): Promise<Movie> {
  const { data } = await api.put<Movie>(`/movies/${id}`, payload)
  return data
}

export async function deleteMovie(id: number): Promise<void> {
  await api.delete(`/movies/${id}`)
}
