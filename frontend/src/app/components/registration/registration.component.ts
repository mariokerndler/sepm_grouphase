import { Component, OnInit } from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  FormGroup,
  PatternValidator,
  ValidationErrors,
  ValidatorFn,
  Validators
} from '@angular/forms';
import {Artwork} from '../../dtos/artwork';
import {MatDialog, MatDialogRef} from '@angular/material/dialog';
import {AuthService} from '../../services/auth.service';
import {Router} from '@angular/router';
import {AuthRequest} from '../../dtos/auth-request';
import {LoginComponent} from '../login/login.component';
import {UserService} from '../../services/user.service';


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
    private userService: UserService,
    private router: Router) {
    this.registerForm = this.formBuilder.group({
      firstname: ['', [Validators.required, Validators.pattern('[a-zA-Z-äöüßÄÖÜ]*')]],
      lastname: ['', [Validators.required, Validators.pattern('[a-zA-Z-äöüßÄÖÜ]*')]],
      email: ['', [Validators.required, Validators.email]],
      address: ['', [Validators.required]],
      username: ['', [Validators.required]],
      password: ['', [Validators.required, Validators.minLength(8)]],
      confirm: ['', [Validators.required, Validators.minLength(8)]],
    },{
      validator: this.mustMatch('password', 'confirm')
    });
  }

  /**
   * Form validation will start after the method is called, additionally an AuthRequest will be sent
   */
  createNewUser() {
    this.submitted = true;
    if (this.registerForm.valid) {


      const firstname = this.registerForm.controls.firstname.value;
      const lastname = this.registerForm.controls.lastname.value;
      const username = this.registerForm.controls.username.value;
      const email = this.registerForm.controls.email.value;
      const address = this.registerForm.controls.address.value;
      const password = this.registerForm.controls.password.value;

      this.userService.createUser(firstname, lastname, username, email, address, password).subscribe();
      this.onNoClick();


     // this.authenticateUser(authRequest);
    } else {
      console.log('Invalid input');
    }
  }


  /**
   * Send authentication data to the authService. If the authentication was successfully, the user will be forwarded to the message page
   *
   * @param authRequest authentication data from the user login form
   */
  /*
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
*/
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

  mustMatch(controlName: string, matchingControlName: string) {
    return (formGroup: FormGroup) => {
      const control = formGroup.controls[controlName];
      const matchingControl = formGroup.controls[matchingControlName];

      if (matchingControl.errors && !matchingControl.errors.mustMatch) {
        return;
      }

      // set error on matchingControl if validation fails
      if (control.value !== matchingControl.value) {
        matchingControl.setErrors({ mustMatch: true });
      } else {
        matchingControl.setErrors(null);
      }
      return null;
    };
  }

}

