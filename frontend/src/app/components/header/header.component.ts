import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../services/auth.service';
import {Globals} from '../../global/globals';
import {MatDialog} from '@angular/material/dialog';
import {LoginComponent} from '../login/login.component';
import {RegistrationComponent} from '../registration/registration.component';
import {UserService} from '../../services/user.service';
import {ApplicationUserDto} from '../../dtos/applicationUserDto';


@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  isReady = false;
  user: ApplicationUserDto;

  constructor(
    public authService: AuthService,
    public globals: Globals,
    public dialog: MatDialog,
    private userService: UserService) {
  }

  ngOnInit() {
    if (this.authService.isLoggedIn()) {
      const email: string = this.authService.getUserAuthEmail();
      /*
      this.userService.getUserByEmail(email)
        .subscribe((user) => {
            this.user = user;
            this.isReady = true;
          }
        );
       */
      this.isReady = true;
    } else {
      this.isReady = true;
    }
  }

  openDialog(isLogin: boolean) {
    if (isLogin) {
      this.dialog.open(LoginComponent);
    } else {
      this.dialog.open(RegistrationComponent);
    }
  }
}
