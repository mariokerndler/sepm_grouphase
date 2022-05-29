import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-commission-card',
  templateUrl: './commission-card.component.html',
  styleUrls: ['./commission-card.component.scss']
})
export class CommissionCardComponent implements OnInit {
  userProfilePicture = 'https://picsum.photos/150/150';
  constructor() { }

  ngOnInit(): void {
  }

}
