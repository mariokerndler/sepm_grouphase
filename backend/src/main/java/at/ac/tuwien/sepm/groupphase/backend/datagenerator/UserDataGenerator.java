package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artwork;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtworkRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.utils.FileType;
import at.ac.tuwien.sepm.groupphase.backend.utils.UserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

@Profile("generateData")
@Component
public class UserDataGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final UserRepository userRepository;
    private final ArtistRepository artistRepository;
    private final PasswordEncoder passwordEncoder;
    private static final int NUMBER_OF_USERS_TO_GENERATE = 20;
    private  final ArtworkRepository artworkRepo;
    private static final int NUMBER_OF_ARTWORKS_TO_GENERATE = 6;
    private  static final String dir="./data";
    private  final ArtistRepository artistRepo;
    public UserDataGenerator(UserRepository userRepository, ArtistRepository artistRepository, PasswordEncoder passwordEncoder, ArtworkRepository artworkRepo, ArtistRepository artistRepo) {
        this.userRepository = userRepository;
        this.artistRepository = artistRepository;
        this.passwordEncoder = passwordEncoder;
        this.artworkRepo = artworkRepo;
        this.artistRepo = artistRepo;
    }

    @PostConstruct
    private void generateUser() {
        // TODO: Maybe differentiate by explicitly checking whether the one entity is actually the admin ?
        if (userRepository.findAll().size() > NUMBER_OF_USERS_TO_GENERATE + 1) {
            LOGGER.debug("User already generated");
        } else {
            for (int i = 0; i < NUMBER_OF_USERS_TO_GENERATE; i++) {
                ApplicationUser user = new ApplicationUser(String.format("testUser%s", i), "bob", "test", "test", "test", passwordEncoder.encode("test")
                    , false, UserRole.User);
                userRepository.save(user);
            }
        }
        Artist artist = new Artist(String.format("testUser%s", -1), "bob", "test", "test", "test", passwordEncoder.encode("test")
            , false, UserRole.Artist, null, null, 1.0, null, null, null, null, null);

        artistRepository.save(artist);

        //Artwork Generation.
        createFolderIfNotExists();
        fetchSampleImageData();
        if(artworkRepo.findAll().size()>= NUMBER_OF_ARTWORKS_TO_GENERATE){
            LOGGER.debug("Images already generated");
        }
        else{
            for (int i = 0; i < NUMBER_OF_ARTWORKS_TO_GENERATE; i++) {

                Artwork artwork = new Artwork(String.format("artwork%s", i + 1), "okay dog pls", String.format(dir + "/image%s", i), FileType.PNG, artistRepo.findAll().get(0), null, null);
                artworkRepo.save(artwork);

            }
        }
    }

    private void fetchSampleImageData()   {
        //using https://de.imgbb.com/ images deleted in 3 months.
        //BBcode Vollansicht of https://ibb.co/album/bb7VyX/embeds
        String urlsRaw="[img]https://i.ibb.co/nRZPtmX/image2.jpg[/img]\n" +
            "[img]https://i.ibb.co/jbdkttt/image1.jpg[/img]\n" +
            "[img]https://i.ibb.co/FwGKvtJ/image3.jpg[/img]\n" +
            "[img]https://i.ibb.co/yNRpzcV/175-1752789-mount-olympus-greek-mythology-mount-olympus.jpg[/img]\n" +
            "[img]https://i.ibb.co/4Tc7cNR/hqdefault.jpg[/img]\n" +
            "[img]https://i.ibb.co/Z64F6Xn/backPose.png[/img]";


        urlsRaw=urlsRaw.replace("[img]","");
        urlsRaw=urlsRaw.replace("/[img]","");
        String[] urls= urlsRaw.split("\n");

        for(int i=0;i<urls.length;i++){
            try(InputStream in = new URL(urls[i]).openStream()){
                String filename=this.dir+String.format("/image%s.png",i);
                File file = new File(filename);
                if(!file.exists()){
                    Files.copy(in, Paths.get(filename));
                    LOGGER.info("Saved Sample Image: "+filename);
                }
            }
            catch (IOException e){
                LOGGER.info("hm...");
            }
        }

    }
    private void createFolderIfNotExists(){
        File file = new File(dir);
        if(!file.exists()){
            file.mkdir();
            LOGGER.info("Created Folder: "+file.getAbsolutePath());
        }
        LOGGER.info("Folder already exists:" +file.getAbsolutePath());
    }

}

