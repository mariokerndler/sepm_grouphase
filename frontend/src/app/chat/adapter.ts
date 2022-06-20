import {ChatAdapter, IChatParticipant, Message, ParticipantResponse} from 'ng-chat';
import {Observable} from 'rxjs';
import {ChatService} from '../services/chat-service';
import {ChatMessageDto} from '../dtos/chat-message-dto';

export class Adapter implements ChatAdapter {


  constructor(private chatService: ChatService, private userId: string) {

  }

  friendsListChangedHandler(participantsResponse: ParticipantResponse[]): void {

  }

  messageReceivedHandler(participant: IChatParticipant, message: Message): void {
  }

  getMessageHistory(destinataryId: any): Observable<Message[]> {
    return this.chatService.chatHistoryMapper(this.userId, destinataryId);
  }

  listFriends(): Observable<ParticipantResponse[]> {
    return this.chatService.chatListWrapper(this.userId);
  }

  onFriendsListChanged(participantsResponse: ParticipantResponse[]): void {
    this.listFriends();
  }

  onMessageReceived(participant: IChatParticipant, message: Message): void {
  }

  sendMessage(message: Message): void {
    const m: ChatMessageDto = {
      dateSent: message.dateSeen, fromId: message.fromId, message: message.message, toId: message.toId
    };
    this.chatService.postChatMessage(m).subscribe();

  }
}
