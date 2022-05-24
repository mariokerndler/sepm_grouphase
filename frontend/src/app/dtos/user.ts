export class User {
  id?: number;
  userName: string;
  name: string;
  surname: string;
  email: string;
  password: string;
  admin: boolean;
  userRole: UserRole;
  profilePicture: string;
  address: string;
}

export enum UserRole{
  user = 'User',
  admin = 'Admin',
  artist = 'Artist',
}
