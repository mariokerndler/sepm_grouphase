import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {LoginComponent} from './components/login/login.component';
import {AuthGuard} from './guards/auth.guard';
import {ArtistPageComponent} from './components/artist-page/artist-page/artist-page.component';
import {ArtistPageEditComponent} from './components/artist-page/artist-page-edit/artist-page-edit.component';
import {LogoutComponent} from './components/logout/logout.component';
import {ImageFeedComponent} from './components/image-feed/image-feed.component';
import {UserPageComponent} from './components/user-page/user-page.component';
import {UserPageEditComponent} from './components/user-page/user-page-edit/user-page-edit.component';
import {ArtistFeedComponent} from './components/artist-feed/artist-feed.component';
import {CommissionFeedComponent} from './components/commission/commission-feed/commission-feed.component';
import {CommissionDetailsComponent} from './components/commission/commission-details/commission-details.component';
import {CommissionCreationComponent} from './components/commission/commission-creation/commission-creation.component';
import {CommissionPageComponent} from './components/artist-page/commission-page/commission-page.component';
import {
  CommissionTimelineComponent
} from './components/commission/commission-timeline-assets/commission-timeline/commission-timeline.component';
import {ChatComponent} from './chat/chat.component';
import {TermsOfServiceComponent} from './components/footer/terms-of-service/terms-of-service.component';
import {PrivacyPolicyComponent} from './components/footer/privacy-policy/privacy-policy.component';
import {ContactComponent} from './components/footer/contact/contact.component';
import {AboutComponent} from './components/footer/about/about.component';
import {CheckoutComponent} from './components/checkout/checkout.component';
import {CancelComponent} from './components/cancel/cancel.component';
import {SuccessComponent} from './components/success/success.component';

const routes: Routes = [
  {path: '', redirectTo: 'feed', pathMatch: 'full'},
  {path: 'login', component: LoginComponent},
  {path: 'logout', component: LogoutComponent},
  {path: 'artist/:id', component: ArtistPageComponent},
  {path: 'artist/:id/edit', canActivate: [AuthGuard], component: ArtistPageEditComponent},
  {path: 'user/:id/commissions', component: CommissionPageComponent},
  {path: 'user/:id', component: UserPageComponent},
  {path: 'user/:id/edit', canActivate: [AuthGuard], component: UserPageEditComponent},
  {path: 'feed', component: ImageFeedComponent},
  {path: 'artists', component: ArtistFeedComponent},
  {path: 'commissions', component: CommissionFeedComponent},
  {path: 'commissions/:id', component: CommissionDetailsComponent},
  {path: 'commissions/:id/timeline', component: CommissionTimelineComponent},
  {path: 'commission-creation', component: CommissionCreationComponent},
  {path: 'chat/:id', component: ChatComponent},
  {path: 'terms', component: TermsOfServiceComponent},
  {path: 'privacy-policy', component: PrivacyPolicyComponent},
  {path: 'contact', component: ContactComponent},
  {path: 'about', component: AboutComponent},
  {path: 'checkout', component: CheckoutComponent},
  {path: 'cancel', component: CancelComponent},
  {path: 'success', component: SuccessComponent},
  {path: '**', redirectTo: 'feed'},

];

@NgModule({
  imports: [RouterModule.forRoot(routes, {useHash: true})],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
