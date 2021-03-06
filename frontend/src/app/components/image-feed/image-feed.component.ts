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
import {DeleteArtworkComponent} from '../delete-artwork/delete-artwork.component';
import {MatDialog} from '@angular/material/dialog';

@Component({
  selector: 'app-image-feed',
  templateUrl: './image-feed.component.html',
  styleUrls: ['./image-feed.component.scss']
})
export class ImageFeedComponent implements OnInit {

  @Input() artworks: ArtworkDto[];
  @Input() isUser = false;
  @Input() isInProfile;
  @ViewChild(MatPaginator) paginator: MatPaginator;

  tagsLoaded = false;
  imagesLoaded = false;
  artistsLoaded = false;
  pageSize = 10;
  currentPage = 0;
  totalSize = 0;

  //maybe use as component
  isOpen = false;
  tagHidden = false;
  cols = 3;
  //rename Artwork
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
    tagIds: [],
    artistIds: []
  };
  public selectedArtwork: number = null;

  constructor(
    public dialog: MatDialog,
    private formBuilder: FormBuilder,
    private artworkService: ArtworkService,
    private artistService: ArtistService,
    private tagService: TagService,
    private notificationService: NotificationService,
    public globalFunctions: GlobalFunctions,
    public globals: Globals) {
  }

  ngOnInit(): void {
    this.loadFeed();
    this.loadArtists();
    this.loadAllTags();
    // document.documentElement.style.setProperty(`--${your-variable}`, value
  }

  compareFunction = (o1: any, o2: any) => o1.id === o2.id;

  public loadFeed() {
    if (this.artworks) {
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

    this.artworkService.search(this.searchParams)
      .subscribe(
        //temporary solution until be is fixed.
        (artworks) => {
          artworks.forEach(a => {
            try {
              const img = new URL(a.imageUrl);
              artworks.forEach(b => {
                if (b.imageUrl === a.imageUrl && b !== a) {
                  artworks.splice(artworks.indexOf(b), 1);
                }
              });
            } catch (_) {
              artworks.splice(artworks.indexOf(a), 1);
            }
          });
          this.images = artworks;
          this.imagesLoaded = true;
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
    this.searchParams.pageNr = 0;
    this.loadFeed();
  }

  toggleView() {
    this.tagHidden = !this.tagHidden;
  }

  public loadAllTags() {
    this.tagService.getAllTags()
      .subscribe(data => {
        this.tags = data;
        this.tagsLoaded = true;
      });
  }

  public loadArtists() {
    this.artistService.getAllArtists()
      .subscribe(data => {
          this.artists = data;
          this.artistsLoaded = true;
        }
      );
  }

  onTagSelect($event: MatCheckboxChange) {
    this.searchParams.tagIds = this.tags
      .filter(menuitem => menuitem.selected)
      .map(menuitem => menuitem.id.toString());
    this.searchParams.pageNr = 0;
    this.loadFeed();
  }


  setSelectedArtwork(i: number) {
    this.selectedArtwork = i;
    document.documentElement.style.setProperty(`--bgFilter`, 'blur(4px)');
  }

  removeMissing(artwork: ArtworkDto) {
    const updatedArtworks = [];
    for (const i of this.images) {
      if(i !== artwork){
        updatedArtworks.push(i);
      }
    }
    this.images = updatedArtworks;
  }

  deleteImage(artwork: ArtworkDto) {
    const dialogRef = this.dialog.open(DeleteArtworkComponent, {
      data: {
        artwork,
      }
    });
    dialogRef.afterClosed().subscribe(
      data => {
        this.artworks = this.artworks.filter(d => d !== data);
        this.loadFeed();
      }
    );

  }
}
