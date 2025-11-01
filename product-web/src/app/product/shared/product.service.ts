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
  
}
