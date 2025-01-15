'use client'

import { createContext, ReactNode, useCallback, useContext, useEffect, useState } from 'react'
import { useRouter } from 'next/navigation'
import { auth, DecodedToken, Role } from '@/lib/auth'
import { jwtDecode } from 'jwt-decode'

interface AuthContextType {
  role: Role
  isAuthenticated: boolean
  hasPermission: (requiredRole: Role[]) => boolean
  updateAuthState: (token: string) => void
  logout: () => void
}

const AuthContext = createContext<AuthContextType | undefined>(undefined)

export function AuthProvider({ children }: { children: ReactNode }) {
  const router = useRouter()
  const [role, setRole] = useState<Role>('NONE')
  const [isAuthenticated, setIsAuthenticated] = useState(false)

  const hasPermission = (requiredRoles: Role[]) => {
    console.log('AuthProvider.isAuthenticated:', isAuthenticated)
    console.log('AuthProvider.hasPermission:', requiredRoles)
    const roleHierarchy: Record<Role, number> = {
      ADMIN: 3,
      PROJECT_MANAGER: 2,
      TEAM_MEMBER: 1,
      NONE: 0
    }

    const userRoleLevel = roleHierarchy[role]
    return requiredRoles.some((role) => roleHierarchy[role] <= userRoleLevel)
  }

  const updateAuthState = useCallback(
    async (token: string) => {
      console.log('AuthProvider.updateAuthState:', token)
      try {
        await auth.setToken(token)
        const decodedToken: DecodedToken = jwtDecode(token)
        console.log('AuthProvider.updateAuthState - roles:', decodedToken.roles)
        setRole(decodedToken.roles[0])
        setIsAuthenticated(true)
      } catch (error) {
        console.error('AuthProvider - Failed to decode token', error)
        await logout()
      }
    },
    [setIsAuthenticated, setRole]
  )

  const restoreAuthState = useCallback(async () => {
    console.log('AuthProvider.restoreAuthState')
    const token = await auth.getToken()
    if (token) {
      await updateAuthState(token)
    }
  }, [updateAuthState, auth.getToken])

  const logout = useCallback(async () => {
    console.log('AuthProvider.logout')
    await auth.removeToken()
    setIsAuthenticated(false)
    setRole('NONE')
    router.replace('/login')
  }, [setIsAuthenticated, setRole])

  useEffect(() => {
    restoreAuthState()
  }, [restoreAuthState])

  return (
    <AuthContext.Provider
      value={{
        role,
        isAuthenticated,
        hasPermission,
        updateAuthState,
        logout
      }}
    >
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
