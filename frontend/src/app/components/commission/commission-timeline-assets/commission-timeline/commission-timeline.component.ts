import {Component, OnInit, ViewEncapsulation} from '@angular/core';
import {CommissionService} from '../../../../services/commission.service';
import {ActivatedRoute} from '@angular/router';
import {CommissionDto} from '../../../../dtos/commissionDto';

@Component({
  selector: 'app-commission-timeline',
  templateUrl: './commission-timeline.component.html',
  styleUrls: ['./commission-timeline.component.scss'],
  encapsulation: ViewEncapsulation.None,
})
export class CommissionTimelineComponent implements OnInit {
  data: CommissionDto;
  isPlaying = true;
  isReady = false;
  public selectedArtwork: number = null;
  endDate: string;
  startDate: string;
  artworks;

  constructor(private commissionService: CommissionService, private route: ActivatedRoute) {
  }

  ngOnInit(): void {
    this.getCommission();
  }

  getCommission() {
    const id = +this.route.snapshot.paramMap.get('id');
    this.commissionService.getCommissionById(id)
      .subscribe(
        (commission) => {
          console.log(commission);
          this.data = commission;
          this.startDate = new Date(commission.issueDate).toLocaleDateString();
          this.endDate = new Date(new Date(commission.deadlineDate).getTime() - 24 * 60 * 60 * 1000).toLocaleDateString();
          this.artworks = Array.from(this.data.artworkDto.sketchesDtos);
          this.artworks.push(this.data.artworkDto);
          console.log(this.artworks);
          this.isReady = true;
        }
      );
  }

  setSelectedArtwork(i: number) {
    this.selectedArtwork = i;
    console.log(this.selectedArtwork);
    document.documentElement.style.setProperty(`--bgFilter`, 'blur(4px)');
  }
}
