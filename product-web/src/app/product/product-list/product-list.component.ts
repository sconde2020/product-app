import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CardModule } from 'primeng/card';
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';
import { Router } from '@angular/router';
import { Product } from '../model/product.model';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { ProductService } from '../services/product.service';
import { ProductListResponse } from '../model/product-list-response';
import { MessageModule } from 'primeng/message';
import { ERROR_MESSAGES } from '../constants/error-message.constants';
import { TABLE_CONFIG } from '../constants/page.constants';

@Component({
  selector: 'app-product-list',
  imports: [CommonModule, CardModule, FormsModule, TableModule, ButtonModule, MessageModule],
  templateUrl: './product-list.component.html',
  styleUrl: './product-list.component.scss',
})
export class ProductListComponent {
  products!: Product[];
  totalRecords: number = TABLE_CONFIG.DEFAULTS.TOTAL_RECORDS;
  currentPage: number = TABLE_CONFIG.DEFAULTS.CURRENT_PAGE;
  pageSize: number = TABLE_CONFIG.DEFAULTS.PAGE_SIZE;
  sortField: string = TABLE_CONFIG.DEFAULTS.SORT_FIELD;
  sortOrder: string = TABLE_CONFIG.DEFAULTS.SORT_ORDER;

  apiErrorStatus: boolean = false;
  apiErrorMessage: string = '';

  constructor(
    private productService: ProductService,
    private router: Router,
    private route: ActivatedRoute,
  ) {}

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
    this.products = this.products.filter((p) => p.id !== productId);
    this.totalRecords = this.products.length;
  }

  lazyLoadProducts(event: any) {
    console.log('Lazy load event:', event);

    this.pageSize = event.rows;
    this.currentPage = Math.floor(event.first / event.rows);

    this.sortField = event.sortField || TABLE_CONFIG.DEFAULTS.SORT_FIELD;
    this.sortOrder =
      event.sortOrder === 1 ? TABLE_CONFIG.SORT_ORDER.ASC : TABLE_CONFIG.SORT_ORDER.DESC;

    this.loadProducts();
  }

  getRowStyleClass(rowIndex: number): string {
    return rowIndex % 2 === 0 ? 'even-row' : 'odd-row';
  }

  private loadProducts(): void {
    this.productService
      .getAllProducts(this.currentPage, this.pageSize, this.sortField, this.sortOrder)
      .subscribe({
        next: (response: ProductListResponse) => {
          this.products = response.content;
          this.totalRecords = response?.page?.totalElements || 10;
        },
        error: (error) => {
          this.apiErrorStatus = true;
          this.apiErrorMessage = error.message || ERROR_MESSAGES.FETCH_PRODUCTS;
        },
      });
  }
}
