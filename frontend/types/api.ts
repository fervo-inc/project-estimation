export type LaborCategory = {
  id: number;
  name: string;
  description: string;
  hourlyRate: number;
}

export type Material = {
  id: number;
  name: string;
  category: string;
  subCategory?: string;
  unitType: string;
  unitPrice: number;
  inStock: number;
  leadTimeDays: number;
  vendorId: number;
}

export type ProjectsSummary = {
  totalProjects: number,
  totalMaterialCost: number,
  totalLaborCost: number,
  averageCost: number
}

export type Project = {
  id: number;
  name: string;
  description: string;
  location: string;
  startDate: string;
  endDate: string;
  status: string;
  laborCost: number;
  materialCost: number;
  totalCost: number;
}

