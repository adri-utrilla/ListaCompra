import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms'; 
import { UserService } from '../user.service';


@Component({
  selector: 'app-registar1',
  standalone: true,
  imports: [CommonModule,FormsModule],
  templateUrl: './registar1.component.html',
  styleUrl: './registar1.component.css'
})
export class Register1Component {

  email? : string
  pwd1? : string
  pwd2? : string
  respuestaOK : boolean 
  mensajeError : boolean
  contraseniasNoCoinciden : boolean

  constructor(private service : UserService) {
    this.respuestaOK = false;
    this.mensajeError = false;
    this.contraseniasNoCoinciden = false;
   }

  registrar() {
    this.respuestaOK = false;
    this.mensajeError = false;
    this.contraseniasNoCoinciden = false;
    if (this.pwd1 != this.pwd2) {
      console.error('Las contraseñas no coinciden');
      this.contraseniasNoCoinciden = true;
      return;
    }

    this.service.register1(this.email!, this.pwd1!, this.pwd2!).subscribe(
      ok => {
        console.log('Registro exitoso', ok);
        this.respuestaOK = true;
      },
      error => {
        console.error('Error en el registro', error);
        this.mensajeError = true;
      }
    );
  }
}
