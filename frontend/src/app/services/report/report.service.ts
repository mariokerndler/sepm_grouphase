import {Injectable} from '@angular/core';
import {MatDialog} from '@angular/material/dialog';
import {ReportDialogComponent} from './report-dialog/report-dialog.component';

export interface ReportDialogData {
  referenceId: number;
  reportType: ReportType;
}

export enum ReportType {
  artwork = 'Artwork',
  user = 'User',
  artist = 'Artist',
  commission = 'Commission'
}

@Injectable({
  providedIn: 'root'
})
export class ReportService {

  constructor(
    private dialog: MatDialog,
  ) {
  }

  public openReportDialog(referenceId: number, reportType: ReportType) {
    this.dialog.open(ReportDialogComponent, {
      width: '750px',
      data: {
        referenceId,
        reportType
      }
    });
  }
}
