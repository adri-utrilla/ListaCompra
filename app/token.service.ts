import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TokenService {
  private tokenNav = 'token';
  private apiUrlToken = 'https://localhost:8000/tokens/validar';

  constructor(private http: HttpClient) { }

  setToken(token: string) {
    localStorage.setItem(this.tokenNav, token);
  }

  getToken(): string | null {
    return localStorage.getItem(this.tokenNav);
  }

  validarToken():Observable<void>{
    const token = this.getToken();
    if(!token){
      throw new Error('No hay token');
    }

    const headers = new HttpHeaders({'Content-Type': 'application/json'});
    return this.http.put<void>(this.apiUrlToken, token, {headers});
  }
}
