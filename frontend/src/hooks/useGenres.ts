import {
  useMutation,
  useQuery,
  useQueryClient,
} from '@tanstack/react-query'
import {
  createGenre,
  deleteGenre,
  fetchGenres,
  updateGenre,
} from '@/api/genres'
import type { GenreRequest } from '@/types'

export const genreKeys = {
  all: ['genres'] as const,
}

export function useGenres() {
  return useQuery({ queryKey: genreKeys.all, queryFn: fetchGenres })
}

export function useCreateGenre() {
  const qc = useQueryClient()
  return useMutation({
    mutationFn: (payload: GenreRequest) => createGenre(payload),
    onSuccess: () => qc.invalidateQueries({ queryKey: genreKeys.all }),
  })
}

export function useUpdateGenre() {
  const qc = useQueryClient()
  return useMutation({
    mutationFn: ({ id, payload }: { id: number; payload: GenreRequest }) =>
      updateGenre(id, payload),
    onSuccess: () => qc.invalidateQueries({ queryKey: genreKeys.all }),
  })
}

export function useDeleteGenre() {
  const qc = useQueryClient()
  return useMutation({
    mutationFn: (id: number) => deleteGenre(id),
    onSuccess: () => qc.invalidateQueries({ queryKey: genreKeys.all }),
  })
}
