import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { CardModule } from 'primeng/card';
import { MessageModule } from 'primeng/message';
import { Product } from '../model/product.model';
import { ActivatedRoute, Router } from '@angular/router';
import { ProductService } from '../services/product.service';
import { ButtonModule } from 'primeng/button';
import { ERROR_MESSAGES } from '../constants/error-message.constants';

@Component({
  selector: 'app-product-detail',
  imports: [
    CommonModule,
    CardModule,
    ButtonModule,
    MessageModule
  ],
  templateUrl: './product-detail.component.html',
  styleUrl: './product-detail.component.scss'
})
export class ProductDetailComponent implements OnInit {
  product!: Product;
  apiErrorStatus: boolean = false;
  apiErrorMessage: string = '';

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private productService: ProductService
  ) {}

  ngOnInit(): void {
    const productId = this.route.snapshot.paramMap.get('id');
    if (productId) {
      this.productService.getProductById(+productId).subscribe({
        next: (data: Product) => {
          this.product = data;
        },
        error: (error) => {
          this.apiErrorStatus = true;
          this.apiErrorMessage = error.message || ERROR_MESSAGES.FETCH_PRODUCT_DETAILS;
        }
      });
    }
  }

  onBackToList(): void {
    this.router.navigate(['/products']);
  }
}
