import { Component } from '@angular/core';
import { UserService } from '../user.service';
import { ok } from 'assert';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-cambio-contrasena',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './cambio-contrasena.component.html',
  styleUrl: './cambio-contrasena.component.css'
})
export class CambioContrasenaComponent {
  pwd1?: string;
  pwd2?: string;
  recToken?: string | null = null;
  respuestaOK?: boolean;
  mensajeError?: boolean;
  contraseniasNoCoinciden?: boolean;
  contraseniaInvalida?: boolean;

  constructor(private route: ActivatedRoute, private userService: UserService) {
    this.respuestaOK = false;
    this.mensajeError = false;
    this.contraseniaInvalida = false;
  }

  ngOnInit() {
    this.route.queryParamMap.subscribe(params => {
      this.recToken = params.get('recToken');
      console.log('Token capturado desde query param:', this.recToken);
    });
  }

  cambiarContrasena() {
    this.contraseniasNoCoinciden = false;
    this.contraseniaInvalida = false;

    if (!this.validarContrasena(this.pwd1)) {
      console.log(this.pwd1)
      console.error('La contraseña no cumple con los requisitos de seguridad (mayuscula, minuscula y numero)');
      this.contraseniaInvalida = true;
      return;
    }

    if (this.pwd1 !== this.pwd2) {
      console.error('Las contraseñas no coinciden');
      this.contraseniasNoCoinciden = true;
      return;
    }

    this.userService.cambiarContrasena(this.pwd1!, this.recToken!).subscribe(
      ok => {
        console.log('Se ha guardado su contraseña', ok);
        this.respuestaOK = true;
      },
      error => {
        console.error('No se ha podido cambiar la contraseña', error);
        this.mensajeError = true;
      }
    );
  }

  private validarContrasena(contrasena?: string): boolean {
    if (!contrasena) {
      console.log(this.pwd1)
      return false;
    }
    
    const regex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).{6,}$/;
    console.log(regex)
    console.log(regex.test(contrasena))
    return regex.test(contrasena);
  }
 
}