package at.ac.tuwien.sepm.groupphase.backend.utils;

import at.ac.tuwien.sepm.groupphase.backend.entity.*;
import com.sun.xml.bind.v2.model.core.Ref;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artwork;
import at.ac.tuwien.sepm.groupphase.backend.exception.FileManagerException;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@NoArgsConstructor
@Component
public class ImageFileManager {

    public void createFolderIfNotExists(String url)   {
        File f = new File(url);
        if (!f.isDirectory() && !f.exists()) {
            try {
                Files.createDirectories(f.toPath());
            } catch (IOException e) {
                throw  new FileManagerException("Error when creating user Folder");
            }
            log.info("Created"+ url);
        }
    }

    public void createCommission(Commission c) throws IOException {
        createFolderIfNotExists(ImageDataPaths.assetAbsoluteLocation + ImageDataPaths.commissionLocation + c.getCustomer().getId() + c.getTitle());
    }

    public int countFiles(String path) {
        if (new File(path).listFiles() == null) {
            return 0;
        }
        return new File(path).listFiles().length;
    }

    public String writeReferenceImage(Commission c, Reference r) {

        String relPath = ImageDataPaths.commissionLocation + c.getCustomer().getId() + c.getTitle();
        relPath += "\\" + ImageDataPaths.refIdentifier + countFiles(ImageDataPaths.assetAbsoluteLocation + relPath);
        try (FileOutputStream outputStream = new FileOutputStream(ImageDataPaths.assetAbsoluteLocation + relPath)) {
            outputStream.write(r.getImageData());
            log.info(relPath);
            return relPath;
        } catch (IOException e) {
            log.info(e.getMessage());
            e.printStackTrace();
            return "error";
        }
    }

    public String writeSketchImage(Commission c, Sketch s) {
        String relPath = ImageDataPaths.commissionLocation + c.getId() + "\\" + ImageDataPaths.sketchIdentifier + s.getId();
        try (FileOutputStream outputStream = new FileOutputStream(ImageDataPaths.assetAbsoluteLocation + relPath)) {
            outputStream.write(s.getImageData());
            return relPath;
        } catch (IOException e) {
            log.info(e.getMessage());
            e.printStackTrace();
            return "error";
        }
    }

    public String writeArtworkImage(Commission c, Artwork aw) {
        String relPath = ImageDataPaths.commissionLocation + c.getId() + "\\" + ImageDataPaths.awhIdentifier + aw.getId();
        try (FileOutputStream outputStream = new FileOutputStream(relPath)) {
            outputStream.write(aw.getImageData());
            return relPath;
        } catch (IOException e) {
            log.info(e.getMessage());
            e.printStackTrace();
            return "error";
        }
    }

    public void deleteCommissionComponent(String url) {
        File imageFile = new File(ImageDataPaths.assetAbsoluteLocation + url);
        imageFile.delete();
    }

    public void deleteCommission(Commission c) {
        String url = ImageDataPaths.assetAbsoluteLocation + ImageDataPaths.commissionLocation + c.getId();
        File delFile = new File(url);
        deleteDirectory(delFile);
    }

    boolean deleteDirectory(File directoryToBeDeleted) {
        File[] allContents = directoryToBeDeleted.listFiles();
        if (allContents != null) {
            for (File file : allContents) {
                deleteDirectory(file);
            }
        }
        return directoryToBeDeleted.delete();
    }














    public String writeArtistImage(Artwork a) {

        try (
            FileOutputStream outputStream = new FileOutputStream(
                ImageDataPaths.assetAbsoluteLocation
                    + ImageDataPaths.artistProfileLocation
                    + a.getArtist().getUserName() + "/"
                    + a.getName() + '.'
                    + a.getFileType().toString().toLowerCase(Locale.ROOT))) {
            outputStream.write(a.getImageData());

            return ImageDataPaths.artistProfileLocation + a.getArtist().getUserName() + "/" + a.getName() + '.' + a.getFileType().toString().toLowerCase(Locale.ROOT);
            //return  ImageDataPaths.artistProfilePictureLocation+"/"+a.getName();
        } catch (IOException e) {
            log.info(e.getMessage());
            e.printStackTrace();
            return "";
        }
    }



    public void deleteArtistImage(Artwork a) {
        File imageFile = new File(ImageDataPaths.assetAbsoluteLocation + ImageDataPaths.artistProfileLocation + a.getArtist().getUserName() + "/" + a.getName());
        imageFile.delete();
    }

    public String writeAndReplaceArtistProfileImage(Artist a) {

        File f = new File(ImageDataPaths.assetAbsoluteLocation + ImageDataPaths.artistProfilePictureLocation + a.getUserName());
        if (!f.isDirectory() || !f.exists()) {
            try {
                Files.createDirectories(f.toPath());
            } catch (IOException e) {
                log.info(e.getMessage());
                throw new FileManagerException(e.getMessage());
            }
        }
        File profilePicture = new File(ImageDataPaths.assetAbsoluteLocation + ImageDataPaths.artistProfilePictureLocation + a.getUserName() + "/"
            + ImageDataPaths.artistProfilePictureIdentifier);
        try (FileOutputStream outputStream = new FileOutputStream(profilePicture)) {
            outputStream.write(a.getProfilePicture().getImageData());
            return ImageDataPaths.artistProfilePictureLocation + a.getUserName() + "/"
                + ImageDataPaths.artistProfilePictureIdentifier;
        } catch (IOException e) {
            log.info(e.getMessage());
            e.printStackTrace();
            return "";
        }

    }


    public void renameArtistFolder(Artist artist, String oldUserName)   {
        File oldImageFile = new File(ImageDataPaths.assetAbsoluteLocation+ImageDataPaths.artistProfileLocation + oldUserName);
        try {
            Files.move(oldImageFile.toPath(), oldImageFile.toPath().resolveSibling(artist.getUserName()));
        } catch (IOException e) {
            log.info(e.getMessage());
            throw new FileManagerException(e.getMessage());
        }

    }
}
