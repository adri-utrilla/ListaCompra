import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class PagosService {

  private apiUrlPagos = 'https://localhost:9000/pagos'
  constructor(private http : HttpClient) { }

  prepararPago(importe: number): Observable<string> {
    const token = sessionStorage.getItem('token');
    let headers = new HttpHeaders();
    headers = headers.set('token', token!);
    return this.http.put(this.apiUrlPagos + '/prepararTransaccion', importe, {responseType: 'text', headers});
  }

  hacerUsuarioPremium(): Observable<any> {
    const apiUrlPremium = 'https://localhost:9000/users/hacersePremium'; 
    const token = sessionStorage.getItem('token');
    console.log('Token:', token);
    let headers = new HttpHeaders();
    headers = headers.set('token', token!);
    return this.http.post<any>(apiUrlPremium,{},{headers});
  }
  
  verificarPremium(): Observable<{ isPremium: boolean }> {
    const token = sessionStorage.getItem('token');
    let headers = new HttpHeaders().set('token', token!);
    return this.http.get<{ isPremium: boolean }>('https://localhost:9000/users/verificarPremium', { headers });
  }
}
