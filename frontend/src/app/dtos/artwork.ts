export class Artwork {
  id: number;
  name: string;
  description: string;
  imageData: Uint8Array;
  imageUrl: string;
  fileType: FileType;
  artistId: number;

}

export enum FileType {
  png = 'PNG',
  gif = 'GIG',
  jpg = 'JPG',
}
