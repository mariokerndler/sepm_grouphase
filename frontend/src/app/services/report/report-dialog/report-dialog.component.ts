import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {NotificationService} from '../../notification/notification.service';
import {ApplicationUserDto} from '../../../dtos/applicationUserDto';
import {NotificationDto, NotificationType} from '../../../dtos/notificationDto';
import {DatePipe} from '@angular/common';
import {ReportDialogData} from '../report.service';
import {GlobalFunctions} from '../../../global/globalFunctions';
import {Globals} from '../../../global/globals';
import {AuthService} from '../../auth.service';

@Component({
  selector: 'app-report-dialog',
  templateUrl: './report-dialog.component.html',
  styleUrls: ['./report-dialog.component.scss'],
  providers: [DatePipe]
})
export class ReportDialogComponent {

  reportText = '';
  reportCategories: NotificationType[] = [];
  chosenReportCategory: NotificationType;
  private referenceId: number;

  constructor(
    public dialogRef: MatDialogRef<ReportDialogComponent>,
    public globalFunctions: GlobalFunctions,
    private globals: Globals,
    private notificationService: NotificationService,
    private datePipe: DatePipe,
    private authService: AuthService,
    @Inject(MAT_DIALOG_DATA) public data: ReportDialogData
  ) {
    this.referenceId = data.referenceId;
    this.filterNotificationTypes();
  }

  isValidReportText(): boolean {
    return this.reportText.trim().length > 0;
  }

  submitReport() {
    const dto = this.createNotificationDto();
    console.log(dto);
    this.notificationService.createNotification(dto).subscribe();
  }

  private createNotificationDto(): NotificationDto {
    return {
      createdAt: this.datePipe.transform(new Date(), 'yyyy-MM-dd HH:mm:ss'),
      read: false,
      referenceId: this.referenceId,
      title: this.createReportText(),
      type: this.chosenReportCategory,
      userId: this.globals.adminId
    };
  }

  private createReportText(): string {
    return 'From: "' + this.authService.getUserName() + '": \n' + this.reportText;
  }

  private filterNotificationTypes() {
    const keys = Object.keys(NotificationType);
    keys.forEach(type => {
      if (type.includes('report')) {
        this.reportCategories.push(NotificationType[type]);
      }
    });
  }
}
