import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {CommissionService} from '../../../../services/commission.service';
import {ActivatedRoute, Router} from '@angular/router';
import {CommissionDto} from '../../../../dtos/commissionDto';
import {SketchDto} from '../../../../dtos/sketchDto';
import { DomSanitizer } from '@angular/platform-browser';


@Component({
  selector: 'app-commission-timeline',
  templateUrl: './commission-timeline.component.html',
  styleUrls: ['./commission-timeline.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class CommissionTimelineComponent implements OnInit {
  data: CommissionDto;
  isPlaying = false;
  isReady = false;
  public selectedArtwork: number = null;
  endDate: string;
  startDate: string;
  timelapseGif: SketchDto;
  dia: SketchDto;
  artworks;
  image: any;
  constructor( private commissionService: CommissionService, private route: ActivatedRoute, private router: Router) {
  }

  ngOnInit(): void {
    this.getCommission();
  }


  getCommission() {
    const id = +this.route.snapshot.paramMap.get('id');
    this.commissionService.getCommissionById(id)
      .subscribe(
        (commission) => {
          this.data = commission;
          this.data.artworkDto.imageUrl = this.data.artworkDto.imageUrl + '.' + this.data.artworkDto.fileType.toLowerCase();
          if(commission.status !== 'COMPLETED'){
            this.router.navigate(['/commissions', id]);
            return;
          }
          this.startDate = new Date(commission.issueDate).toLocaleDateString();
          this.endDate = new Date(new Date(commission.deadlineDate).getTime() - 24 * 60 * 60 * 1000).toLocaleDateString();
          this.artworks = [];
          const sketches = this.data.artworkDto.sketchesDtos;
          for (const sketch of sketches) {
            if (sketch.fileType !== 'GIF') {
              this.artworks.push(sketch);
            } else {
              this.timelapseGif = sketch;
            }
          }
          this.artworks.push(this.data.artworkDto);
          this.isReady = true;
        }
      );
  }


  setSelectedArtwork(i: number) {
    this.selectedArtwork = i;
    document.documentElement.style.setProperty(`--bgFilter`, 'blur(4px)');
  }
}
