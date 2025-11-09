import { Component, OnInit } from '@angular/core';
import { ProductService } from '../services/product.service';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { CardModule } from 'primeng/card';
import { ActivatedRoute, Router } from '@angular/router';
import { Product } from '../model/product.model';
import { ButtonModule } from 'primeng/button';
import { MessageModule } from "primeng/message";
import { ERROR_MESSAGES } from '../constants/error-message.constants';

@Component({
  selector: 'app-product-new',
  imports: [
    CommonModule,
    CardModule,
    ButtonModule,
    ReactiveFormsModule,
    MessageModule
],
  templateUrl: './product-new.component.html',
  styleUrls: ['./product-new.component.scss']
})
export class ProductNewComponent implements OnInit {  
  productId!: number | null;
  productToUpdate: Product | null = null;
  productForm!: FormGroup;
  apiErrorStatus: boolean = false;
  apiErrorMessage: string = '';

  constructor(
    private router: Router,
    private route: ActivatedRoute,
    private formBuilder: FormBuilder,
    private productService: ProductService
  ) { }

  
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
        }
      });
    } else {
      this.initializeForm();
    }    
  }

  initializeForm(): void {
    this.productForm = this.formBuilder.group({
      id: [this.productId],
      category: ['', Validators.required],
      name: ['', Validators.required],
      description: [''],
      price: [0, Validators.required],
      quantity: [0]
    });
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
        }
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
        }
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
