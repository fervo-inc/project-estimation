"use client"

import { useState } from "react"
import { Plus } from 'lucide-react'

import { Button } from "@/components/ui/button"
import { VendorsTable } from "@/components/vendors/vendors-table"
import { AddVendorDialog } from "@/components/vendors/add-vendor-dialog"
import { useAuth } from "@/contexts/auth-context"

export default function VendorsPage() {
  const { hasPermission } = useAuth()
  const [isAddDialogOpen, setIsAddDialogOpen] = useState(false)

  return (
    <div className="flex flex-col gap-4">
      <div className="flex items-center justify-between">
        <h1 className="text-3xl font-bold">Vendors</h1>
        {hasPermission(['ADMIN', 'PROJECT_MANAGER']) && (
          <Button onClick={() => setIsAddDialogOpen(true)}>
            <Plus className="mr-2 h-4 w-4" />
            Add Vendor
          </Button>
        )}
      </div>
      <VendorsTable />
      <AddVendorDialog 
        open={isAddDialogOpen} 
        onOpenChange={setIsAddDialogOpen}
      />
    </div>
  )
}

