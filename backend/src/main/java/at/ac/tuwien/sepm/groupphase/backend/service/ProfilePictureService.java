package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.ProfilePicture;

public interface ProfilePictureService {

    ProfilePicture getDefaultProfilePicture(ApplicationUser applicationUser);
}
