import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../services/auth.service';
import {Globals} from '../../global/globals';
import {MatDialog} from '@angular/material/dialog';
import {LoginComponent} from '../login/login.component';
import {RegistrationComponent} from '../registration/registration.component';


@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  constructor(
    public authService: AuthService,
    public globals: Globals,
    public dialog: MatDialog) {}

  ngOnInit() {
  }

  openDialog(isLogin: boolean) {
    if(isLogin) {
      this.dialog.open(LoginComponent);
    } else {
      this.dialog.open(RegistrationComponent);
    }
  }
}
