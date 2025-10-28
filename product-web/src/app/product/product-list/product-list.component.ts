import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { Router } from '@angular/router';
import { Product } from '../model/product.model';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
 

@Component({
  selector: 'app-product-list',
  imports: [
    CommonModule,
    FormsModule,
    TableModule,
    ButtonModule,
  ],
  templateUrl: './product-list.component.html',
  styleUrl: './product-list.component.scss'
})
export class ProductListComponent {
  totalRecords: number = 0;
  first: number = 0;
  rows: number = 10;

  products: Product[] = [
    { id: 1, name: 'Laptop Pro', price: 1499.99, quantity: 5 },
    { id: 2, name: 'Wireless Mouse', price: 29.99, quantity: 150 },
    { id: 3, name: 'Mechanical Keyboard', price: 99.99 }, // quantity omitted
    { id: 4, name: 'USB-C Hub', price: 49.99, quantity: 25 },
    { id: 5, name: 'Laptop Pro', price: 1499.99, quantity: 5 },
    { id: 6, name: 'Wireless Mouse', price: 29.99, quantity: 150 },
    { id: 7, name: 'Mechanical Keyboard', price: 99.99 }, // quantity omitted
    { id: 8, name: 'USB-C Hub', price: 49.99, quantity: 25 },
    { id: 9, name: 'Laptop Pro', price: 1499.99, quantity: 5 },
    { id: 10, name: 'Wireless Mouse', price: 29.99, quantity: 150 },
    { id: 11, name: 'Mechanical Keyboard', price: 99.99 }, // quantity omitted
    { id: 12, name: 'USB-C Hub', price: 49.99, quantity: 25 },
    { id: 13, name: 'Laptop Pro', price: 1499.99, quantity: 5 },
    { id: 14, name: 'Wireless Mouse', price: 29.99, quantity: 150 },
    { id: 15, name: 'Mechanical Keyboard', price: 99.99 }, // quantity omitted
    { id: 16, name: 'USB-C Hub', price: 49.99, quantity: 25 },
    { id: 17, name: 'Laptop Pro', price: 1499.99, quantity: 5 },
    { id: 18, name: 'Wireless Mouse', price: 29.99, quantity: 150 },
    { id: 19, name: 'Mechanical Keyboard', price: 99.99 }, // quantity omitted
    { id: 20, name: 'USB-C Hub', price: 49.99, quantity: 25 },
    { id: 21, name: 'Laptop Pro', price: 1499.99, quantity: 5 },
    { id: 22, name: 'Wireless Mouse', price: 29.99, quantity: 150 },
    { id: 23, name: 'Mechanical Keyboard', price: 99.99 }, // quantity omitted
    { id: 24, name: 'USB-C Hub', price: 49.99, quantity: 25 },
  ];

  constructor(
    private router: Router,
    private route: ActivatedRoute
  ) {}


  createNewProduct() {
    this.router.navigate(['new'], { relativeTo: this.route });
 }

  viewDetails(id: number): void {
    this.router.navigate(['details', id], { relativeTo: this.route });
  }

  deleteProduct(product: Product): void {
    this.products = this.products.filter(p => p.id !== product.id);
    this.totalRecords = this.products.length;
  }
  
  onPageChange(event: any) {
    this.first = event.first;
    this.rows = event.rows;
  }

  onRowsChange(rows: number) {
    this.rows = rows;
    this.first = 0; 
  }

  getLastPageEntry(): number {
    const lastEntry = this.first + this.rows;
    return lastEntry > this.totalRecords ? this.totalRecords : lastEntry;
  }

  getRowStyleClass(rowIndex: number): string {
    return rowIndex % 2 === 0 ? 'even-row' : 'odd-row';
 }

}