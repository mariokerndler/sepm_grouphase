import {Injectable} from '@angular/core';
import {ApplicationUserDto} from '../dtos/applicationUserDto';
import {catchError, Observable} from 'rxjs';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {tap} from 'rxjs/operators';
import {NotificationService} from './notification/notification.service';
import {UserRole} from '../dtos/artistDto';

const backendUrl = 'http://localhost:8080';
const baseUri = backendUrl + '/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  headers = new HttpHeaders({
    auth: 'frontend'
  });
  options = {headers: this.headers};


  constructor(
    private http: HttpClient,
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

    return this.http.post<ApplicationUserDto>(baseUri, user, this.options)
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
        }));
  }

  getAll(): Observable<ApplicationUserDto[]> {
    return this.http.get<ApplicationUserDto[]>(baseUri, this.options);
  }

  getUserById(id: number, errorAction?: () => void): Observable<ApplicationUserDto> {
    return this.http.get<ApplicationUserDto>(`${baseUri}/${id}`, this.options)
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
      `${baseUri}`,
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

    return this.http.get<ApplicationUserDto[]>(baseUri, searchOptions)
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

    return this.http.get<ApplicationUserDto>(`${baseUri}/email`, searchOptions)
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
}
