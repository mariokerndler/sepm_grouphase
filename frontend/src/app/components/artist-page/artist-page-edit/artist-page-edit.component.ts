import {Component, ElementRef, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {NotificationService} from '../../../services/notification/notification.service';
import {ArtistDto, UserRole} from '../../../dtos/artistDto';
import {Subscription} from 'rxjs';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {CdkDragDrop, moveItemInArray} from '@angular/cdk/drag-drop';
import {LayoutComponent} from './layoutComponent';
import {COMMA, ENTER} from '@angular/cdk/keycodes';
import {TagDto} from '../../../dtos/tagDto';
import {MatAutocompleteSelectedEvent} from '@angular/material/autocomplete';
import {ArtistProfileSettings} from './artistProfileSettings';
import {ArtistService} from '../../../services/artist.service';
import {TagService} from '../../../services/tag.service';
import {Location} from '@angular/common';
import {Color} from '@angular-material-components/color-picker';
import {GlobalFunctions} from '../../../global/globalFunctions';
import {AuthService} from '../../../services/auth.service';
import {ApplicationUserDto} from '../../../dtos/applicationUserDto';
import {UserService} from '../../../services/user.service';

@Component({
  selector: 'app-artist-page-edit',
  templateUrl: './artist-page-edit.component.html',
  styleUrls: ['./artist-page-edit.component.scss']
})
export class ArtistPageEditComponent implements OnInit, OnDestroy {

  @ViewChild('fileInput') pfpInput: ElementRef;
  @ViewChild('tagInput') tagInput: ElementRef<HTMLInputElement>;

  artist: ArtistDto;
  user: ApplicationUserDto;
  isArtist: boolean;

  editForm: FormGroup;

  passwordForm: FormGroup;
  showPassword = false;

  appearanceForm: FormGroup;

  artistProfilePicture;

  isReady = false;

  availableComponents: LayoutComponent[] = [
    {componentName: 'Gallery', disabled: false, tags: []},
    {componentName: 'Reviews', disabled: false, tags: []}
  ];

  chosenComponents: LayoutComponent[] = [
    {componentName: 'Profile information', disabled: true, tags: []}
  ];

  selectedComponent: LayoutComponent;
  separatorKeysCodes: number[] = [ENTER, COMMA];
  tagForm = new FormControl();
  allTags: TagDto[] = [];

  private routeSubscription: Subscription;
  private tempArtistUrl = 'https://picsum.photos/150/150';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private location: Location,
    private artistService: ArtistService,
    private userService: UserService,
    private tagService: TagService,
    private notificationService: NotificationService,
    private formBuilder: FormBuilder,
    private globalFunctions: GlobalFunctions,
    private authService: AuthService
  ) {
    this.fillFormValidators();
  }

  private static updateArtist(
    oldArtist: ArtistDto,
    username?: string,
    name?: string,
    surname?: string,
    email?: string,
    address?: string,
    profileSettings?: string
  ): ArtistDto {
    const updatedArtist: ArtistDto = oldArtist;

    if (username) {
      updatedArtist.userName = username.valueOf();
    }

    if (name) {
      updatedArtist.name = name.valueOf();
    }

    if (surname) {
      updatedArtist.surname = surname.valueOf();
    }

    if (email) {
      updatedArtist.email = email.valueOf();
    }

    if (address) {
      updatedArtist.address = address.valueOf();
    }

    if (profileSettings) {
      updatedArtist.profileSettings = profileSettings.valueOf().replace(/"/g, '\'');
    }

    return updatedArtist;
  }

  private static updateUser(
    oldUser: ApplicationUserDto,
    username?: string,
    name?: string,
    surname?: string,
    email?: string,
    address?: string,
  ): ApplicationUserDto {
    const updatedUser: ApplicationUserDto = oldUser;

    if (username) {
      updatedUser.userName = username.valueOf();
    }

    if (name) {
      updatedUser.name = name.valueOf();
    }

    if (surname) {
      updatedUser.surname = surname.valueOf();
    }

    if (email) {
      updatedUser.email = email.valueOf();
    }

    if (address) {
      updatedUser.address = address.valueOf();
    }

    return updatedUser;
  }

  ngOnInit() {
    //  Fetch user and check if it's an artist
    this.routeSubscription = this.route.params.subscribe(
      (params) => this.userService.getUserById(params.id, () => this.navigateToArtistList())
        .subscribe((user) => {
          this.user = user;

          // Check if the user can edit this page
          if (this.user.email !== this.authService.getUserAuthEmail()) {
            this.goBack();
          }

          // Check if it's an artist
          if (this.user.userRole === UserRole.artist) {
            this.isArtist = true;

            // Fetch artist
            this.artistService.getArtistById(this.user.id, () => this.navigateToArtistList())
              .subscribe((artist) => {
                this.artist = artist;

                // Fetch artist profile settings
                if (this.artist.profileSettings) {
                  this.artist.profileSettings = this.artist.profileSettings.replace(/'/g, '\"');
                }
              });
          }

          this.setFormValues();
          this.isReady = true;
        })
    );

    // Load tags
    this.tagService.getAllTags().subscribe(
      (tags) => {
        this.allTags = tags;
      }
    );

    // TODO: Fetch real pfp
    this.artistProfilePicture = this.tempArtistUrl;
  }

  ngOnDestroy() {
    this.routeSubscription.unsubscribe();
  }

  goBack() {
    this.location.back();
  }

  updateUserInformation() {
    if (!this.editForm.valid) {
      return;
    }

    // Get values from the form
    const name = this.editForm.controls.firstname.value;
    const surname = this.editForm.controls.lastname.value;
    const username = this.editForm.controls.username.value;
    const email = this.editForm.controls.email.value;
    const address = this.editForm.controls.address.value;

    // TODO: Description is missing

    if (this.artist) {
      this.artistService.updateArtist(ArtistPageEditComponent.updateArtist(
        this.artist,
        username,
        name,
        surname,
        email,
        address)
      ).subscribe();
    } else {
      this.userService.updateUser(ArtistPageEditComponent.updateUser(
        this.user,
        username,
        name,
        surname,
        email,
        address)
      ).subscribe();
    }
  }

  updatePassword() {
    if (this.passwordForm.valid) {
      const oldPassword = this.passwordForm.controls.oldPassword.value;
      const newPassword = this.passwordForm.controls.password.value;
      const confirmPassword = this.passwordForm.controls.confirm.value;

      // TODO: Update password for user and artist
      console.log(oldPassword + ' ' + newPassword + ' ' + confirmPassword);
    }
  }

  updateAppearance() {
    if (this.appearanceForm.valid) {
      const backgroundColor = this.appearanceForm.controls.backgroundColor.value as Color;
      const primaryColor = this.appearanceForm.controls.primaryColor.value as Color;
      const secondaryColor = this.appearanceForm.controls.secondaryColor.value as Color;
      const headerColor = this.appearanceForm.controls.headerColor.value as Color;

      const artistProfileSetting: ArtistProfileSettings = {
        layout: this.chosenComponents
      };

      if (backgroundColor) {
        artistProfileSetting.backgroundColor = backgroundColor;
      }

      if (primaryColor) {
        artistProfileSetting.primaryColor = primaryColor;
      }

      if (secondaryColor) {
        artistProfileSetting.secondaryColor = secondaryColor;
      }

      if (headerColor) {
        artistProfileSetting.headerColor = headerColor;
      }

      console.log(artistProfileSetting);

      this.artistService.updateArtist(ArtistPageEditComponent.updateArtist(
        this.artist,
        undefined,
        undefined,
        undefined,
        undefined,
        undefined,
        JSON.stringify(artistProfileSetting))).subscribe();
    }
  }

  /**
   * Drop function for rearranging profile layout components.
   *
   * @param event the drop event fired by the drag and drop system
   */
  drop(event: CdkDragDrop<({ disabled: boolean; componentName: string })[], any>) {
    if (event.previousContainer === event.container) {
      moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
    } else {
      const item = this.availableComponents[event.previousIndex];
      const newItem = {
        componentName: item.componentName,
        disabled: item.disabled,
        tags: [...item.tags]
      };

      newItem.componentName += this.chosenComponents.length;
      this.chosenComponents.splice(event.currentIndex, 0, newItem);
    }
  }

  /**
   * When a layout component gets clicked
   *
   * @param component The clicked component
   */
  componentClick(component: LayoutComponent) {
    if (component.componentName !== 'Profile information') {
      this.selectedComponent = component;
    } else {
      return;
    }
  }

  /**
   * Gets called when the profile picture changes
   *
   * @param file The newly uploaded file
   */
  onFileChanged(file: any) {
    if (file.target.files && file.target.files[0]) {
      const reader = new FileReader();
      reader.onload = (e: any) => {
        const image = new Image();
        image.src = e.target.result;
        image.onload = (_) => {
          this.artistProfilePicture = e.target.result;
        };
      };
      reader.readAsDataURL(file.target.files[0]);
    }
  }

  /**
   * Removes a given tag from the chosen tag list.
   *
   * @param tag The tag that will be removed
   */
  removeTag(tag: TagDto): void {
    const index = this.selectedComponent.tags.indexOf(tag);

    if (index >= 0) {
      this.selectedComponent.tags.splice(index, 1);
    }
  }

  /**
   * Select a tag (autocomplete function)
   *
   * @param event Autocomplete event
   */
  selectedTag(event: MatAutocompleteSelectedEvent): void {
    const value: TagDto = event.option.value;
    this.selectedComponent.tags.push(value);
    this.tagInput.nativeElement.value = '';
    this.tagForm.setValue(null);
  }

  private navigateToArtistList() {
    this.router.navigate(['/artists'])
      .catch((_) => this.notificationService.displayErrorSnackbar('Could not navigate to artist list.'));
  }

  private fillFormValidators() {
    this.editForm = this.formBuilder.group({
      firstname: ['', [Validators.required, Validators.pattern('[a-zA-Z-äöüßÄÖÜ]*')]],
      lastname: ['', [Validators.required, Validators.pattern('[a-zA-Z-äöüßÄÖÜ]*')]],
      username: ['', [Validators.required, Validators.pattern('[a-zA-Z0-9]*')]],
      email: ['', [Validators.required, Validators.email]],
      description: ['', Validators.maxLength(512)],
      address: ['', [Validators.required]]
    });

    this.passwordForm = this.formBuilder.group({
      oldPassword: ['', [Validators.required, Validators.minLength(8)]],
      password: ['', [Validators.required, Validators.minLength(8)]],
      confirm: ['', [Validators.required, Validators.minLength(8)]]
    }, {
      validator: this.globalFunctions.mustMatch('password', 'confirm')
    });

    this.appearanceForm = this.formBuilder.group({
      backgroundColor: ['', []],
      primaryColor: ['', []],
      secondaryColor: ['', []],
      headerColor: ['', []]
    });
  }

  private setFormValues() {
    this.editForm.controls['firstname'].setValue(this.artist ? this.artist.name : this.user.name);
    this.editForm.controls['lastname'].setValue(this.artist ? this.artist.surname : this.user.surname);
    this.editForm.controls['username'].setValue(this.artist ? this.artist.userName : this.user.userName);
    this.editForm.controls['email'].setValue(this.artist ? this.artist.email : this.user.email);
    this.editForm.controls['address'].setValue(this.artist ? this.artist.address : this.user.address);

    if (this.isArtist) {
      this.setFormValuesForArtist();
    }
  }

  private setFormValuesForArtist() {
    this.editForm.controls['description'].setValue(this.artist?.description);

    if (!this.artist?.profileSettings) {
      return;
    }

    const profileSettings: ArtistProfileSettings = JSON.parse(this.artist.profileSettings);

    if (!profileSettings) {
      return;
    }

    if (profileSettings.backgroundColor) {
      this.appearanceForm.controls['backgroundColor'].setValue('#' + profileSettings.backgroundColor.hex);
    }

    if (profileSettings.primaryColor) {
      this.appearanceForm.controls['primaryColor'].setValue('#' + profileSettings.primaryColor.hex);
    }

    if (profileSettings.secondaryColor) {
      this.appearanceForm.controls['secondaryColor'].setValue('#' + profileSettings.secondaryColor.hex);
    }

    if (profileSettings.headerColor) {
      this.appearanceForm.controls['headerColor'].setValue('#' + profileSettings.headerColor.hex);
    }

    this.chosenComponents = profileSettings.layout;
  }
}