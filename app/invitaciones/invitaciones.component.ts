import { Component } from '@angular/core';
import { FormGroup, ReactiveFormsModule, Validators, FormBuilder, ValidatorFn, AbstractControl, ValidationErrors, FormsModule  } from '@angular/forms';
import { ListaService } from '../lista.service';
import { CommonModule } from '@angular/common';
import { ManagerService } from '../manager.service';

@Component({
  selector: 'app-invitaciones',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './invitaciones.component.html',
  styleUrl: './invitaciones.component.css'
})
export class InvitacionesComponent {
  idLista?: number;
  email?: string;
  emailEliminar?: string;
  UrlGenerada?: string
  hayUrlGenerada: boolean = false;
  errorEliminar: String = '';
  errorInvitar: String = '';
  eliminado: boolean = false;

  constructor(private listaService: ListaService, public manager: ManagerService){}
  
  invitarUsuario(){
    if(!this.manager.listaSeleccionada?.id || !this.email){
      console.error('Faltan datos');
      return;
    }
    this.listaService.invitarUsuario(this.manager.listaSeleccionada?.id!, this.email).subscribe(
      (respone) => {
        this.UrlGenerada = respone;
        this.hayUrlGenerada = true;
        console.log('Url generada:', this.UrlGenerada);
      }, 
      (error) => {
        console.error('Error al invitar usuario', error);
        this.errorInvitar = error.status == 401 ? 'No eres el dueño de la lista para invitar a mas usuarios.' : 'Error inesperado, intentalo de nuevo mas tarde.';
      }
    );
  }

  eliminarUsuario(){
    if(!this.manager.listaSeleccionada?.id || !this.emailEliminar){
      console.error('Faltan datos');
      return;
    }
    this.listaService.eliminarUsuario(this.manager.listaSeleccionada?.id!, this.emailEliminar).subscribe(
      (respone) => {
        console.log('Usuario eliminado:', respone);
        this.eliminado = true;
        this.errorEliminar = 'Usuario eliminado correctamente';

      }, 
      (error) => {
        console.error('Error al eliminar usuario', error);
        this.eliminado = false;
        this.errorEliminar = 'Error al eliminar usuario';
      }
    );
  }
}
