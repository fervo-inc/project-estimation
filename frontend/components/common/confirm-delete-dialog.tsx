'use client'

import { useCallback } from 'react'

import {
  AlertDialog,
  AlertDialogAction,
  AlertDialogCancel,
  AlertDialogContent,
  AlertDialogDescription,
  AlertDialogFooter,
  AlertDialogHeader,
  AlertDialogTitle
} from '@/components/ui/alert-dialog'
import { deleteResource } from '@/lib/api'
import { useToast } from '@/hooks/use-toast'

type ConfirmDeleteDialogProps = {
  id: number | undefined
  deleteEndpointUrl: string
  resourceName: string
  message: string
  onOpenChange: (open: boolean) => void
  onSuccess?: (idDeleted: number) => void
}

export function ConfirmDeleteDialog({
  id,
  deleteEndpointUrl,
  resourceName,
  message,
  onOpenChange,
  onSuccess
}: ConfirmDeleteDialogProps) {
  const { toast } = useToast()

  const handleDelete = useCallback(
    (id: number | undefined) => async () => {
      if (id === undefined) return
      try {
        await deleteResource(`${deleteEndpointUrl}/${id}`)
        onSuccess?.(id)
        toast({
          title: 'Success',
          description: `${resourceName} deleted successfully`
        })
      } catch (error) {
        toast({
          title: 'Error',
          description: `Failed to delete ${resourceName}`,
          variant: 'destructive'
        })
      }
    },
    [toast]
  )

  return (
    <AlertDialog open={id !== undefined} onOpenChange={onOpenChange}>
      <AlertDialogContent>
        <AlertDialogHeader>
          <AlertDialogTitle>Are you sure?</AlertDialogTitle>
          <AlertDialogDescription>{message}</AlertDialogDescription>
        </AlertDialogHeader>
        <AlertDialogFooter>
          <AlertDialogCancel>Cancel</AlertDialogCancel>
          <AlertDialogAction
            onClick={handleDelete(id)}
            className="bg-destructive text-destructive-foreground hover:bg-destructive/90"
          >
            Delete
          </AlertDialogAction>
        </AlertDialogFooter>
      </AlertDialogContent>
    </AlertDialog>
  )
}
