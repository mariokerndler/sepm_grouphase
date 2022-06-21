import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Component, Input} from '@angular/core';
import {loadStripe} from '@stripe/stripe-js';
import {Globals} from '../../global/globals';
import {CommissionDto} from '../../dtos/commissionDto';

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.scss'],
})
export class CheckoutComponent {

  @Input() commission: CommissionDto;
  // We load  Stripe
  stripePromise = loadStripe(this.globals.stripePublicKey);
  private paymentBaseURI = this.globals.backendUri + '/payments/checkout';
  private headers = new HttpHeaders({
    auth: 'frontend'
  });
  private options = {headers: this.headers};

  constructor(
    private http: HttpClient,
    private globals: Globals
  ) {
  }

  async pay(): Promise<void> {
    // here we create a payment object
    const payment = {
      name: 'Iphone',
      currency: 'eur',
      // amount on cents *10 => to be on dollar
      amount: 99900,
      quantity: '1',
      cancelUrl: 'http://localhost:4200/#/cancel/',
      successUrl: 'http://localhost:4200/#/success/',
    };

    const stripe = await this.stripePromise;

    // this is a normal http calls for a backend api
    this.http
      .post(this.paymentBaseURI, payment, this.options)
      .subscribe((data: any) => {
        // I use stripe to redirect To Checkout page of Stripe platform
        stripe.redirectToCheckout({
          sessionId: data.id,
        });
      });
  }
}
