import {ChatAdapter, IChatParticipant, Message, ParticipantResponse} from 'ng-chat';
import {Observable} from "rxjs";

export class Adapter implements ChatAdapter{
  friendsListChangedHandler(participantsResponse: ParticipantResponse[]): void {
  }

  messageReceivedHandler(participant: IChatParticipant, message: Message): void {
  }

  getMessageHistory(destinataryId: any): Observable<Message[]> {
    return undefined;
  }

  listFriends(): Observable<ParticipantResponse[]> {
    return undefined;
  }

  onFriendsListChanged(participantsResponse: ParticipantResponse[]): void {
  }

  onMessageReceived(participant: IChatParticipant, message: Message): void {
  }

  sendMessage(message: Message): void {
  }
}
