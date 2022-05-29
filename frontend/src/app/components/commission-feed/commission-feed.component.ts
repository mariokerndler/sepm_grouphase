import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-commission-feed',
  templateUrl: './commission-feed.component.html',
  styleUrls: ['./commission-feed.component.scss']
})
export class CommissionFeedComponent implements OnInit {
  userProfilePicture = 'https://picsum.photos/150/150';
  constructor() { }

  ngOnInit(): void {
  }

}
