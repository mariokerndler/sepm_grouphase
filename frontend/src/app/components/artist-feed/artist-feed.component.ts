import {Component, OnInit} from '@angular/core';
import {ArtistService} from '../../services/artist.service';
import {ArtistDto} from '../../dtos/artistDto';
import {FormControl} from '@angular/forms';
import {map, Observable, startWith} from 'rxjs';
import {MatTableDataSource} from '@angular/material/table';

@Component({
  selector: 'app-artist-feed',
  templateUrl: './artist-feed.component.html',
  styleUrls: ['./artist-feed.component.scss']
})
export class ArtistFeedComponent implements OnInit {
  isReady = false;

  artistSlice: ArtistDto[] = [];
  pageSize = 10;
  currentPage = 0;
  totalSize = 0;

  searchFormControl = new FormControl();
  filteredOptions: Observable<ArtistDto[]>;

  public dataSource = new MatTableDataSource<ArtistDto>();

  private artists: ArtistDto[];

  constructor(
    private artistService: ArtistService
  ) {
  }

  ngOnInit(): void {
    this.artistService.getAllArtists()
      .subscribe((artists) => {
        this.artists = artists;
        this.totalSize = this.artists.length;
        //this.iterator();

        this.filteredOptions = this.searchFormControl.valueChanges.pipe(
          startWith(''),
          map(value => this._filter(value))
        );

        this.isReady = true;
      });
  }

  /*
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
   */

  private _filter(value: string): ArtistDto[] {
    const filterValue = value.toLowerCase();

    return this.artists.filter(option => option.userName.toLowerCase().includes(filterValue));
  }
}
