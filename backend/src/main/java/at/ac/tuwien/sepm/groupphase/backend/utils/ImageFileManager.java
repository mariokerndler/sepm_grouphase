package at.ac.tuwien.sepm.groupphase.backend.utils;

import at.ac.tuwien.sepm.groupphase.backend.entity.*;
import at.ac.tuwien.sepm.groupphase.backend.exception.FileManagerException;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Locale;
import java.util.Objects;

@Slf4j
@NoArgsConstructor
@Component
public class ImageFileManager {

    public void createFolderIfNotExists(String url) {
        log.trace("calling createFolderIfNotExists() ...");

        File f = new File(url);
        if (!f.isDirectory() && !f.exists()) {
            try {
                Files.createDirectories(f.toPath());
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                throw new FileManagerException(e.getMessage(), e);
            }
            log.info("Created a folder at url='{}'", url);
        }
    }

    public void createCommission(Commission c) {
        log.trace("calling createCommission() ...");
        createFolderIfNotExists(ImageDataPaths.assetAbsoluteLocation + ImageDataPaths.commissionLocation + c.getCustomer().getId() + c.getTitle());
    }

    public int countFiles(String path) {
        log.info("counting files of " + path);
        if (new File(path).listFiles() == null) {
            return 0;
        }
        log.info("counting files result" + new File(path).listFiles().length);
        return Objects.requireNonNull(new File(path).listFiles()).length;
    }

    public String writeReferenceImage(Commission c, Reference r) {
        log.trace("calling writeReferenceImage() ...");


        String relPath = ImageDataPaths.commissionLocation + c.getCustomer().getId() + c.getTitle();
        relPath += "\\" + ImageDataPaths.refIdentifier + countFiles(ImageDataPaths.assetAbsoluteLocation + relPath);
        log.info(relPath);
        try (FileOutputStream outputStream = new FileOutputStream(ImageDataPaths.assetAbsoluteLocation + relPath)) {
            outputStream.write(r.getImageData());
            log.info("Wrote reference images to disk at path='{}'", relPath);
            return relPath;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new FileManagerException(e.getMessage(), e);
        }
    }

    public String writeReferenceDatagenImage(Commission c, Reference r, String url) {
        log.trace("calling writeReferenceImage() ...");
        String relPath = url  + "." + r.getFileType().toString().toLowerCase(Locale.ROOT);
        log.info(relPath);
        try (FileOutputStream outputStream = new FileOutputStream(ImageDataPaths.assetAbsoluteLocation + relPath)) {
            outputStream.write(r.getImageData());
            log.info("Wrote reference images to disk at path='{}'", relPath);
            return relPath;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new FileManagerException(e.getMessage(), e);
        }
    }

    public String writeSketchImage(Commission c, Sketch s) {
        log.trace("calling writeSketchImage() ...");
        log.info("calling writeReferenceImage() ...");
        String relPath = ImageDataPaths.commissionLocation + +c.getCustomer().getId() + c.getTitle();
        relPath += "\\" + ImageDataPaths.sketchIdentifier + countFiles(ImageDataPaths.assetAbsoluteLocation + relPath);
        try (FileOutputStream outputStream = new FileOutputStream(ImageDataPaths.assetAbsoluteLocation + relPath)) {
            log.info(s.toString() + " " + s.getImageData().length);
            outputStream.write(s.getImageData());
            log.info("Wrote sketch images to disk at path='{}'", relPath);
            return relPath;
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new FileManagerException(e.getMessage(), e);
        }
    }

    public String writeCommissionArtwork(Commission c, Artwork aw) {
        log.trace("calling writeArtworkImage() ...");
        String relPath = ImageDataPaths.commissionLocation + +c.getCustomer().getId() + c.getTitle();
        relPath += "\\" + c.getTitle();
        if (aw.getImageData() == null) {
            log.info(ImageDataPaths.assetAbsoluteLocation + relPath);
            log.info("Creating empty image");
            File image = new File(ImageDataPaths.assetAbsoluteLocation + relPath);
            try {
                image.createNewFile();
                return relPath;
            } catch (IOException e) {
                throw new FileManagerException(e.getMessage(), e);
            }
        } else {
            log.info("Writing final Artwork");
            try (FileOutputStream outputStream = new FileOutputStream(ImageDataPaths.assetAbsoluteLocation + relPath + "." + aw.getFileType().toString().toLowerCase(Locale.ENGLISH))) {
                outputStream.write(aw.getImageData());
                log.info("Wrote artwork images to disk at path='{}'", relPath);
                return relPath;
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                throw new FileManagerException(e.getMessage(), e);
            }
        }
    }

    public boolean deleteCommissionComponent(String url) {
        log.trace("calling deleteCommissionComponent() ...");
        File imageFile = new File(ImageDataPaths.assetAbsoluteLocation + url);
        boolean isDeleted = imageFile.delete();
        log.info("Deleted file at url='{}'", url);
        return isDeleted;
    }

    public void deleteCommission(Commission c) {
        log.trace("calling deleteCommission() ...");
        String url = ImageDataPaths.assetAbsoluteLocation + ImageDataPaths.commissionLocation + c.getId();
        File delFile = new File(url);
        deleteDirectory(delFile);
        log.info("Deleted commission files at path='{}'", url);
    }

    public boolean deleteDirectory(File directoryToBeDeleted) {
        log.trace("calling deleteDirectory() ...");
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        boolean isDeleted = directoryToBeDeleted.delete();
        log.info("Deleted directory at path='{}'", directoryToBeDeleted.getPath());
        return isDeleted;
    }


    public String writeArtistImage(Artwork a) {
        log.trace("calling writeArtistImage() ...");
        try (
            FileOutputStream outputStream = new FileOutputStream(
                ImageDataPaths.assetAbsoluteLocation
                    + ImageDataPaths.artistProfileLocation
                    + a.getArtist().getUserName() + "/"
                    + a.getName() + '.'
                    + a.getFileType().toString().toLowerCase(Locale.ROOT))) {
            outputStream.write(a.getImageData());

            log.info("Wrote artist image to disk, with id='{}'", a.getId());
            return ImageDataPaths.artistProfileLocation + a.getArtist().getUserName() + "/" + a.getName() + '.' + a.getFileType().toString().toLowerCase(Locale.ROOT);
            //return  ImageDataPaths.artistProfilePictureLocation+"/"+a.getName();
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new FileManagerException(e.getMessage(), e);
        }
    }

    public boolean deleteArtistImage(Artwork a) {
        log.trace("calling deleteArtistImage() ...");
        File imageFile = new File(ImageDataPaths.assetAbsoluteLocation + ImageDataPaths.artistProfileLocation + a.getArtist().getUserName() + "/" + a.getName());
        boolean isDeleted = imageFile.delete();
        log.info("Deleted artist image from disk with id='{}'", a.getId());
        return isDeleted;
    }

    public String writeAndReplaceUserProfileImage(ApplicationUser a) {
        log.trace("calling writeAndReplaceUserProfileImage() ...");
        File f = new File(ImageDataPaths.assetAbsoluteLocation + ImageDataPaths.userProfilePictureLocation + a.getUserName());
        if (!f.isDirectory() || !f.exists()) {
            try {
                Files.createDirectories(f.toPath());
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                throw new FileManagerException(e.getMessage(), e);
            }
        }
        File profilePicture = new File(ImageDataPaths.assetAbsoluteLocation + ImageDataPaths.userProfilePictureLocation + a.getUserName() + "\\"
            + ImageDataPaths.userProfilePictureIdentifier + "." + a.getProfilePicture().getFileType().toString().toLowerCase(Locale.ROOT));
        try (FileOutputStream outputStream = new FileOutputStream(profilePicture)) {
            outputStream.write(a.getProfilePicture().getImageData());

            log.info("Wrote or replaced application user profile image at disk for user with id='{}'", a.getId());
            return ImageDataPaths.userProfilePictureLocation + a.getUserName() + "\\"
                + ImageDataPaths.userProfilePictureIdentifier + "." + a.getProfilePicture().getFileType().toString().toLowerCase(Locale.ROOT);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new FileManagerException(e.getMessage(), e);
        }

    }

    public boolean deleteUserProfileImage(ApplicationUser a) {
        log.trace("calling deleteUserProfileImage() ...");
        File userPfpDir = new File(ImageDataPaths.assetAbsoluteLocation + ImageDataPaths.userProfilePictureLocation + a.getUserName());
        boolean isDeleted = false;

        if (userPfpDir.isDirectory() && userPfpDir.exists()) {
            File userPfp = new File(ImageDataPaths.assetAbsoluteLocation + ImageDataPaths.userProfilePictureLocation + a.getUserName() + "\\"
                + ImageDataPaths.userProfilePictureIdentifier);

            isDeleted = userPfp.delete();
        }

        isDeleted = isDeleted && userPfpDir.delete();
        log.info("Deleted user profile image and directory from disk for application user with id='{}'", a.getId());
        return isDeleted;
    }

    public void renameArtistFolders(Artist artist, String oldUserName) {
        log.trace("calling renameArtistFolder() ...");
        File oldImageFile = new File(ImageDataPaths.assetAbsoluteLocation + ImageDataPaths.artistProfileLocation + oldUserName);
        try {
            Files.move(oldImageFile.toPath(), oldImageFile.toPath().resolveSibling(artist.getUserName()));
            log.info("Renamed artist folder for artist with id='{}'", artist.getId());
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new FileManagerException(e.getMessage(), e);
        }
        renameUserFolder(artist, oldUserName);
    }

    public void renameUserFolder(ApplicationUser applicationUser, String oldUserName) {
        log.trace("calling renameUserProfilePictureFolder() ...");
        //rename profile folder
        File oldImageProfileFolder = new File(ImageDataPaths.assetAbsoluteLocation + ImageDataPaths.userProfilePictureLocation + oldUserName);
        if (!oldImageProfileFolder.exists()) {
            try {
                Files.createDirectory(oldImageProfileFolder.toPath());
            } catch (IOException e) {
                log.error(e.getMessage(), e);
                throw new FileManagerException(e.getMessage(), e);
            }
        }

        try {
            Files.move(oldImageProfileFolder.toPath(), oldImageProfileFolder.toPath().resolveSibling(applicationUser.getUserName()));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new FileManagerException(e.getMessage(), e);
        }

    }
}
