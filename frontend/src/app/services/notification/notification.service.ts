import {Component, Inject, Injectable} from '@angular/core';
import {HttpErrorResponse} from '@angular/common/http';
import {EMPTY, Observable} from 'rxjs';
import {MAT_DIALOG_DATA, MatDialog} from '@angular/material/dialog';
import {MAT_SNACK_BAR_DATA, MatSnackBar} from '@angular/material/snack-bar';

export interface DialogData {
  title: string;
  message: string;
}

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  constructor(
    private dialog: MatDialog,
    private snackBar: MatSnackBar) {
  }

  /**
   * Displays a given message and title.
   *
   * @param message - The message to display.
   * @param title   - The title of the message to display.
   */
  public displaySimpleDialog(title: string, message: string) {
    this.openDialog(title, message);
  }

  public displayErrorSnackbar(message: string) {
    this.snackBar.openFromComponent(ErrorSnackbarComponent, {
      data: message,
      //duration: 500
    });
  }

  public displaySuccessSnackbar(message: string) {
    this.snackBar.openFromComponent(SuccessSnackbarComponent, {
      data: message,
      //duration: 2000,
    });
  }

  /**
   * Displays an error for the user on a failed http operation and returns {@link EMPTY}
   * to possibly let the app continue.
   *
   * @param operation - The name of the operation that failed. Will be displayed as "<operation> failed".
   */
  public notifyUserAboutFailedOperation<T>(operation = 'operation') {
    return (error: HttpErrorResponse): Observable<T> => {
      console.log(error);
      const message = error.error.message || JSON.stringify(error.error).replace(/[{}]/g, '');

      this.openDialog(`${operation} failed`, message);

      return EMPTY;
    };
  }

  private openDialog(title: string, message: string) {
    this.dialog.open(SimpleDialogComponent, {
      data: {
        title,
        message
      }
    });
  }
}

@Component({
  selector: 'app-simple-dialog',
  templateUrl: 'notification-templates/simple-dialog-template.html'
})
export class SimpleDialogComponent {
  constructor(@Inject(MAT_DIALOG_DATA) public data: DialogData) {
  }
}

@Component({
  selector: 'app-error-snackbar',
  templateUrl: 'notification-templates/snackbar-notification-template.html',
  styles: [
    `
      .notification {
        color: #cb212b;
      }
    `
  ]
})
export class ErrorSnackbarComponent {
  constructor(@Inject(MAT_SNACK_BAR_DATA) public data: string) {
  }
}

@Component({
  selector: 'app-success-snackbar',
  templateUrl: 'notification-templates/snackbar-notification-template.html',
  styles: [
    `
      .notification {
        color: white;
      }
    `
  ]
})
export class SuccessSnackbarComponent {
  constructor(@Inject(MAT_SNACK_BAR_DATA) public data: string) {
  }
}
