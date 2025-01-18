import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-aceptar-invitacion',
  template: `
    <div *ngIf="mensaje">{{ mensaje }}</div>
  `
})
export class AceptarInvitacionComponent implements OnInit {
  mensaje: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private http: HttpClient,
    private router: Router
  ) {}

  ngOnInit(): void {
    const idLista = this.route.snapshot.queryParamMap.get('idLista');
    const email = this.route.snapshot.queryParamMap.get('email');

    if (idLista && email) {
      this.http.get(`http://localhost:80/listas/aceptarInvitacion?idLista=${idLista}&email=${email}`, { responseType: 'text' }).subscribe(
        () => {
          this.mensaje = 'Invitación aceptada con éxito. Redirigiendo...';
          setTimeout(() => {
            this.router.navigate(['/GestorLista']);
          }, 3000);
        },
        (error) => {
          this.mensaje = 'Error al aceptar la invitación.';
        }
      );
    } else {
      this.mensaje = 'Parámetros inválidos en la URL.';
    }
  }
}
