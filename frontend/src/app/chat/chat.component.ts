import {Component, OnInit} from '@angular/core';
import {ChatAdapter} from 'ng-chat';
import {Adapter} from './adapter';

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.scss']
})


export class ChatComponent implements OnInit {

  public userId;
  public adapter: ChatAdapter = new Adapter();

  constructor() {
  }

  getUserId(): void {
    const id = localStorage.getItem('userId');
    if (id !== null) {
      this.userId = id;
    }
  }

  ngOnInit(): void {
    this.getUserId();
  }

}
