import {Injectable} from '@angular/core';
import {Globals} from '../global/globals';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {catchError} from "rxjs";
import {ApplicationUserDto} from "../dtos/applicationUserDto";
import {tap} from "rxjs/operators";

@Injectable({
  providedIn: 'root'
})
export class PaymentServiceService {

  constructor(
    public globals: Globals,
    private http: HttpClient
  ) {
  }

  async createOrder() {
    const accessToken = await this.generateAccessToken();
    const url = `${this.globals.paymentBaseUrl}/v2/checkout/orders`;
    const response = await fetch(url, {
      method: 'post',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${accessToken}`,
      },
      body: JSON.stringify({
        intent: 'CAPTURE',
        purchase_units: [
          {
            amount: {
              currency_code: 'USD',
              value: '100.00',
            },
          },
        ],
      }),
    });
    const data = await response.json();
    return data;
  }

  async capturePayment(orderId) {
    const accessToken = await this.generateAccessToken();
    const url = `${this.globals.paymentBaseUrl}/v2/checkout/orders/${orderId}/capture`;
    const response = await fetch(url, {
      method: 'post',
      headers: {
        'Content-Type': 'application/json',
        Authorization: `Bearer ${accessToken}`,
      },
    });
    const data = await response.json();
    return data;
  }

  generateAccessToken(errorAction?: () => void) {
    const auth = Buffer.from(this.globals.clientID + ':' + this.globals.appSecret).toString('base64');

    const authOptions = {
      headers: new HttpHeaders({
        Authorization: `Basic ${auth}`
      })
    };

    return this.http.post(`${this.globals.paymentBaseUrl}/v1/oauth2/token`, 'grant_type=client_credentials', authOptions)
      .pipe(
        catchError((err) => {
          if (errorAction != null) {
            errorAction();
          }

          return this.notificationService.notifyUserAboutFailedOperation<ApplicationUserDto>('Creating User')(err);
        }),
        tap(_ => {
          if (successAction != null) {
            successAction();
          }
        })
      )

    const data = response.json();
    return data.access_token;
  }
}
