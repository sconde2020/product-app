import { Injectable } from '@angular/core';
import { Observable, of } from 'rxjs';
import { Product } from '../model/product.model';
import { MOCK_PRODUCTS } from './product-mock.utils';

@Injectable({
  providedIn: 'root'
})
export class ProductService {


  private products: Product[] = MOCK_PRODUCTS;
  
  constructor() { }

  getProductById(id: number) : Observable<Product | null> {
    const product = this.products.find(p => p.id === id) || null;
    return of(product);
  }

  getAllProducts(): Observable<Product[]> {
    return of(this.products);
  }

  createProduct(product: Product): Observable<Product> {
    this.products.push(product);
    return of(product);
  }

  updateProduct(updatedProduct: Product): Observable<Product | null> {
    const index = this.products.findIndex(p => p.id === updatedProduct.id);
    if (index !== -1) {
      this.products[index] = updatedProduct;
      return of(updatedProduct);
    }
    return of(null);
  }

  deleteProduct(id: number): Observable<boolean> {
    const index = this.products.findIndex(p => p.id === id);
    if (index !== -1) {
      this.products.splice(index, 1);
      return of(true);
    }
    return of(false);
  }
  
}
