import {
  useMutation,
  useQuery,
  useQueryClient,
} from '@tanstack/react-query'
import {
  createMovie,
  deleteMovie,
  fetchMovie,
  fetchMovies,
  updateMovie,
} from '@/api/movies'
import type { MovieRequest } from '@/types'

export const movieKeys = {
  all: ['movies'] as const,
  detail: (id: number) => ['movies', id] as const,
}

export function useMovies() {
  return useQuery({ queryKey: movieKeys.all, queryFn: fetchMovies })
}

export function useMovie(id: number) {
  return useQuery({
    queryKey: movieKeys.detail(id),
    queryFn: () => fetchMovie(id),
    enabled: Number.isFinite(id),
  })
}

export function useCreateMovie() {
  const qc = useQueryClient()
  return useMutation({
    mutationFn: (payload: MovieRequest) => createMovie(payload),
    onSuccess: () => qc.invalidateQueries({ queryKey: movieKeys.all }),
  })
}

export function useUpdateMovie() {
  const qc = useQueryClient()
  return useMutation({
    mutationFn: ({ id, payload }: { id: number; payload: MovieRequest }) =>
      updateMovie(id, payload),
    onSuccess: (_data, { id }) => {
      qc.invalidateQueries({ queryKey: movieKeys.all })
      qc.invalidateQueries({ queryKey: movieKeys.detail(id) })
    },
  })
}

export function useDeleteMovie() {
  const qc = useQueryClient()
  return useMutation({
    mutationFn: (id: number) => deleteMovie(id),
    onSuccess: () => qc.invalidateQueries({ queryKey: movieKeys.all }),
  })
}
