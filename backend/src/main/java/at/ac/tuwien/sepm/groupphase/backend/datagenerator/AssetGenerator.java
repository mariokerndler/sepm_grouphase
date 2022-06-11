package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.exception.FileManagerException;
import at.ac.tuwien.sepm.groupphase.backend.utils.AssetUrls;
import at.ac.tuwien.sepm.groupphase.backend.utils.FileType;
import at.ac.tuwien.sepm.groupphase.backend.utils.ImageDataPaths;
import at.ac.tuwien.sepm.groupphase.backend.utils.ImageFileManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.URL;

@Slf4j
@Profile("generateAssets")
@Component
public class AssetGenerator {

    private final ImageFileManager imageFileManager;

    public AssetGenerator(ImageFileManager imageFileManager) {
        this.imageFileManager = imageFileManager;
    }

    @PostConstruct
    private void generateAssets() {
        downloadDefaultPfpIfNotPresent();
    }

    private void downloadDefaultPfpIfNotPresent() {

        File f = new File(ImageDataPaths.assetAbsoluteLocation + ImageDataPaths.defaultUserProfilePictureLocation + "." + FileType.PNG.name().toLowerCase());

        if (!f.exists() && !f.isFile()) {
            try {
                URL url = new URL(AssetUrls.default_pfp_url);

                OutputStream output = new FileOutputStream(f);

                InputStream stream = url.openStream();

                output.write(stream.readAllBytes());

            } catch (IOException e) {
                log.info(e.getMessage());
                throw new FileManagerException(e.getMessage());
            }
        }

    }
}