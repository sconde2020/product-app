import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class ApiService {

  constructor(private http : HttpClient) { }

  private baseUrl = environment.baseUrl;

  getAll<T>(endpoint: string, params?: Record<string, any>): Observable<T>{
    const url = `${this.baseUrl}/${endpoint}`;
    return this.http.get<T>(url, { params });
  }

  getById<T>(endpoint: string, id: string | number): Observable<T> {
    const url = `${this.baseUrl}/${endpoint}/${id}`;
    return this.http.get<T>(url);
  }

  create<T>(endpoint: string, body: Partial<T>): Observable<T> {
    const url = `${this.baseUrl}/${endpoint}`;
    return this.http.post<T>(url, body);
  }

  update<T>(endpoint: string, id: string | number, body: Partial<T>): Observable<T> {
    const url = `${this.baseUrl}/${endpoint}/${id}`;
    return this.http.put<T>(url, body);
  }

  delete(endpoint: string, id: string | number): Observable<void> {
    const url = `${this.baseUrl}/${endpoint}/${id}`;
    return this.http.delete<void>(url);
  }
}
