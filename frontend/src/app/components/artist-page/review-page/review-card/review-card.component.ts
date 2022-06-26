import {Component, Input} from '@angular/core';
import {ReviewDto} from '../../../../dtos/reviewDto';
import {Router} from '@angular/router';
import {NotificationService} from '../../../../services/notification/notification.service';
import {UserRole} from '../../../../dtos/artistDto';

@Component({
  selector: 'app-review-card',
  templateUrl: './review-card.component.html',
  styleUrls: ['./review-card.component.scss']
})
export class ReviewCardComponent {

  @Input() review: ReviewDto;

  constructor(
    private router: Router,
    private notificationService: NotificationService
  ) { }

  navigateToUser() {
    if (this.review.customerDto.userRole === UserRole.user) {
      this.router.navigate(['/user', this.review.customerDto.id])
        .catch(() => this.notificationService.displayErrorSnackbar('Could not navigate to user.'));
    } else {
      this.router.navigate(['/artist', this.review.customerDto.id])
        .catch(() => this.notificationService.displayErrorSnackbar('Could not navigate to user.'));
    }
  }

  navigateToCommission() {
    this.router.navigate(['/commissions', this.review.commissionId])
      .catch(() => this.notificationService.displayErrorSnackbar('Could not navigate to commission id.'));
  }
}
