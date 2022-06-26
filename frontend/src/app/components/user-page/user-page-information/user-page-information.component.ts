import {Component, Input, OnInit} from '@angular/core';
import {ApplicationUserDto} from '../../../dtos/applicationUserDto';
import {ChatDto} from '../../../dtos/chatDto';
import {ChatService} from '../../../services/chat-service';
import {Router} from '@angular/router';

@Component({
  selector: 'app-user-information',
  templateUrl: './user-page-information.component.html',
  styleUrls: ['../../artist-page/artist-information/artist-information.component.scss']
})
export class UserPageInformationComponent implements OnInit {

  @Input() user: ApplicationUserDto;
  loggedInUserId;

  constructor(
    private chatService: ChatService,
    private router: Router
  ) {
  }

  ngOnInit(): void {
    this.loggedInUserId= Number.parseInt(localStorage.getItem('userId'), 10);
  }

  messageUser() {
    const chat: ChatDto = {
      chatPartnerId: this.user.id, userId: this.loggedInUserId
    };
    console.log(chat);
    this.chatService.postChat(chat).subscribe(success => {
      this.router.navigate(['/chat/' + this.loggedInUserId]);
    }, error => {
      this.router.navigate(['/chat/' + this.loggedInUserId]);
    });
  }

}
