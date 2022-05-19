import {Component, Inject} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {ArtistPageEditDialogData} from '../common/ArtistPageEditDialogData';

@Component({
  selector: 'app-artist-page-edit',
  templateUrl: './artist-page-edit.component.html',
  styleUrls: ['./artist-page-edit.component.scss']
})
export class ArtistPageEditComponent{

  constructor(
    public dialogRef: MatDialogRef<ArtistPageEditComponent>,
    @Inject(MAT_DIALOG_DATA) public data: ArtistPageEditDialogData
  ) { }

  onNoClick(): void {
    this.dialogRef.close();
  }
}
