import { Injectable } from '@angular/core';
import {User, UserRole} from '../dtos/user';
import {catchError, Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';
import {NotificationService} from '../common/service/notification.service';
import {tap} from 'rxjs/operators';

const backendUrl = 'http://localhost:4200';
const baseUri = backendUrl + '/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {



  constructor(
    private http: HttpClient,
    private notificationService: NotificationService) {}

  createUser(firstName: string, lastName: string, username: string, email: string, password: string
             , errorAction?: () => void
             , successAction?: () => void): Observable<User> {
      const userRole = UserRole.user;
      const user = {firstName, lastName, username, email, password, admin: false, userRole} as User;
      console.log(user);
      return this.http.post<User>(baseUri, user)
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
  //Todo get Users by email and username to check if they already exist
}
