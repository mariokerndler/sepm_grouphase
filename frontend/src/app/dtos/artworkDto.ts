import {TagDto} from './tagDto';
import {SketchDto} from './sketchDto';

export class ArtworkDto {
  id: number;
  name: string;
  commissionId: number;
  description: string;
  imageData: number[];
  imageUrl: string;
  fileType: FileType;
  sketchesDtos: SketchDto[];
  artistId: number;
  row: string;
  col: string;
  tagsDtos: TagDto[];
}

export enum FileType {
  png = 'PNG',
  gif = 'GIF',
  jpg = 'JPG',
}
