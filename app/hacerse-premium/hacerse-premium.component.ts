import { Component } from '@angular/core';
import { PagosService } from '../pagos.service';
import { loadStripe, Stripe, StripeCardElement, StripeElements } from '@stripe/stripe-js';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-hacerse-premium',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './hacerse-premium.component.html',
  styleUrls: ['./hacerse-premium.component.css']
})
export class HacersePremiumComponent {
  importe: number = 3;
  nombre: string = '';
  clientSecret: string | null = null;
  stripe: Stripe | null = null;
  elements: StripeElements | null = null;
  cardElement: StripeCardElement | null = null;
  mensaje: string = '';
  isSuccess: boolean = false;
  yaPremium: boolean = false;

  constructor(private pagosService: PagosService) {}

  async ngOnInit() {
    this.stripe = await loadStripe('pk_test_51Q7a7MAXrAwE3n4ItETHJBKvjmcZDCvaMYtf9RwAkdGbg1IRhTB2fQ5hGsfS54IK4OmJAeZkbf4Fima3IcykLuaP00pbFeaUQS');
    if (this.stripe) {
      this.elements = this.stripe.elements();
      this.cardElement = this.elements.create('card');
      this.cardElement.mount('#card-element');
    }

    
    this.pagosService.verificarPremium().subscribe(
      (response) => {
        this.yaPremium = response.isPremium;
        if (this.yaPremium) {
          this.mensaje = 'Ya eres un usuario premium. No necesitas pagar de nuevo.';
          this.isSuccess = true;
        }
      },
      (error) => {
        console.error('Error al verificar el estado premium:', error);
      }
    );
  }

  prepararTransaccion() {
    if (this.yaPremium) {
      this.mensaje = 'Ya eres premium. No puedes preparar otra transacción.';
      this.isSuccess = false;
      return;
    }

    this.pagosService.prepararPago(this.importe).subscribe(
      (secret) => {
        this.clientSecret = secret;
        this.mensaje = 'Pago preparado con éxito. Puede confirmar el pago.';
        this.isSuccess = true;
      },
      (error) => {
        console.error('Error al preparar la transacción:', error);
        this.mensaje = 'Hubo un error al preparar la transacción.';
        this.isSuccess = false;
      }
    );
  }

  confirmarPago() {
    if (!this.stripe || !this.clientSecret || !this.cardElement) {
      this.mensaje = 'No se puede confirmar el pago. Falta información.';
      this.isSuccess = false;
      return;
    }

    this.stripe
      .confirmCardPayment(this.clientSecret, {
        payment_method: {
          card: this.cardElement,
          billing_details: {
            name: this.nombre,
          },
        },
      })
      .then((result) => {
        if (result.error) {
          this.mensaje = 'Error en el pago. Por favor, inténtelo nuevamente.';
          this.isSuccess = false;
        } else {
          this.pagosService.hacerUsuarioPremium().subscribe(
            () => {
              this.mensaje = '¡Pago realizado con éxito! Ahora eres un usuario premium.';
              this.isSuccess = true;
              this.yaPremium = true;
            },
            (error) => {
              this.mensaje = 'El pago se realizó, pero hubo un error al actualizar tu cuenta a premium.';
              this.isSuccess = false;
              console.error('Error al actualizar el estado a premium:', error);
            }
          );
        }
      });
  }
}
