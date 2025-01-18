import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { tap } from 'rxjs/operators';
import { Observable } from 'rxjs';
 
@Injectable({
  providedIn: 'root'
})
export class UserService {
 
private apiUrlToken='https://localhost:8000/tokens/validar';
private apiUrlGenerica='https://localhost:9000/users';
 
private token: string | null = null;
 
  constructor(private http: HttpClient) { }


  checkCookie() {
    return this.http.get<{ token: string, email: string }>(this.apiUrlGenerica + "/checkCookie", { 
      withCredentials: true 
    });
  }  
 
  register1(email : string, pwd1 : string, pwd2 : string) {
    let info = {
      email : email,
      pwd1 : pwd1,
      pwd2 : pwd2
    }
    return this.http.post<any>(this.apiUrlGenerica + "/registrar1", info);
  }

  correoCambio(email: string){
    return this.http.post<any>(this.apiUrlGenerica + "/correoCambioContrasena", email);
  }
  
  cambiarContrasena(pwd: string, recToken: string) {
    const body = { password: pwd, token: recToken };
    return this.http.post<any>(this.apiUrlGenerica + "/cambioContrasena", body);
  }

 
  iniciarSesion1(email : string, pwd : string) {
    let info = {
      email : email,
      pwd : pwd
    }
    let respuesta;
    let urlIniciarSesion1=this.apiUrlGenerica+'/login1';
 
      return this.http.put<any>(urlIniciarSesion1, info,  { responseType: 'text' as 'json', withCredentials : true}).pipe(
        tap(response=> {
          this.token = response;
          localStorage.setItem('token', this.token!);
          sessionStorage.setItem('token', this.token!);
          sessionStorage.setItem('email', email!);
          document.cookie = "authToken="+this.token!+"; expires=Fri, 31 Dec 2024 12:00:00 UTC; path=/";
        }
      ));
      
  }
 
  getToken(): string | null {
    if(this.token){
      return this.token;
    }
    const tokenNav = localStorage.getItem('authToken') || sessionStorage.getItem('token');

    if(tokenNav){
      this.token = tokenNav;
    }
    return null;
  }

  validarToken(): Observable<boolean>{
    const token = this.getToken();
    if(!token){
      throw new Error('No hay token');
    }

    return this.http.put<boolean>(this.apiUrlToken, token, {withCredentials : true});
  }
 
  logout(): void {
    this.token = null;
    localStorage.removeItem('authToken');
  }
}