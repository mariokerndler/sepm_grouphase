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
import {UserRole} from '../../../dtos/artistDto';
import {formatDate} from '@angular/common';
import {StepperSelectionEvent} from '@angular/cdk/stepper';
import {HttpErrorResponse} from '@angular/common/http';
import {NotificationService} from '../../../services/notification/notification.service';


@Component({
  selector: 'app-commission-creation',
  templateUrl: './commission-creation.component.html',
  styleUrls: ['./commission-creation.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class CommissionCreationComponent implements OnInit {
  selectedImage;
  previewImages: any[] = [];
  selectedReferences = [];
  commissionForm = new FormGroup({
    title: new FormControl(''),
    description: new FormControl(''),
    price: new FormControl(''),
    date: new FormControl(''),
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
      userRole: UserRole.admin
    },
    deadlineDate: '',
    feedbackSend: 0,
    id: null,
    instructions: '',
    issueDate: '',
    price: 0,
    referencesDtos: [],
    sketchesShown: 0,
    title: '',
    feedbackRounds: 1
  };

  constructor(private artworkService: ArtworkService, private artistService: ArtistService,
              private tagService: TagService, private _formBuilder: FormBuilder, breakpointObserver: BreakpointObserver,
              public globalFunctions: GlobalFunctions,
              private commissionService: CommissionService, private notificationService: NotificationService,) {
    this.stepperOrientation = breakpointObserver
      .observe('(min-width: 800px)')
      .pipe(map(({matches}) => (matches ? 'horizontal' : 'vertical')));
  }

  ngOnInit(): void {

    this.secondFormGroup = this._formBuilder.group({
      secondCtrl: ['', Validators.required],
    });
  }

  onFileChanged() {

  }

  fileSelected() {

    this.selectedReferences = this.commissionForm.value.references;
    console.log((this.selectedReferences.length));
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
            const base64result = reader.result.toString().split(',')[1];
            const dataType = ((reader.result.toString().split(',')[0]).split(';')[0]).split('/')[1];
            let filetype = FileType.jpg;
            if (dataType === 'png') {
              filetype = FileType.png;
            }
            if (dataType === 'gif') {
              filetype = FileType.gif;
            }
            const binary = new Uint8Array(this.globalFunctions.base64ToBinaryArray(base64result));
            const imageData = Array.from(binary);
            const r = new ReferenceDto();
            //TODO: very likley redundant data and URL
            r.imageData = imageData;
            r.imageUrl = event.target.result;
            r.name = ref.name;
            r.fileType = filetype;
            console.log(r);
            this.commission.referencesDtos.push(r);
          };
        });

      }
    }
  }


  submitCommission() {

    this.commission.title = this.commissionForm.value.title;
    this.commission.instructions = this.commissionForm.value.description;
    this.commission.price = this.commissionForm.value.price;
    this.commission.issueDate = formatDate(Date.now(), 'yyyy-MM-dd HH:mm:ss', 'en_US');
    this.commission.deadlineDate = this.commissionForm.value.date + ' 01:01:01';

    this.commissionService.createCommission(this.commission).subscribe(ret => {

      }, (error: HttpErrorResponse) => {
      this.notificationService.displayErrorSnackbar(error.error);
      }, () => {
      this.notificationService.displaySuccessSnackbar('Commission created successfully');

      }
    );
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
}
