import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class Globals {
  readonly backendUri: string = Globals.findBackendUrl();
  readonly projectName: string = 'Artsperience';
  readonly assetsPath: string = 'assets/';
  readonly defaultProfilePicture: string = 'data/default_pfp.png';
  // eslint-disable-next-line max-len
  readonly stripePublicKey: string = 'pk_test_51LCfpACibvYbUKMDIsb5pccCixWRenzwqfMgtEyNv1byXCbxAAsVSMuSXEmT1K5PFqAUOXnOdYdFB0jLYrinTokR00bQLEUqUS';

  private static findBackendUrl(): string {
    if (window.location.port === '4200') { // local `ng serve`, backend at localhost:8080
      return 'http://localhost:8080/api/v1';
    } else {
      // assume deployed somewhere and backend is available at same host/port as frontend
      return window.location.protocol + '//' + window.location.host + window.location.pathname + 'api/v1';
    }
  }
}

