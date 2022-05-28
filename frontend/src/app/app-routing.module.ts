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

const routes: Routes = [
  {path: '', component: HomeComponent},
  {path: 'login', component: LoginComponent},
  {path: 'logout', component: LogoutComponent},
  {path: 'message', canActivate: [AuthGuard], component: MessageComponent},
  {path: 'artist/:id', component: ArtistPageComponent},
  {path: 'artist/:id/edit', component:  ArtistPageEditComponent},
  {path:'feed',component: ImageFeedComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes, {useHash: true})],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
