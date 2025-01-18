import { Routes } from '@angular/router';
import { LoginComponent } from './login/login.component';
import { Register1Component } from './registar1/registar1.component';
import { GestorListasComponent } from './lista-gestor/lista-gestor.component';
import { DetalleListaComponent } from './detalle-lista/detalle-lista.component';
import { InvitacionesComponent } from './invitaciones/invitaciones.component';
import { CorreoCambioComponent } from './correo-cambio/correo-cambio.component';
import { HacersePremiumComponent } from './hacerse-premium/hacerse-premium.component';
import { CambioContrasenaComponent } from './cambio-contrasena/cambio-contrasena.component';
import { AceptarInvitacionComponent } from './aceptar-invitacion/aceptar-invitacion.component';


export const routes: Routes = [
    {path: 'Login', component: LoginComponent },
    {path: 'Register', component: Register1Component },
    {path: 'GestorLista', component: GestorListasComponent},
    {path: 'DetalleLista', component: DetalleListaComponent},
    {path: 'UsuariosLista', component: InvitacionesComponent},
    {path: 'correoContrasena', component: CorreoCambioComponent},
    {path: 'hacersePremium', component: HacersePremiumComponent},
    {path: 'cambioContrasena', component: CambioContrasenaComponent},
    {path: 'InvitarUsuario', component: InvitacionesComponent},
    {path: 'AceptarInvitacion', component: AceptarInvitacionComponent}
];
