import {ChatAdapter, IChatParticipant, Message, ParticipantResponse} from 'ng-chat';
import {forkJoin, Observable, of, switchMap} from 'rxjs';
import {ChatService} from '../services/chat-service';

export class Adapter implements ChatAdapter {


  constructor(private chatService: ChatService, private userId: string) {

  }

  friendsListChangedHandler(participantsResponse: ParticipantResponse[]): void {

  }

  messageReceivedHandler(participant: IChatParticipant, message: Message): void {
  }

  getMessageHistory(destinataryId: any): Observable<Message[]> {
    return this.chatService.chatHistoryMapper(this.userId,destinataryId);
  }

  listFriends(): Observable<ParticipantResponse[]> {
      return this.chatService.chatListWrapper(this.userId);
  }

  onFriendsListChanged(participantsResponse: ParticipantResponse[]): void {
  }

  onMessageReceived(participant: IChatParticipant, message: Message): void {
  }

  sendMessage(message: Message): void {
  }
}
