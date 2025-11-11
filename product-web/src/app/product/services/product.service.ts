import { Injectable } from '@angular/core';
import { Product } from '../model/product.model';
import { ApiService } from '../../core/api.service';
import { ProductListResponse } from '../model/product-list-response';
import { Observable } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { PageParams } from '../model/page-params';
import { handleApiError } from '../../core/error.utils';

@Injectable({
  providedIn: 'root',
})
export class ProductService {
  private readonly endpoint = 'products';

  constructor(private apiService: ApiService) {}

  getProductById(id: number): Observable<Product> {
    return this.apiService
      .getById<Product>(this.endpoint, id)
      .pipe(catchError((err) => handleApiError(err)));
  }

  getAllProducts(
    currentPage: number,
    pageSize: number,
    sortField: string,
    sortOrder: string,
  ): Observable<ProductListResponse> {
    const params: PageParams = {
      page: currentPage,
      size: pageSize,
      sortBy: sortField,
      direction: sortOrder,
    };

    return this.apiService
      .getAll<ProductListResponse>(this.endpoint, params)
      .pipe(catchError((err) => handleApiError(err)));
  }

  createProduct(product: Product): Observable<Product> {
    return this.apiService
      .create<Product>(this.endpoint, product)
      .pipe(catchError((err) => handleApiError(err)));
  }

  updateProduct(updatedProduct: Product): Observable<Product> {
    return this.apiService
      .update<Product>(this.endpoint, updatedProduct.id, updatedProduct)
      .pipe(catchError((err) => handleApiError(err)));
  }

  deleteProduct(id: number): Observable<void> {
    return this.apiService.delete(this.endpoint, id).pipe(catchError((err) => handleApiError(err)));
  }
}
