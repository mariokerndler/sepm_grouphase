import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {CommissionDto} from '../../../dtos/commissionDto';
import {ArtworkService} from '../../../services/artwork.service';
import {ArtistService} from '../../../services/artist.service';
import {TagService} from '../../../services/tag.service';
import {BreakpointObserver} from '@angular/cdk/layout';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {StepperOrientation} from '@angular/material/stepper';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';
import {GlobalFunctions} from '../../../global/globalFunctions';
import {CommissionService} from '../../../services/commission.service';
import {ArtistDto, UserRole} from '../../../dtos/artistDto';
import {formatDate} from '@angular/common';
import {STEPPER_GLOBAL_OPTIONS, StepperSelectionEvent} from '@angular/cdk/stepper';
import {HttpErrorResponse} from '@angular/common/http';
import {NotificationService} from '../../../services/notification/notification.service';
import {CommissionStatus} from '../../../global/CommissionStatus';
import {UserService} from '../../../services/user.service';
import {ApplicationUserDto} from '../../../dtos/applicationUserDto';
import {ChatParticipantStatus, ChatParticipantType} from 'ng-chat';
import {Globals} from '../../../global/globals';
import {ActivatedRoute, Router} from '@angular/router';


@Component({
  selector: 'app-commission-edit',
  templateUrl: './commission-edit.component.html',
  styleUrls: ['./commission-edit.component.scss'],
  providers: [
    {
      provide: STEPPER_GLOBAL_OPTIONS,
      useValue: {showError: true},
    },
  ],
  encapsulation: ViewEncapsulation.None,

})
export class CommissionEditComponent implements OnInit {
  artists: ArtistDto[];
  startDate = new Date(Date.now() + 24 * 60 * 60 * 1000);
  hasSubmitted = false;


  commissionForm = new FormGroup({
    description: new FormControl('', [Validators.required, Validators.maxLength(512)]),
    price: new FormControl('', [Validators.required, Validators.min(0), Validators.max(this.globals.maxCommissionPrice)]),
    date: new FormControl('', [Validators.required]),
  });

  stepperOrientation: Observable<StepperOrientation>;

  commission: CommissionDto = {
    artistDto: undefined,
    customerDto: {
      id: 1,
      userName: 'admin',
      name: '',
      surname: '',
      email: '',
      address: '',
      password: 'string',
      admin: true,
      userRole: UserRole.admin,
      profilePictureDto: null,
      displayName: '',
      avatar: null,
      participantType: ChatParticipantType.User,
      status: ChatParticipantStatus.Online
    },
    deadlineDate: '',
    feedbackSent: 0,
    id: null,
    instructions: '',
    issueDate: '',
    price: 0,
    referencesDtos: [],
    sketchesShown: 0,
    title: '',
    feedbackRounds: 1,
    artworkDto: null,
    status: CommissionStatus.listed,
    artistCandidatesDtos: []
  };
  userId: string;
  customer: ApplicationUserDto;

  constructor(private artworkService: ArtworkService,
              private artistService: ArtistService,
              private tagService: TagService,
              private _formBuilder: FormBuilder,
              breakpointObserver: BreakpointObserver,
              public globalFunctions: GlobalFunctions,
              private commissionService: CommissionService,
              private notificationService: NotificationService,
              private userService: UserService,
              public globals: Globals,
              private router: Router,
              private route: ActivatedRoute) {
    this.stepperOrientation = breakpointObserver
      .observe('(min-width: 800px)')
      .pipe(map(({matches}) => (matches ? 'horizontal' : 'vertical')));
  }

  ngOnInit(): void {
    this.getCommission();
  }

  updateCommission() {
    this.hasSubmitted = true;
    if (this.commissionForm.valid) {
      console.log(this.commissionForm);
      this.commission.instructions = this.commissionForm.value.description;
      this.commission.price = this.commissionForm.value.price;
      this.commission.deadlineDate = this.formatDate() + ' 01:01:01';
      console.log(this.commission);
      this.commissionService.updateCommission(this.commission).subscribe(
        ret => {
          this.navigateToCommissionDetails(this.commission.id);
        }, (error: HttpErrorResponse) => {
          this.notificationService.displayErrorSnackbar(error.error);
        }, () => {
          this.notificationService.displaySuccessSnackbar('Commission updated successfully');
        }
      );
    }
  }

  formatDate() {
    return formatDate(this.commissionForm.value.date, 'yyyy-MM-dd', 'en_US');
  }

  createDto() {
    this.commission.instructions = this.commissionForm.value.description;
    this.commission.price = this.commissionForm.value.price;
    this.commission.deadlineDate = this.commissionForm.value.deadlineDate + ' 01:01:01';
  }

  setIndex($event: StepperSelectionEvent) {
    if ($event.selectedIndex === 3) {
      this.createDto();
    }
  }

  formatEndDate(date: string) {
    return (new Date(date)).toLocaleDateString();
  }

  getCommission() {
    const id = +this.route.snapshot.paramMap.get('id');
    this.commissionService.getCommissionById(id)
      .subscribe((commission) => {
        this.commission = commission;
        console.log(commission);
        this.commissionForm.value.description = this.commission.instructions;
        this.commissionForm.value.price = this.commission.price;
        this.commissionForm.value.deadlineDate = this.commission.deadlineDate;
      });
  }

  private navigateToCommissionDetails(id: number) {
    this.router.navigate(['/commissions', id])
      .catch((_) => {
        this.notificationService.displayErrorSnackbar('Could not route to this site.');
      });
  }


}
