"use client"

import { useState } from "react"
import { Plus } from 'lucide-react'

import { Button } from "@/components/ui/button"
import { LaborTable } from "@/components/labor/labor-table"
import { AddLaborDialog } from "@/components/labor/add-labor-dialog"
import { useAuth } from "@/contexts/auth-context"

export default function LaborPage() {
  const { hasPermission } = useAuth()
  const [isAddDialogOpen, setIsAddDialogOpen] = useState(false)
  const [key, setKey] = useState(0) // Used to force re-render of table

  return (
    <div className="flex flex-col gap-4">
      <div className="flex items-center justify-between">
        <h1 className="text-3xl font-bold">Labor</h1>
        {hasPermission(['ADMIN', 'PROJECT_MANAGER']) && (
          <Button onClick={() => setIsAddDialogOpen(true)}>
            <Plus className="mr-2 h-4 w-4" />
            Add Labor
          </Button>
        )}
      </div>
      <LaborTable key={key} />
      <AddLaborDialog 
        open={isAddDialogOpen} 
        onOpenChange={setIsAddDialogOpen}
        onSuccess={() => setKey(prev => prev + 1)}
      />
    </div>
  )
}

