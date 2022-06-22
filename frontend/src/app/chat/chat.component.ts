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
  public messages: any[] = [];
  oldMessages = [];

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
    this.chatService.chatListWrapper(this.userId).subscribe(p => {
      p.forEach(part => {
        this.chatService.chatHistoryMapper(this.userId, part.participant.id).subscribe(hist => {
          this.oldMessages.push(hist);
        });
      });
    });
    const runAsync = () => {
      this.adapter.onFriendsListChanged(this.userId);
      setTimeout(() => {
        const v = runAsync();
        this.chatService.chatListWrapper(this.userId).subscribe(p => {
          const newMessages = [];
          p.forEach(part => {
            this.chatService.chatHistoryMapper(this.userId, part.participant.id).subscribe(hist => {

              newMessages.push(hist);
              if (hist.length > this.oldMessages[p.indexOf(part)].length) {
                if (hist[hist.length - 1].fromId !== Number.parseInt(this.userId, 10)) {
                  this.adapter.onMessageReceived(p.filter(c => c.participant.id === hist[hist.length - 1].fromId)[0].participant,
                    hist[hist.length - 1]);
                }
              }
              this.oldMessages[p.indexOf(part)] = hist;
            });
          });
        });
      }, 1500);
      return;
    };
    runAsync();
  }


}
