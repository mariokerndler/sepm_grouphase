export class ArtworkDto {
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
  gif = 'GIF',
  jpg = 'JPG',
}
