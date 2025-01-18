import { HttpEvent, HttpHandler, HttpInterceptor, HttpRequest } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, Observable, throwError } from 'rxjs';
import { UserService } from './user.service';

@Injectable({
  providedIn: 'root'
})
export class TokenInterceptorService implements HttpInterceptor{
  private urlListas = 'https://localhost:80';
  constructor(private userservice: UserService) { }
  
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = this.userservice.getToken() || sessionStorage.getItem('token');
    console.log('Token en el interceptor:'+ token);

    if (token && req.url.startsWith(this.urlListas)) {
      const clonedReq = req.clone({
        headers: req.headers.set('token', token)
      });
      console.log('Solicitud interceptada y clonada:', clonedReq);
      return next.handle(clonedReq).pipe(
        catchError(err => {
          console.error('Error en la solicitud:', err);
          return throwError(err);
        })
      );
    }
  
    return next.handle(req);
  }

 
}



//ng serve --ssl --ssl-key src/app/ssl/localhost.key --ssl-cert src/app/ssl/localhost.crt