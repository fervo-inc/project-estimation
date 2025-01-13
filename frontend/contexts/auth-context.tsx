"use client"

import { createContext, useContext, ReactNode, useState } from 'react'
import { useRouter } from 'next/navigation'
import { auth } from '@/lib/auth'

type Role = 'ADMIN' | 'PROJECT_MANAGER' | 'TEAM_MEMBER'

interface AuthContextType {
  role: Role
  isAuthenticated: boolean
  hasPermission: (requiredRole: Role[]) => boolean
  updateAuthState: (token: string) => void
  logout: () => void
}

const AuthContext = createContext<AuthContextType | undefined>(undefined)

export function AuthProvider({ 
  children,
}: { 
  children: ReactNode
}) {
  const router = useRouter()
  const [role, setRole] = useState<Role>('ADMIN')
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
    await auth.setToken(token)
    setIsAuthenticated(true)
    // TODO: Decode JWT to get role
    setRole('ADMIN') // Temporary default
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

