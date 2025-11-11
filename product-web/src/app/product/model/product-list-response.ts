export interface Product {
  id: number;
  name: string;
  price: number;
  category: string;
  description: string;
  quantity: number;
}

export interface PageInfo {
  size: number;
  number: number;
  totalElements: number;
  totalPages: number;
}

export interface ProductListResponse {
  content: Product[];
  page: PageInfo;
}
