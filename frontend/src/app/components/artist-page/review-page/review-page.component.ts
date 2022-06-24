import {Component, Input, OnInit} from '@angular/core';
import {ApplicationUserDto} from '../../../dtos/applicationUserDto';
import {ReviewDto} from '../../../dtos/reviewDto';
import {NotificationService} from '../../../services/notification/notification.service';
import {CommissionService} from '../../../services/commission.service';
import {CommissionSearchDto} from '../../../dtos/commissionSearchDto';
import {SearchConstraint} from '../../../global/SearchConstraint';
import {Globals} from '../../../global/globals';

@Component({
  selector: 'app-review-page',
  templateUrl: './review-page.component.html',
  styleUrls: ['./review-page.component.scss']
})
export class ReviewPageComponent implements OnInit {

  @Input() user: ApplicationUserDto;
  reviews: ReviewDto[] = [];
  hasReviews = false;

  constructor(
    private notificationService: NotificationService,
    private commissionService: CommissionService,
    private globals: Globals
  ) { }

  ngOnInit(): void {
    this.fetchReviews();
  }

  private fetchReviews() {
    const searchCom: CommissionSearchDto = {
      date: SearchConstraint.none,
      name: '',
      priceRange: [0, this.globals.maxCommissionPrice],
      artistId: String(this.user.id),
      userId: '',
      pageNr: 0
    };

    this.commissionService.filterDetailedCommissions(searchCom).subscribe(
      (commissions) => {
        commissions.forEach(commission => {
          if(commission.reviewDto) {
            this.reviews.push(commission.reviewDto);
          }
        });
        this.hasReviews = this.reviews.length > 0;
      }
    );
  }
}
