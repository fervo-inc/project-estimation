'use client'

import { createContext, ReactNode, useContext, useState } from 'react'
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
  /**
   * **Issuer**
   *
   * It identifies the entity (e.g. server or application) that created and signed the token.
   */
  iss: string
  /**
   * **Subject**
   *
   * The subject, i.e., the principal/user for whom this token has been issued
   */
  sub: string
  /**
   * **Audience**
   *
   * Represents an array of audience identifiers or intended recipients.
   * Typically used to specify the audience for a particular operation, token, or resource.
   *
   * Example: "TakeCostClient"
   */
  aud: string[]
  /**
   * **Expiration**
   *
   * Represents the exponent value used in mathematical calculations or algorithms.
   * Typically utilized as a power in exponential expressions.
   */
  exp: number
  /**
   * **Issued At**
   *
   * The `iat` (issued at) variable typically represents the timestamp
   * for when a particular object, token, or piece of data was created
   * or issued. It is commonly used in contexts such as authentication
   * or token management.
   *
   * This value is usually expressed as an epoch in seconds
   */
  iat: number
  /**
   * **JWT ID**
   *
   * Represents the unique identifier for a JSON Web Token (JWT).
   * This identifier is used to prevent replay attacks by ensuring
   * that each token is used only once.
   */
  jti: string
  /**
   * **Not Before**
   *
   * Indicates the "Not Before" timestamp for a token or process.
   *
   * The `nbf` (Not Before) property specifies the earliest time, in seconds
   * since the epoch, at which the token or process is considered valid.
   * Attempts to use the token or execute the process before this time
   * should result in a failure or rejection.
   */
  nbf: number
  /**
   * **Roles/Permissions**
   *
   * Represents a collection of roles associated with a user, functionality, or system component.
   * Each role defines a specific set of permissions, responsibilities, or behaviors.
   *
   * @type {Role[]}
   */
  roles: Role[]

  [key: string]: any
}

const AuthContext = createContext<AuthContextType | undefined>(undefined)

export function AuthProvider({ children }: { children: ReactNode }) {
  const router = useRouter()
  const [role, setRole] = useState<Role>('TEAM_MEMBER')
  const [isAuthenticated, setIsAuthenticated] = useState(false)

  const hasPermission = (requiredRoles: Role[]) => {
    const roleHierarchy: Record<Role, number> = {
      ADMIN: 3,
      PROJECT_MANAGER: 2,
      TEAM_MEMBER: 1
    }

    const userRoleLevel = roleHierarchy[role]
    return requiredRoles.some((role) => roleHierarchy[role] <= userRoleLevel)
  }

  const updateAuthState = async (token: string) => {
    try {
      await auth.setToken(token)
      const decodedToken: DecodedToken = jwtDecode(token)
      setRole(decodedToken.roles[0])
      setIsAuthenticated(true)
    } catch (error) {
      console.error('Failed to decode token', error)
      await logout()
    }
  }

  const logout = async () => {
    await auth.removeToken()
    setIsAuthenticated(false)
    setRole('TEAM_MEMBER')
    router.replace('/login')
  }

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
