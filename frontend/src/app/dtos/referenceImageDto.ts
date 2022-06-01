import {FileType} from './artworkDto';

export class ReferenceImageDto {
  id: number;
  imageData: number[];
  imageUrl: string;
  fileType: FileType;
}
