import {Component, ElementRef, OnDestroy, OnInit, ViewChild} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {FakerGeneratorService} from '../../../services/faker-generator.service';
import {NotificationService} from '../../../services/notification/notification.service';
import {Artist} from '../../../dtos/artist';
import {Subscription} from 'rxjs';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-artist-page-edit',
  templateUrl: './artist-page-edit.component.html',
  styleUrls: ['./artist-page-edit.component.scss']
})
export class ArtistPageEditComponent implements OnInit, OnDestroy{

  @ViewChild('fileInput') pfpInput: ElementRef;

  artist: Artist;
  isArtist: boolean;

  editForm: FormGroup;

  passwordForm: FormGroup;
  showPassword = false;

  appearanceForm: FormGroup;

  artistProfilePicture;
  private routeSubscription: Subscription;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private fakerService: FakerGeneratorService,
    private notificationService: NotificationService,
    private formBuilder: FormBuilder,
  ) {
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

    // TODO: Figure out why this is reversed?
    this.appearanceForm = this.formBuilder.group({
      backgroundColor: ['', []],
      primaryColor: ['', []],
      secondaryColor: ['', []],
      headerColor: ['', []]
    });
  }

  private static checkIfArtist(artist: Artist): boolean {
    return artist.artworkIds.length > 0;
  }

  ngOnInit() {
    this.routeSubscription = this.route.params
      .subscribe(_ => this.fakerService
        .generateFakeArtist(1, 2, 5)
        .subscribe(artist => this.artist = artist));

    this.isArtist = ArtistPageEditComponent.checkIfArtist(this.artist);
    this.artistProfilePicture = this.artist.profilePicture;

    this.setFormValues();
  }

  ngOnDestroy() {
    this.routeSubscription.unsubscribe();
  }

  navigateToSave() {

  }

  saveSettings() {
    this.updateUser();
    this.updatePassword();
    this.updateAppearance();
  }

  updateUser() {
    if (this.editForm.valid) {
      const firstname = this.editForm.controls.firstname.value;
      const lastname = this.editForm.controls.lastname.value;
      const username = this.editForm.controls.username.value;
      const email = this.editForm.controls.email.value;
      const description = this.editForm.controls.description.value;

      // TODO: Update User
      console.log(firstname + ' ' + lastname + ' ' + username + ' ' + email + ' ' + description);
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

      // TODO: Update appearance
      console.log(bgColor + ' ' + primaryColor + ' ' + secondaryColor + ' ' + headerColor);
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
    this.editForm.controls['firstname'].setValue(this.artist.firstName);
    this.editForm.controls['lastname'].setValue(this.artist.lastName);
    this.editForm.controls['username'].setValue(this.artist.username);
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
}
