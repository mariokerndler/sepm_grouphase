import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {MessageDto} from '../dtos/messageDto';
import {Observable} from 'rxjs';
import {Globals} from '../global/globals';

@Injectable({
  providedIn: 'root'
})
export class MessageService {

  private messageBaseUri: string = this.globals.backendUri + '/messages';

  constructor(private httpClient: HttpClient, private globals: Globals) {
  }

  /**
   * Loads all messages from the backend
   */
  getMessage(): Observable<MessageDto[]> {
    return this.httpClient.get<MessageDto[]>(this.messageBaseUri);
  }

  /**
   * Loads specific message from the backend
   *
   * @param id of message to load
   */
  getMessageById(id: number): Observable<MessageDto> {
    console.log('Load message details for ' + id);
    return this.httpClient.get<MessageDto>(this.messageBaseUri + '/' + id);
  }

  /**
   * Persists message to the backend
   *
   * @param message to persist
   */
  createMessage(message: MessageDto): Observable<MessageDto> {
    console.log('Create message with title ' + message.title);
    return this.httpClient.post<MessageDto>(this.messageBaseUri, message);
  }
}
