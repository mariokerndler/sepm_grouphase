import {Component, OnDestroy, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {FakerGeneratorService} from '../../../services/faker-generator.service';
import {NotificationService} from '../../../common/service/notification.service';
import {Artist} from '../../../dtos/artist';
import {Subscription} from 'rxjs';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';

@Component({
  selector: 'app-artist-page-edit',
  templateUrl: './artist-page-edit.component.html',
  styleUrls: ['./artist-page-edit.component.scss']
})
export class ArtistPageEditComponent implements OnInit, OnDestroy{

  artist: Artist;
  isArtist: boolean;

  editForm: FormGroup;
  submitted = false;
  hidePassword = true;
  hideConfirmPassword = true;

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
      //password: ['', [Validators.required, Validators.minLength(8)]],
      //confirm: ['', [Validators.required, Validators.minLength(8)]],
      description: ['', Validators.maxLength(512)]
    }, {
      //validator: this.mustMatch('password', 'confirm')
    });
  }

  private static checkIfArtist(artist: Artist): boolean {
    return artist.artworkIds.length > 0;
  }

  ngOnInit() {
    this.routeSubscription = this.route.params
      .subscribe(params => this.fakerService
        .generateFakeArtist(1, 2, 5)
        .subscribe(artist => this.artist = artist));

    this.isArtist = ArtistPageEditComponent.checkIfArtist(this.artist);

    this.setFormValues();
  }

  ngOnDestroy() {
    this.routeSubscription.unsubscribe();
  }

  navigateToSave() {

  }

  saveUser() {
    this.submitted = true;
    if (this.editForm.valid) {
      const firstname = this.editForm.controls.firstname.value;
      const lastname = this.editForm.controls.lastname.value;
      const username = this.editForm.controls.username.value;
      const email = this.editForm.controls.email.value;
      const description = this.editForm.controls.description.value;
      //const password = this.editForm.controls.password.value;

      // TODO: Save User
      console.log(firstname + ' ' + lastname + ' ' + username + ' ' + email + ' ' + description);
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
    //this.editForm.controls['password'].setValue(this.artist.password);
  }
}
