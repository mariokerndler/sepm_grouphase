import {Component, Input, OnInit} from '@angular/core';
import {UserService} from '../../../services/user.service';
import {ApplicationUserDto} from '../../../dtos/applicationUserDto';
import {GlobalFunctions} from '../../../global/globalFunctions';
import {Globals} from '../../../global/globals';
import {MatDialog} from '@angular/material/dialog';
import {ReviewDialogComponent} from '../../artist-page/review-dialog/review-dialog.component';
import {AuthService} from '../../../services/auth.service';
import {CommissionDto} from '../../../dtos/commissionDto';
import {CommissionStatus} from '../../../global/CommissionStatus';

@Component({
  selector: 'app-commission-card',
  templateUrl: './commission-card.component.html',
  styleUrls: ['./commission-card.component.scss']
})
export class CommissionCardComponent implements OnInit {
  @Input() commission: CommissionDto;
  profilePicture;
  user: ApplicationUserDto;

  constructor(
    private userService: UserService,
    private auth: AuthService,
    public globalFunctions: GlobalFunctions,
    public globals: Globals,
    public dialog: MatDialog) {
  }

  ngOnInit(): void {
    this.user = this.commission.customerDto;
    this.setProfilePicture();
  }

  canWriteReview(): boolean {
    return (this.auth.getUserId() === this.commission.customerDto.id)
      && this.commission.status === CommissionStatus.completed
      && this.commission.reviewDto == null;
  }

  openReviewDialog() {
    const dialogRef = this.dialog.open(ReviewDialogComponent, {
      width: '750px',
      data: {
        commission: this.commission
      }
    });
  }

  private setProfilePicture() {
    if (!this.user.profilePictureDto) {
      this.profilePicture = this.globals.defaultProfilePicture;
    } else {
      this.profilePicture = this.user.profilePictureDto.imageUrl;
    }
  }
}
