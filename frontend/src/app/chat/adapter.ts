import {ChatAdapter,  Message, ParticipantResponse} from 'ng-chat';
import {Observable} from 'rxjs';
import {ChatService} from '../services/chat-service';
import {ChatMessageDto} from '../dtos/chat-message-dto';

export class Adapter extends ChatAdapter {


  constructor(private chatService: ChatService, private userId: string) {
    super();
  }
  getMessageHistory(destinataryId: any): Observable<Message[]> {
    console.log(destinataryId);
    return this.chatService.chatHistoryMapper(this.userId, destinataryId,);
  }

  listFriends(): Observable<ParticipantResponse[]> {
    return this.chatService.chatListWrapper(this.userId);
  }

  onFriendsListChanged(participantsResponse: ParticipantResponse[]): void {
    this.listFriends();
  }

  sendMessage(message: Message): void {
    const m: ChatMessageDto = {
      dateSent: message.dateSeen, fromId: message.fromId, message: message.message, toId: message.toId
    };
    this.chatService.userService.getUserById( message.toId).subscribe(user=>{
      this.chatService.postChatMessage(m).subscribe();
    });
  }
}
