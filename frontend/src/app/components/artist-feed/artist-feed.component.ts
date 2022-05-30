import {Component, OnInit, ViewChild} from '@angular/core';
import {ArtistService} from '../../services/artist.service';
import {NotificationService} from '../../services/notification/notification.service';
import {ArtistDto} from '../../dtos/artistDto';
import {MatPaginator} from '@angular/material/paginator';
import {GlobalFunctions} from '../../global/globalFunctions';

@Component({
  selector: 'app-artist-feed',
  templateUrl: './artist-feed.component.html',
  styleUrls: ['./artist-feed.component.scss']
})
export class ArtistFeedComponent implements OnInit {

  @ViewChild(MatPaginator) paginator: MatPaginator;

  isReady = false;

  artistSlice: ArtistDto[] = [];
  pageSize = 10;
  currentPage = 0;
  totalSize = 0;

  private artists: ArtistDto[];

  constructor(
    private artistService: ArtistService,
    private notificationService: NotificationService,
    private glogalFunctions: GlobalFunctions
  ) {
  }

  ngOnInit(): void {
    this.artistService.getAllArtists()
      .subscribe((artists) => {
        this.artists = artists;
        //this.artists = this.glogalFunctions.shuffleArray(this.artists);
        this.totalSize = this.artists.length;
        this.iterator();
        this.isReady = true;
      });
  }

  handlePage(e: any) {
    this.currentPage = e.pageIndex;
    this.pageSize = e.pageSize;
    this.iterator();
  }

  private iterator() {
    const end = (this.currentPage + 1) * this.pageSize;
    const start = this.currentPage * this.pageSize;
    this.artistSlice = this.artists.slice(start, end);
  }
}
