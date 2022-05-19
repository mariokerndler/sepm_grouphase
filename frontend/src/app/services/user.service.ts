import { Injectable } from '@angular/core';
import {User, UserRole} from '../dtos/user';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';

const backendUrl = 'http://localhost:8080';
const baseUri = backendUrl + '/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {



  constructor( private http: HttpClient,) {}

  createUser(firstName: string, lastName: string, username: string, email: string, password: string): Observable<User> {
      const userRole = UserRole.user;
      const user = {name: firstName, surname: lastName, userName: username, email, password, admin: false, userRole} as User;
      console.log(user);
      return this.http.post<User>(baseUri, user);
  }
}
