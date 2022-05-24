package at.ac.tuwien.sepm.groupphase.backend.utils;

import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artwork;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@NoArgsConstructor
@Component
public class ImageFileManager {


    public void writeArtistImage(Artwork a){
        File outputImage = new File( ImageDataPaths.artistProfileLocation+a.getArtist().getUserName() +a.getName() + "." + a.getFileType());
        //
        // File directory = new File(ImageDataPaths.artistProfileLocation+a.getArtist().getUserName());
        if (!outputImage.getParentFile().exists()) {
            outputImage.getParentFile().mkdirs();
        }
        try (FileOutputStream outputStream = new FileOutputStream(outputImage, true)) {
            outputStream.write(a.getImageData());
        } catch (IOException e) {
            log.info(e.getMessage());
            e.printStackTrace();
        }
    }


    public List<byte[]> readArtistImages(Artist artist){
        List<byte[]> images = new ArrayList<>();
        try (Stream<Path> walk = Files.walk(Paths.get(ImageDataPaths.artistProfileLocation+""), 1)) {
            List<String> result = walk.filter(Files::isDirectory).map(Path::toString).collect(Collectors.toList());

            result.forEach(
            file->{
                try {
                    byte[] imageData= Files.readAllBytes(Path.of(file));
                    images.add(imageData);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return  images;
    }
    public void deleteArtistImage(Artwork a){
        File imageFile = new File( ImageDataPaths.artistProfileLocation+a.getArtist().getUserName()+a.getName());
        imageFile.delete();
    }

    public void writeAndReplaceArtistProfileImage(  Artist a) throws IOException {

        File f = new File(ImageDataPaths.artistProfilePictureLocation+"/"+a.getUserName());
        if(!f.isDirectory() || ! f.exists()){
            Files.createDirectories(f.toPath());
        }
        File profilePicture   = new File(ImageDataPaths.artistProfilePictureLocation+"/"+a.getUserName()+"/"
            +ImageDataPaths.artistProfilePictureIdentifier);
        try (FileOutputStream outputStream = new FileOutputStream(profilePicture)) {
            outputStream.write(a.getProfilePicture().getImageData());
        } catch (IOException e) {
            log.info(e.getMessage());
            e.printStackTrace();
        }

    }
    public  byte[] readArtistProfileImageData(Artist a){
                    try {
                        byte[] imageData= Files.readAllBytes(Path.of(ImageDataPaths.artistProfilePictureLocation+"/"+a.getUserName()+"/"
                            +ImageDataPaths.artistProfilePictureIdentifier));
                        return imageData;
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                    return null;
    }
    public void renameArtistFolder(Artist artist,String oldUserName) throws IOException {
        File oldImageFile = new File( ImageDataPaths.artistProfileLocation+oldUserName);
        Files.move(oldImageFile.toPath(),oldImageFile.toPath().resolveSibling( ImageDataPaths.artistProfileLocation+ artist.getUserName() ));
    }
}