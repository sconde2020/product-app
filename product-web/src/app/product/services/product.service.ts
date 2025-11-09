import { Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';
import { Product } from '../model/product.model';
import { ApiService } from '../../core/services/api.service';
import { ProductListResponse } from '../model/product-list-response';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
 
  private readonly endpoint = 'products';

  constructor(private apiService: ApiService) { }

  getProductById(id: number): Observable<Product> {
    return this.apiService.getById<Product>(this.endpoint, id);
  }

  getAllProducts(): Observable<Product[]> {
    return this.apiService.getAll<ProductListResponse>(this.endpoint)
      .pipe(map(response => response.content));
  }

  createProduct(product: Product): Observable<Product> {
    return this.apiService.create<Product>(this.endpoint, product);
  }

  updateProduct(updatedProduct: Product): Observable<Product> {
    return this.apiService.update<Product>(this.endpoint, updatedProduct.id, updatedProduct);
  }

  deleteProduct(id: number): Observable<void> {
    return this.apiService.delete(this.endpoint, id);
  }
  
}
