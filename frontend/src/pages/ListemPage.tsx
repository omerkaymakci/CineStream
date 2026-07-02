import { EmptyState } from '@/components/EmptyState'

export function ListemPage() {
  return (
    <div className="mx-auto max-w-7xl px-4 py-8 sm:px-6">
      <h1 className="mb-1 text-3xl font-bold">Listem</h1>
      <p className="mb-8 text-muted">Daha sonra izlemek için kaydettiğin filmler</p>

      <EmptyState
        icon="❤️"
        title="Listen henüz boş"
        description="İzleme listesi özelliği yakında geliyor. O zamana kadar filmleri keşfetmeye devam et."
      />
    </div>
  )
}
