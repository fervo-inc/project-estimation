"use client"

import { createContext, useContext, ReactNode, useState } from 'react'
import { useRouter } from 'next/navigation'
import { auth } from '@/lib/auth'
import { jwtDecode } from 'jwt-decode'

type Role = 'ADMIN' | 'PROJECT_MANAGER' | 'TEAM_MEMBER'

interface AuthContextType {
  role: Role
  isAuthenticated: boolean
  hasPermission: (requiredRole: Role[]) => boolean
  updateAuthState: (token: string) => void
  logout: () => void
}

interface DecodedToken {
  role: Role
  exp: number
  [key: string]: any
}

const AuthContext = createContext<AuthContextType | undefined>(undefined)

export function AuthProvider({
  children,
}: {
  children: ReactNode
}) {
  const router = useRouter()
  const [role, setRole] = useState<Role>('TEAM_MEMBER')
  const [isAuthenticated, setIsAuthenticated] = useState(false)

  const hasPermission = (requiredRoles: Role[]) => {
    const roleHierarchy: Record<Role, number> = {
      'ADMIN': 3,
      'PROJECT_MANAGER': 2,
      'TEAM_MEMBER': 1
    }

    const userRoleLevel = roleHierarchy[role]
    return requiredRoles.some(role => roleHierarchy[role] <= userRoleLevel)
  }

  const updateAuthState = async (token: string) => {
    try {
      await auth.setToken(token)
      const decodedToken: DecodedToken = jwtDecode(token)
      setRole(decodedToken.role)
      setIsAuthenticated(true)
    } catch (error) {
      console.error('Failed to decode token', error)
      logout()
    }
  }

  const logout = async () => {
    await auth.removeToken()
    setIsAuthenticated(false)
    setRole('TEAM_MEMBER')
    router.replace('/login')
  }

  return (
    <AuthContext.Provider value={{
      role,
      isAuthenticated,
      hasPermission,
      updateAuthState,
      logout
    }}>
      {children}
    </AuthContext.Provider>
  )
}

export function useAuth() {
  const context = useContext(AuthContext)
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider')
  }
  return context
}

