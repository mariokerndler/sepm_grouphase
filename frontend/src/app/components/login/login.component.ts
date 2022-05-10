import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {AuthService} from '../../services/auth.service';
import {AuthRequest} from '../../dtos/auth-request';
import {FakerGeneratorService} from '../../services/faker-generator.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  loginForm: FormGroup;
  // After first submission attempt, form validation will start
  submitted = false;
  // Error flag
  error = false;
  errorMessage = '';

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private fakerService: FakerGeneratorService) {
    this.loginForm = this.formBuilder.group({
      username: ['', [Validators.required]],
      password: ['', [Validators.required, Validators.minLength(8)]]
    });
  }

  /**
   * Form validation will start after the method is called, additionally an AuthRequest will be sent
   */
  loginUser() {
    this.testFakerFunctions();

    this.submitted = true;
    if (this.loginForm.valid) {
      const authRequest: AuthRequest = new AuthRequest(this.loginForm.controls.username.value, this.loginForm.controls.password.value);
      this.authenticateUser(authRequest);
    } else {
      console.log('Invalid input');
    }
  }

  /**
   * Send authentication data to the authService. If the authentication was successfully, the user will be forwarded to the message page
   *
   * @param authRequest authentication data from the user login form
   */
  authenticateUser(authRequest: AuthRequest) {
    console.log('Try to authenticate user: ' + authRequest.email);
    this.authService.loginUser(authRequest).subscribe({
      next: () => {
        console.log('Successfully logged in user: ' + authRequest.email);
        this.router.navigate(['/message']);
      },
      error: error => {
        console.log('Could not log in due to:');
        console.log(error);
        this.error = true;
        if (typeof error.error === 'object') {
          this.errorMessage = error.error.error;
        } else {
          this.errorMessage = error.error;
        }
      }
    });
  }

  /**
   * Error flag will be deactivated, which clears the error message
   */
  vanishError() {
    this.error = false;
  }

  ngOnInit() {
  }

  testFakerFunctions() {
    // Fake User
    this.fakerService.generateFakeUser(1).subscribe({
      next: (x) => console.log(x)
    });

    // Fake Users
    this.fakerService.generateFakeUserByAmount(3).subscribe({
      next: (x) => console.log(x)
    });

    // Fake Tag
    this.fakerService.generateFakeTag(1).subscribe({
      next: (x) => console.log(x)
    });

    // Fake Tags
    this.fakerService.generateFakeTagByAmount(3).subscribe({
      next: (x) => console.log(x)
    });

    // Fake Gallery
    this.fakerService.generateFakeGallery(1, 1, 1).subscribe({
      next: (x) => console.log(x)
    });

    // Fake Galleries
    this.fakerService.generateFakeGalleryByAmount(3).subscribe({
      next: (x) => console.log(x)
    });

    // Fake Artist
    this.fakerService.generateFakeArtist(1, 1, 3).subscribe({
      next: (x) => console.log(x)
    });

    // Fake Artists
    this.fakerService.generateFakeArtistByAmount(3).subscribe({
      next: (x) => console.log(x)
    });

    // Fake Sketch
    this.fakerService.generateFakeSketch(1).subscribe({
      next: (x) => console.log(x)
    });

    // Fake Sketches
    this.fakerService.generateFakeSketchByAmount(3).subscribe({
      next: (x) => console.log(x)
    });

    // Fake Artwork
    this.fakerService.generateFakeArtwork(1).subscribe({
      next: (x) => console.log(x)
    });

    // Fake Artworks
    this.fakerService.generateFakeArtworkByAmount(3).subscribe({
      next: (x) => console.log(x)
    });

    // Fake Commission
    this.fakerService.generateFakeCommission(1, 1, 1).subscribe({
      next: (x) => console.log(x)
    });

    // Fake Commissions
    this.fakerService.generateFakeCommissionByAmount(3).subscribe({
      next: (x) => console.log(x)
    });
  }

}
