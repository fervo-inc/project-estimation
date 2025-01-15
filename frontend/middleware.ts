import type {NextRequest} from 'next/server'
import {NextResponse} from 'next/server'

const COOKIE_NAME = 'auth_token'

// Define protected routes
const protectedRoutes = ['/dashboard', '/projects', '/materials', '/vendors', '/labor', '/estimates', '/settings']

export function middleware(request: NextRequest) {
  // Get the pathname of the request
  const {pathname} = request.nextUrl

  // Check if the pathname is a protected route
  const isProtectedRoute = protectedRoutes.some((route) => pathname.startsWith(route))
  console.log(`Middleware - Protected Route: ${isProtectedRoute}`)
  if (isProtectedRoute) {
    // Get the token from the HTTP-only cookie
    const token = request.cookies.get(COOKIE_NAME)
    console.log('Middleware - Token:', token)
    // If no token is present, redirect to the login page
    if (!token) {
      console.log('Middleware - No cookie present, redirecting to login page')
      const url = new URL('/login', request.url)
      url.searchParams.set('from', pathname)
      return NextResponse.redirect(url)
    }

    const authRequest = request.clone()
    authRequest.headers.set('Authorization', `Bearer ${token}`)

    return NextResponse.next({
      request: authRequest
    })
  }

  return NextResponse.next()
}

export const config = {
  matcher: [
    /*
     * Match all request paths except for the ones starting with:
     * - api (API routes)
     * - _next/static (static files)
     * - _next/image (image optimization files)
     * - favicon.ico (favicon file)
     */
    '/((?!api|_next/static|_next/image|favicon.ico).*)'
  ]
}
