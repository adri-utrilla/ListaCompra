import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { producto } from './modelo/producto.model';
import { Observable } from 'rxjs';
import { lista } from './modelo/lista.model';
import { TokenInterceptorService } from './token-interceptor.service';
import { get } from 'node:http';

@Injectable({
  providedIn: 'root'
})
export class ListaService {

  private apiURLGenerica = 'https://localhost:80/listas';
  constructor(private http: HttpClient, private tokenInterceptorService: TokenInterceptorService) { }

  crearLista(nuevaLista: String){
    const token = sessionStorage.getItem('token');
    let headers = new HttpHeaders();
    headers = headers.set('token', token!);
    return this.http.post<any>(this.apiURLGenerica + '/crearLista', nuevaLista, {headers});
  }
  aniadirProducto(idLista: number, producto: producto): Observable<any> {
    let apiURLEspecifica = this.apiURLGenerica + '/addProducto';
    const headers = new HttpHeaders({ 'Content-Type': 'application/json', 'idLista': idLista, 'token': sessionStorage.getItem('token')! });
    return this.http.post<any>(apiURLEspecifica, producto, {headers});
  }

  obtenerLista(): Observable<lista[]> {
    const token = sessionStorage.getItem('token');
    let headers = new HttpHeaders();
    headers = headers.set('token', token!);
    return this.http.get<lista[]>(this.apiURLGenerica + '/obtenerListas?email=' + sessionStorage.getItem('email'), {headers});
  }

  eliminarLista(idLista: string): Observable<void> {
    const headers = new HttpHeaders({
        'Content-Type': 'application/json',
        'token': sessionStorage.getItem('token')! 
    });
    return this.http.delete<void>(this.apiURLGenerica + '/eliminarLista' + idLista, { headers });
  }

  verLista(idLista: number): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json', 'token': sessionStorage.getItem('token')! });
    return this.http.get<any>(this.apiURLGenerica +'/verLista' +idLista, { headers });
  }

  invitarUsuario(idLista: number, invitado: string): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json', 'idLista': idLista, 'email': invitado, 'token': sessionStorage.getItem('token')! });
    return this.http.post<any>(this.apiURLGenerica + '/invitarUsuario', invitado, {headers, responseType: 'text' as 'json'});
  }

  eliminarUsuario(idLista: number, email: string): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json', 'idLista': idLista, 'email': email, 'token': sessionStorage.getItem('token')! });
    return this.http.delete<any>(this.apiURLGenerica + '/eliminarUsuario', {headers, responseType: 'text' as 'json'});
  }

  comprarProducto(idProducto: string, unidadesCompradas: number): Observable<producto> {
    const apiURLEspecifica = this.apiURLGenerica + '/comprar';
    const cuerpo = { idProducto, unidadesCompradas };  
  
    return this.http.put<producto>(apiURLEspecifica, cuerpo, { headers: new HttpHeaders({ 'Content-Type': 'application/json', 'token': sessionStorage.getItem('token')! }) });
  }
  eliminarProducto(idProducto: string): Observable<any> {
    const apiURLEspecifica = this.apiURLGenerica + '/eliminarProducto';
    return this.http.delete<any>(apiURLEspecifica + idProducto, { headers: new HttpHeaders({ 'Content-Type': 'application/json', 'token':sessionStorage.getItem('token')! }) });
  }

  editarProducto(idProducto: string, nombre: string, unidadesPedidas: number): Observable<producto> {
    const apiURLEspecifica = this.apiURLGenerica + '/editar';
    const cuerpo = { idProducto, nombre, unidadesPedidas };
  
    return this.http.put<producto>(apiURLEspecifica, cuerpo, { headers: new HttpHeaders({ 'Content-Type': 'application/json', 'token': sessionStorage.getItem('token')! }) });
  }
  
}
