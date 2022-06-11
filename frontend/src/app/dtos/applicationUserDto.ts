import {UserRole} from './artistDto';
import {ProfilePictureDto} from './profilePictureDto';

export class ApplicationUserDto {
  id?: number;
  userName: string;
  profilePictureDto: ProfilePictureDto;
  name: string;
  surname: string;
  email: string;
  address: string;
  password: string;
  admin: boolean;
  userRole: UserRole;
}
