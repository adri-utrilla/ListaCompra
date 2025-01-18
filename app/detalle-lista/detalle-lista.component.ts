import { Component } from '@angular/core';
import { producto } from '../modelo/producto.model';
import { ListaService } from '../lista.service';
import { ManagerService } from '../manager.service';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-detalle-lista',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './detalle-lista.component.html',
  styleUrl: './detalle-lista.component.css'
})
export class DetalleListaComponent {
  idLista?: number;
  nuevoProducto : string='';
  unidadesPedidas : number=0;
  unidadesCompradas: number=0;
  producto : producto = new producto();
  lista? : string="";
  misProductos: producto[] = [];
  unidadesAcomprar: number = 0;
  errorLista : string = "";
  
  ws = new WebSocket('wss://localhost:80/wsListas?email=' + sessionStorage.getItem('email'));

  constructor(private listaService: ListaService, public manager : ManagerService){
    this.lista=this.manager.listaSeleccionada?.nombre;
    this.listaService.verLista(this.manager.listaSeleccionada?.id!).subscribe(
      (response) => {
       // this.lista = response;
        this.misProductos = response.productos; 
        console.log('Lista cargada:', this.lista);
      },
      (error) => {
        console.error('Error al cargar la lista:', error);
      }
    );
    this.ws.onmessage = (event) => {
      const data = JSON.parse(event.data);
      console.log('Mensaje WebSocket recibido:', data);
      
      if (data.tipo === 'actualizacionDeLista' && data.idLista === this.manager.listaSeleccionada?.id) {
        this.cargarLista();
      }
    };
  
    this.ws.onerror = (event) => {
      console.error('Error en WebSocket:', event);
    };
  
    this.ws.onclose = (event) => {
      console.log('WebSocket cerrado:', event);
    };
  }

  aniadirProducto(){
    console.log('voy a almacenar producto');
    this.producto.crearProducto (this.nuevoProducto,this.unidadesPedidas, this.unidadesCompradas);
    this.listaService.aniadirProducto(this.manager.listaSeleccionada!.id,this.producto).subscribe(
      (response) => {
        console.log('producto agregado correctamente:', response);
        this.cargarLista();
      },
      (error) => {
        console.error('Error al almacenar el producto:', error);
        this.errorLista = error.status == 402 ? 'Para crear mas añadir mas productos hazte premium.' : 'Error inesperado, intentalo de nuevo mas tarde.';
      }
    );
  }

  cargarLista() {
    this.listaService.verLista(this.manager.listaSeleccionada?.id!).subscribe(
      (response) => {
        this.misProductos = response.productos; 
        console.log('Lista cargada:', this.lista);
      },
      (error) => {
        console.error('Error al cargar la lista:', error);
      }
    );
  }

  comprarProducto(idProducto: string, unidadesCompradas: number): void {
    this.listaService.comprarProducto(idProducto, unidadesCompradas).subscribe(
      (productoActualizado) => {
        console.log('Producto actualizado:', productoActualizado);
      },
      (error) => {
        console.error('Error al comprar producto:', error);
      }
    );
  }
  
  
  eliminarProducto(idProducto: string): void {
    this.listaService.eliminarProducto(idProducto).subscribe(
      (response) => {
        console.log('Producto eliminado:', response);
        this.cargarLista();
      },
      (error) => {
        console.error('Error al eliminar producto:', error);
      }
    );
  }

  editarProducto(idProducto: string, nombre: string, unidadesPedidas: number): void {
    this.listaService.editarProducto(idProducto, nombre, unidadesPedidas).subscribe(
      (productoActualizado) => {
        console.log('Producto actualizado:', productoActualizado);
      },
      (error) => {
        console.error('Error al editar producto:', error);
      }
    );
  }
  
}


