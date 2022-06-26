import {TagDto} from '../../../dtos/tagDto';

export class LayoutComponent {
  componentName: string;
  type: LayoutComponentType;
  disabled: boolean;
  tags: TagDto[];
}

export enum LayoutComponentType {
  information = 'Information',
  gallery = 'Gallery',
  reviews = 'Reviews'
}
