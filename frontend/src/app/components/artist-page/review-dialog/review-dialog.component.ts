import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {Globals} from '../../../global/globals';
import {CommissionService} from '../../../services/commission.service';
import {ReviewDto} from '../../../dtos/reviewDto';
import {CommissionDto} from '../../../dtos/commissionDto';
import {SimpleCommissionDto} from '../../../dtos/simpleCommissionDto';

@Component({
  selector: 'app-review-dialog',
  templateUrl: './review-dialog.component.html',
  styleUrls: ['./review-dialog.component.scss']
})
export class ReviewDialogComponent{

  rating = 3;
  ratingArr = [];

  reviewText = '';
  private simpleCommission: SimpleCommissionDto;

  constructor(
    public dialogRef: MatDialogRef<ReviewDialogComponent>,
    public globals: Globals,
    private commissionService: CommissionService,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {
    this.simpleCommission = data.commission;
    this.rating = globals.defaultStarRating;
    for (let index = 0; index < this.globals.maxStarRating; index++) {
      this.ratingArr.push(index);
    }
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  onClick(rating: number) {
    this.rating = rating;
  }

  isValidReviewText(): boolean {
    return this.reviewText.trim().length > 0;
  }

  showIcon(index: number) {
    if(this.rating >= index + 1) {
      return 'star';
    } else {
      return 'star_border';
    }
  }

  submitReview() {
    this.commissionService.getCommissionById(this.simpleCommission.id)
      .subscribe(
        (commission) => {
          commission.reviewDto = {
            artistDto: commission.artistDto,
            customerDto: commission.customerDto,
            text: this.reviewText.trim(),
            commissionId: commission.id,
            starRating: this.rating
          };

          console.log(commission);

          this.commissionService.updateCommission(commission).subscribe(
            (value) => {
              this.onNoClick();
            }
          );
        }
    );
  }
}
