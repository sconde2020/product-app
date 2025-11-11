import { TestBed } from '@angular/core/testing';

import { ApiService } from './api.service';

import { provideHttpClient } from '@angular/common/http';
import { provideHttpClientTesting, HttpTestingController } from '@angular/common/http/testing';
import { environment } from '../../environments/environment';

describe('ApiService (standalone providers)', () => {
  let service: ApiService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ApiService, provideHttpClient(), provideHttpClientTesting()],
    });

    service = TestBed.inject(ApiService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  afterEach(() => {
    httpMock.verify();
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  describe('getAll', () => {
    it('should call GET and return data', () => {
      const mockData = [{ id: 1, name: 'Test' }];
      service.getAll('users').subscribe((res) => {
        expect(res).toEqual(mockData);
      });

      const req = httpMock.expectOne(`${environment.baseUrl}/users`);
      expect(req.request.method).toBe('GET');
      req.flush(mockData);
    });

    it('should pass query params', () => {
      const params = { page: 1, size: 10 };
      service.getAll('users', params).subscribe();

      const req = httpMock.expectOne(`${environment.baseUrl}/users?page=1&size=10`);
      expect(req.request.method).toBe('GET');
      req.flush([]);
    });
  });

  describe('getById', () => {
    it('should call GET with id', () => {
      const mockData = { id: 1, name: 'Test' };
      service.getById('users', 1).subscribe((res) => {
        expect(res).toEqual(mockData);
      });

      const req = httpMock.expectOne(`${environment.baseUrl}/users/1`);
      expect(req.request.method).toBe('GET');
      req.flush(mockData);
    });
  });

  describe('create', () => {
    it('should call POST with body', () => {
      const body = { name: 'New User' };
      const mockResponse = { id: 2, name: 'New User' };

      service.create('users', body).subscribe((res) => {
        expect(res).toEqual(mockResponse);
      });

      const req = httpMock.expectOne(`${environment.baseUrl}/users`);
      expect(req.request.method).toBe('POST');
      expect(req.request.body).toEqual(body);
      req.flush(mockResponse);
    });
  });

  describe('update', () => {
    it('should call PUT with id and body', () => {
      const body = { name: 'Updated User' };
      const mockResponse = { id: 1, name: 'Updated User' };

      service.update('users', 1, body).subscribe((res) => {
        expect(res).toEqual(mockResponse);
      });

      const req = httpMock.expectOne(`${environment.baseUrl}/users/1`);
      expect(req.request.method).toBe('PUT');
      expect(req.request.body).toEqual(body);
      req.flush(mockResponse);
    });
  });

  describe('delete', () => {
    it('should call DELETE with id', () => {
      service.delete('users', 1).subscribe((res) => {
        expect(res).toBeNull();
      });

      const req = httpMock.expectOne(`${environment.baseUrl}/users/1`);
      expect(req.request.method).toBe('DELETE');
      req.flush(null);
    });
  });

  describe('error handling', () => {
    it('should return an error Observable on HTTP error', () => {
      service.getAll('users').subscribe({
        next: () => fail('Expected an error'),
        error: (err) => {
          // err is the Error object thrown by handleHttpError
          expect(err).toBeTruthy();
          expect(err.message).toBe('Server error'); // as returned by handleHttpError
          expect((err as any).apiError).toBeTruthy();
          expect((err as any).apiError.status).toBe(500);
        },
      });

      const req = httpMock.expectOne(`${environment.baseUrl}/users`);
      req.flush('Server error', { status: 500, statusText: 'Internal Server Error' });
    });
  });
});
