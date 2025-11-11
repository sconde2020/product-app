import { Component, OnInit } from '@angular/core';
import { ProductService } from '../services/product.service';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { CardModule } from 'primeng/card';
import { ActivatedRoute, Router } from '@angular/router';
import { Product } from '../model/product.model';
import { ButtonModule } from 'primeng/button';
import { MessageModule } from 'primeng/message';
import { ERROR_MESSAGES } from '../constants/error-message.constants';
import { LimitTextDirective } from '../directives/limit-text.directive';
import {
  FORM_ERROR_MESSAGES,
  FORM_LABELS,
  FORM_PLACEHOLDERS,
} from '../constants/product-form.constants';

@Component({
  selector: 'app-product-new',
  imports: [
    CommonModule,
    CardModule,
    ButtonModule,
    ReactiveFormsModule,
    MessageModule,
    LimitTextDirective,
  ],
  templateUrl: './product-new.component.html',
  styleUrls: ['./product-new.component.scss'],
})
export class ProductNewComponent implements OnInit {
  productId!: number | null;
  productToUpdate: Product | null = null;
  productForm!: FormGroup;

  errors = FORM_ERROR_MESSAGES;
  placeholders = FORM_PLACEHOLDERS;
  labels = FORM_LABELS;

  apiErrorStatus = false;
  apiErrorMessage = '';

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private formBuilder: FormBuilder,
    private productService: ProductService,
  ) {}

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');
    this.productId = idParam ? +idParam : null;

    if (this.productId !== null) {
      this.productService.getProductById(this.productId).subscribe({
        next: (product: Product) => {
          this.productToUpdate = product;
          this.initializeForm();
          this.productForm.patchValue(this.productToUpdate);
        },
        error: (error) => {
          this.apiErrorStatus = true;
          this.apiErrorMessage = error.message || ERROR_MESSAGES.FETCH_PRODUCT_DETAILS;
        },
      });
    } else {
      this.initializeForm();
    }
  }

  initializeForm(): void {
    this.productForm = this.formBuilder.group({
      id: [this.productId],
      category: ['', [Validators.required]],
      name: ['', [Validators.required]],
      description: ['', [Validators.required]],
      price: [0.1, [Validators.required, Validators.min(0.01)]],
      quantity: [1, [Validators.required, Validators.min(1)]],
    });
  }

  invalidateField(fieldName: string): boolean {
    const field = this.productForm.get(fieldName);
    return !!(field && field.invalid && (field.dirty || field.touched));
  }

  onSubmit(): void {
    if (this.productId !== null) {
      this.updateProduct();
    } else {
      this.createProduct();
    }
  }

  createProduct(): void {
    if (this.productForm.valid) {
      this.productService.createProduct(this.productForm.value).subscribe({
        next: () => {
          this.backToList();
        },
        error: (error) => {
          this.apiErrorStatus = true;
          this.apiErrorMessage = error.message || ERROR_MESSAGES.CREATE_PRODUCT;
        },
      });
    }
  }

  updateProduct(): void {
    if (this.productForm.valid) {
      this.productService.updateProduct(this.productForm.value).subscribe({
        next: () => {
          this.backToList();
        },
        error: (error) => {
          this.apiErrorStatus = true;
          this.apiErrorMessage = error.message || ERROR_MESSAGES.UPDATE_PRODUCT;
        },
      });
    }
  }

  onCancel(): void {
    this.backToList();
  }

  backToList(): void {
    this.router.navigate(['/products']);
  }
}
