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
                throw new FileManagerException("Error when creating user Folder");
            }
            log.info("Created a folder at url='{}'", url);
        }
    }

    public void createCommission(Commission c) throws IOException {
        log.trace("calling createCommission() ...");
        createFolderIfNotExists(ImageDataPaths.assetAbsoluteLocation + ImageDataPaths.commissionLocation + c.getCustomer().getId() + c.getTitle());
    }

    public int countFiles(String path) {
        log.trace("calling countFiles() ...");
        if (new File(path).listFiles() == null) {
            return 0;
        }
        return Objects.requireNonNull(new File(path).listFiles()).length;
    }

    public String writeReferenceImage(Commission c, Reference r) {
        log.trace("calling writeReferenceImage() ...");
        String relPath = ImageDataPaths.commissionLocation + c.getCustomer().getId() + c.getTitle();
        relPath += "\\" + ImageDataPaths.refIdentifier + countFiles(ImageDataPaths.assetAbsoluteLocation + relPath);
        try (FileOutputStream outputStream = new FileOutputStream(ImageDataPaths.assetAbsoluteLocation + relPath)) {
            outputStream.write(r.getImageData());
            log.info("Wrote reference images to disk at path='{}'", relPath);
            return relPath;
        } catch (IOException e) {
            log.error(e.getMessage());
            return "error";
        }
    }

    public String writeSketchImage(Commission c, Sketch s) {
        log.trace("calling writeSketchImage() ...");
        String relPath = ImageDataPaths.commissionLocation + c.getId() + "\\" + ImageDataPaths.sketchIdentifier + s.getId();
        try (FileOutputStream outputStream = new FileOutputStream(ImageDataPaths.assetAbsoluteLocation + relPath)) {
            outputStream.write(s.getImageData());
            log.info("Wrote sketch images to disk at path='{}'", relPath);
            return relPath;
        } catch (IOException e) {
            log.error(e.getMessage());
            return "error";
        }
    }

    public String writeArtworkImage(Commission c, Artwork aw) {
        log.trace("calling writeArtworkImage() ...");
        String relPath = ImageDataPaths.commissionLocation + c.getId() + "\\" + ImageDataPaths.awhIdentifier + aw.getId();
        try (FileOutputStream outputStream = new FileOutputStream(relPath)) {
            outputStream.write(aw.getImageData());
            log.info("Wrote artwork images to disk at path='{}'", relPath);
            return relPath;
        } catch (IOException e) {
            log.error(e.getMessage());
            return "error";
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
            log.error(e.getMessage());
            return "";
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
                log.error(e.getMessage());
                throw new FileManagerException(e.getMessage());
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
            log.error(e.getMessage());
            throw new FileManagerException(e.getMessage());
        }

    }

    public boolean deleteUserProfileImage(ApplicationUser a) {
        log.trace("calling deleteUserProfileImage() ...");
        File f = new File(ImageDataPaths.assetAbsoluteLocation + ImageDataPaths.userProfilePictureLocation + a.getUserName());

        if (f.isDirectory() && f.exists()) {
            File profilePicture = new File(ImageDataPaths.assetAbsoluteLocation + ImageDataPaths.userProfilePictureLocation + a.getUserName() + "\\"
                + ImageDataPaths.userProfilePictureIdentifier);

            return profilePicture.delete();
        }

        boolean isDeleted = f.delete();
        log.info("Deleted user profile image from disk for application user with id='{}'", a.getId());
        return isDeleted;
    }

    public void renameArtistFolder(Artist artist, String oldUserName) {
        log.trace("calling renameArtistFolder() ...");
        File oldImageFile = new File(ImageDataPaths.assetAbsoluteLocation + ImageDataPaths.artistProfileLocation + oldUserName);
        try {
            Files.move(oldImageFile.toPath(), oldImageFile.toPath().resolveSibling(artist.getUserName()));
            log.info("Renamed artist folder for artist with id='{}'", artist.getId());
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new FileManagerException(e.getMessage());
        }
        //rename profile folder
        File oldImageProfileFolder = new File(ImageDataPaths.assetAbsoluteLocation + ImageDataPaths.userProfilePictureLocation + oldUserName);
        try {
            Files.move(oldImageProfileFolder.toPath(), oldImageProfileFolder.toPath().resolveSibling(artist.getUserName()));
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new FileManagerException(e.getMessage());
        }

    }
}
