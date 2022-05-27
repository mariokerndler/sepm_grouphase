import {Component, Input, OnInit} from '@angular/core';
import {ArtworkService} from '../services/artwork.service';
import {TagSearch} from '../dtos/tag-search';
import {ArtworkDto} from '../dtos/artworkDto';
import {ArtistDto} from '../dtos/artistDto';
import {FormBuilder} from '@angular/forms';
import {ArtistService} from '../services/artist.service';
import {TagService} from '../services/tag.service';
import {TagDto} from '../dtos/tagDto';
import {MatSelectionListChange} from "@angular/material/list";

@Component({
  selector: 'app-image-feed',
  templateUrl: './image-feed.component.html',
  styleUrls: ['./image-feed.component.scss']
})


export class ImageFeedComponent implements OnInit {
  @Input() artworks: ArtworkDto[];
  //maybe use as component
  filterArtistId = null;

  cols= 3;
  //rename Artwork
  url = 'assets/';
  images: ArtworkDto[];
  artists: ArtistDto[];
  tags: TagDto[];
  selectedTags: TagDto[]=[];
  searchParams: TagSearch = {
    pageNr: 0,
    randomSeed: 1,
    searchOperations: '',
    tagIds: []
  };
  tagForm = this.formBuilder.group({
    selectedTags: ''
  });
  public selectedArtwork: number = null;

  constructor(private formBuilder: FormBuilder, private artworkService: ArtworkService
    , private artistService: ArtistService, private tagService: TagService) {
    this.artworkService = artworkService;
    this.tagService = tagService;
    this.artistService = artistService;
  }

  ngOnInit(): void {
    this.loadFeed();
    this.loadArtists();
    this.loadAllTags();
   // document.documentElement.style.setProperty(`--${your-variable}`, value
  }
  compareFunction = (o1: any, o2: any) => o1.id === o2.id;
  public loadFeed() {
    this.artworkService.search(this.searchParams).subscribe(
      data => {

        data.forEach(a => {


        });
        this.images = data;
      }, error => {
        console.log('no error handling exists so im just here to say hi');
      }
    );
  }

  private loadArtists() {
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

  private loadAllTags() {
    this.tagService.getAllTags().subscribe(
      data => {
        console.log(data);
        this.tags = data;
      }, error => {
        console.log('no error handling exists so im just here to say hi');
      }
    );
  }

  public nextPage(): void {
    this.searchParams.pageNr += 1;
    this.loadFeed();
  }


  public previousPage(): void {
    if (this.searchParams.pageNr !== 0) {
      this.searchParams.pageNr -= 1;
      this.loadFeed();
    }

  }
  //not working
  public onListSelectionChange(event: any): void {
    console.log(event.value );
  }
}
