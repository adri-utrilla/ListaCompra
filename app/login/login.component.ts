import { Component } from '@angular/core';
import { FormGroup, ReactiveFormsModule, Validators, FormBuilder, ValidatorFn, AbstractControl, ValidationErrors  } from '@angular/forms';
import { UserService } from '../user.service';
import { CommonModule } from '@angular/common';
import { create } from 'node:domain';
import { on } from 'node:events';
import { HttpErrorResponse } from '@angular/common/http';
import { Router } from '@angular/router';
imports: [CommonModule, ReactiveFormsModule]

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css'
})
export class LoginComponent {
  loginForm: FormGroup;
  submitted = false;
  successMessage: string | null = null; 
  errorMessage: string | null = null;   

  constructor(private formBuilder: FormBuilder, private userService: UserService, private router: Router) {
    this.loginForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      pwd: ['', [Validators.required, Validators.minLength(6), createPasswordValidator()]]
    },);
   }

   onSubmit() {
    this.submitted=true;
    if(this.loginForm.invalid){
      console.warn("Formulario invalido");
    }
    else{
      console.log("todo OK" + JSON.stringify(this.loginForm.value, null, 2));
      console.table(this.loginForm.value);
    this.userService.iniciarSesion1(this.loginForm.controls['email'].value, this.loginForm.controls['pwd'].value).subscribe(
      (data: { token: string }) => {
        console.log(JSON.stringify(data));
        const token = data.token;
        this.successMessage = 'Inicio de sesión exitoso. Redirigiendo...';
          this.router.navigate(['/GestorLista']);
        if (token) {
          console.log('Token recibido:', token);
          sessionStorage.setItem('token', token);
          sessionStorage.setItem('email', this.loginForm.controls['email'].value);
          this.successMessage = 'Inicio de sesión exitoso. Redirigiendo...';
          this.router.navigate(['/GestorLista']);
        }
      },
      (error: HttpErrorResponse) => {
        console.error('Error en el login', error);
        this.errorMessage = error.status == 403 
        ? 'Credenciales incorrectas. Por favor, intenta de nuevo.'
        : 'Ocurrió un error inesperado. Por favor, intenta más tarde.';
      }
    );
    }
  }

  onReset() {
    this.loginForm.reset();
    this.submitted = false;
    this.successMessage = null;
    this.errorMessage = null;
  }
  
  cambiarContrasena(){
    this.router.navigate(['/correoContrasena']);
  }
}

export function createPasswordValidator(): ValidatorFn {
  return (control:AbstractControl) : ValidationErrors | null => {
    const value = control.value;
    if (!value) {
        return null;
    }
    const hasUpperCase = /[A-Z]+/.test(value);
    const hasLowerCase = /[a-z]+/.test(value);
    const hasNumeric = /[0-9]+/.test(value);
    const passwordValid = hasUpperCase && hasLowerCase && hasNumeric;
          return !passwordValid ? {passwordStrength:true}: null;
  };
}
