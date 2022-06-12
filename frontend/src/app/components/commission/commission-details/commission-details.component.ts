import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {CommissionDto} from '../../../dtos/commissionDto';
import {UserService} from '../../../services/user.service';
import {ApplicationUserDto} from '../../../dtos/applicationUserDto';
import {ArtworkService} from '../../../services/artwork.service';
import {CommissionService} from '../../../services/commission.service';
import {ActivatedRoute} from '@angular/router';
import {FileType} from '../../../dtos/artworkDto';
import {GlobalFunctions} from '../../../global/globalFunctions';
import {FormControl, FormGroup} from '@angular/forms';
import {SketchDto} from '../../../dtos/sketchDto';
import {TagSearch} from '../../../dtos/tag-search';


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

  constructor(private userService: UserService,
              private artworkService: ArtworkService,
              private commissionService: CommissionService,
              private route: ActivatedRoute, private globalFunctions: GlobalFunctions) {
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
        console.log(commission.artworkDto);
        if (this.commission.artworkDto == null) {
          const searchPar: TagSearch = {
            artistIds: [], pageNr: 0, randomSeed: 0, searchOperations: 'id:3', tagIds: []
          };
          //this would be a temporary fix but sadly artworks are bugged rn so idk
          this.artworkService.search(searchPar).subscribe(aw => {
            console.log(aw);
              this.commission.artworkDto = aw[0];
              this.commission.artworkDto.commissionId=this.commission.id;
            this.user = commission.customerDto;
            this.hasLoaded = true;
            if (this.commission.referencesDtos.length !== 0) {
              this.hasReferences = true;
            }
            if (this.commission.artworkDto.sketchesDtos != null) {
              if (this.commission.artworkDto.sketchesDtos.length !== 0) {
                this.hasSketches = true;
              }
            }
            if (this.userId === this.commission.customerDto.id.toString()) {
              this.userEdit = true;
            }
            if (this.commission.artistDto != null) {
              if (this.userId === this.commission.artistDto.id.toString()) {
                this.artistEdit = true;
              }
            }
            if (commission.feedbackSend < commission.sketchesShown) {
              this.allowFeedback = true;
            } else {
              this.allowSketch = true;
            }
            console.log(this.commission);
            console.log(this.artistEdit);
            }
          );
          /** this.commission.artworkDto = {
            name: '',
            imageUrl: '',
            sketchesDtos: [],
            id: 0,
            fileType: FileType.jpg,
            artistId: 0,
            imageData: [],
            description: '',
            row: '0',
            col: '0',
            tags: []
          };+*/
        }
      });
  }

  fileSelected(fileInput: any) {
    this.uploadedSketch = fileInput.target.files[0];
    console.log(this.uploadedSketch);
    const sketch = new SketchDto();
    const reader = new FileReader();
    reader.readAsDataURL(this.uploadedSketch);
    reader.onload = (event) => {
      sketch.imageData = event.target.result;
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
      sketch.image = imageData;
      sketch.fileType = filetype;
      this.uploadedSketchDto = sketch;
    };
  }

  updateCommission() {
    //artist added sketch
    if (this.uploadedSketchDto !== null) {
      if (this.commission.artworkDto.sketchesDtos == null) {
        this.commission.artworkDto.sketchesDtos = [];
      }
      this.commission.artworkDto.sketchesDtos.push(this.uploadedSketchDto);
      this.commission.sketchesShown+=1;
      this.uploadedSketchDto = null;
    }
    console.log('updating '+ this.commission);
      this.commissionService.updateCommission(this.commission).subscribe();
    }


}
