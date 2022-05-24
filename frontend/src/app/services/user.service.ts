import { Injectable } from '@angular/core';
import {User, UserRole} from '../dtos/user';
import {catchError, Observable} from 'rxjs';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {tap} from 'rxjs/operators';
import {NotificationService} from './notification/notification.service';

const backendUrl = 'http://localhost:8080';
const baseUri = backendUrl + '/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  headers = new HttpHeaders({
    auth: 'Registration' });
  options = { headers: this.headers};


  constructor(
    private http: HttpClient,
    private notificationService: NotificationService) {}

  createUser(firstName: string, lastName: string, username: string, email: string, address: string, password: string
             , errorAction?: () => void
             , successAction?: () => void): Observable<User> {
      const userRole = UserRole.user;
      const user = {name: firstName, surname: lastName, userName: username, address, email, password, admin: false, userRole} as User;
      console.log(user);
      return this.http.post<User>(baseUri, user, this.options)
        .pipe(
          catchError((err) => {
            if(errorAction != null) {
              errorAction();
            }

            return this.notificationService.notifyUserAboutFailedOperation<User>('Creating User')(err);
          }),
          tap(_ => {
            if(successAction != null) {
              successAction();
            }
          }));
  }
}
