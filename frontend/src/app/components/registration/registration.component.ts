import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup, PatternValidator, Validators} from '@angular/forms';
import {Artwork} from '../../dtos/artwork';
import {MatDialog, MatDialogRef} from '@angular/material/dialog';
import {AuthService} from '../../services/auth.service';
import {Router} from '@angular/router';
import {AuthRequest} from '../../dtos/auth-request';
import {LoginComponent} from '../login/login.component';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.scss']
})
export class RegistrationComponent implements OnInit {

  registerForm: FormGroup;
  // After first submission attempt, form validation will start
  submitted = false;
  // Error flag
  error = false;
  errorMessage = '';
  hidePassword = true;
  hideConfirmPassword = true;


  constructor(
    public dialogRef: MatDialogRef<RegistrationComponent>,
    public dialog: MatDialog,
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private router: Router) {
    this.registerForm = this.formBuilder.group({
      firstname: ['', [Validators.required]],
      lastname: ['', [Validators.required]],
      email: ['', [Validators.required, Validators.email]],
      username: ['', [Validators.required]],
      password: ['', [Validators.required, Validators.minLength(8)]],
      confirm: ['', [Validators.required, Validators.minLength(8)]],
    });
  }

  /**
   * Form validation will start after the method is called, additionally an AuthRequest will be sent
   */
  createNewUser() {
    this.submitted = true;
    if (this.registerForm.valid) {
      // registrationRequest
      const authRequest: AuthRequest = new AuthRequest(this.registerForm.controls.username.value,
        this.registerForm.controls.password.value);
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
        this.onNoClick();
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

  onNoClick(): void {
    this.dialogRef.close();
  }

  openDialog() {
    this.onNoClick();
    this.dialog.open(LoginComponent);
  }

  ngOnInit() {}


}
