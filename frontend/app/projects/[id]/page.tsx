'use client'

import { useEffect, useState } from 'react'
import { useParams } from 'next/navigation'
import { PieChart, Pie, ResponsiveContainer, Cell, Legend, Tooltip } from 'recharts'
import { CircleDollarSign, Users, Warehouse } from 'lucide-react'

import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'
import { fetchWithAuth } from '@/lib/api'
import { useToast } from '@/components/ui/use-toast'

interface ProjectEstimate {
  totalMaterialCost: number
  totalLaborCost: number
  totalCost: number
}

interface CostBreakdown {
  totalMaterialCost: number
  totalLaborCost: number
}

// Mock data for project ID 1
const mockProjectData = {
  estimate: {
    totalMaterialCost: 275000,
    totalLaborCost: 185000,
    totalCost: 460000
  },
  costBreakdown: {
    totalMaterialCost: 275000,
    totalLaborCost: 185000
  }
}

export default function ProjectDashboard() {
  const { id } = useParams()
  const { toast } = useToast()
  const [isLoading, setIsLoading] = useState(true)
  const [estimate, setEstimate] = useState<ProjectEstimate | null>(null)
  const [costBreakdown, setCostBreakdown] = useState<CostBreakdown | null>(null)

  useEffect(() => {
    const fetchData = async () => {
      try {
        // Use mock data for project ID 1, real API for others
        if (id === '1') {
          setEstimate(mockProjectData.estimate)
          setCostBreakdown(mockProjectData.costBreakdown)
          setIsLoading(false)
          return
        }

        const [estimateData, breakdownData] = await Promise.all([
          fetchWithAuth(`/projects/${id}/estimate`),
          fetchWithAuth(`/projects/${id}/cost-breakdown`)
        ])

        setEstimate(estimateData)
        setCostBreakdown(breakdownData)
      } catch (error) {
        toast({
          title: 'Error',
          description: 'Failed to fetch project data',
          variant: 'destructive'
        })
      } finally {
        setIsLoading(false)
      }
    }

    fetchData()
  }, [id, toast])

  if (isLoading) {
    return (
      <div className="flex items-center justify-center min-h-[400px]">
        <div className="text-lg">Loading project data...</div>
      </div>
    )
  }

  if (!estimate || !costBreakdown) {
    return (
      <div className="flex items-center justify-center min-h-[400px]">
        <div className="text-lg text-destructive">Failed to load project data</div>
      </div>
    )
  }

  const chartData = [
    { name: 'Material Costs', value: costBreakdown.totalMaterialCost },
    { name: 'Labor Costs', value: costBreakdown.totalLaborCost }
  ]

  const COLORS = ['#22c55e', '#3b82f6']

  return (
    <div className="flex flex-col gap-6">
      <div className="flex items-center justify-between">
        <h1 className="text-3xl font-bold">Project Dashboard</h1>
      </div>

      <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-3">
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Material Costs</CardTitle>
            <Warehouse className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">${estimate.totalMaterialCost.toLocaleString()}</div>
            <p className="text-xs text-muted-foreground">Total material expenses for this project</p>
          </CardContent>
        </Card>
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Labor Costs</CardTitle>
            <Users className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">${estimate.totalLaborCost.toLocaleString()}</div>
            <p className="text-xs text-muted-foreground">Total labor expenses for this project</p>
          </CardContent>
        </Card>
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Total Estimate</CardTitle>
            <CircleDollarSign className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">${estimate.totalCost.toLocaleString()}</div>
            <p className="text-xs text-muted-foreground">Combined total of material and labor costs</p>
          </CardContent>
        </Card>
      </div>

      <Card>
        <CardHeader>
          <CardTitle>Cost Distribution</CardTitle>
        </CardHeader>
        <CardContent>
          <div className="h-[300px]">
            <ResponsiveContainer width="100%" height="100%">
              <PieChart>
                <Pie
                  data={chartData}
                  cx="50%"
                  cy="50%"
                  labelLine={false}
                  label={({ name, value }) => `${name}: $${value.toLocaleString()}`}
                  outerRadius={100}
                  fill="#8884d8"
                  dataKey="value"
                >
                  {chartData.map((entry, index) => (
                    <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                  ))}
                </Pie>
                <Tooltip formatter={(value: number) => `$${value.toLocaleString()}`} />
                <Legend />
              </PieChart>
            </ResponsiveContainer>
          </div>
        </CardContent>
      </Card>
    </div>
  )
}
