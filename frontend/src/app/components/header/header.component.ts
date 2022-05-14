import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../services/auth.service';
import {LoginComponent} from '../login/login.component';
import {MatDialog} from '@angular/material/dialog';



@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  constructor(public authService: AuthService, public dialog: MatDialog){}

  ngOnInit() {
  }

  openDialog() {
    this.dialog.open(LoginComponent);
  }
}
