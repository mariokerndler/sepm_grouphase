import {Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {NotificationDto} from '../../../dtos/notificationDto';
import {ReportType} from '../../../services/report/report.service';
import {GlobalFunctions} from '../../../global/globalFunctions';
import {Router} from '@angular/router';
import {NotificationService} from '../../../services/notification/notification.service';
import {UserService} from '../../../services/user.service';
import {ArtworkService} from '../../../services/artwork.service';
import {ArtistService} from '../../../services/artist.service';
import {CommissionService} from '../../../services/commission.service';
import {ArtworkDto} from '../../../dtos/artworkDto';
import {DatePipe} from '@angular/common';
import {CommissionDto} from '../../../dtos/commissionDto';

@Component({
  selector: 'app-report-panel',
  templateUrl: './report-panel.component.html',
  styleUrls: ['./report-panel.component.scss'],
  providers: [DatePipe]
})
export class ReportPanelComponent implements OnInit {

  @Input() report: NotificationDto;
  @Output() deleteEmitter = new EventEmitter();
  fromId: number;
  fromUserName: string;
  reportText: string;
  fromReportType: ReportType;
  finished = false;

  selectedArtwork;
  artworks: ArtworkDto[] = [];
  hasArtworks = false;

  commission: CommissionDto;

  constructor(
    public globalFunctions: GlobalFunctions,
    private router: Router,
    private notificationService: NotificationService,
    private userService: UserService,
    private artworkService: ArtworkService,
    private artistService: ArtistService,
    private commissionService: CommissionService,
    private datePipe: DatePipe
  ) {
  }

  ngOnInit(): void {
    this.selectedArtwork = null;
    this.fillReportFields();

    if (this.isArtworkType()) {
      this.artworkService.findArtworkById(this.report.referenceId).subscribe(
        (artwork) => {
          this.artworks.push(artwork);
          this.hasArtworks = this.artworks.length > 0;
        }
      );
    }
  }

  performAction() {
    switch (this.fromReportType) {
      case ReportType.artist:
        this.artistService.deleteArtistrById(this.report.referenceId).subscribe(
          (_) => {
            this.finished = true;
            this.deleteNotification();
          }
        );
        break;
      case ReportType.artwork:
        this.artworkService.deleteArtworkById(this.report.referenceId).subscribe(
          (_) => {
            this.finished = true;
            this.deleteNotification();
          }
        );
        break;
      case ReportType.commission:
        this.commissionService.getCommissionById(this.report.referenceId).subscribe(
          (commission) => {
            this.commissionService.deleteCommission(commission).subscribe();
            this.finished = true;
            this.commission = commission;
            this.deleteNotification();
          }
        );
        break;
      case ReportType.user:
        this.userService.deleteUserById(this.report.referenceId).subscribe(
          (_) => {
            this.finished = true;
            this.deleteNotification();
          }
        );
        break;
    }
  }

  getActionText(): string {
    switch (this.fromReportType) {
      case ReportType.artist:
        return 'Delete artist';
      case ReportType.artwork:
        return 'Delete artwork';
      case ReportType.commission:
        return 'Delete commission';
      case ReportType.user:
        return 'Delete user';
    }
  }

  getReferenceText() {
    switch (this.fromReportType) {
      case ReportType.artist:
        return 'Go to artist';
      case ReportType.artwork:
        return 'Show artwork';
      case ReportType.commission:
        return 'Go to commission';
      case ReportType.user:
        return 'Go to user';
    }
  }

  getReferenceAction() {
    switch (this.fromReportType) {
      case ReportType.artist:
        this.router.navigate(['/artist', this.report.referenceId])
          .catch(_ => this.notificationService.displayErrorSnackbar('Could not navigate to artist.'));
        break;
      case ReportType.artwork:
        break;
      case ReportType.commission:
        this.router.navigate(['/commissions', this.report.referenceId])
          .catch(_ => this.notificationService.displayErrorSnackbar('Could not navigate to commission.'));
        break;
      case ReportType.user:
        this.router.navigate(['/user', this.report.referenceId])
          .catch(_ => this.notificationService.displayErrorSnackbar('Could not navigate to user.'));
        break;
    }
  }

  deleteNotification() {
    this.notificationService.deleteNotification(this.report).subscribe(
      (_) => {
        this.deleteEmitter.emit(true);
        this.writeNotificationToDeletedUser();
      }
    );
  }

  isArtworkType() {
    return this.fromReportType === ReportType.artwork;
  }

  private fillReportFields() {
    const split = this.report.title.split('$$');
    this.fromId = Number(split[0]);
    this.fromUserName = String(split[1]);
    this.reportText = String(split[2]);
    this.fromReportType = ReportType[split[3].toLowerCase()];
  }

  private writeNotificationToDeletedUser() {
    let userId: number;
    let title: string;

    if (this.fromReportType === ReportType.artwork) {
      userId = this.artworks[0].artistId;
      title = 'Your artwork has been deleted, because of report from another user.';
    } else if (this.fromReportType === ReportType.commission) {
      userId = this.commission.customerDto.id;
      title = 'Your commission has been deleted, because of report from another user.';
    }

    this.notificationService.createNotification(
      {
        createdAt: this.datePipe.transform(new Date(), 'yyyy-MM-dd HH:mm:ss'),
        read: false,
        referenceId: 0,
        title,
        type: this.report.type,
        userId
      }
    ).subscribe();
  }
}
