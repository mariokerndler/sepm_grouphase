import {Component, OnDestroy, OnInit} from '@angular/core';
import {Subscription} from 'rxjs';
import {ActivatedRoute, Router} from '@angular/router';
import {ArtistDto, UserRole} from '../../../dtos/artistDto';
import {NotificationService} from '../../../services/notification/notification.service';
import {ArtistService} from '../../../services/artist.service';
import {ArtistProfileSettings} from '../artist-page-edit/artistProfileSettings';
import {AuthService} from '../../../services/auth.service';
import {UserService} from '../../../services/user.service';
import {ApplicationUserDto} from '../../../dtos/applicationUserDto';

@Component({
  selector: 'app-artist-page',
  templateUrl: './artist-page.component.html',
  styleUrls: ['./artist-page.component.scss']
})
export class ArtistPageComponent implements OnInit, OnDestroy {

  artist: ArtistDto;
  user: ApplicationUserDto;
  profileSettings: ArtistProfileSettings;
  isReady = false;
  isArtist = false;
  canEdit = false;
  private routeSubscription: Subscription;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private artistService: ArtistService,
    private userService: UserService,
    private notificationService: NotificationService,
    private authService: AuthService
  ) { }

  private static navigateToArtistList() {
    console.log('FAILED');
  }

  ngOnInit(): void {
    this.routeSubscription = this.route.params.subscribe(
      (params) => this.userService.getUserById(params.id, () => ArtistPageComponent.navigateToArtistList())
        .subscribe((user) => {
          this.user = user;

          if(this.user.userRole === UserRole.artist) {
            this.isArtist = true;

            this.artistService.getArtistById(params.id, () => ArtistPageComponent.navigateToArtistList())
              .subscribe((artist) => {
                this.artist = artist;

                if(this.artist.profileSettings) {
                  this.profileSettings = JSON.parse(this.artist.profileSettings.replace(/'/g, '\"'));
                }

                this.canEdit = this.authService.getUserAuthEmail() === this.artist.email;
              });
          } else if (this.user.userRole === UserRole.user) {
            this.navigateToUserPage();
          }

          this.canEdit = this.authService.getUserAuthEmail() === this.user.email;
          this.isReady = true;
        })
    );

    /*
        this.routeSubscription = this.route.params.subscribe(
      (params) => this.artistService.getArtistById(params.id, () => ArtistPageComponent.navigateToArtistList())
        .subscribe((artist) => {
          this.artist = artist;
          if(this.artist.profileSettings) {
            this.profileSettings = JSON.parse(this.artist.profileSettings.replace(/'/g, '\"'));
          }
          this.isReady = true;
          this.canEdit = this.authService.getUserAuthEmail() === this.artist.email;
        })
    );
     */
  }

  ngOnDestroy() {
    this.routeSubscription.unsubscribe();
  }

  navigateToEdit() {
    this.router.navigate(['/artist', this.artist.id, 'edit'])
      .catch(
        (error) => {
          this.notificationService.displayErrorSnackbar(error.toString());
        }
      );
  }

  navigateToUserPage() {
    this.router.navigate(['/user', this.user.id])
      .catch(
        (error) => {
          this.notificationService.displayErrorSnackbar(error.toString());
        }
      );
  }
}
