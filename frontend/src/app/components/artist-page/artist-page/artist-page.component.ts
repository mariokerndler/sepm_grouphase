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
import {Location} from '@angular/common';
import {Globals} from '../../../global/globals';

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
  tabIndex = 0;
  profilePicture;

  private routeSubscription: Subscription;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private location: Location,
    private artistService: ArtistService,
    private userService: UserService,
    private notificationService: NotificationService,
    private authService: AuthService,
    public globals: Globals
  ) {
  }

  ngOnInit(): void {
    this.routeSubscription = this.route.params.subscribe(
      (params) => this.userService.getUserById(params.id, () => this.navigateToArtistList())
        .subscribe((user) => {
          this.user = user;
          console.log(user);

          this.setProfilePicture();

          if (this.user.userRole === UserRole.artist) {
            this.isArtist = true;

            this.artistService.getArtistById(params.id, () => this.navigateToArtistList())
              .subscribe((artist) => {
                this.artist = artist;

                if (this.artist.profileSettings) {
                  this.profileSettings = JSON.parse(this.artist.profileSettings.replace(/'/g, '\"'));
                }

                this.canEdit = this.authService.getUserAuthEmail() === this.artist.email;
                this.isReady = true;
              });
          } else if (this.user.userRole === UserRole.user) {
            this.navigateToUserPage();
          }
        })
    );

    window.onload = () => {
      const reloading = sessionStorage.getItem('reloading');
      if (reloading) {
        sessionStorage.removeItem('reloading');
        this.changeIndex(1);
        this.notificationService.displaySuccessSnackbar('You successfully uploaded a new artwork');
      }
    };
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

  goBack() {
    this.location.back();
  }

  changeIndex($event: any) {
    this.tabIndex = $event;
  }


  private navigateToArtistList() {
    this.router.navigate(['/artists'])
      .catch(
        (error) => {
          this.notificationService.displayErrorSnackbar(error.toString());
        }
      );
  }

  private setProfilePicture() {
    if(!this.user.profilePictureDto) {
      this.profilePicture = this.globals.defaultProfilePicture;
    } else {
      this.profilePicture = this.user.profilePictureDto.imageUrl;
    }
  }
}
