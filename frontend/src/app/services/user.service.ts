import {Injectable} from '@angular/core';
import {ApplicationUserDto} from '../dtos/applicationUserDto';
import {catchError, Observable} from 'rxjs';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {tap} from 'rxjs/operators';
import {NotificationService} from './notification/notification.service';
import {UserRole} from '../dtos/artistDto';
import {Globals} from '../global/globals';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private headers = new HttpHeaders({
    auth: 'frontend'
  });
  private options = {headers: this.headers};
  private userBaseUI = this.globals.backendUri + '/users';

  constructor(
    private http: HttpClient,
    private globals: Globals,
    private notificationService: NotificationService) {
  }

  createUser(firstName: string, lastName: string, username: string, email: string, address: string, password: string
    , errorAction?: () => void
    , successAction?: () => void): Observable<ApplicationUserDto> {
    const userRole = UserRole.user;
    const user = {
      name: firstName,
      surname: lastName,
      userName: username,
      address,
      email,
      password,
      admin: false,
      userRole
    } as ApplicationUserDto;

    return this.http.post<ApplicationUserDto>(this.userBaseUI, user, this.options)
      .pipe(
        catchError((err) => {
          if (errorAction != null) {
            errorAction();
          }

          return this.notificationService.notifyUserAboutFailedOperation<ApplicationUserDto>('Creating User')(err);
        }),
        tap(_ => {
          if (successAction != null) {
            successAction();
          }

          this.notificationService.displaySuccessSnackbar('Successfully registered.');
        }));
  }

  getAll(): Observable<ApplicationUserDto[]> {
    return this.http.get<ApplicationUserDto[]>(this.userBaseUI, this.options);
  }

  getUserById(id: number, errorAction?: () => void): Observable<ApplicationUserDto> {
    return this.http.get<ApplicationUserDto>(`${this.userBaseUI}/${id}`, this.options)
      .pipe(
        catchError((err) => {
          if (errorAction != null) {
            errorAction();
          }
          return this.notificationService.notifyUserAboutFailedOperation<ApplicationUserDto>('Finding user by id')(err);
        })
      );
  }

  updateUser(user: ApplicationUserDto, errorAction?: () => void, successAction?: () => void): Observable<ApplicationUserDto> {
    return this.http.put<ApplicationUserDto>(
      `${this.userBaseUI}`,
      user,
      this.options
    ).pipe(
      catchError((err) => {
        if (errorAction != null) {
          errorAction();
        }
        return this.notificationService.notifyUserAboutFailedOperation<ApplicationUserDto>('Editing User')(err);
      }),
      tap(_ => {
        this.notificationService.displaySuccessSnackbar(`Successfully updated "${user.userName}"!`);
        if (successAction != null) {
          successAction();
        }
      }));
  }

  searchUser(searchOperations: string, errorAction?: () => void): Observable<ApplicationUserDto[]> {
    const params = new HttpParams()
      .set('searchOperations', searchOperations);

    const searchOptions = {
      headers: this.headers,
      params
    };

    return this.http.get<ApplicationUserDto[]>(this.userBaseUI, searchOptions)
      .pipe(
        catchError((err) => {
          if (errorAction != null) {
            errorAction();
          }

          return this.notificationService
            .notifyUserAboutFailedOperation<ApplicationUserDto[]>('Search user by search operations')(err);
        })
      );
  }

  getUserByEmail(email: string, errorAction?: () => void): Observable<ApplicationUserDto> {
    const params = new HttpParams()
      .set('email', email);

    const searchOptions = {
      headers: this.headers,
      params
    };

    return this.http.get<ApplicationUserDto>(`${this.userBaseUI}/email`, searchOptions)
      .pipe(
        catchError((err) => {
          if (errorAction != null) {
            errorAction();
          }

          return this.notificationService
            .notifyUserAboutFailedOperation<ApplicationUserDto>('Search user by search operations')(err);
        })
      );
  }

  updateUserPassword(id: number, newPassword: string, oldPassword: string): Observable<any> {
    const params = new HttpParams()
      .set('password', newPassword)
      .set('oldPassword', oldPassword);

    const options = {
      header: this.headers,
      params
    };

    return this.http.post(`${this.userBaseUI}/${id}/updatePassword`, null, options)
      .pipe(
        catchError((error) => this.notificationService.notifyUserAboutFailedOperation('Updating user password')(error)),
        tap(_ => {
          this.notificationService.displaySuccessSnackbar('Successfully updated user password.');
        })
      );
  }

  upgradeUser(id: number): Observable<any> {
    const params = new HttpParams()
      .set('id', id);

    const options = {
      header: this.headers,
      params
    };

    return this.http.put(`${this.userBaseUI}/${id}/upgrade`, null, options)
      .pipe(
        catchError((error) => this.notificationService.notifyUserAboutFailedOperation('Upgrading user to artist')(error)),
        tap(_ => {
          this.notificationService.displaySuccessSnackbar('Successfully upgraded user to artist.');
        })
      );
  }

  deleteUserById(id: number): Observable<any> {
    const params = new HttpParams()
      .set('id', id);

    const options = {
      header: this.headers,
      params
    };

    return this.http.delete(`${this.userBaseUI}/${id}`, options)
      .pipe(
        catchError((error) => this.notificationService.notifyUserAboutFailedOperation('Deleting user')(error)),
        tap(_ => {
          this.notificationService.displaySuccessSnackbar('Successfully deleted user.');
        })
      );
  }
}
