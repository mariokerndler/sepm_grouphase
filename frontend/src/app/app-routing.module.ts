import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from './components/home/home.component';
import {LoginComponent} from './components/login/login.component';
import {AuthGuard} from './guards/auth.guard';
import {MessageComponent} from './components/message/message.component';
import {ArtistPageComponent} from './components/artist-page/artist-page/artist-page.component';
import {ArtistPageEditComponent} from './components/artist-page/artist-page-edit/artist-page-edit.component';
import {LogoutComponent} from './components/logout/logout.component';
import {ImageFeedComponent} from './components/image-feed/image-feed.component';
import {UserPageComponent} from './components/user-page/user-page.component';
import {UserPageEditComponent} from './components/user-page/user-page-edit/user-page-edit.component';

const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'login', component: LoginComponent},
  {path: 'logout', component: LogoutComponent},
  {path: 'message', canActivate: [AuthGuard], component: MessageComponent},
  {path: 'artist/:id', component: ArtistPageComponent},
  {path: 'artist/:id/edit', component:  ArtistPageEditComponent},
  {path:'feed',component: ImageFeedComponent}
  {path: 'artist/:id/edit', component: ArtistPageEditComponent},
  {path: 'user/:id', component: UserPageComponent},
  {path: 'user/:id/edit', component: UserPageEditComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {useHash: true})],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
