import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {Globals} from '../global/globals';
import {NotificationService} from './notification/notification.service';
import {CheckoutPaymentDto} from '../dtos/CheckoutPaymentDto';
import {loadStripe} from '@stripe/stripe-js';
import {catchError} from 'rxjs';
import {tap} from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class CheckoutService {

  private stripePromise = loadStripe(this.globals.stripePublicKey);
  private headers = new HttpHeaders({
    auth: 'frontend'
  });
  private options = {headers: this.headers};
  private paymentsBaseUI = this.globals.backendUri + '/payments';

  constructor(
    private http: HttpClient,
    private globals: Globals,
    private notificationService: NotificationService) {
  }

  async payWithCheckout(commissionId: number,
                        currency: string,
                        cancelUrl: string,
                        successUrl: string,
                        errorAction?: () => void,
                        successAction?: () => void): Promise<void> {

    console.log('Entered paywith checkout');
    // here we create a payment object
    const paymentDto: CheckoutPaymentDto = {
      commissionId,
      currency,
      cancelUrl,
      successUrl
    };

    const stripe = await this.stripePromise;

    // this is a normal http calls for a backend api
    const request = this.http.post(this.paymentsBaseUI + '/checkout', paymentDto, this.options)
      .pipe(
        catchError((err) => {
          if (errorAction != null) {
            errorAction();
          }

          console.log('Failed call to backend');

          return this.notificationService.notifyUserAboutFailedOperation<CheckoutPaymentDto>('Paying commission')(err);
        }),
        tap((data: any) => {
          if (successAction != null) {
            successAction();
          }

          console.log('Successfull call to backend');

          stripe.redirectToCheckout({
            sessionId: data.id,
          });
        }));

    request.subscribe();
  }
}
