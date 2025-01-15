export type LaborCategory = {
  id: number
  name: string
  description: string
  hourlyRate: number
}

export type Material = {
  id: number
  name: string
  category: string
  subCategory?: string
  unitType: string
  unitPrice: number
  inStock: number
  leadTimeDays: number
  vendorId: number
}

export type ProjectsSummary = {
  totalProjects: number
  totalMaterialCost: number
  totalLaborCost: number
  averageCost: number
}

export type Project = {
  id: number
  name: string
  description: string
  location: string
  startDate: string
  endDate: string
  status: string
}

export type PagedResponse<T> = {
  content: T[]
  pageable: {
    pageNumber: number
    pageSize: number
    sort: {
      empty: boolean
      sorted: boolean
      unsorted: boolean
    }
    offset: number
    paged: boolean
    unpaged: boolean
  }
  last: boolean
  totalElements: number
  totalPages: number
  size: number
  number: number
  sort: {
    empty: boolean
    sorted: boolean
    unsorted: boolean
  }
  first: boolean
  numberOfElements: number
  empty: boolean
}
