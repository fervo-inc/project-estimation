import { Inter } from 'next/font/google'
import { Toaster } from "@/components/ui/toaster"

import { RootLayoutClient } from "@/components/layout/root-layout-client"
import { AuthProvider } from "@/contexts/auth-context"

import "./globals.css"

const inter = Inter({ subsets: ["latin"] })

export const metadata = {
  title: 'Project Estimation',
  description: 'Project estimation and management system',
}

export default function RootLayout({
  children,
}: {
  children: React.ReactNode
}) {
  return (
    <html lang="en" suppressHydrationWarning>
      <body className={`${inter.className} min-h-screen bg-background`}>
        <AuthProvider>
          <RootLayoutClient>{children}</RootLayoutClient>
          <Toaster />
        </AuthProvider>
      </body>
    </html>
  )
}

