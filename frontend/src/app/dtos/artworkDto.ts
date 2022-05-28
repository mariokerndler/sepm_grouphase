export class ArtworkDto {
  id: number;
  name: string;
  description: string;
  imageData: number[];
  imageUrl: string;
  fileType: FileType;
  artistId: number;

}

export enum FileType {
  png = 'PNG',
  gif = 'GIF',
  jpg = 'JPG',
}
