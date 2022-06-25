import {Component, Input} from '@angular/core';
import {CommissionDto} from '../../dtos/commissionDto';
import {CheckoutService} from '../../services/checkout.service';

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.scss'],
})
export class CheckoutComponent {

  @Input() commission: CommissionDto;

  constructor(
    private checkoutService: CheckoutService
  ) {
  }

  async pay(): Promise<void> {
    console.log('Entered Pay method');
    return await this.checkoutService.payWithCheckout(
      this.commission.id,
      'eur',
      'http://localhost:4200/#/commissions/' + this.commission.id,
      'http://localhost:4200/#/commissions/' + this.commission.id);
  }

  //TODO: Remove after testing
  payTest(): void {
    this.checkoutService.paymentWithoutCheckoutTest(
      this.commission.id,
      'eur',
      'http://localhost:4200/#/cancel',
      'http://localhost:4200/#/success')
      .subscribe();
    window.location.reload();
  }
}
