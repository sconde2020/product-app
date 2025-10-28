import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProductNewComponent } from './product-new.component';

describe('ProductNewComponent', () => {
  let component: ProductNewComponent;
  let fixture: ComponentFixture<ProductNewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProductNewComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProductNewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
