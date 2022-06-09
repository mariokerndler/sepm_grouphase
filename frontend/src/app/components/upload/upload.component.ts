import {Component, ElementRef, Inject, NgZone, OnInit, ViewChild} from '@angular/core';
import {ArtworkDto, FileType} from '../../dtos/artworkDto';
import {ArtworkService} from '../../services/artwork.service';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {CdkTextareaAutosize} from '@angular/cdk/text-field';
import {map, Observable, startWith, take} from 'rxjs';
import {NotificationService} from '../../services/notification/notification.service';
import {TagDto} from '../../dtos/tagDto';
import {TagService} from '../../services/tag.service';
import {MatAutocompleteSelectedEvent} from '@angular/material/autocomplete';
import {MatChipInputEvent} from '@angular/material/chips';
import {LayoutComponent} from '../artist-page/artist-page-edit/layoutComponent';
import {COMMA, ENTER} from '@angular/cdk/keycodes';

@Component({
  selector: 'app-upload',
  templateUrl: './upload.component.html',
  styleUrls: ['./upload.component.scss']
})
export class UploadComponent implements OnInit {

  @ViewChild('autosize') autosize: CdkTextareaAutosize;
  @ViewChild('tagInput') tagInput: ElementRef<HTMLInputElement>;
  uploadForm: FormGroup;
  file: any;
  selectedImage;

  selectedTags: TagDto[] = [];
  separatorKeysCodes: number[] = [ENTER, COMMA];
  tagForm = new FormControl();
  allTags: TagDto[] = [];
  filteredTags: Observable<TagDto[]>;


  constructor(
    private artworkService: ArtworkService,
    private tagService: TagService,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private formBuilder: FormBuilder,
    public dialogRef: MatDialogRef<UploadComponent>,
    private _ngZone: NgZone,
    private notificationService: NotificationService) {
    this.uploadForm = this.formBuilder.group({
      artworkName: ['', [Validators.required]],
      description: [''],
    });
    this.filteredTags = this.tagForm.valueChanges.pipe(
      startWith(null),
      map((tag: TagDto | null) => (tag ? this.filterTags(tag) : this.allTags.slice()))
    );
  }

  ngOnInit(): void {
    this.tagService.getAllTags().subscribe(
      (tags) => {
        this.allTags = tags;
      }
    );
  }

  onNoClick(): void {
    this.dialogRef.close();
  }

  onFileChanged() {
    if (this.file.target.files && this.file.target.files[0]) {
      const reader = new FileReader();
      reader.onload = (e: any) => {
        const image = new Image();
        image.src = e.target.result;
        image.onload = (_) => {
          this.selectedImage = e.target.result;
        };
      };
      reader.readAsDataURL(this.file.target.files[0]);
      reader.onload = () => {
        if (this.uploadForm.valid) {
          const base64result = reader.result.toString().split(',')[1];
          const dataType = ((reader.result.toString().split(',')[0]).split(';')[0]).split('/')[1];
          let filetype = FileType.jpg;
          if (dataType === 'png') {
            filetype = FileType.png;
          }
          if (dataType === 'gif') {
            filetype = FileType.gif;
          }
          const binary = new Uint8Array(this.base64ToBinaryArray(base64result));
          const image = Array.from(binary);
          this.uploadNewImage(this.uploadForm.controls.artworkName.value, this.uploadForm.controls.description.value, image, filetype);
          this.dialogRef.close();
          //window.location.reload();
        }
      };
    }
  }

  uploadNewImage(name, description, imageData, filetype) {
    console.log(this.selectedTags);
    const artwork = {
      name, description, imageData,
      imageUrl: '', fileType: filetype, artistId: this.data.artist.id, tags: this.selectedTags
    } as ArtworkDto;
    this.artworkService.createArtwork(artwork, null,
      () => this.notificationService.displaySuccessSnackbar('You successfully uploaded a new image')).subscribe();
  }


  base64ToBinaryArray(base64: string) {

    const binary = window.atob(base64);
    const length = binary.length;
    const bytes = new Uint8Array(length);
    for (let i = 0; i < length; i++) {
      bytes[i] = binary.charCodeAt(i);
    }
    return bytes.buffer;
  }

  triggerResize() {
    // Wait for changes to be applied, then trigger textarea resize.
    this._ngZone.onStable.pipe(take(1)).subscribe(() => this.autosize.resizeToFitContent(true));
  }

  fileSelected(file: any) {
    this.file = file;
    if (file.target.files && file.target.files[0]) {
      const reader = new FileReader();
      reader.onload = (e: any) => {
        const image = new Image();
        image.src = e.target.result;
        image.onload = (_) => {
          this.selectedImage = e.target.result;
        };
      };
      reader.readAsDataURL(file.target.files[0]);
    }
  }

  /**
   * Removes a given tag from the chosen tag list.
   *
   * @param tag The tag that will be removed
   */
  removeTag(tag: TagDto): void {
    const index = this.selectedTags.indexOf(tag);

    if (index >= 0) {
      this.selectedTags.splice(index, 1);
    }
  }

  /**
   * Select a tag (autocomplete function)
   *
   * @param event Autocomplete event
   */
  selectedTag(event: MatAutocompleteSelectedEvent): void {
    const value: TagDto = event.option.value;
    this.selectedTags.push(value);
    this.tagInput.nativeElement.value = '';
    this.tagForm.setValue(null);
  }

  /**
   * Add a tag via text input
   *
   * @param event Add a tag via text input
   */
  addTag(event: MatChipInputEvent): void {
    const value = (event.value || '').trim();
    if (value) {
      // Find tag
      const result = this.allTags.filter(tag => tag.name.includes(value));
      if (result.length !== 0) {
        return;
      } else {
        this.selectedTags.push(result[0]);
      }
    }

    event.chipInput?.clear();

    this.tagForm.setValue(null);
  }

  private filterTags(value: TagDto): TagDto[] {
    let filterValue;
    if (typeof value == 'string') {
      filterValue = String(value).toLowerCase();
    } else {
      filterValue = value.name.toLowerCase();
    }

    return this.allTags.filter(tag => tag.name.toLowerCase().includes(filterValue));
  }


}
