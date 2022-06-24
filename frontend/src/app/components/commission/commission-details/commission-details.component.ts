import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {CommissionDto} from '../../../dtos/commissionDto';
import {UserService} from '../../../services/user.service';
import {ApplicationUserDto} from '../../../dtos/applicationUserDto';
import {ArtworkService} from '../../../services/artwork.service';
import {CommissionService} from '../../../services/commission.service';
import {ActivatedRoute, Router} from '@angular/router';
import {ArtworkDto, FileType} from '../../../dtos/artworkDto';
import {GlobalFunctions} from '../../../global/globalFunctions';
import {FormControl, FormGroup} from '@angular/forms';
import {SketchDto} from '../../../dtos/sketchDto';
import {TagSearch} from '../../../dtos/tag-search';
import {ArtistService} from '../../../services/artist.service';
import {NotificationService} from '../../../services/notification/notification.service';
import {CommissionStatus} from '../../../global/CommissionStatus';
import {UploadComponent} from '../../upload/upload.component';
import {MatDialog} from '@angular/material/dialog';
import {Globals} from '../../../global/globals';
import {Location} from '@angular/common';


@Component({
  selector: 'app-commission-details',
  templateUrl: './commission-details.component.html',
  styleUrls: ['./commission-details.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class CommissionDetailsComponent implements OnInit {

  profilePicture;
  commission: CommissionDto;
  user: ApplicationUserDto;
  userId: string;
  feedbackButtonStates = ['Write Feedback', 'Hide'];
  feedbackButtonIndex = 0;
  hasLoaded = false;
  hasReferences = false;
  hasSketches = false;
  allowFeedback = false;
  allowSketch = false;
  artistEdit = false;
  userEdit = false;
  sketchForm = new FormGroup({
    sketchDescription: new FormControl('')
  });
  items = Array.from({length: 100000}).map((_, i) => `Item #${i}`);
  uploadedSketch: any;
  uploadedSketchDto: SketchDto;
  finalImage: any;
  finalImageDto: ArtworkDto;
  public selectedArtwork: number = null;
  public selectedReference: number = null;
  startDate: string;
  endDate: string;
  updatedEndDate: string;
  sketches;

  public selectedArtistId;
  //Just dummy data.
  artistIds = [];
  constructor(private userService: UserService,
              private artworkService: ArtworkService,
              private commissionService: CommissionService,
              private route: ActivatedRoute,
              private location: Location,
              private globalFunctions: GlobalFunctions,
              private artistService: ArtistService,
              private notificationService: NotificationService,
              private router: Router,
              private dialog: MatDialog,
              public globals: Globals) {
  }

  ngOnInit(): void {
    this.getCommission();
    this.getUserId();
    this.getUserRole();

  }

  getUserId(): void {
    const id = localStorage.getItem('userId');
    if (id !== null) {
      this.userId = id;
      console.log(id);
    }
  }

  getUserRole(): string {
    const role = localStorage.getItem('userRole');
    if (role !== null) {
      return role;
    }
  }

  setSelectedArtwork(i: number, isReference: boolean) {
    if(isReference){
      this.selectedReference = i;
    } else {
      this.selectedArtwork = i;
    }
    document.documentElement.style.setProperty(`--bgFilter`, 'blur(4px)');
  }

  getCommission() {
    const id = +this.route.snapshot.paramMap.get('id');
    this.commissionService.getCommissionById(id, () => this.navigationError())
      .subscribe((commission) => {
          this.commission = commission;
           this.sketches = [];
           if(commission.artworkDto) {
             const sketches = commission.artworkDto.sketchesDtos;
             for (const sketch of sketches) {
               if (sketch.fileType !== 'GIF') {
                 this.sketches.push(sketch);
               }
             }
           }

          this.startDate = new Date(commission.issueDate).toLocaleDateString();
          this.endDate = new Date(commission.deadlineDate).toLocaleDateString();
          this.updatedEndDate = new Date(new Date(commission.deadlineDate).getTime() - 24 * 60 * 60 * 1000).toLocaleDateString();
          //this.commission.artistCandidatesDtos = [];
          this.artistIds.forEach(a => {
            this.artistService.getArtistById(a).subscribe(result => {
              this.commission.artistCandidatesDtos.push(result);
            });
          });
          if (this.commission.artworkDto == null) {
            const searchPar: TagSearch = {
              artistIds: [], pageNr: 0, randomSeed: 0, searchOperations: 'id:3', tagIds: []
            };
            //this would be a temporary fix but sadly artworks are bugged rn so idk
                this.user = commission.customerDto;
                this.checkCommissionState(this.commission);
          } else {

            this.user = commission.customerDto;
            this.checkCommissionState(this.commission);
          }
        this.setProfilePicture();
        }
      );
  }

  navigationError() {
    this.location.back();
    this.notificationService.displayErrorSnackbar('Could not find commission');
  }

  checkCommissionState(commission: CommissionDto): void {
    if (commission.referencesDtos.length !== 0) {
      this.hasReferences = true;
    }
    if (this.commission.artworkDto !== null) {
    if (commission.artworkDto.sketchesDtos != null) {
      if (commission.artworkDto.sketchesDtos.length !== 0) {
        this.hasSketches = true;
      }
    }
      if (this.commission.sketchesShown > this.commission.feedbackSent) {

        this.uploadedSketchDto = this.commission.artworkDto.sketchesDtos[(this.commission.artworkDto.sketchesDtos.length - 1)];
        console.log(this.uploadedSketchDto);
      }
    }
    if (this.userId === this.commission.customerDto.id.toString()) {
      this.userEdit = true;
    }
    if (commission.artistDto != null) {
      if (this.userId === this.commission.artistDto.id.toString()) {
        this.artistEdit = true;
      }
    }
    if (commission.feedbackSent < commission.sketchesShown) {
      this.allowFeedback = true;
      this.allowSketch = false;
    } else {
      this.allowSketch = true;
    }
    this.hasLoaded = true;
    console.log(this.commission);
    console.log(this.artistEdit);
    console.log(this.userEdit);
    console.log(this.allowSketch);
  }


  fileSelected(fileInput: any, description: string) {
    this.uploadedSketch = fileInput.target.files[0];
    console.log(this.uploadedSketch);
    const sketch = new SketchDto();
    const reader = new FileReader();
    reader.readAsDataURL(this.uploadedSketch);
    reader.onload = (event) => {
      const extractedValues: [FileType, number[]] = this.globalFunctions.extractImageAndFileType(reader.result.toString());
      sketch.imageData = extractedValues[1];
      sketch.description = description;
      sketch.fileType = extractedValues[0];
      sketch.imageUrl = 'default';
      sketch.artworkId = this.commission.artworkDto.id;
      this.uploadedSketchDto = sketch;
      this.updateCommission();
    };
  }
  updateCommission() {
    //artist added sketch
    if (this.uploadedSketchDto !== null && this.artistEdit) {
      if (this.commission.artworkDto.sketchesDtos == null) {
        this.commission.artworkDto.sketchesDtos = [];
      }
      if (this.artistEdit) {
        this.commission.artworkDto.sketchesDtos.push(this.uploadedSketchDto);
        this.commission.sketchesShown += 1;
        this.uploadedSketchDto = null;
      }
    }
    if (this.userEdit) {
      this.uploadedSketchDto.artworkId = this.commission.artworkDto.id;
      this.commission.feedbackSent++;
      this.commission.artworkDto.sketchesDtos[this.commission.artworkDto.sketchesDtos.length - 1] = this.uploadedSketchDto;
    }
    console.log(this.commission);
    this.commission.artworkDto.sketchesDtos[this.commission.artworkDto.sketchesDtos.length-1].description
      += '%' + new Date().toLocaleDateString();
    this.commission.artworkDto.sketchesDtos[this.commission.artworkDto.sketchesDtos.length-1].customerFeedback
      += '%' + new Date().toLocaleDateString();
    this.commissionService.updateCommission(this.commission).subscribe((commission) => console.log(commission));
    this.checkCommissionState(this.commission);
  }



  toggleFeedbackField() {
    if (this.feedbackButtonIndex === 1) {
      this.feedbackButtonIndex = 0;
    } else {
      this.feedbackButtonIndex = 1;
    }
  }

  navigateToArtist(a) {
    this.router.navigate(['/artist', a.id])
      .catch((_) => this.notificationService.displayErrorSnackbar(`Could not navigate to the artist with username ${a.userName}.`));
  }

  chooseArtist() {
    if (this.selectedArtistId != null) {
      this.commission.status = CommissionStatus.negotiating;
      this.artistService.getArtistById(this.selectedArtistId).subscribe(artist => {
        this.commission.artistDto = artist;
        console.log(this.selectedArtistId);
        this.commissionService.updateCommission(this.commission).subscribe(ok => {
          this.notificationService.displaySuccessSnackbar('Artist selected successfully');
        });
      });
    } else {
      this.notificationService.displaySuccessSnackbar('Please choose an artist by clicking on them');
    }
  }

  //triggered by Artist
  applyArtist() {

    if (this.commission.artistCandidatesDtos === null) {
      this.commission.artistCandidatesDtos = [];
    }

    this.artistService.getArtistById(Number.parseInt(this.userId, 10)).subscribe(data => {
      if (this.commission.artistCandidatesDtos.filter(a => a.id === data.id).length > 0) {
        this.notificationService.displaySimpleDialog('', 'You have already applied for this commission');
      } else {
        this.commission.artistCandidatesDtos.push(data);
        this.commissionService.updateCommission(this.commission).subscribe(() => {
          this.notificationService.displaySuccessSnackbar('You have applied for this commission');
        });
      }
    });
  }

  //triggered by User
  startCommission() {
    this.commission.status=CommissionStatus.inProgress;
    this.commissionService.updateCommission(this.commission).subscribe(success=>{
      this.notificationService.displaySuccessSnackbar('Commission is now in progress');
    });
  }

  openDialog() {
   const dialogRef = this.dialog.open(UploadComponent, {
      data: {
        artist: this.commission.artistDto,
        commission: this.commission
      }
    });

    dialogRef.afterClosed().subscribe(
      () => this.dialog.open(UploadComponent, {
        data: {
          artist: this.commission.artistDto,
          commission: this.commission,
          timelapse: true,
        }
      })
    );
  }

  openSketchDialog() {
    const dialogRef = this.dialog.open(UploadComponent, {
      data: {
        artist: this.commission.artistDto,
        commission: this.commission,
        sketch: true,
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if(result.event === 'sketchSelected'){
        this.fileSelected(result.data, result.feedback);
      }
    });
  }

  calculateProgress(): number{

    let negValue = 33;

    if(this.commission.feedbackRounds !== 0){
      negValue = negValue / this.commission.feedbackRounds;
    }

    switch (this.commission.status){
      case CommissionStatus.listed: return 0;
      case CommissionStatus.negotiating: return negValue;
      case CommissionStatus.inProgress:
        if(this.commission.feedbackRounds !== 0) {
          let calcValue = ((this.commission.feedbackSent + 1 ) / this.commission.feedbackRounds);
          if(calcValue > 1){
            calcValue = 1;
          }
          return 66 * calcValue;
        } else {
          return 66;
        }
      case CommissionStatus.completed: return 100;
      default: return 0;
    }
  }

  private setProfilePicture() {
    if (!this.user.profilePictureDto) {
      this.profilePicture = this.globals.defaultProfilePicture;
    } else {
      this.profilePicture = this.user.profilePictureDto.imageUrl;
    }
  }
}
