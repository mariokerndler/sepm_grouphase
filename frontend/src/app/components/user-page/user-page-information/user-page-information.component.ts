import {Component, Input, OnInit} from '@angular/core';
import {ApplicationUserDto} from '../../../dtos/applicationUserDto';

@Component({
  selector: 'app-user-information',
  templateUrl: './user-page-information.component.html',
  styleUrls: ['../../artist-page/artist-information/artist-information.component.scss']
})
export class UserPageInformationComponent implements OnInit {

  @Input() user: ApplicationUserDto;

  constructor() { }

  ngOnInit(): void {
  }

}
