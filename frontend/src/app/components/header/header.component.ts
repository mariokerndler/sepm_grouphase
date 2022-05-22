import {Component, OnInit} from '@angular/core';
import {AuthService} from '../../services/auth.service';
import {Globals} from '../../global/globals';


@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  constructor(
    public authService: AuthService,
    public globals: Globals) {}

  ngOnInit() {
  }

}
