import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from '@angular/common/http';
import {catchError, mergeMap, Observable, of} from 'rxjs';
import {NotificationService} from './notification/notification.service';
import {Globals} from '../global/globals';
import {ApplicationUserDto} from '../dtos/applicationUserDto';
import {ChatParticipantStatus, ParticipantResponse} from 'ng-chat';

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
              private notificationService: NotificationService) {
  }
 chatListWrapper(id: string, errorAction?: () => void): Observable<ParticipantResponse[]>{

   return this.http.get<ApplicationUserDto[]>(`${this.chatsBaseUri}/${id}`, this.options)
     .pipe(
       mergeMap(success=>of(success.map(user =>{
           const participantResponse = new ParticipantResponse();
           user.displayName = user.userName;
           user.status= ChatParticipantStatus.Online;
           participantResponse.participant = user;
           return participantResponse;
         })))
       ,catchError((err) => {
         if (errorAction != null) {
           errorAction();
         }
         return this.notificationService.notifyUserAboutFailedOperation<ParticipantResponse[]>('Finding chat list')(err);
       })
     );
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
}
