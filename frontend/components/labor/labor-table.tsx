'use client'

import { useToast } from '@/hooks/use-toast'
import {
  ColumnDef,
  ColumnFiltersState,
  flexRender,
  getCoreRowModel,
  getFilteredRowModel,
  getPaginationRowModel,
  getSortedRowModel,
  PaginationState,
  SortingState,
  useReactTable,
  VisibilityState
} from '@tanstack/react-table'
import { Edit2, MoreHorizontal, Trash2 } from 'lucide-react'
import { useCallback, useEffect, useMemo, useState } from 'react'

import { Button } from '@/components/ui/button'
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuSeparator,
  DropdownMenuTrigger
} from '@/components/ui/dropdown-menu'
import { Input } from '@/components/ui/input'
import { Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from '@/components/ui/table'
import { EmptyPagedResponse } from '@/constants/api'
import { useAuth } from '@/contexts/auth-context'
import { fetchWithAuth } from '@/lib/api'
import { LaborCategory, PagedResponse } from '@/types/api'
import { ConfirmDeleteDialog } from '../common/confirm-delete-dialog'

export function LaborTable() {
  const [data, setData] = useState<PagedResponse<LaborCategory>>(EmptyPagedResponse)
  const [loading, setLoading] = useState(true)
  const [sorting, setSorting] = useState<SortingState>([])
  const [columnFilters, setColumnFilters] = useState<ColumnFiltersState>([])
  const [columnVisibility, setColumnVisibility] = useState<VisibilityState>({})
  const [rowSelection, setRowSelection] = useState({})
  const [editing, setEditing] = useState<LaborCategory | null>(null)
  const [deleteModel, setDeleteModel] = useState<LaborCategory | null>(null)
  const [pagination, setPagination] = useState<PaginationState>({
    pageSize: 10,
    pageIndex: 0
  })
  const { toast } = useToast()
  const { hasPermission } = useAuth()

  const handleDelete = useCallback(
    async (id: number) => {
      setData({
        ...data,
        content: data.content.filter((material) => material.id !== id)
      })
      setDeleteModel(null)
    },
    [data]
  )

  const fetchLaborCategories = useCallback(
    async (pageIndex: number, pageSize: number, order: 'asc' | 'desc' = 'asc') => {
      try {
        const response = await fetchWithAuth<PagedResponse<LaborCategory>>(
          `/labor-categories?page=${pageIndex}&size=${pageSize}&order=${order}`
        )
        setData(response)
      } catch (error) {
        toast({
          title: 'Error',
          description: 'Failed to fetch labor categories',
          variant: 'destructive'
        })
      } finally {
        setLoading(false)
      }
    },
    []
  )

  useEffect(() => {
    fetchLaborCategories(pagination.pageIndex, pagination.pageSize)
  }, [pagination.pageIndex, pagination.pageSize])

  const columns: ColumnDef<LaborCategory>[] = useMemo(
    () => [
      {
        accessorKey: 'name',
        header: 'Category Name',
        cell: ({ row }) => <div>{row.getValue('name')}</div>
      },
      {
        accessorKey: 'description',
        header: 'Description',
        cell: ({ row }) => <div>{row.getValue('description')}</div>
      },
      {
        accessorKey: 'hourlyRate',
        header: 'Hourly Rate',
        cell: ({ row }) => <div className="font-medium">${Number(row.getValue('hourlyRate')).toFixed(2)}</div>
      },
      {
        id: 'actions',
        cell: ({ row }) => {
          const labor = row.original

          return (
            hasPermission(['ADMIN', 'PROJECT_MANAGER']) && (
              <DropdownMenu>
                <DropdownMenuTrigger asChild>
                  <Button variant="ghost" className="h-8 w-8 p-0">
                    <MoreHorizontal className="h-4 w-4" />
                  </Button>
                </DropdownMenuTrigger>
                <DropdownMenuContent align="end">
                  <DropdownMenuItem
                    onClick={() => {
                      setEditing(labor)
                    }}
                  >
                    <Edit2 className="mr-2 h-4 w-4" />
                    Edit
                  </DropdownMenuItem>
                  <DropdownMenuSeparator />
                  <DropdownMenuItem
                    className="text-destructive"
                    onClick={() => {
                      setDeleteModel(labor)
                    }}
                  >
                    <Trash2 className="mr-2 h-4 w-4" />
                    Delete
                  </DropdownMenuItem>
                </DropdownMenuContent>
              </DropdownMenu>
            )
          )
        }
      }
    ],
    []
  )

  const table = useReactTable({
    manualPagination: true,
    data: data.content,
    columns,
    onSortingChange: setSorting,
    onColumnFiltersChange: setColumnFilters,
    getCoreRowModel: getCoreRowModel(),
    getPaginationRowModel: getPaginationRowModel(),
    getSortedRowModel: getSortedRowModel(),
    getFilteredRowModel: getFilteredRowModel(),
    onColumnVisibilityChange: setColumnVisibility,
    onRowSelectionChange: setRowSelection,
    onPaginationChange: setPagination,
    state: {
      sorting,
      columnFilters,
      columnVisibility,
      rowSelection
    }
  })

  if (loading) {
    return <div>Loading...</div>
  }

  return (
    <div className="space-y-4 min-w-[800px] w-full max-w-[1400px]">
      <div className="flex items-center gap-2">
        <Input
          placeholder="Filter labor categories..."
          value={(table.getColumn('name')?.getFilterValue() as string) ?? ''}
          onChange={(event) => table.getColumn('name')?.setFilterValue(event.target.value)}
          className="max-w-sm"
        />
      </div>
      <div className="rounded-md border">
        <Table>
          <TableHeader>
            {table.getHeaderGroups().map((headerGroup) => (
              <TableRow key={headerGroup.id}>
                {headerGroup.headers.map((header) => {
                  return (
                    <TableHead key={header.id}>
                      {header.isPlaceholder ? null : flexRender(header.column.columnDef.header, header.getContext())}
                    </TableHead>
                  )
                })}
              </TableRow>
            ))}
          </TableHeader>
          <TableBody>
            {table.getRowModel().rows?.length ? (
              table.getRowModel().rows.map((row) => (
                <TableRow key={row.id} data-state={row.getIsSelected() && 'selected'}>
                  {row.getVisibleCells().map((cell) => (
                    <TableCell key={cell.id}>{flexRender(cell.column.columnDef.cell, cell.getContext())}</TableCell>
                  ))}
                </TableRow>
              ))
            ) : (
              <TableRow>
                <TableCell colSpan={columns.length} className="h-24 text-center">
                  No labor categories found.
                </TableCell>
              </TableRow>
            )}
          </TableBody>
        </Table>
      </div>
      <div className="flex items-center justify-end space-x-2">
        <div className="flex-1 text-sm text-muted-foreground">
          {table.getFilteredSelectedRowModel().rows.length} of {table.getFilteredRowModel().rows.length} row(s)
          selected.
        </div>
        <div className="space-x-2">
          <Button
            variant="outline"
            size="sm"
            onClick={() => table.previousPage()}
            disabled={!table.getCanPreviousPage()}
          >
            Previous
          </Button>
          <Button variant="outline" size="sm" onClick={() => table.nextPage()} disabled={!table.getCanNextPage()}>
            Next
          </Button>
        </div>
      </div>

      <ConfirmDeleteDialog
        id={deleteModel?.id}
        deleteEndpointUrl={'/labor-categories'}
        resourceName="Labor"
        message={`Are you sure you want to remove "${deleteModel?.name}"?`}
        onOpenChange={() => setDeleteModel(null)}
        onSuccess={handleDelete}
      />
    </div>
  )
}
