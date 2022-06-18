import {Component, OnInit} from '@angular/core';
import {ChatAdapter} from 'ng-chat';
import {Adapter} from './adapter';
import {ChatService} from '../services/chat-service';

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.scss']
})


export class ChatComponent implements OnInit {

  public userId;
  public adapter: ChatAdapter;

  constructor(private chatService: ChatService) {

  }

  getUserId(): void {
    const id = localStorage.getItem('userId');
    if (id !== null) {
      this.userId = id;
    }
  }

  ngOnInit(): void {
    this.getUserId();
    this.adapter = new Adapter(this.chatService, this.userId);
  }

}
