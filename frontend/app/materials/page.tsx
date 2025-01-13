"use client"

import { useState } from "react"
import { Plus } from 'lucide-react'

import { Button } from "@/components/ui/button"
import { MaterialsTable } from "@/components/materials/materials-table"
import { AddMaterialDialog } from "@/components/materials/add-material-dialog"
import { useAuth } from "@/contexts/auth-context"

export default function MaterialsPage() {
  const { hasPermission } = useAuth()
  const [isAddDialogOpen, setIsAddDialogOpen] = useState(false)

  return (
    <div className="flex flex-col gap-4">
      <div className="flex items-center justify-between">
        <h1 className="text-3xl font-bold">Materials</h1>
        {hasPermission(['ADMIN', 'PROJECT_MANAGER']) && (
          <Button onClick={() => setIsAddDialogOpen(true)}>
            <Plus className="mr-2 h-4 w-4" />
            Add Material
          </Button>
        )}
      </div>
      <MaterialsTable />
      <AddMaterialDialog 
        open={isAddDialogOpen} 
        onOpenChange={setIsAddDialogOpen}
      />
    </div>
  )
}

