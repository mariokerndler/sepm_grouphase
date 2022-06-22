import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {catchError, mergeMap, Observable, of} from 'rxjs';
import {NotificationService} from './notification/notification.service';
import {Globals} from '../global/globals';
import {ApplicationUserDto} from '../dtos/applicationUserDto';
import {ChatParticipantStatus, ParticipantResponse} from 'ng-chat';
import {ChatMessageDto} from '../dtos/chat-message-dto';
import {tap} from 'rxjs/operators';
import {ChatDto} from '../dtos/chatDto';
import {UserService} from './user.service';

@Injectable({
  providedIn: 'root'
})
export class ChatService {
  private chatsBaseUri: string = this.globals.backendUri + '/chats';
  private headers = new HttpHeaders({
    auth: 'frontend'
  });
  private options = {
    headers: this.headers,
  };

  constructor(private http: HttpClient,
              private globals: Globals,
              private notificationService: NotificationService, public  userService: UserService,) {
  }




  chatListWrapper(id: string, errorAction?: () => void): Observable<ParticipantResponse[]> {

    return this.http.get<ApplicationUserDto[]>(`${this.chatsBaseUri}/${id}`, this.options)
      .pipe(
        mergeMap(success => of(success.map(user => {
          const participantResponse = new ParticipantResponse();
          user.displayName = user.userName;
          user.status = ChatParticipantStatus.Online;
          participantResponse.participant = user;
          return participantResponse;
        })))
        , catchError((err) => {
          if (errorAction != null) {
            errorAction();
          }
          return this.notificationService.notifyUserAboutFailedOperation<ParticipantResponse[]>('Finding chat list')(err);
        })
      );
  }

  chatHistoryMapper(userId: string, participantId: string): Observable<ChatMessageDto[]> {
    const params = new HttpParams()
      .set('userId', userId)
      .set('participantId', participantId);
    const searchOptions = {
      headers: this.headers,
      params
    };
    return this.http.get<ChatMessageDto[]>(`${this.chatsBaseUri}` + '/history', searchOptions);
  }

  getChatList(id: string, errorAction?: () => void): Observable<ApplicationUserDto[]> {
    return this.http.get<ApplicationUserDto[]>(`${this.chatsBaseUri}/${id}`, this.options)
      .pipe(
        catchError((err) => {
          if (errorAction != null) {
            errorAction();
          }
          return this.notificationService.notifyUserAboutFailedOperation<ApplicationUserDto[]>('Finding chat list')(err);
        })
      );
  }

  postChatMessage(message: ChatMessageDto, errorAction?: () => void, successAction?: () => void): Observable<ChatMessageDto> {
    return this.http.post<ChatMessageDto>(this.chatsBaseUri, message, this.options)
      .pipe(
        catchError((err) => {
          if (errorAction != null) {
            errorAction();
          }
          return this.notificationService.notifyUserAboutFailedOperation<ChatMessageDto>('Error sending Message')(err);
        }),
        tap(_ => {
          if (successAction != null) {
            successAction();
          }
        }));
  }

  postChat(message: ChatDto, errorAction?: () => void, successAction?: () => void): Observable<ChatDto> {
    return this.http.post<ChatDto>(this.chatsBaseUri + '/chat', message, this.options)
      .pipe(
        tap(_ => {
          if (successAction != null) {
            successAction();
          }
        }));
  }
}
