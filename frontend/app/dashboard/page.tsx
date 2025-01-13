"use client"

import { useEffect, useState } from "react"
import { Bar, BarChart, ResponsiveContainer, XAxis, YAxis } from "recharts"
import { Building2, CircleDollarSign, TrendingUp, Users } from 'lucide-react'

import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { ProjectsSummary } from "@/types/api"

const mockSummary: ProjectsSummary = {
  totalProjects: 12,
  totalMaterialCost: 150000,
  totalLaborCost: 85000,
  averageCost: 19583.33
}

const mockChartData = [
  {
    name: "Jan",
    total: 2500,
  },
  {
    name: "Feb",
    total: 3500,
  },
  {
    name: "Mar",
    total: 4500,
  },
  {
    name: "Apr",
    total: 2780,
  },
  {
    name: "May",
    total: 1890,
  },
  {
    name: "Jun",
    total: 2390,
  },
]

export default function DashboardPage() {
  const [summary, setSummary] = useState<ProjectsSummary>(mockSummary)

  return (
    <div className="flex flex-col gap-4 min-w-[800px] w-full max-w-[1400px]">
      <h1 className="text-3xl font-bold">Dashboard</h1>
      <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-4">
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Total Projects</CardTitle>
            <Building2 className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">{summary.totalProjects}</div>
            <p className="text-xs text-muted-foreground">Active projects</p>
          </CardContent>
        </Card>
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Material Costs</CardTitle>
            <CircleDollarSign className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">
              ${summary.totalMaterialCost.toLocaleString()}
            </div>
            <p className="text-xs text-muted-foreground">Total material expenses</p>
          </CardContent>
        </Card>
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Labor Costs</CardTitle>
            <Users className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">
              ${summary.totalLaborCost.toLocaleString()}
            </div>
            <p className="text-xs text-muted-foreground">Total labor expenses</p>
          </CardContent>
        </Card>
        <Card>
          <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
            <CardTitle className="text-sm font-medium">Average Cost</CardTitle>
            <TrendingUp className="h-4 w-4 text-muted-foreground" />
          </CardHeader>
          <CardContent>
            <div className="text-2xl font-bold">
              ${summary.averageCost.toLocaleString()}
            </div>
            <p className="text-xs text-muted-foreground">Per project average</p>
          </CardContent>
        </Card>
      </div>
      <Card className="col-span-4">
        <CardHeader>
          <CardTitle>Cost Overview</CardTitle>
          <CardDescription>
            Monthly project costs for the current year
          </CardDescription>
        </CardHeader>
        <CardContent className="pl-2">
          <ResponsiveContainer width="100%" height={350}>
            <BarChart data={mockChartData}>
              <XAxis
                dataKey="name"
                stroke="#888888"
                fontSize={12}
                tickLine={false}
                axisLine={false}
              />
              <YAxis
                stroke="#888888"
                fontSize={12}
                tickLine={false}
                axisLine={false}
                tickFormatter={(value) => `$${value}`}
              />
              <Bar dataKey="total" fill="currentColor" radius={[4, 4, 0, 0]} className="fill-primary" />
            </BarChart>
          </ResponsiveContainer>
        </CardContent>
      </Card>
    </div>
  )
}

