"use client"

import { usePathname } from "next/navigation"
import Link from "next/link"
import { Building2, CircleDollarSign, Hammer, LayoutDashboard, Users, Store } from 'lucide-react'
import { ThemeProvider } from "next-themes"

import { cn } from "@/lib/utils"
import { Button } from "@/components/ui/button"
import {
  Sidebar,
  SidebarContent,
  SidebarHeader,
  SidebarProvider,
  SidebarTrigger,
} from "@/components/ui/sidebar"
import { DarkModeToggle } from "@/components/dark-mode-toggle"
import { UserNav } from "@/components/layout/user-nav"

export function MainLayout({ children }: { children: React.ReactNode }) {
  const pathname = usePathname()

  const navigation = [
    {
      name: "Dashboard",
      href: "/dashboard",
      icon: LayoutDashboard,
      current: pathname === "/dashboard",
    },
    {
      name: "Projects",
      href: "/projects",
      icon: Building2,
      current: pathname.startsWith("/projects"),
    },
    {
      name: "Materials",
      href: "/materials",
      icon: Hammer,
      current: pathname.startsWith("/materials"),
    },
    {
      name: "Vendors",
      href: "/vendors",
      icon: Store,
      current: pathname.startsWith("/vendors"),
    },
    {
      name: "Labor",
      href: "/labor",
      icon: Users,
      current: pathname.startsWith("/labor"),
    },
    {
      name: "Estimates",
      href: "/estimates",
      icon: CircleDollarSign,
      current: pathname.startsWith("/estimates"),
    },
  ]

  return (
    <ThemeProvider
      attribute="class"
      defaultTheme="system"
      enableSystem
      disableTransitionOnChange
    >
      <SidebarProvider>
        <div className="flex h-screen bg-background">
          <Sidebar>
            <SidebarHeader className="border-b px-2 py-4">
              <h1 className="text-xl font-bold">Project Estimation</h1>
            </SidebarHeader>
            <SidebarContent className="flex flex-col h-full">
              <nav className="flex flex-1 flex-col gap-1 p-2">
                {navigation.map((item) => (
                  <Button
                    key={item.name}
                    variant={item.current ? "secondary" : "ghost"}
                    className={cn(
                      "w-full justify-start gap-2",
                      item.current && "bg-secondary"
                    )}
                    asChild
                  >
                    <Link href={item.href}>
                      <item.icon className="h-4 w-4" />
                      {item.name}
                    </Link>
                  </Button>
                ))}
              </nav>
              <div className="border-t mt-auto">
                <UserNav />
              </div>
            </SidebarContent>
          </Sidebar>
          <div className="flex flex-1 flex-col overflow-hidden">
            <header className="flex h-14 items-center gap-4 border-b bg-background px-4 lg:h-[60px] lg:px-6">
              <SidebarTrigger />
              <div className="flex-1" />
              <DarkModeToggle />
            </header>
            <main className="flex-1 overflow-y-auto p-4 md:p-6 lg:p-8 mx-auto w-full max-w-7xl">
              {children}
            </main>
          </div>
        </div>
      </SidebarProvider>
    </ThemeProvider>
  )
}

