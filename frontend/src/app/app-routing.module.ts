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

const routes: Routes = [
  {path: '', redirectTo: 'feed', pathMatch: 'full'},
  {path: 'login', component: LoginComponent},
  {path: 'logout', component: LogoutComponent},
  {path: 'artist/:id', component: ArtistPageComponent},
  {path: 'artist/:id/edit', canActivate: [AuthGuard], component: ArtistPageEditComponent},
  {path: 'user/:id/commissions', component: CommissionPageComponent},
  {path: 'user/:id', component: UserPageComponent},
  {path: 'user/:id/edit', component: UserPageEditComponent},
  {path: 'feed', component: ImageFeedComponent},
  {path: 'artists', component: ArtistFeedComponent},
  {path: 'commissions', component: CommissionFeedComponent},
  {path: 'commissions/:id', component: CommissionDetailsComponent},
  {path: 'commission-creation', component: CommissionCreationComponent},
  {path: '**', redirectTo: 'feed'},

];

@NgModule({
  imports: [RouterModule.forRoot(routes, {useHash: true})],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
