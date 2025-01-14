'use client'

import { useState } from 'react'
import { Plus } from 'lucide-react'

import { Button } from '@/components/ui/button'
import { ProjectsTable } from '@/components/projects/projects-table'
import { AddProjectDialog } from '@/components/projects/add-project-dialog'
import { useAuth } from '@/contexts/auth-context'

export default function ProjectsPage() {
  const { hasPermission } = useAuth()
  const [isAddDialogOpen, setIsAddDialogOpen] = useState(false)

  return (
    <div className="flex flex-col gap-4">
      <div className="flex items-center justify-between">
        <h1 className="text-3xl font-bold">Projects</h1>
        {hasPermission(['ADMIN', 'PROJECT_MANAGER']) && (
          <Button onClick={() => setIsAddDialogOpen(true)}>
            <Plus className="mr-2 h-4 w-4" />
            Add Project
          </Button>
        )}
      </div>
      <ProjectsTable />
      <AddProjectDialog open={isAddDialogOpen} onOpenChange={setIsAddDialogOpen} />
    </div>
  )
}
