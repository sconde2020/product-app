import { Injectable } from '@angular/core';
import { Product } from '../model/product.model';
import { ApiService } from '../../core/api.service';
import { ProductListResponse } from '../model/product-list-response';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { ERROR_MESSAGES } from '../constants/error-message.constants';
import { PageParams } from '../model/page-params';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
 
  private readonly endpoint = 'products';

  constructor(private apiService: ApiService) { }

  private handleError(err: any): Observable<never> {
    const message = err?.error?.message || err?.message || ERROR_MESSAGES.UNKNOWN_ERROR;
    return throwError(() => new Error(message));
  }

  getProductById(id: number): Observable<Product> {
    return this.apiService.getById<Product>(this.endpoint, id)
      .pipe(catchError(err => this.handleError(err)));
  }

  getAllProducts(
    currentPage: number, 
    pageSize: number, 
    sortField: string, 
    sortOrder: string
  ): Observable<ProductListResponse> 
  {
    const params: PageParams = {
      page: currentPage,
      size: pageSize,
      sortBy: sortField,
      direction: sortOrder
    };

    return this.apiService.getAll<ProductListResponse>(this.endpoint, params)
      .pipe(catchError(err => this.handleError(err)));
  }

  createProduct(product: Product): Observable<Product> {
    return this.apiService.create<Product>(this.endpoint, product)
      .pipe(catchError(err => this.handleError(err)));
  }

  updateProduct(updatedProduct: Product): Observable<Product> {
    return this.apiService.update<Product>(this.endpoint, updatedProduct.id, updatedProduct)
      .pipe(catchError(err => this.handleError(err)));
  }

  deleteProduct(id: number): Observable<void> {
    return this.apiService.delete(this.endpoint, id)
      .pipe(catchError(err => this.handleError(err)));
  }
  
}
