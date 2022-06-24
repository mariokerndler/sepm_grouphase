import {Component, Inject, Injectable} from '@angular/core';
import {HttpClient, HttpErrorResponse, HttpHeaders, HttpParams} from '@angular/common/http';
import {catchError, EMPTY, Observable} from 'rxjs';
import {MAT_DIALOG_DATA, MatDialog} from '@angular/material/dialog';
import {MAT_SNACK_BAR_DATA, MatSnackBar} from '@angular/material/snack-bar';
import {Globals} from '../../global/globals';
import {NotificationDto, NotificationType} from '../../dtos/notificationDto';
import {tap} from 'rxjs/operators';

export interface DialogData {
  title: string;
  message: string;
}

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  private readonly snackbarDuration = 2000;
  private headers = new HttpHeaders({
    auth: 'frontend'
  });
  private notificationBaseUri: string = this.globals.backendUri + '/notifications';

  constructor(
    private dialog: MatDialog,
    private snackBar: MatSnackBar,
    private http: HttpClient,
    private globals: Globals) {
  }

  /**
   * Fetch all notifications from a user.
   *
   * @param userId The user id for which the notifications will be fetched.
   * @param type Type of notifications that will be fetched.
   * @param limit A limit on how many notifications will be fetched.
   * @param errorAction Action that triggers after a failed fetch operation.
   * @param successAction Action that takes the notification name as input.
   */
  getNotificationsByUserId(
    userId: number,
    type?: NotificationType,
    limit?: number,
    errorAction?: () => void,
    successAction?: (string) => void): Observable<NotificationDto[]> {
    const params = new HttpParams().set('userId', userId);
    if (type) {
      params.set('notificationType', type.toString());
    }
    if (limit) {
      params.set('limit', limit.valueOf());
    }

    const searchOptions = {
      headers: this.headers,
      params
    };

    return this.http.get<NotificationDto[]>(this.notificationBaseUri, searchOptions)
      .pipe(
        catchError((err) => {
          if (errorAction != null) {
            errorAction();
          }

          return this.notifyUserAboutFailedOperation<NotificationDto[]>
          ('Get notifications by user with id="' + userId + '"')(err);
        }),
        tap((success) => {
          if (successAction != null) {
            success.forEach(x => {
              successAction(x.title);
            });
          }
        })
      );
  }

  /**
   * Fetch all unread notifications from a user.
   *
   * @param userId The user id for which the notifications will be fetched.
   * @param errorAction Action that triggers after a failed fetch operation.
   * @param successAction Action that takes the notification name as input.
   */
  getUnreadNotificationsByUserId(
    userId: number,
    errorAction?: () => void,
    successAction?: (string) => void): Observable<NotificationDto[]> {
    const params = new HttpParams()
      .set('userId', userId);

    const searchOptions = {
      headers: this.headers,
      params
    };

    return this.http.get<NotificationDto[]>(this.notificationBaseUri + '/unread', searchOptions)
      .pipe(
        catchError((err) => {
          if (errorAction != null) {
            errorAction();
          }

          return this.notifyUserAboutFailedOperation<NotificationDto[]>
          ('Get unread notifications by user with id="' + userId + '"')(err);
        }),
        tap((success) => {
          if (successAction != null) {
            success.forEach(x => {
              successAction(x.title);
            });
          }
        })
      );
  }

  /**
   * Patch all notifications with given search parameters.
   *
   * @param userId The user id for which the notifications will be used for search.
   * @param hasRead This field will be set for all the found notifications.
   * @param type Type of notifications that will be used for search.
   * @param errorAction Action that triggers after a failed patch operation.
   * @param successAction Action that takes the notification name as input.
   */
  patchNotificationsIsRead(
    userId: number,
    hasRead: boolean,
    type?: NotificationType,
    errorAction?: () => void,
    successAction?: () => void): Observable<NotificationDto[]> {
    const params = new HttpParams()
      .set('userId', userId);

    if (type) {
      params.set('notificationType', type.toString());
    }

    const searchOptions = {
      headers: this.headers,
      params
    };

    return this.http.patch<NotificationDto[]>(`${this.notificationBaseUri}/${hasRead}?${params.toString()}`, searchOptions)
      .pipe(
        catchError((err) => {
          if (errorAction != null) {
            errorAction();
          }

          return this.notifyUserAboutFailedOperation<NotificationDto[]>
          ('Patching notifications of user with id="' + userId + '"')(err);
        }),
        tap((_) => {
          if (successAction != null) {
            successAction();
          }
        })
      );
  }

  /**
   * Patch notification with given id from a given user id.
   *
   * @param id The id of the notification that will be patched.
   * @param userId The user id that will be used for filtering.
   * @param hasRead The isRead field that will be patched to this value.
   * @param errorAction Action that triggers after a failed patch operation.
   * @param successAction Action that takes the notification name as input.
   */
  pathNotificationIsReadOfIdFromUser(
    id: number,
    userId: number,
    hasRead: boolean,
    errorAction?: () => void,
    successAction?: (string) => void): Observable<NotificationDto> {
    const params = new HttpParams()
      .set('userId', userId);

    const searchOptions = {
      headers: this.headers,
      params
    };

    return this.http.patch<NotificationDto>(`${this.notificationBaseUri}/${id}/${hasRead}?${params.toString()}`, searchOptions)
      .pipe(
        catchError((err) => {
          if (errorAction != null) {
            errorAction();
          }

          return this.notifyUserAboutFailedOperation<NotificationDto>
          ('Patching notifications with id="' + id + '" of user with id="' + userId + '"')(err);
        }),
        tap((success) => {
          if (successAction != null) {
            successAction(success.title);
          }
        })
      );
  }

  /**
   * Delete a given message
   *
   * @param notification - The message that will be deleted.
   */
  deleteNotification(notification: NotificationDto): Observable<void> {
    const deleteOptions = {headers: this.headers, body: notification};
    return this.http.delete<void>(this.notificationBaseUri, deleteOptions)
      .pipe(
        catchError((err) =>
          this.notifyUserAboutFailedOperation<void>('Could not delete notification with id="' + notification.id + '"')(err))
      );
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

  /**
   * Display an error snackbar with a given message.
   *
   * @param message - The message title.
   */
  public displayErrorSnackbar(message: string) {
    this.snackBar.openFromComponent(ErrorSnackbarComponent, {
      data: message,
      duration: this.snackbarDuration
    });
  }

  /**
   * Display an error snackbar with a given message.
   *
   * @param message - The message title.
   */
  public displaySuccessSnackbar(message: string) {
    this.snackBar.openFromComponent(SuccessSnackbarComponent, {
      data: message,
      duration: this.snackbarDuration,
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
