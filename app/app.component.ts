import { Component } from '@angular/core';
import { Router, RouterOutlet } from '@angular/router';
import { Register1Component } from "./registar1/registar1.component";
import { LoginComponent } from "./login/login.component";
import { GestorListasComponent } from "./lista-gestor/lista-gestor.component";
import { UserService } from './user.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, Register1Component, LoginComponent, GestorListasComponent],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Lista de compra personal';
  menuOpen = false;

  constructor(private userService: UserService, private router: Router) {
    this.userService.checkCookie().subscribe(
      (response) => {
        console.log('Cookie válida:', response);
        sessionStorage.setItem('email', response.email);
        sessionStorage.setItem('token', response.token);
      },
      (error) => {
        console.error('Cookie no válida:', error);
      }
    );
  }

  toggleMenu(): void {
    this.menuOpen = !this.menuOpen;
  }
}



//ng serve --ssl --ssl-key src/app/ssl/localhost.key --ssl-cert src/app/ssl/localhost.crt