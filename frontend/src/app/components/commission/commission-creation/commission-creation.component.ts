import { Component, OnInit } from '@angular/core';
import {CommissionDto} from '../../../dtos/commissionDto';
import {ArtworkService} from '../../../services/artwork.service';
import {ArtistService} from '../../../services/artist.service';
import {TagService} from '../../../services/tag.service';
import { BreakpointObserver } from '@angular/cdk/layout';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {StepperOrientation} from '@angular/material/stepper';
import {Observable} from 'rxjs';
import {map} from 'rxjs/operators';

@Component({
  selector: 'app-commission-creation',
  templateUrl: './commission-creation.component.html',
  styleUrls: ['./commission-creation.component.scss']
})
export class CommissionCreationComponent implements OnInit {
  isLinear = false;
  firstFormGroup: FormGroup;
  secondFormGroup: FormGroup;
  stepperOrientation: Observable<StepperOrientation>;
  commission: CommissionDto={
    artistId: 0,
    comArtworkId: 0,
    description: '',
    endDate: undefined,
    feedback: [],
    feedbackSend: 0,
    id: 0,
    price: 0,
    referenceImageIds: [],
    references:[],
    sketchesShown: 0,
    startDate: undefined,
    title: '',
    userId: 0
  };
  constructor(private artworkService: ArtworkService, private artistService: ArtistService,
    private tagService: TagService,private _formBuilder: FormBuilder, breakpointObserver: BreakpointObserver) {
    this.stepperOrientation = breakpointObserver
      .observe('(min-width: 800px)')
      .pipe(map(({matches}) => (matches ? 'horizontal' : 'vertical')));
  }

  ngOnInit(): void {
    this.firstFormGroup = this._formBuilder.group({
      firstCtrl: ['', Validators.required],
    });
    this.secondFormGroup = this._formBuilder.group({
      secondCtrl: ['', Validators.required],
    });
  }

  uploadReference() {

  }

}
