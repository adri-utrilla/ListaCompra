import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { ListaService } from '../lista.service';
import { response } from 'express';
import { lista } from '../modelo/lista.model';
import { Router } from '@angular/router';
import { ManagerService } from '../manager.service';
import { producto } from '../modelo/producto.model';

@Component({
  selector: 'app-gestor-listas',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './lista-gestor.component.html',
  styleUrl: './lista-gestor.component.css'
})
export class GestorListasComponent {

  nuevaLista?: string;
  misListas: lista[] = [];
  unidadesCompradas : number = 0;
  unidadesPedidas : number = 0;
  errorLista : string = "";
  errorEliminar : string = "";

  ws = new WebSocket('wss://localhost:80/wsListas?email=' + sessionStorage.getItem('email'));

  constructor(private service: ListaService, private router :Router, public manager : ManagerService) {
    
    this.service.obtenerLista().subscribe(
      result => {
        this.misListas = result;
      },
      error => {
        console.error('Error al obtener las listas', error);
      }
    )

    this.ws.onerror = function(event){
      alert(event);
    }
  }

  agregarLista() {
    this.service.crearLista(this.nuevaLista!).subscribe(
      (response) => {
        let listaCreada = new lista();
        listaCreada.nombre = response.nombre;
        listaCreada.id = response.id;
        console.log('Lista creada con exito', response);
        this.misListas.push(listaCreada);
      },
      (error) => {
        console.error('Error al crear la lista', error);
        this.errorLista = error.status == 402 ? 'Para crear mas listas hazte premium.' : 'Error inesperado, intentalo de nuevo mas tarde.';
      }
    );
  }
  agregarProducto(indice : number) {
    this.manager.listaSeleccionada=this.misListas[indice];
    this.router.navigate(['/DetalleLista']);
  }

  obtenerListas() {
    this.service.obtenerLista().subscribe(
      (result) => {
        this.misListas = result;
      },
      (error) => {
        console.error('Error al obtener las listas', error);
      }
    );
  }

  eliminarLista(indice: number): void {
    const idLista = this.misListas[indice].id;
    console.log(`Intentando eliminar la lista con ID: ${idLista}`);
    
    this.service.eliminarLista(idLista.toString()).subscribe(
      () => {
        console.log('Lista eliminada con éxito');
        this.misListas.splice(indice, 1);
      },
      (error) => {
        console.error('Error al eliminar la lista:', error);
        this.errorEliminar = error.status == 401 ? 'No eres el usuario propietario de la lista.' : 'Error inesperado, intentalo de nuevo mas tarde.';
      }
    );
}


  verLista(indice: number) {
    this.manager.listaSeleccionada = this.misListas[indice];
    this.router.navigate(['/DetalleLista']);
  }
  usuariosLista(indice: number){
    this.manager.listaSeleccionada = this.misListas[indice];
    this.router.navigate(['/InvitarUsuario']);
  }
}