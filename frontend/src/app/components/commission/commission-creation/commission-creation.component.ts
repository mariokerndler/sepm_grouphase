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
import {FileType} from '../../../dtos/artworkDto';
import {GlobalFunctions} from '../../../global/globalFunctions';

import {ReferenceDto} from '../../../dtos/referenceDto';
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
import {Router} from '@angular/router';


@Component({
  selector: 'app-commission-creation',
  templateUrl: './commission-creation.component.html',
  styleUrls: ['./commission-creation.component.scss'],
  providers: [
    {
      provide: STEPPER_GLOBAL_OPTIONS,
      useValue: {showError: true},
    },
  ],
  encapsulation: ViewEncapsulation.None,

})
export class CommissionCreationComponent implements OnInit {
  selectedImage;
  artists: ArtistDto[];
  previewImages: any[] = [];
  selectedReferences = [];
  startDate = new Date(Date.now() + 24 * 60 * 60 * 1000);
  hasSubmitted = false;


  commissionForm = new FormGroup({
    title: new FormControl('', [Validators.required, Validators.maxLength(100), Validators.pattern('^[a-zA-Z0-9 ]*$')]),
    description: new FormControl('', [Validators.required, Validators.maxLength(512)]),
    price: new FormControl('', [Validators.required, Validators.min(0), Validators.max(this.globals.maxCommissionPrice)]),
    date: new FormControl('', [Validators.required]),
    references: new FormControl(''),
    feedbackRounds: new FormControl('')
  });
  imageForm: FormGroup;
  secondFormGroup: FormGroup;
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
              private router: Router) {
    this.stepperOrientation = breakpointObserver
      .observe('(min-width: 800px)')
      .pipe(map(({matches}) => (matches ? 'horizontal' : 'vertical')));
  }

  getUserId(): void {
    const id = localStorage.getItem('userId');
    if (id !== null) {
      this.userId = id;
    }
  }

  ngOnInit(): void {

    this.secondFormGroup = this._formBuilder.group({
      secondCtrl: ['', Validators.required],
    });
    this.getUserId();
    this.userService.getUserById(Number.parseInt(this.userId, 10)).subscribe(data => {
        this.customer = data;
      }
    );
  }

  fileSelected() {

    this.selectedReferences = this.commissionForm.value.references;
    if (this.selectedReferences != null) {
      if (this.selectedReferences.length > 0) {
        this.previewImages = [];
        const image = new Image();

        this.selectedReferences.forEach(ref => {
          image.src = ref;
          this.previewImages.push(image);
          const reader = new FileReader();
          reader.readAsDataURL(ref);
          reader.onload = (event) => {
            const extractedValues: [FileType, number[]] = this.globalFunctions.extractImageAndFileType(reader.result.toString());
            const r = new ReferenceDto();
            //TODO: very likely redundant data and URL
            r.imageData = extractedValues[1];
            r.imageUrl = event.target.result;
            r.name = ref.name;
            r.fileType = extractedValues[0];
            this.commission.referencesDtos.push(r);
          };
        });

      }
    }
  }

  submitCommission() {
    this.hasSubmitted = true;
    if (this.commissionForm.valid) {
      this.commission.title = this.commissionForm.value.title;
      this.commission.instructions = this.commissionForm.value.description;
      this.commission.price = this.commissionForm.value.price;
      this.commission.deadlineDate = this.commissionForm.value.date + ' 01:01:01';
      this.commission.customerDto = this.customer;
      this.commission.referencesDtos.forEach(r => r.imageUrl = '');
      this.commission.deadlineDate = formatDate(this.commissionForm.value.date, 'yyyy-MM-dd', 'en_US') + ' 01:01:01';
      this.commissionService.createCommission(this.commission).subscribe(
        ret => {
          this.navigateToCommissionDetails(ret.id);
        }, (error: HttpErrorResponse) => {
          this.notificationService.displayErrorSnackbar(error.error);
        }, () => {
          this.notificationService.displaySuccessSnackbar('Commission created successfully');
        }
      );
    }
  }

  formatDate() {
    return formatDate(this.commissionForm.value.date, 'yyyy-MM-dd', 'en_US');
  }


  removeReference(i) {
    this.commission.referencesDtos.splice(i, 1);
    this.commissionForm.value.references.remove(i);

  }


  createDto() {
    this.commission.title = this.commissionForm.value.title;
    this.commission.instructions = this.commissionForm.value.description;
    this.commission.price = this.commissionForm.value.price;
    this.commission.issueDate = formatDate(Date.now(), 'yyyy-MM-dd HH:mm:ss', 'en_US');
    this.commission.deadlineDate = this.commissionForm.value.date + ' 01:01:01';
  }

  setIndex($event: StepperSelectionEvent) {
    if ($event.selectedIndex === 3) {
      this.createDto();
    }
  }

  formatEndDate(date: string) {
    return (new Date(date)).toLocaleDateString();
  }

  private navigateToCommissionDetails(id: number) {
    this.router.navigate(['/commissions', id])
      .catch((_) => {
        this.notificationService.displayErrorSnackbar('Could not route to this site.');
      });
  }
}
