import {Injectable} from '@angular/core';
import {catchError, Observable} from 'rxjs';
import {CommissionDto} from '../dtos/commissionDto';
import {HttpClient, HttpHeaders, HttpParams} from '@angular/common/http';
import {NotificationService} from './notification/notification.service';
import {Globals} from '../global/globals';
import {tap} from 'rxjs/operators';
import {SimpleCommissionDto} from '../dtos/simpleCommissionDto';
import {CommissionSearchDto} from "../dtos/commissionSearchDto";
import {ArtworkDto} from "../dtos/artworkDto";





@Injectable({
  providedIn: 'root'
})
export class CommissionService {

  private commissionBaseUri: string = this.globals.backendUri + '/commissions';
  private headers = new HttpHeaders({
    auth: 'frontend'
  });
  private options = {headers: this.headers};

  constructor(private http: HttpClient,
              private notificationService: NotificationService,
              private globals: Globals) {
  }


  /**
   * Fetches all commissions stored in the system
   *
   * @return observable list of found commissions.
   */
  getAllCommissions(): Observable<SimpleCommissionDto[]> {
    return this.http.get<SimpleCommissionDto[]>(this.commissionBaseUri, this.options)
      .pipe(
        catchError(this.notificationService.notifyUserAboutFailedOperation<SimpleCommissionDto[]>('Fetching all commissions'))
      );
  }


  updateCommission(commission: CommissionDto, errorAction?: () => void, successAction?: () => void): Observable<CommissionDto> {
    return this.http.put<CommissionDto>(
      `${this.commissionBaseUri}`,
      commission,
      this.options
    ).pipe(
      catchError((err) => {
        if (errorAction != null) {
          errorAction();
        }
        return this.notificationService.notifyUserAboutFailedOperation<CommissionDto>('Editing Commission')(err);
      }),
      tap(_ => {
        this.notificationService.displaySuccessSnackbar(`Successfully updated "${commission.title}"!`);
        if (successAction != null) {
          successAction();
        }
      }));
  }


  createCommission(commission: CommissionDto, errorAction?: () => void, successAction?: () => void): Observable<CommissionDto> {
    return this.http.post<CommissionDto>(this.commissionBaseUri, commission, this.options)
      .pipe(
        catchError((err) => {
          if (errorAction != null) {
            errorAction();
          }

          return this.notificationService.notifyUserAboutFailedOperation<CommissionDto>('Creating Commission')(err);
        }),
        tap(_ => {
          if (successAction != null) {
            successAction();
          }
        }));
  }


  deleteCommission(commission: CommissionDto): Observable<void> {
    const deleteOptions = {headers: this.headers, body: commission};
    return this.http.delete<void>(`${this.commissionBaseUri}`, deleteOptions);
  }


  getCommissionById(id: number, errorAction?: () => void): Observable<CommissionDto> {
    return this.http.get<CommissionDto>(`${this.commissionBaseUri}/${id}`, this.options)
      .pipe(
        catchError((err) => {
          if (errorAction != null) {
            errorAction();
          }
          return this.notificationService.notifyUserAboutFailedOperation<CommissionDto>('Finding commission by id')(err);
        })
      );
  }


  filterCommissions(searchCom: CommissionSearchDto, errorAction?: () => void): Observable<SimpleCommissionDto[]> {
    const params = new HttpParams()
      .set('priceRange', searchCom.priceRange.toString())
      .set('dateSearch', searchCom.date.toString())
      .set('name', searchCom.name)
      .set('pageNr', searchCom.pageNr == null ? '0' : searchCom.pageNr)
      .set('artistId', searchCom.artistId);
    const searchOptions = {
      headers: this.headers,
      params
    };
    return this.http.get<SimpleCommissionDto[]>(this.commissionBaseUri, searchOptions)
      .pipe(
        catchError((err) => {
          if (errorAction != null) {
            errorAction();
          }
          return this.notificationService.notifyUserAboutFailedOperation<SimpleCommissionDto[]>('Searching Commissions')(err);
        })
      );
  }
}
