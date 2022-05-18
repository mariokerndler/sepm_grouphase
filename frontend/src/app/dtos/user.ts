export class User {
  id?: number;
  firstName: string;
  lastName: string;
  username: string;
  email: string;
  password: string;
  admin: boolean;
  userRole: UserRole;
}

export enum UserRole{
  user = 'User',
  admin = 'Admin',
  artist = 'Artist',
}
