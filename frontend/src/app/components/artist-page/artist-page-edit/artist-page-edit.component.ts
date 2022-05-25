import {Component, ElementRef, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {NotificationService} from '../../../services/notification/notification.service';
import {ArtistDto} from '../../../dtos/artistDto';
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

@Component({
  selector: 'app-artist-page-edit',
  templateUrl: './artist-page-edit.component.html',
  styleUrls: ['./artist-page-edit.component.scss']
})
export class ArtistPageEditComponent implements OnInit, OnDestroy{

  @ViewChild('fileInput') pfpInput: ElementRef;
  @ViewChild('tagInput') tagInput: ElementRef<HTMLInputElement>;

  artist: ArtistDto;
  isArtist: boolean;

  editForm: FormGroup;

  passwordForm: FormGroup;
  showPassword = false;

  appearanceForm: FormGroup;

  artistProfilePicture;

  availableComponents: LayoutComponent[] = [
    { componentName: 'Gallery', disabled: false, tags: []},
    { componentName: 'Reviews', disabled: false, tags: []}
  ];

  chosenComponents: LayoutComponent[] = [
    { componentName: 'Profile information', disabled: true, tags: [] }
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
    private tagService: TagService,
    private notificationService: NotificationService,
    private formBuilder: FormBuilder,
  ) {
    this.fillFormValidators();
  }

  private static checkIfArtist(artist: ArtistDto): boolean {
    if(!artist?.artworkIds) {
      return false;
    }

    return artist.artworkIds.length > 0;
  }

  ngOnInit() {
    this.routeSubscription = this.route.params.subscribe(
      (params) => this.artistService.getArtistById(params.id, () => this.navigateToArtistList())
        .subscribe((artist) => {
          this.artist = artist;

          this.isArtist = ArtistPageEditComponent.checkIfArtist(this.artist);
          // this.artistProfilePicture = this.artist.profilePicture;
          this.setFormValues();
        })
    );

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

  navigateToSave() {

  }

  goBack() {
    this.location.back();
  }

  saveSettings() {
    console.log(this.chosenComponents);
  }

  updateUser() {
    if (this.editForm.valid) {
      const firstname = this.editForm.controls.firstname.value;
      const lastname = this.editForm.controls.lastname.value;
      const username = this.editForm.controls.username.value;
      const email = this.editForm.controls.email.value;
      //const description = this.editForm.controls.description.value;

      // TODO: Update profile picture
      const updateArtist: ArtistDto = {
        id: this.artist.id,
        userName: username,
        name: firstname,
        surname: lastname,
        email,
        address: this.artist.address,
        password: this.artist.password,
        admin: this.artist.admin,
        userRole: this.artist.userRole,
        reviewScore: this.artist.reviewScore,
        galleryId: this.artist.galleryId,
        artworkIds: this.artist.artworkIds,
        commissions: this.artist.commissions,
        reviews: this.artist.reviews,
        artistSettings: null
        //profilePicture: this.artistProfilePicture,
      };

      console.log(updateArtist);
      console.log(JSON.stringify(updateArtist));

      this.artistService.updateArtist(updateArtist).subscribe(
        (_) => {
          console.log('UPDATED USER');
          this.goBack();
        }
      );
    }
  }

  updatePassword() {
    if(this.passwordForm.valid) {
      const oldPassword = this.passwordForm.controls.oldPassword.value;
      const newPassword = this.passwordForm.controls.password.value;
      const confirmPassword = this.passwordForm.controls.confirm.value;

      // TODO: Update password
      console.log(oldPassword + ' ' + newPassword + ' ' + confirmPassword);
    }
  }

  updateAppearance() {
    console.log(this.appearanceForm);
    if(this.appearanceForm.valid) {
      const bgColor = this.appearanceForm.controls.backgroundColor.value;
      const primaryColor = this.appearanceForm.controls.primaryColor.value;
      const secondaryColor = this.appearanceForm.controls.secondaryColor.value;
      const headerColor = this.appearanceForm.controls.headerColor.value;

      const artistProfileSetting: ArtistProfileSettings = {
        backgroundColor: bgColor,
        primaryColor,
        secondaryColor,
        headerColor,
        layout: this.chosenComponents
      };

      console.log(artistProfileSetting);
    }
  }

  drop(event: CdkDragDrop<({ disabled: boolean; componentName: string })[], any>) {
    if (event.previousContainer === event.container) {
      moveItemInArray(event.container.data, event.previousIndex, event.currentIndex);
    } else {
      const item = this.availableComponents[event.previousIndex];
      const newItem = {
        componentName:  item.componentName,
        disabled: item.disabled,
        tags: [...item.tags]
      };

      newItem.componentName += this.chosenComponents.length;
      this.chosenComponents.splice(event.currentIndex, 0, newItem);
    }
  }

  mustMatch(controlName: string, matchingControlName: string) {
    return (formGroup: FormGroup) => {
      const control = formGroup.controls[controlName];
      const matchingControl = formGroup.controls[matchingControlName];

      if (matchingControl.errors && !matchingControl.errors.mustMatch) {
        return;
      }

      if (control.value !== matchingControl.value) {
        matchingControl.setErrors({ mustMatch: true });
      } else {
        matchingControl.setErrors(null);
      }
      return null;
    };
  }

  setFormValues() {
    this.editForm.controls['firstname'].setValue(this.artist.name);
    this.editForm.controls['lastname'].setValue(this.artist.surname);
    this.editForm.controls['username'].setValue(this.artist.userName);
    this.editForm.controls['email'].setValue(this.artist.email);

    if(this.isArtist) {
      this.editForm.controls['description'].setValue(this.artist.description);
    }
  }

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

  componentClick(component: LayoutComponent) {
    if (component.componentName !== 'Profile information') {
      this.selectedComponent = component;
    } else {
      return;
    }
  }

  removeTag(tag: TagDto): void {
    const index = this.selectedComponent.tags.indexOf(tag);

    if(index >= 0) {
      this.selectedComponent.tags.splice(index, 1);
    }
  }

  selectedTag(event: MatAutocompleteSelectedEvent): void {
    const value: TagDto = event.option.value;
    this.selectedComponent.tags.push(value);
    this.tagInput.nativeElement.value = '';
    this.tagForm.setValue(null);
  }

  private navigateToArtistList() {
    // TODO: Implement
  }

  private fillFormValidators() {
    this.editForm = this.formBuilder.group({
      firstname: ['', [Validators.required, Validators.pattern('[a-zA-Z-äöüßÄÖÜ]*')]],
      lastname: ['', [Validators.required, Validators.pattern('[a-zA-Z-äöüßÄÖÜ]*')]],
      username: ['', [Validators.required, Validators.pattern('[a-zA-Z0-9]*')]],
      email: ['', [Validators.required, Validators.email]],
      description: ['', Validators.maxLength(512)]
    });

    this.passwordForm = this.formBuilder.group({
      oldPassword: ['', [Validators.required, Validators.minLength(8)]],
      password: ['', [Validators.required, Validators.minLength(8)]],
      confirm: ['', [Validators.required, Validators.minLength(8)]]
    }, {
      validator: this.mustMatch('password', 'confirm')
    });

    this.appearanceForm = this.formBuilder.group({
      backgroundColor: ['', []],
      primaryColor: ['', []],
      secondaryColor: ['', []],
      headerColor: ['', []]
    });
  }
}
