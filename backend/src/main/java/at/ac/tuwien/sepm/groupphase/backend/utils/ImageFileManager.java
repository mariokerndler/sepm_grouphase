package at.ac.tuwien.sepm.groupphase.backend.utils;

import at.ac.tuwien.sepm.groupphase.backend.entity.*;
import com.sun.xml.bind.v2.model.core.Ref;
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

        private void createFolderIfNotExists(String url) throws IOException {
            File f = new File(url);
            if (!f.isDirectory() || !f.exists()) {
                Files.createDirectories(f.toPath());
            }
        }
        public void createCommission (Commission c) throws IOException {
            createFolderIfNotExists(ImageDataPaths.assetAbsoluteLocation+ImageDataPaths.commissionLocation+c.getId());
        }



        public  String writeReferenceImage(Commission c, Reference r){
            String relPath=   ImageDataPaths.commissionLocation +    c.getId() + "\\" +ImageDataPaths.refIdentifier+r.getId() ;
            try (FileOutputStream outputStream = new FileOutputStream(ImageDataPaths.assetAbsoluteLocation+relPath)) {
                outputStream.write(r.getImageData());
                return relPath;
            } catch (IOException e) {
                log.info(e.getMessage());
                e.printStackTrace();
                return "error";
            }
        }
    public  String writeSketchImage(Commission c, Sketch s){
        String relPath=   ImageDataPaths.commissionLocation +    c.getId() + "\\" +ImageDataPaths.sketchIdentifier+s.getId() ;
        try (FileOutputStream outputStream = new FileOutputStream(ImageDataPaths.assetAbsoluteLocation+relPath)) {
            outputStream.write(s.getImageData());
            return relPath;
        } catch (IOException e) {
            log.info(e.getMessage());
            e.printStackTrace();
            return "error";
        }
    }
    public  String writeArtworkImage(Commission c, Artwork aw){
        String relPath=   ImageDataPaths.commissionLocation +    c.getId() + "\\" +ImageDataPaths.awhIdentifier+aw.getId() ;
        try (FileOutputStream outputStream = new FileOutputStream(relPath)) {
            outputStream.write(aw.getImageData());
            return relPath;
        } catch (IOException e) {
            log.info(e.getMessage());
            e.printStackTrace();
            return "error";
        }
    }

    public void  deleteCommissionComponent(String url){
        File imageFile = new File(ImageDataPaths.assetAbsoluteLocation +url);
        imageFile.delete();
    }

    public void   deleteCommission(Commission c) {
            String url=ImageDataPaths.assetAbsoluteLocation+ImageDataPaths.commissionLocation+c.getId();
            File delFile= new File(url);
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



    public void deleteArtistImage(Artwork a) {
        File imageFile = new File(ImageDataPaths.assetAbsoluteLocation + ImageDataPaths.artistProfileLocation + a.getArtist().getUserName() + "/" + a.getName());
        imageFile.delete();
    }

    public String writeAndReplaceArtistProfileImage(Artist a) throws IOException {

        File f = new File(ImageDataPaths.assetAbsoluteLocation + ImageDataPaths.artistProfilePictureLocation + a.getUserName());
        if (!f.isDirectory() || !f.exists()) {
            Files.createDirectories(f.toPath());
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
    public void renameArtistFolder(Artist artist, String oldUserName) throws IOException {
        File oldImageFile = new File(ImageDataPaths.artistProfileLocation + oldUserName);
        Files.move(oldImageFile.toPath(), oldImageFile.toPath().resolveSibling(ImageDataPaths.artistProfileLocation + artist.getUserName()));
    }
}
