import {BrowserModule} from '@angular/platform-browser';
import {CUSTOM_ELEMENTS_SCHEMA, NgModule} from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HttpClientModule} from '@angular/common/http';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {httpInterceptorProviders} from './interceptors';
import { NgxMatFileInputModule } from '@angular-material-components/file-input';

// Plugins
import {AngularMaterialModule} from './components/angular-material/angular-material.module';
import {FlexLayoutModule} from '@angular/flex-layout';
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {
  MAT_COLOR_FORMATS,
  NGX_MAT_COLOR_FORMATS,
  NgxMatColorPickerModule
} from '@angular-material-components/color-picker';

// Routing
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';

// Components
import {HeaderComponent} from './components/header/header.component';
import {FooterComponent} from './components/footer/footer.component';
import {HomeComponent} from './components/home/home.component';
import {LoginComponent} from './components/login/login.component';
import {MessageComponent} from './components/message/message.component';
import {MidsectionComponent} from './components/midsection/midsection/midsection.component';
import {CardViewComponent} from './components/midsection/card-view/card-view.component';
import {ArtistPageComponent} from './components/artist-page/artist-page/artist-page.component';
import {ArtistInformationComponent} from './components/artist-page/artist-information/artist-information.component';
import {
  ErrorSnackbarComponent,
  SimpleDialogComponent,
  SuccessSnackbarComponent
} from './services/notification/notification.service';
import {ArtistPageEditComponent} from './components/artist-page/artist-page-edit/artist-page-edit.component';
import {DragDropModule} from '@angular/cdk/drag-drop';
import {RegistrationComponent} from './components/registration/registration.component';
import {UserPageComponent} from './components/user-page/user-page.component';
import {
  UserPageInformationComponent
} from './components/user-page/user-page-information/user-page-information.component';
import {UserPageEditComponent} from './components/user-page/user-page-edit/user-page-edit.component';
import {ImageFeedComponent} from './components/image-feed/image-feed.component';
import {GalleryCarouselComponent} from './components/gallery-carousel/gallery-carousel.component';
import {ArtistFeedComponent} from './components/artist-feed/artist-feed.component';
import {ArtistFeedCardComponent} from './components/artist-feed/artist-feed-card/artist-feed-card.component';
import {LogoutComponent} from './components/logout/logout.component';
import {ArtistGalleryComponent} from './components/artist-page/artist-gallery/artist-gallery.component';
import {UploadComponent} from './components/upload/upload.component';
import {CommissionFeedComponent} from './components/commission/commission-feed/commission-feed.component';
import {CommissionCardComponent} from './components/commission/commission-card/commission-card.component';
import {CommissionDetailsComponent} from './components/commission/commission-details/commission-details.component';
import { CommissionCreationComponent } from './components/commission/commission-creation/commission-creation.component';
import { STEPPER_GLOBAL_OPTIONS } from '@angular/cdk/stepper';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    HomeComponent,
    LoginComponent,
    MessageComponent,
    MidsectionComponent,
    CardViewComponent,
    ArtistPageComponent,
    ArtistInformationComponent,
    SimpleDialogComponent,
    ArtistPageEditComponent,
    ErrorSnackbarComponent,
    SuccessSnackbarComponent,
    ArtistGalleryComponent,
    RegistrationComponent,
    LogoutComponent,
    ImageFeedComponent,
    GalleryCarouselComponent,
    LogoutComponent,
    UserPageComponent,
    UserPageInformationComponent,
    UserPageEditComponent,
    ArtistFeedComponent,
    ArtistFeedCardComponent,
    UploadComponent,
    CommissionFeedComponent,
    CommissionCardComponent,
    CommissionDetailsComponent,
    CommissionCreationComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    NgxMatFileInputModule,
    ReactiveFormsModule,
    HttpClientModule,
    NgbModule,
    FormsModule,
    BrowserAnimationsModule,
    AngularMaterialModule,
    FlexLayoutModule,
    NgxMatColorPickerModule,
    DragDropModule
  ],
  providers: [
    {
      provide: STEPPER_GLOBAL_OPTIONS,
      useValue: { displayDefaultIndicatorType: false }
    },
    httpInterceptorProviders,
    {
      provide: MAT_COLOR_FORMATS,
      useValue: NGX_MAT_COLOR_FORMATS
    }
  ],
  bootstrap: [AppComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AppModule {
}
