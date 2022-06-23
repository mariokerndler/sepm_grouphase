import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {Globals} from '../../../global/globals';
import {CommissionService} from '../../../services/commission.service';
import {SimpleCommissionDto} from '../../../dtos/simpleCommissionDto';

@Component({
  selector: 'app-review-dialog',
  templateUrl: './review-dialog.component.html',
  styleUrls: ['./review-dialog.component.scss']
})
export class ReviewDialogComponent{

  rating = 0;

  reviewText = '';
  private simpleCommission: SimpleCommissionDto;

  constructor(
    public dialogRef: MatDialogRef<ReviewDialogComponent>,
    public globals: Globals,
    private commissionService: CommissionService,
    @Inject(MAT_DIALOG_DATA) public data: any
  ) {
    this.simpleCommission = data.commission;
  }

  updateRating(n: number) {
    this.rating = n;
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  isValidReviewText(): boolean {
    return this.reviewText.trim().length > 0;
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
