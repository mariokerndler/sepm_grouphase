import {BrowserModule} from '@angular/platform-browser';
import {NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {HttpClientModule} from '@angular/common/http';
import {NgbModule} from '@ng-bootstrap/ng-bootstrap';
import {httpInterceptorProviders} from './interceptors';

// Plugins
import { AngularMaterialModule} from './angular-material/angular-material.module';
import { FlexLayoutModule} from '@angular/flex-layout';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

// Routing
import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';

// Components
import {HeaderComponent} from './components/header/header.component';
import {FooterComponent} from './components/footer/footer.component';
import {HomeComponent} from './components/home/home.component';
import {LoginComponent} from './components/login/login.component';
import {MessageComponent} from './components/message/message.component';
import { MidsectionComponent } from './components/midsection/midsection/midsection.component';
import { CardViewComponent } from './components/midsection/card-view/card-view.component';
import { ArtistPageComponent } from './components/artist-page/artist-page/artist-page.component';
import { ArtistInformationComponent } from './components/artist-page/artist-information/artist-information.component';
import {SimpleDialogComponent} from './common/service/notification.service';

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
    SimpleDialogComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    ReactiveFormsModule,
    HttpClientModule,
    NgbModule,
    FormsModule,
    BrowserAnimationsModule,
    AngularMaterialModule,
    FlexLayoutModule
  ],
  providers: [
    httpInterceptorProviders,
  ],
  bootstrap: [AppComponent],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class AppModule {
}
