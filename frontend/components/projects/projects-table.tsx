'use client'

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
import { Edit2, FileText, MoreHorizontal, Trash2 } from 'lucide-react'
import Link from 'next/link'
import { useCallback, useEffect, useMemo, useState } from 'react'

import { ConfirmDeleteDialog } from '@/components/common/confirm-delete-dialog'
import { Badge } from '@/components/ui/badge'
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
import { useToast } from '@/hooks/use-toast'
import { fetchWithAuth } from '@/lib/api'
import { PagedResponse, Project } from '@/types/api'
import { AddProjectDialog } from './add-project-dialog'
import { EditProjectDialog } from './edit-project-dialog'

export function ProjectsTable() {
  const [data, setData] = useState<PagedResponse<Project>>(EmptyPagedResponse)
  const [loading, setLoading] = useState(false)
  const [sorting, setSorting] = useState<SortingState>([])
  const [columnFilters, setColumnFilters] = useState<ColumnFiltersState>([])
  const [columnVisibility, setColumnVisibility] = useState<VisibilityState>({})
  const [rowSelection, setRowSelection] = useState({})
  const [isAddDialogOpen, setIsAddDialogOpen] = useState(false) // Added state for add dialog
  const [editingProject, setEditingProject] = useState<Project | null>(null)
  const [deleteModel, setDeleteModel] = useState<Project | null>(null)
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
        content: data.content.filter((project) => project.id !== id)
      })
      setDeleteModel(null)
    },
    [data]
  )

  const fetchProjects = useCallback(async (pageIndex: number, pageSize: number, order: 'asc' | 'desc' = 'asc') => {
    setLoading(true)
    try {
      const response = await fetchWithAuth<PagedResponse<Project>>(
        `/projects?page=${pageIndex}&size=${pageSize}&order=${order}`
      )
      setData(response)
    } catch (error) {
      console.log('Failed to fetch projects:', error)
      toast({
        title: 'Error',
        description: 'Failed to fetch projects: ' + (error instanceof Error ? error.message : 'Unknown error'),
        variant: 'destructive'
      })
    } finally {
      setLoading(false)
    }
  }, [])

  useEffect(() => {
    fetchProjects(pagination.pageIndex, pagination.pageSize)
  }, [pagination.pageIndex, pagination.pageSize])

  const columns: ColumnDef<Project>[] = useMemo(
    () => [
      {
        accessorKey: 'name',
        header: 'Project Name',
        cell: ({ row }) => <div>{row.getValue('name')}</div>
      },
      {
        accessorKey: 'location',
        header: 'Location',
        cell: ({ row }) => <div>{row.getValue('location')}</div>
      },
      {
        accessorKey: 'startDate',
        header: 'Start Date',
        cell: ({ row }) => <div>{new Date(row.getValue('startDate')).toLocaleDateString()}</div>
      },
      {
        accessorKey: 'endDate',
        header: 'End Date',
        cell: ({ row }) => <div>{new Date(row.getValue('endDate')).toLocaleDateString()}</div>
      },
      {
        accessorKey: 'status',
        header: 'Status',
        cell: ({ row }) => {
          const status = row.getValue('status') as string
          return (
            <Badge variant={status === 'COMPLETED' ? 'default' : status === 'IN_PROGRESS' ? 'secondary' : 'outline'}>
              {status.replace('_', ' ')}
            </Badge>
          )
        }
      },
      {
        id: 'view',
        cell: ({ row }) => {
          const project = row.original
          return (
            <Button variant="ghost" size="sm" asChild className="flex items-center gap-2">
              <Link href={`/projects/${project.id}`}>
                <FileText className="h-4 w-4" />
                Project Dashboard
              </Link>
            </Button>
          )
        }
      },
      {
        id: 'actions',
        cell: ({ row }) => {
          const project = row.original

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
                      setEditingProject(project)
                    }}
                  >
                    <Edit2 className="mr-2 h-4 w-4" />
                    Edit
                  </DropdownMenuItem>
                  <DropdownMenuSeparator />
                  <DropdownMenuItem
                    className="text-destructive"
                    onClick={() => {
                      setDeleteModel(project)
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
    [hasPermission]
  )

  const table = useReactTable({
    manualPagination: true,
    data: data.content,
    columns,
    rowCount: data.totalElements,
    pageCount: data.totalPages,
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
      rowSelection,
      pagination
    }
  })

  if (loading) {
    return <div>Loading...</div>
  }

  return (
    <div className="space-y-4 min-w-[800px] w-full max-w-[1400px]">
      {' '}
      {/* Updated filter container */}
      <div className="flex items-center justify-between">
        <Input
          placeholder="Filter projects..."
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
                  No projects found.
                </TableCell>
              </TableRow>
            )}
          </TableBody>
        </Table>
      </div>
      <div className="flex items-center justify-end space-x-2">
        <div className="text-muted-foreground flex-1 text-sm">
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
      <EditProjectDialog
        onOpenChange={(isOpen) => {
          if (!isOpen) setEditingProject(null)
        }}
        project={editingProject}
        onSuccess={() => {
          // TODO: Implement refresh logic when API is connected
          setEditingProject(null)
        }}
      />
      <AddProjectDialog
        onOpenChange={(isOpen) => {
          if (!isOpen) setEditingProject(null)
        }}
        open={isAddDialogOpen}
        onSuccess={() => {
          // TODO: Implement refresh logic when API is connected
          setIsAddDialogOpen(false)
        }}
      />
      <ConfirmDeleteDialog
        id={deleteModel?.id}
        deleteEndpointUrl={'/projects'}
        resourceName="Project"
        message={`Are you sure you want to remove "${deleteModel?.name}"?`}
        onOpenChange={() => setDeleteModel(null)}
        onSuccess={handleDelete}
      />
    </div>
  )
}
