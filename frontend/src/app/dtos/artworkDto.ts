import {TagDto} from './tagDto';
import {SketchDto} from './sketchDto';

export class ArtworkDto {
  id: number;
  name: string;
  description: string;
  imageData: number[];
  imageUrl: string;
  fileType: FileType;
  sketchesDtos: SketchDto[];
  artistId: number;
  row: string;
  col: string;
  tags: TagDto[];
}

export enum FileType {
  png = 'PNG',
  gif = 'GIF',
  jpg = 'JPG',
}
