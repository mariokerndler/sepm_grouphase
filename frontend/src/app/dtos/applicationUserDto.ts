import {UserRole} from './artistDto';
import {ProfilePictureDto} from './profilePictureDto';
import {ChatParticipantStatus, ChatParticipantType, IChatParticipant} from 'ng-chat';

export class ApplicationUserDto implements  IChatParticipant {
  id: number;
  userName: string;
  profilePictureDto: ProfilePictureDto;
  name: string;
  surname: string;
  email: string;
  address: string;
  password: string;
  admin: boolean;
  userRole: UserRole;
  readonly avatar: string | null;
  displayName: string;
  participantType: ChatParticipantType= ChatParticipantType.User;
   status: ChatParticipantStatus;
}
