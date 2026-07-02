/**
 * Reads an image File, downscales it to `maxWidth` (preserving aspect ratio)
 * and returns a compact JPEG base64 data URL. Downscaling keeps the value small
 * enough to store in the DB and ship inside movie JSON responses.
 */
export async function fileToPosterDataUrl(
  file: File,
  maxWidth = 500,
  quality = 0.82,
): Promise<string> {
  const dataUrl = await readAsDataUrl(file)
  const img = await loadImage(dataUrl)

  const scale = Math.min(1, maxWidth / img.width)
  const width = Math.round(img.width * scale)
  const height = Math.round(img.height * scale)

  const canvas = document.createElement('canvas')
  canvas.width = width
  canvas.height = height

  const ctx = canvas.getContext('2d')
  if (!ctx) return dataUrl // fallback: original data URL

  ctx.drawImage(img, 0, 0, width, height)
  return canvas.toDataURL('image/jpeg', quality)
}

function readAsDataUrl(file: File): Promise<string> {
  return new Promise((resolve, reject) => {
    const reader = new FileReader()
    reader.onload = () => resolve(reader.result as string)
    reader.onerror = () => reject(reader.error)
    reader.readAsDataURL(file)
  })
}

function loadImage(src: string): Promise<HTMLImageElement> {
  return new Promise((resolve, reject) => {
    const img = new Image()
    img.onload = () => resolve(img)
    img.onerror = () => reject(new Error('Görsel yüklenemedi'))
    img.src = src
  })
}
