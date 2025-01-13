import { cookies } from 'next/headers'
import { NextResponse } from 'next/server'

const COOKIE_NAME = 'auth_token'

export async function POST(request: Request) {
  const { token } = await request.json()
  
  // Set HTTP-only cookie
  cookies().set({
    name: COOKIE_NAME,
    value: token,
    httpOnly: true,
    secure: process.env.NODE_ENV === 'production',
    sameSite: 'lax',
    path: '/',
    // Set cookie expiry to 24 hours
    maxAge: 60 * 60 * 24
  })

  return new NextResponse('Token set', { status: 200 })
}

export async function DELETE() {
  // Delete the cookie
  cookies().delete(COOKIE_NAME)
  
  return new NextResponse('Token deleted', { status: 200 })
}

