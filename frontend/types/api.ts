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

