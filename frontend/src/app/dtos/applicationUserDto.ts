import {UserRole} from './artistDto';

export class ApplicationUserDto {
  id?: number;
  userName: string;
  name: string;
  surname: string;
  email: string;
  address: string;
  password: string;
  admin: boolean;
  userRole: UserRole;
}
