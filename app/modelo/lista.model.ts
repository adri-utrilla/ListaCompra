import { producto } from "./producto.model";


export class lista {
    id: number;
    nombre: string;
    productos: producto[];
    constructor() {
        this.id = 0;
        this.nombre = '';
        this.productos = [];
    }

    inicializarLista(nombre:string, id:number){
        this.nombre = nombre;
        this.id = id;
    }
}