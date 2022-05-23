export class User {
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

export enum UserRole{
  user = 'User',
  admin = 'Admin',
  artist = 'Artist',
}
