package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.ProfilePicture;
import at.ac.tuwien.sepm.groupphase.backend.service.ProfilePictureService;
import at.ac.tuwien.sepm.groupphase.backend.utils.FileType;
import at.ac.tuwien.sepm.groupphase.backend.utils.ImageDataPaths;
import at.ac.tuwien.sepm.groupphase.backend.utils.ImageFileManager;
import org.springframework.stereotype.Service;

@Service
public class ProfilePictureServiceImpl implements ProfilePictureService {

    private final ImageFileManager ifm;

    public ProfilePictureServiceImpl(ImageFileManager ifm) {
        this.ifm = ifm;
    }

    public ProfilePicture getDefaultProfilePicture(ApplicationUser applicationUser) {

        ifm.downloadPfpIfNotPresent();

        return ProfilePicture.builder()
            .fileType(FileType.PNG)
            .imageData(ifm.getByteArray(ImageDataPaths.assetAbsoluteLocation + ImageDataPaths.defaultUserProfilePictureLocation))
            .applicationUser(applicationUser)
            .build();
    }
}
