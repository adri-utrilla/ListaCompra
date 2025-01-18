import { Component } from '@angular/core';
import { UserService } from '../user.service';
import { ok } from 'assert';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-correo-cambio',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './correo-cambio.component.html',
  styleUrl: './correo-cambio.component.css'
})
export class CorreoCambioComponent {
  email?: string;
  respuestaOK?: boolean;
  mensajeError?: boolean;

  constructor(private userService: UserService) {
    this.respuestaOK = false;
    this.mensajeError = false;
  }

  enviarEmailPwd() {
    this.userService.correoCambio(this.email!).subscribe(
      ok => {
        console.log('Le hemos enviado un correo', ok);
        this.respuestaOK = true;
      },
      error => {
        console.error('El correo no es válido', error);
        this.mensajeError = true;
      }
    );
  }
}