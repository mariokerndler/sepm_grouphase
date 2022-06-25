import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class Globals {
  readonly backendUri: string = Globals.findBackendUrl();
  readonly projectName: string = 'Artsperience';
  readonly assetsPath: string = 'assets/';
  readonly defaultProfilePicture: string = 'data/default_pfp.png';
  readonly maxStarRating: number = 5;
  readonly defaultStarRating: number = 3;
  readonly maxCommissionPrice: number = 10000;
  readonly adminId: number = 1;

  private static findBackendUrl(): string {
    if (window.location.port === '4200') { // local `ng serve`, backend at localhost:8080
      return 'http://localhost:8080/api/v1';
    } else {
      // assume deployed somewhere and backend is available at same host/port as frontend
      return window.location.protocol + '//' + window.location.host + window.location.pathname + 'api/v1';
    }
  }
}

