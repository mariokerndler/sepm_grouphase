import { Injectable } from '@angular/core';
import {User} from '../dtos/user';
import {Observable} from 'rxjs';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  constructor( private http: HttpClient,) {}

  createUser(firstName: string, lastName: string, username: string, email: string, password: string): Observable<User> {
      const user = {firstName, lastName, username, email, password} as User;
      console.log(user);
     // return this.http.post(baseUri, user);
      return null;
  }

  //Todo get Users by email and username to check if they already exist

}
