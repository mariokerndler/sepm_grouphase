import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {CommissionDto} from '../../../dtos/commissionDto';
import {UserService} from '../../../services/user.service';
import {ApplicationUserDto} from '../../../dtos/applicationUserDto';
import {ArtworkService} from '../../../services/artwork.service';
import {CommissionService} from '../../../services/commission.service';
import {ActivatedRoute, Router} from '@angular/router';
import {FileType} from '../../../dtos/artworkDto';
import {GlobalFunctions} from '../../../global/globalFunctions';
import {FormControl, FormGroup} from '@angular/forms';
import {SketchDto} from '../../../dtos/sketchDto';
import {TagSearch} from '../../../dtos/tag-search';
import {ArtistService} from '../../../services/artist.service';
import {NotificationService} from '../../../services/notification/notification.service';


@Component({
  selector: 'app-commission-details',
  templateUrl: './commission-details.component.html',
  styleUrls: ['./commission-details.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class CommissionDetailsComponent implements OnInit {

  userProfilePicture = 'https://picsum.photos/150/150';
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
  public selectedArtwork: number = null;

  public selectedArtistId = 4;
  //Just dummy data.
  artistIds = [4, 5, 6, 8, 10, 39];

  constructor(private userService: UserService,
              private artworkService: ArtworkService,
              private commissionService: CommissionService,
              private route: ActivatedRoute, private globalFunctions: GlobalFunctions,
              private artistService: ArtistService, private notificationService: NotificationService,
              private router: Router) {
  }

  ngOnInit(): void {
    this.getCommission();
    this.getUserId();

  }

  getUserId(): void {
    const id = localStorage.getItem('userId');
    if (id !== null) {
      this.userId = id;
    }
  }

  setSelectedArtwork(i: number) {
    this.selectedArtwork = i;
    document.documentElement.style.setProperty(`--bgFilter`, 'blur(4px)');
  }

  getCommission() {
    const id = +this.route.snapshot.paramMap.get('id');
    this.commissionService.getCommissionById(id)
      .subscribe((commission) => {
          this.commission = commission;
          this.commission.artistCandidatesDtos = [];
          this.artistIds.forEach(a => {
            this.artistService.getArtistById(a).subscribe(result => {
              this.commission.artistCandidatesDtos.push(result);
            });
          });
          console.log(commission.artworkDto);
          if (this.commission.artworkDto == null) {
            const searchPar: TagSearch = {
              artistIds: [], pageNr: 0, randomSeed: 0, searchOperations: 'id:3', tagIds: []
            };
            //this would be a temporary fix but sadly artworks are bugged rn so idk
            this.artworkService.search(searchPar).subscribe(aw => {
                console.log(aw);
                this.commission.artworkDto = aw[0];
                this.user = commission.customerDto;
                this.checkCommissionState(this.commission);
              }
            );
          } else {

            this.user = commission.customerDto;
            this.checkCommissionState(this.commission);
          }

        }
      );
  }

  checkCommissionState(commission: CommissionDto): void {
    if (commission.referencesDtos.length !== 0) {
      this.hasReferences = true;
    }
    if (commission.artworkDto.sketchesDtos != null) {
      if (commission.artworkDto.sketchesDtos.length !== 0) {
        this.hasSketches = true;
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
    if (this.commission.sketchesShown > this.commission.feedbackSent) {

      this.uploadedSketchDto = this.commission.artworkDto.sketchesDtos[(this.commission.artworkDto.sketchesDtos.length - 1)];
      console.log(this.uploadedSketchDto);
    }
    this.hasLoaded = true;
    console.log(this.commission);
    console.log(this.artistEdit);
    console.log(this.userEdit);
    console.log(this.allowSketch);
  }


  fileSelected(fileInput: any
  ) {
    this.uploadedSketch = fileInput.target.files[0];
    console.log(this.uploadedSketch);
    const sketch = new SketchDto();
    const reader = new FileReader();
    reader.readAsDataURL(this.uploadedSketch);
    reader.onload = (event) => {
      const extractedValues: [FileType, number[]] = this.globalFunctions.extractImageAndFileType(reader.result.toString());
      sketch.imageData = extractedValues[1];
      sketch.description = 'aaaa';
      sketch.fileType = extractedValues[0];
      sketch.imageUrl = 'default';
      sketch.artworkId = this.commission.artworkDto.id;
      this.uploadedSketchDto = sketch;
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
    this.commissionService.updateCommission(this.commission).subscribe();
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

  }
}
