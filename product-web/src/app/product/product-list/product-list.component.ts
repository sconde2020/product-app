import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CardModule } from 'primeng/card';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { Router } from '@angular/router';
import { Product } from '../model/product.model';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { ProductService } from '../services/product.service';
 

@Component({
  selector: 'app-product-list',
  imports: [
    CommonModule,
    CardModule,
    FormsModule,
    TableModule,
    ButtonModule,
  ],
  templateUrl: './product-list.component.html',
  styleUrl: './product-list.component.scss'
})
export class ProductListComponent implements OnInit {
  totalRecords: number = 0;
  first: number = 0;
  rows: number = 10;
  products!: Product[];

  constructor(
    private productService: ProductService,
    private router: Router,
    private route: ActivatedRoute,
  ) {}


  ngOnInit() {
    this.productService.getAllProducts().subscribe((data: Product[]) => {
      this.products = data;
      this.totalRecords = data.length;
    });
  }

  viewDetails(productId: number): void {
    this.router.navigate(['details', productId], { relativeTo: this.route });
  }

  createNewProduct() {
    this.router.navigate(['new'], { relativeTo: this.route });
  }

  updateProduct(productId: number) {
    this.router.navigate(['update', productId], { relativeTo: this.route });
  }

  deleteProduct(productId: number): void {
    this.products = this.products.filter(p => p.id !== productId);
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