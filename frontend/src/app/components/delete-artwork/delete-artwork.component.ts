import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material/dialog';
import {ArtworkService} from '../../services/artwork.service';
import {NotificationService} from '../../services/notification/notification.service';

@Component({
  selector: 'app-delete-artwork',
  templateUrl: './delete-artwork.component.html',
  styleUrls: ['./delete-artwork.component.scss']
})
export class DeleteArtworkComponent implements OnInit {

  constructor(@Inject(MAT_DIALOG_DATA) public data: any,
              public dialogRef: MatDialogRef<DeleteArtworkComponent>,
              private artworkService: ArtworkService,
              private notificationService: NotificationService) {
  }

  ngOnInit(): void {
  }

  deleteArtwork() {
    this.artworkService.deleteArtwork(this.data.artwork,
      () => this.notificationService.displaySuccessSnackbar('Successfully deleted ' + this.data.artwork.name)).subscribe();
    this.dialogRef.close(this.data.artwork);
  }
}
