import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  Validators
} from '@angular/forms';
import {MatDialog, MatDialogRef} from '@angular/material/dialog';
import {Router} from '@angular/router';
import {LoginComponent} from '../login/login.component';
import {UserService} from '../../services/user.service';
import {GlobalFunctions} from '../../global/globalFunctions';
import {UserRole} from '../../dtos/artistDto';
import {ApplicationUserDto} from '../../dtos/applicationUserDto';


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
    private router: Router,
    private globalFunctions: GlobalFunctions) {
    this.registerForm = this.formBuilder.group({
      firstname: ['', [Validators.required, Validators.pattern('[a-zA-Z-äöüßÄÖÜ]*')]],
      lastname: ['', [Validators.required, Validators.pattern('[a-zA-Z-äöüßÄÖÜ]*')]],
      email: ['', [Validators.required, Validators.email]],
      address: ['', [Validators.required]],
      username: ['', [Validators.required, Validators.pattern('[a-zA-Z0-9]*')]],
      password: ['', [Validators.required, Validators.minLength(8)]],
      confirm: ['', [Validators.required, Validators.minLength(8)]],
    },{
      validator: globalFunctions.mustMatch('password', 'confirm')
    });
  }

  /**
   * Form validation will start after the method is called, additionally an AuthRequest will be sent
   */
  createNewUser() {
    this.submitted = true;
    if (this.registerForm.valid) {


      const name = this.registerForm.controls.firstname.value;
      const surname = this.registerForm.controls.lastname.value;
      const userName = this.registerForm.controls.username.value;
      const email = this.registerForm.controls.email.value;
      const address = this.registerForm.controls.address.value;
      const password = this.registerForm.controls.password.value;

      const user = {name, surname,userName, email, address, password, userRole: UserRole.user, admin: false} as ApplicationUserDto;

      this.userService.createUser(user).subscribe();
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
}

