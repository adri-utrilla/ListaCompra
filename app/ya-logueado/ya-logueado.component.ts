import { Component } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { UserService } from '../user.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-ya-logueado',
  standalone: true,
  imports: [],
  templateUrl: './ya-logueado.component.html',
  styleUrl: './ya-logueado.component.css'
})
export class YaLogueadoComponent {
  constructor(private formBuilder: FormBuilder, private userService: UserService, private router: Router){}
}
