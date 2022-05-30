import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {Router} from '@angular/router';
import {AuthService} from '../../services/auth.service';
import {AuthRequest} from '../../dtos/auth-request';
import {MatDialog, MatDialogRef} from '@angular/material/dialog';
import {RegistrationComponent} from '../registration/registration.component';
import {UserService} from '../../services/user.service';
import {NotificationService} from '../../services/notification/notification.service';

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
  hidePassword = true;

  constructor(
    public dialogRef: MatDialogRef<LoginComponent>,
    public dialog: MatDialog,
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private userService: UserService,
    private notificationService: NotificationService,
    private router: Router) {
    this.loginForm = this.formBuilder.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(8)]]
    });
  }

  /**
   * Form validation will start after the method is called, additionally an AuthRequest will be sent
   */
  loginUser() {
    this.submitted = true;
    if (this.loginForm.valid) {
      const authRequest: AuthRequest = new AuthRequest(this.loginForm.controls.email.value, this.loginForm.controls.password.value);
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

    // Fetch user from email
    this.userService.getUserByEmail(authRequest.email).subscribe(
      (foundUser) => {
        if (!foundUser) {
          this.notificationService.displayErrorSnackbar('Could not authenticate user with e-mail: ' + authRequest.email);
          return;
        }

        // Auth user
        this.authService.loginUser(authRequest, foundUser).subscribe({
          next: () => {
            console.log('Successfully logged in user: ' + authRequest.email);
            this.onNoClick();
            this.router.navigate(['/feed'])
              .catch((err) => console.log(err));
          }
        });
      }
    );
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
    this.dialog.open(RegistrationComponent);
  }

  ngOnInit() {
  }
}
