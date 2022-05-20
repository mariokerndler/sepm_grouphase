package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artwork;
import at.ac.tuwien.sepm.groupphase.backend.entity.Tag;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtworkRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TagRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.utils.FileType;
import at.ac.tuwien.sepm.groupphase.backend.utils.UserRole;
import com.github.javafaker.Faker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.net.ssl.HttpsURLConnection;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Profile("generateData")
@Component
public class UserDataGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final int NUMBER_OF_USERS_TO_GENERATE = 20;
    private static final int NUMBER_OF_PROFILES_TO_GENERATE = 20;
    private static final int NUMBER_OF_TAGS_TO_GENERATE = 30;
    private static final String artistProfileDir = "./data/ap";
    private static final String tagDir = "./data/tags/tags.txt";
    private final UserRepository userRepository;
    private final ArtistRepository artistRepository;
    private final PasswordEncoder passwordEncoder;
    private final ArtworkRepository artworkRepo;
    private final TagRepository tagRepository;

    private final ArtistRepository artistRepo;

    public UserDataGenerator(UserRepository userRepository, ArtistRepository artistRepository, PasswordEncoder passwordEncoder, ArtworkRepository artworkRepo, TagRepository tagRepository, ArtistRepository artistRepo) {
        this.userRepository = userRepository;
        this.artistRepository = artistRepository;
        this.passwordEncoder = passwordEncoder;
        this.artworkRepo = artworkRepo;
        this.tagRepository = tagRepository;
        this.artistRepo = artistRepo;
    }

    @PostConstruct
    private void generateUser() throws IOException {
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


        loadTags(NUMBER_OF_TAGS_TO_GENERATE);
        loadProfiles(NUMBER_OF_PROFILES_TO_GENERATE);

    }
    private void loadTags(int numberOftags) throws FileNotFoundException {
        File text = new File(tagDir);
        List<String> tags= new LinkedList<>();
        Scanner scanner= new Scanner(text);
        while (scanner.hasNext() && numberOftags>0){
            numberOftags--;
            Tag t = new Tag(scanner.nextLine());
            if(!tags.contains(t.getName())) {
                tags.add(t.getName());
                tagRepository.save(t);
            }
        }
    }

    //make sure db is empty before running to avoid Unique key constraint issues
    private void loadProfiles(int numberOfProfiles)   {
        List<Tag> tags = tagRepository.findAll();

        try (Stream<Path> walk = Files.walk(Paths.get(artistProfileDir), 1)) {

            List<String> result = walk.filter(Files::isDirectory).map(Path::toString).collect(Collectors.toList());

            result.subList(0,numberOfProfiles).forEach(

                folder -> {

                    LOGGER.info(folder.toString());
                    Artist a = generateArtistProfile();
                    artistRepository.save(a);
                    LOGGER.info("Saved artist: "+a.getUserName());
                    File artistProfileDir = new File(folder);
                    File[] artistProfileDirectoryListing = artistProfileDir.listFiles();
                    if (artistProfileDirectoryListing != null) {
                        for (File artworkFile : artistProfileDirectoryListing) {
                            if (artworkFile.isFile()) {
                                Faker f = new Faker();
                                Artwork artwork = new Artwork();
                                String description = new Faker().rickAndMorty().quote();
                                if (description.length() > 50) {
                                    description = description.substring(0, 50);
                                }
                                String name = artworkFile.getName();
                                if (name.length() > 50) {
                                    name = name.substring(0, 50);
                                }
                                List<Tag> selectedTags = new LinkedList<Tag>();

                                for (int j = 0; j < f.random().nextInt(0, 10); j++) {

                                    selectedTags.add(tags.get(f.random().nextInt(0, tags.size() - 1)));
                                }

                                artwork.setTags(selectedTags);
                                artwork.setDescription(description);
                                artwork.setArtist(a);
                                artwork.setFileType(FileType.JPG);
                                artwork.setImageUrl(artworkFile.toString());
                                artwork.setName(name);
                                artworkRepo.save(artwork);

                                LOGGER.info("Saved artwork: " + artwork.getImageUrl());
                            }
                        }
                    }

                    else{
                        LOGGER.info("Error saving  artwork: ");
                    }

                });
        }

        catch(IOException e){
            e.printStackTrace();
        }

    }

    private Artist generateArtistProfile () {

        Faker faker = new Faker();
        Artist artist = new Artist();
        artist.setAdmin(false);
        artist.setUserName(faker.name().username());
        artist.setName(faker.name().firstName());
        artist.setSurname(faker.name().lastName());
        artist.setDescription(faker.university().name());
        artist.setReviewScore(faker.random().nextInt(0, 5));
        artist.setAddress(faker.address().fullAddress());
        artist.setEmail(faker.internet().emailAddress());
        artist.setPassword(passwordEncoder.encode(faker.internet().password(8, 15)));
        artist.setUserRole(UserRole.Artist);
        return artist;
    }

    //old approach not working, issues with cloudflare protection 403 error.
    private void downloadSamplePicture ( int numberOfImages) throws IOException {
        // only  incr. when actually saving
        int counter = 0;

        while (numberOfImages > 0) {


            numberOfImages--;

            LOGGER.info("Images loading..." + counter);

            URL url = new URL("https://www.artstation.com/artwork/q9mA4D");
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            con.setRequestProperty("User-Agent", "psb");
            con.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
            //      con.setConnectTimeout(10000);

            con.connect();
            try (InputStream in = con.getInputStream()) {
                String filename = artistProfileDir + String.format("/artwork%s.png", counter);
                File file = new File(filename);
                if (!file.exists()) {
                    Files.copy(in, Paths.get(filename));
                    LOGGER.info("Saved Sample Image: " + filename);
                    counter++;

                }
            } catch (MalformedURLException e) {
                LOGGER.info(e.getMessage());
            } catch (IOException e) {
                LOGGER.info(e.getMessage());
            }


        }

    }

}

