import {AfterViewInit, Component, OnInit, ViewChild} from '@angular/core';
import {ArtistService} from '../../services/artist.service';
import {ArtistDto} from '../../dtos/artistDto';
import {FormControl} from '@angular/forms';
import {map, Observable, startWith} from 'rxjs';
import {MatTableDataSource} from '@angular/material/table';
import {MatPaginator} from '@angular/material/paginator';
import {MatSort} from '@angular/material/sort';

@Component({
  selector: 'app-artist-feed',
  templateUrl: './artist-feed.component.html',
  styleUrls: ['./artist-feed.component.scss']
})
export class ArtistFeedComponent implements OnInit, AfterViewInit {

  @ViewChild(MatPaginator) paginator: MatPaginator;
  @ViewChild(MatSort) sort: MatSort;

  isReady = false;

  searchFormControl = new FormControl();
  filteredOptions: Observable<ArtistDto[]>;

  dataSource: MatTableDataSource<ArtistDto>;
  private artists: ArtistDto[];

  constructor(
    private artistService: ArtistService
  ) {
  }

  ngOnInit(): void {
    // Test
    this.artistService.getAllArtists()
      .subscribe((artists) => {
        this.artists = artists;
        this.dataSource = new MatTableDataSource<ArtistDto>(this.artists);
        this.isReady = true;
      });

    this.filteredOptions = this.searchFormControl.valueChanges.pipe(
      startWith(''),
      map(value => this._filter(value))
    );

  }

  ngAfterViewInit() {
    this.artistService.getAllArtists()
      .subscribe((artists) => {
        this.artists = artists;
        this.dataSource = new MatTableDataSource<ArtistDto>(this.artists);
        this.dataSource.paginator = this.paginator;
        this.dataSource.sort = this.sort;
        this.isReady = true;
      });
  }

  applyFilter(filterValue: string) {
    filterValue = filterValue.trim();
    filterValue = filterValue.toLowerCase();
    this.dataSource.filter = filterValue;
  }

  clearSearchField() {
    this.searchFormControl.patchValue('');
  }

  searchFieldHasValue(): boolean {
    return this.searchFormControl.value !== null;
  }

  private _filter(value: string): ArtistDto[] {
    const filterValue = value.toLowerCase();
    this.applyFilter(filterValue);

    return this.artists.filter(option => option.userName.toLowerCase().includes(filterValue));
  }
}
