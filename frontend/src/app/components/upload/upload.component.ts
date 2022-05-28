import {Component, Inject, NgZone, OnInit, ViewChild} from '@angular/core';
import {ArtworkDto, FileType} from '../../dtos/artworkDto';
import {ArtworkService} from '../../services/artwork.service';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {ArtistDto} from '../../dtos/artistDto';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {CdkTextareaAutosize} from '@angular/cdk/text-field';
import {take} from 'rxjs';

@Component({
  selector: 'app-upload',
  templateUrl: './upload.component.html',
  styleUrls: ['./upload.component.scss']
})
export class UploadComponent implements OnInit {

  @ViewChild('autosize') autosize: CdkTextareaAutosize;
  uploadForm: FormGroup;
  file: any;
  selectedImage;




  constructor(
    private artworkService: ArtworkService,
    @Inject(MAT_DIALOG_DATA) public data: any,
    private formBuilder: FormBuilder,
    public dialogRef: MatDialogRef<UploadComponent>,
    private _ngZone: NgZone) {
    this.uploadForm = this.formBuilder.group({
      artworkName: ['', [Validators.required]],
      description: [''],
    });
  }

  ngOnInit(): void {
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
          if(this.uploadForm.valid) {
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
            window.location.reload();
          }
        };
    }
  }

  uploadNewImage(name, description, imageData, filetype){
   const artwork = {name, description, imageData,
   imageUrl:'', fileType: filetype, artistId:this.data.artist.id} as ArtworkDto;
   this.artworkService.createArtwork(artwork).subscribe();
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

  fileSelected(file: any){
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



}
