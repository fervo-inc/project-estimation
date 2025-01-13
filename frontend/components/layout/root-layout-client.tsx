'use client'

import { usePathname } from 'next/navigation'
import { MainLayout } from "./main-layout"

export function RootLayoutClient({ children }: { children: React.ReactNode }) {
  const pathname = usePathname()
  const isLoginPage = pathname === '/login'

  return isLoginPage ? children : <MainLayout>{children}</MainLayout>
}

