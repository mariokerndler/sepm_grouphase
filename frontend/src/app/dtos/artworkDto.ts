export class ArtworkDto {
  id: number;
  name: string;
  description: string;
  imageData: number[];
  imageUrl: string;
  fileType: FileType;
  artistId: number;
  row: string;
  col: string;
}

export enum FileType {
  png = 'PNG',
  gif = 'GIF',
  jpg = 'JPG',
}
