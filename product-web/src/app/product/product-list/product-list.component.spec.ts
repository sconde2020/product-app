import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ProductListComponent } from './product-list.component';
import { ProductService } from '../services/product.service';
import { Router, ActivatedRoute } from '@angular/router';
import { of, throwError } from 'rxjs';
import { ProductListResponse } from '../model/product-list-response';
import { Product } from '../model/product.model';
import { TABLE_CONFIG } from '../constants/page.constants';

describe('ProductListComponent', () => {
  let component: ProductListComponent;
  let fixture: ComponentFixture<ProductListComponent>;
  let productService: jasmine.SpyObj<ProductService>;
  let router: jasmine.SpyObj<Router>;
  let activatedRoute: ActivatedRoute;

  const mockProducts: Product[] = [
    { id: 1, name: 'Product 1', description: 'Desc 1', category: 'Cat 1', price: 100, quantity: 5 },
    {
      id: 2,
      name: 'Product 2',
      description: 'Desc 2',
      category: 'Cat 2',
      price: 200,
      quantity: 10,
    },
  ];

  const mockResponse: ProductListResponse = {
    content: mockProducts,
    page: { totalElements: 2, totalPages: 1, number: 0, size: 2 },
  };

  beforeEach(async () => {
    const productServiceSpy = jasmine.createSpyObj('ProductService', ['getAllProducts']);
    const routerSpy = jasmine.createSpyObj('Router', ['navigate']);
    activatedRoute = {} as any;

    await TestBed.configureTestingModule({
      providers: [
        { provide: ProductService, useValue: productServiceSpy },
        { provide: Router, useValue: routerSpy },
        { provide: ActivatedRoute, useValue: activatedRoute },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(ProductListComponent);
    component = fixture.componentInstance;
    productService = TestBed.inject(ProductService) as jasmine.SpyObj<ProductService>;
    router = TestBed.inject(Router) as jasmine.SpyObj<Router>;
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  describe('navigation methods', () => {
    it('should navigate to product details', () => {
      component.viewDetails(1);
      expect(router.navigate).toHaveBeenCalledWith(['details', 1], { relativeTo: activatedRoute });
    });

    it('should navigate to create new product', () => {
      component.createNewProduct();
      expect(router.navigate).toHaveBeenCalledWith(['new'], { relativeTo: activatedRoute });
    });

    it('should navigate to update product', () => {
      component.updateProduct(2);
      expect(router.navigate).toHaveBeenCalledWith(['update', 2], { relativeTo: activatedRoute });
    });
  });

  describe('deleteProduct', () => {
    it('should remove product from list', () => {
      component.products = [...mockProducts];
      component.totalRecords = mockProducts.length;

      component.deleteProduct(1);

      expect(component.products.length).toBe(1);
      expect(component.products[0].id).toBe(2);
      expect(component.totalRecords).toBe(1);
    });
  });

  describe('lazyLoadProducts', () => {
    it('should update paging and call loadProducts', () => {
      spyOn<any>(component, 'loadProducts').and.callThrough();
      productService.getAllProducts.and.returnValue(of(mockResponse));

      const event = { first: 2, rows: 2, sortField: 'name', sortOrder: 1 };
      component.lazyLoadProducts(event);

      expect(component.pageSize).toBe(2);
      expect(component.currentPage).toBe(1);
      expect(component.sortField).toBe('name');
      expect(component.sortOrder).toBe(TABLE_CONFIG.SORT_ORDER.ASC);
      expect(component['loadProducts']).toHaveBeenCalled();
    });
  });

  describe('getRowStyleClass', () => {
    it('should return "even-row" for even index', () => {
      expect(component.getRowStyleClass(0)).toBe('even-row');
    });

    it('should return "odd-row" for odd index', () => {
      expect(component.getRowStyleClass(1)).toBe('odd-row');
    });
  });

  describe('loadProducts', () => {
    it('should load products successfully', () => {
      productService.getAllProducts.and.returnValue(of(mockResponse));

      component['loadProducts']();

      expect(component.products).toEqual(mockProducts);
      expect(component.totalRecords).toBe(2);
      expect(component.apiErrorStatus).toBe(false);
      expect(component.apiErrorMessage).toBe('');
    });

    it('should handle error when product loading fails', () => {
      const error = new Error('Failed to load') as any;
      productService.getAllProducts.and.returnValue(throwError(() => error));

      component['loadProducts']();

      expect(component.apiErrorStatus).toBe(true);
      expect(component.apiErrorMessage).toBe('Failed to load');
    });
  });
});
