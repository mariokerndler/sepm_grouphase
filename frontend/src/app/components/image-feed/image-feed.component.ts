import {Component, Input, OnInit, ViewChild} from '@angular/core';
import {ArtworkService} from '../../services/artwork.service';
import {TagSearch} from '../../dtos/tag-search';
import {ArtworkDto} from '../../dtos/artworkDto';
import {ArtistDto} from '../../dtos/artistDto';
import {FormBuilder} from '@angular/forms';
import {ArtistService} from '../../services/artist.service';
import {TagService} from '../../services/tag.service';
import {TagDto} from '../../dtos/tagDto';
import {MatCheckboxChange} from '@angular/material/checkbox';
import {GlobalFunctions} from '../../global/globalFunctions';
import {Globals} from '../../global/globals';
import {NotificationService} from '../../services/notification/notification.service';
import {MatPaginator} from '@angular/material/paginator';

@Component({
  selector: 'app-image-feed',
  templateUrl: './image-feed.component.html',
  styleUrls: ['./image-feed.component.scss']
})
export class ImageFeedComponent implements OnInit {

  @Input() artworks: ArtworkDto[];
  @ViewChild(MatPaginator) paginator: MatPaginator;

  tagsLoaded = false;
  imagesLoaded = false;
  artistsLoaded = false;
  pageSize = 10;
  currentPage = 0;
  totalSize = 0;

  //maybe use as component
  filterArtistId = null;
  isOpen = false;
  tagHidden = false;
  cols = 3;
  //rename Artwork
  url = 'assets/';
  images: ArtworkDto[];
  artists: ArtistDto[];
  tags: TagDto[];
  searchInput = '';
  selectedTags: TagDto[] = [];
  selectedTagsRaw = '';
  searchParams: TagSearch = {
    pageNr: 0,
    randomSeed: 1,
    searchOperations: '',
    tagIds: []
  };
  public selectedArtwork: number = null;


  constructor(private formBuilder: FormBuilder, private artworkService: ArtworkService
    , private artistService: ArtistService, private tagService: TagService,
              public globalFunctions: GlobalFunctions) {
    this.globalFunctions = globalFunctions;
    this.artworkService = artworkService;
    this.tagService = tagService;
    this.artistService = artistService;
    console.log(globalFunctions.artworkNameParser('data\\ap\\tianzi\\artstation_9970348_37433613_Quick painting practice 速涂练习.jpg'));
  }

  ngOnInit(): void {
    this.loadFeed();
    this.loadArtists();
    this.loadAllTags();
    // document.documentElement.style.setProperty(`--${your-variable}`, value
  }

  compareFunction = (o1: any, o2: any) => o1.id === o2.id;

  public loadFeed() {
    if(this.artworks) {
      this.images = this.artworks;
      this.imagesLoaded = true;
      return;
    }

    if (this.searchInput !== '') {
      this.searchParams.randomSeed = 0;
      this.searchParams.searchOperations = 'name~' + this.searchInput;
    } else {
      this.searchParams.randomSeed = 1;
      this.searchParams.searchOperations = '';
    }
    this.artworkService.search(this.searchParams).subscribe(
      data => {
        this.images = data;
      }, error => {
        console.log('no error handling exists so im just here to say hi');
      }
    );

  }

  public nextPage(): void {
    if (this.images.length > 0) {
      this.searchParams.pageNr += 1;
      this.loadFeed();
    }
  }


  public previousPage(): void {
    if (this.searchParams.pageNr !== 0) {
      this.searchParams.pageNr -= 1;
      this.loadFeed();
    }

  }

  public onListSelectionChange(event: any, options: any): void {
    this.searchParams.pageNr=0;
    this.loadFeed();
  }

  toggleView() {
    this.tagHidden = !this.tagHidden;
  }

  public loadAllTags() {
    this.tagService.getAllTags().subscribe(
      data => {
        console.log(data);
        this.tags = data;
      }, error => {
        console.log('no error handling exists so im just here to say hi');
      }
    );
  }

  public loadArtists() {
    this.artistService.getAllArtists().subscribe(
      data => {

        data.forEach(a => {


        });
        this.artists = data;
      }, error => {
        console.log('no error handling exists so im just here to say hi');
      }
    );
  }

  onTagSelect($event: MatCheckboxChange) {
    this.searchParams.tagIds = this.tags
      .filter(menuitem => menuitem.selected)
      .map(menuitem => menuitem.id.toString());
    this.searchParams.pageNr=0;
    console.log(this.searchParams.tagIds);
    this.loadFeed();
  }


  setSelectedArtwork(i:number) {
    this.selectedArtwork=i;
    document.documentElement.style.setProperty(`--bgFilter`, 'blur(4px)');

  }

  handlePage(e: any) {
    this.currentPage = e.pageIndex;
    this.pageSize = e.pageSize;
    //this.iterator();
  }
}
