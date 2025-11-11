import { TestBed, ComponentFixture } from '@angular/core/testing';
import { ProductNewComponent } from './product-new.component';
import { ProductService } from '../services/product.service';
import { FormBuilder } from '@angular/forms';
import { Router, ActivatedRoute } from '@angular/router';
import { Product } from '../model/product.model';
import { of } from 'rxjs';

describe('ProductNewComponent', () => {
  let component: ProductNewComponent;
  let fixture: ComponentFixture<ProductNewComponent>;
  let productService: jasmine.SpyObj<ProductService>;
  let router: jasmine.SpyObj<Router>;
  let activatedRoute: Partial<ActivatedRoute>;
  let formBuilder: FormBuilder;

  const mockProduct: Product = {
    id: 1,
    category: 'Cat 1',
    name: 'Product 1',
    description: 'Desc 1',
    price: 100,
    quantity: 5,
  };

  beforeEach(async () => {
    productService = jasmine.createSpyObj('ProductService', [
      'getProductById',
      'createProduct',
      'updateProduct',
    ]);
    router = jasmine.createSpyObj('Router', ['navigate']);
    activatedRoute = {} as any;
    formBuilder = new FormBuilder();

    await TestBed.configureTestingModule({
      providers: [
        { provide: ProductService, useValue: productService },
        { provide: Router, useValue: router },
        { provide: ActivatedRoute, useValue: activatedRoute },
        { provide: FormBuilder, useValue: formBuilder },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(ProductNewComponent);
    component = fixture.componentInstance;
    productService = TestBed.inject(ProductService) as jasmine.SpyObj<ProductService>;
    router = TestBed.inject(Router) as jasmine.SpyObj<Router>;

    productService.getProductById.and.returnValue(of(mockProduct));
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  describe('ngOnInit', () => {
    it('should initialize the form for new product', () => {
      component.ngOnInit();
      component.productId = null;
      component.productForm = formBuilder.group({
        id: [null],
        category: [''],
        name: [''],
        description: [''],
        price: [0.1],
        quantity: [1],
      });
      expect(component.productForm).toBeDefined();
      expect(component.productForm.get('id')?.value).toBeNull();
    });
  });
});
