import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductDetailComponent } from './product-detail.component';
import { Router, ActivatedRoute, ParamMap, ActivatedRouteSnapshot } from '@angular/router';
import { ProductService } from '../services/product.service';
import { Product } from '../model/product-list-response';
import { of } from 'rxjs';

describe('ProductDetailComponent', () => {
  let component: ProductDetailComponent;
  let fixture: ComponentFixture<ProductDetailComponent>;
  let productService: jasmine.SpyObj<ProductService>;
  let router: jasmine.SpyObj<Router>;
  let activatedRoute: Partial<ActivatedRoute>;

  const mockProduct: Product = {
    id: 1,
    category: 'Cat 1',
    name: 'Product 1',
    description: 'Desc 1',
    price: 100,
    quantity: 5,
  };

  beforeEach(async () => {
    productService = jasmine.createSpyObj('ProductService', ['getProductById']);
    router = jasmine.createSpyObj('Router', ['navigate']);
    activatedRoute = {
      snapshot: {
        paramMap: {
          get: (key: string) => '1', // default: new product
        } as ParamMap,
      } as ActivatedRouteSnapshot,
    };

    await TestBed.configureTestingModule({
      imports: [ProductDetailComponent],
      providers: [
        { provide: ProductService, useValue: productService },
        { provide: Router, useValue: router },
        { provide: ActivatedRoute, useValue: activatedRoute },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(ProductDetailComponent);
    component = fixture.componentInstance;

    productService.getProductById.and.returnValue(of(mockProduct));
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('ngOnInit', () => {
    it('should fetch product details on init', () => {
      component.ngOnInit();
      expect(productService.getProductById).toHaveBeenCalledWith(1);
      expect(component.product).toEqual(mockProduct);
    });
  });

  describe('onBackToList', () => {
    it('should navigate back to product list', () => {
      component.onBackToList();
      expect(router.navigate).toHaveBeenCalledWith(['/products']);
    });
  });
});
