import { Injectable } from '@angular/core';
import {MatDialog} from '@angular/material/dialog';
import {ReportDialogComponent} from './report-dialog/report-dialog.component';
import {ApplicationUserDto} from '../../dtos/applicationUserDto';

export interface ReportDialogData {
  referenceId: number;
}

@Injectable({
  providedIn: 'root'
})
export class ReportService {

  constructor(
    private dialog: MatDialog,
  ) { }

  public openReportDialog(referenceId: number) {


    this.dialog.open(ReportDialogComponent, {
      width: '750px',
      data: {
        referenceId
      }
    });
  }
}
