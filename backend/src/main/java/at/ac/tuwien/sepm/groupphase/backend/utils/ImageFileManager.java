package at.ac.tuwien.sepm.groupphase.backend.utils;

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
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@NoArgsConstructor
@Component
public class ImageFileManager {


    public String writeArtistImage(Artwork a) {

        try (
            FileOutputStream outputStream = new FileOutputStream(ImageDataPaths.assetAbsoluteLocation + ImageDataPaths.artistProfileLocation + a.getArtist().getUserName() + "/" + a.getName())) {
            outputStream.write(a.getImageData());

            return ImageDataPaths.artistProfilePictureLocation + "/" + a.getName();
            //return  ImageDataPaths.artistProfilePictureLocation+"/"+a.getName();
        } catch (IOException e) {
            log.info(e.getMessage());
            e.printStackTrace();
            return "";
        }
    }


    public List<byte[]> readArtistImages(Artist artist) {
        List<byte[]> images = new ArrayList<>();
        try (Stream<Path> walk = Files.walk(Paths.get(ImageDataPaths.artistProfileLocation + ""), 1)) {
            List<String> result = walk.filter(Files::isDirectory).map(Path::toString).collect(Collectors.toList());

            result.forEach(
                file -> {
                    try {
                        byte[] imageData = Files.readAllBytes(Path.of(file));
                        images.add(imageData);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return images;
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

    public byte[] readArtistProfileImageData(Artist a) {
        try {
            byte[] imageData = Files.readAllBytes(Path.of(ImageDataPaths.artistProfilePictureLocation + a.getUserName() + "/"
                + ImageDataPaths.artistProfilePictureIdentifier));
            return imageData;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void renameArtistFolder(Artist artist, String oldUserName) {
        File oldImageFile = new File(ImageDataPaths.artistProfileLocation + oldUserName);
        try {
            Files.move(oldImageFile.toPath(), oldImageFile.toPath().resolveSibling(artist.getUserName()));
        } catch (IOException e) {
            log.info(e.getMessage());
            throw new FileManagerException(e.getMessage());
        }

    }
}
