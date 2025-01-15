import { auth } from './auth'

const API_BASE_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api/v1'

async function buildBaseHeaders(headerInit: HeadersInit = {}) {
  const token = await auth.getToken()
  const headers = {
    'Content-Type': 'application/json',
    Authorization: `Bearer ${token}`,
    ...headerInit
  }
  return headers
}

export async function fetchWithAuth<T>(endpoint: string, options: RequestInit = {}): Promise<T> {
  const headers = await buildBaseHeaders(options.headers)
  const response = await fetch(`${API_BASE_URL}${endpoint}`, {
    ...options,
    headers
    // credentials: 'include' // This ensures cookies are sent with requests
  })

  if (response.status === 401) {
    // Token might be expired or invalid
    await auth.removeToken()
    window.location.href = '/login'
    throw new Error('Session expired')
  }

  if (!response.ok) {
    throw new Error(`API request failed: ${response.statusText}`)
  }

  return response.json() as T
}

export async function deleteResource(
  endpoint: string,
  options: RequestInit = {}
): Promise<{ status: number; deleted: boolean }> {
  const headers = await buildBaseHeaders(options.headers)

  const response = await fetch(`${API_BASE_URL}${endpoint}`, {
    method: 'DELETE',
    headers
  })
  return { status: response.status, deleted: response.status === 200 || response.status === 204 }
}
