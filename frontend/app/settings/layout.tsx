'use client'

import Link from 'next/link'
import { usePathname } from 'next/navigation'
import { cn } from '@/lib/utils'
import { buttonVariants } from '@/components/ui/button'
import { Separator } from '@/components/ui/separator'

interface SettingsLayoutProps {
  children: React.ReactNode
}

export default function SettingsLayout({ children }: SettingsLayoutProps) {
  const pathname = usePathname()

  const sidebarNavItems = [
    {
      title: 'Account',
      href: '/settings/account'
    },
    {
      title: 'Appearance',
      href: '/settings/appearance'
    }
  ]

  return (
    <div className="pb-16">
      <div className="space-y-0.5">
        <h2 className="text-2xl font-bold tracking-tight">Settings</h2>
        <p className="text-muted-foreground">Manage your account settings and set e-mail preferences.</p>
      </div>
      <Separator className="my-6" />
      <div className="flex flex-col space-y-8 lg:flex-row lg:space-x-12 lg:space-y-0">
        <aside className="-mx-4 lg:w-1/5">
          <nav className="flex space-x-2 lg:flex-col lg:space-x-0 lg:space-y-1">
            {sidebarNavItems.map((item) => (
              <Link
                key={item.href}
                href={item.href}
                className={cn(
                  buttonVariants({ variant: 'ghost' }),
                  pathname === item.href ? 'bg-muted hover:bg-muted' : 'hover:bg-transparent hover:underline',
                  'justify-start'
                )}
              >
                {item.title}
              </Link>
            ))}
          </nav>
        </aside>
        <div className="flex-1 lg:max-w-2xl">{children}</div>
      </div>
    </div>
  )
}
