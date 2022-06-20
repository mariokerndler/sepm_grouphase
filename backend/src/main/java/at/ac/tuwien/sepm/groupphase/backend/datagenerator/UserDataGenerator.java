package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.entity.*;
import at.ac.tuwien.sepm.groupphase.backend.repository.*;
import at.ac.tuwien.sepm.groupphase.backend.service.ArtistService;
import at.ac.tuwien.sepm.groupphase.backend.service.CommissionService;
import at.ac.tuwien.sepm.groupphase.backend.utils.ImageDataPaths;
import at.ac.tuwien.sepm.groupphase.backend.utils.ImageFileManager;
import at.ac.tuwien.sepm.groupphase.backend.utils.enums.CommissionStatus;
import at.ac.tuwien.sepm.groupphase.backend.utils.enums.FileType;
import at.ac.tuwien.sepm.groupphase.backend.utils.enums.UserRole;
import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Profile("generateData")
@Component
public class UserDataGenerator {

    private static final int NUMBER_OF_USERS_TO_GENERATE = 20;
    private static final int NUMBER_OF_PROFILES_TO_GENERATE = 40;
    private static final int NUMBER_OF_TAGS_TO_GENERATE = 30;
    private static final int NUMBER_OF_COMMISSIONS_TO_GENERATE = 20;
    private final ArtistService artistService;
    private final UserRepository userRepository;
    private final ArtistRepository artistRepository;
    private final PasswordEncoder passwordEncoder;
    private final ArtworkRepository artworkRepo;
    private final TagRepository tagRepository;
    private final ChatRepository chatRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final CommissionRepository commissionRepository;
    private final CommissionService commissionService;
    private String[] urls = new String[]{
        "https://i.ibb.co/HTT7Ym3/image0.jpg",
        "https://i.ibb.co/7yHp276/image1.jpg",
        "https://i.ibb.co/cDT8JHg/image2.jpg",
        "https://i.ibb.co/wy4PbD4/image3.jpg",
        "https://i.ibb.co/jfQR7W9/sketch1.jpg",
        "https://i.ibb.co/JRcbTDk/sketch2.jpg",
        "https://i.ibb.co/pdtMdcJ/sketch3.jpg",
        "https://i.ibb.co/09Fk1PB/sketch4.jpg",
        "https://i.ibb.co/pf63fMd/sketch.gif"
    };

    public UserDataGenerator(ArtistService artistService,
                             UserRepository userRepository,
                             ArtistRepository artistRepository,
                             PasswordEncoder passwordEncoder,
                             ArtworkRepository artworkRepo,
                             TagRepository tagRepository,
                             ChatRepository chatRepository, ChatMessageRepository chatMessageRepository, CommissionRepository commissionRepository, CommissionService commissionService) {
        this.artistService = artistService;
        this.userRepository = userRepository;
        this.artistRepository = artistRepository;
        this.passwordEncoder = passwordEncoder;
        this.artworkRepo = artworkRepo;
        this.tagRepository = tagRepository;
        this.chatRepository = chatRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.commissionRepository = commissionRepository;
        this.commissionService = commissionService;
    }

    @PostConstruct
    private void generateUser() throws IOException {
        if (tagRepository.findAll().size() < NUMBER_OF_TAGS_TO_GENERATE) {
            loadTags(NUMBER_OF_TAGS_TO_GENERATE);
        }

        if (artistRepository.findAll().size() < NUMBER_OF_PROFILES_TO_GENERATE) {
            loadProfiles(NUMBER_OF_PROFILES_TO_GENERATE);
        }

        if (artistRepository.findApplicationUserByEmail("testartist@test.com") == null) {
            generateArtistTestAccount("testArtist", "12345678");
        }

        if (artistRepository.findApplicationUserByEmail("testuser@test.com") == null) {
            generateUserTestAccount("testUser", "12345678");
        }

        if (userRepository.findAll().size() - NUMBER_OF_PROFILES_TO_GENERATE + 2 < NUMBER_OF_USERS_TO_GENERATE) {
            generateUsers(NUMBER_OF_USERS_TO_GENERATE);
        }

        if (commissionRepository.findAll().size() < 2) {
            generateTestCommissions();
        }
        generateChats();
    }

    private void generateChats() {
        List<ApplicationUser> users = this.userRepository.findAll();

        ApplicationUser testUser = (ApplicationUser) users.stream().filter(a -> a.getUserName().toLowerCase().equals("testartist")).findAny().get();
        Faker f = new Faker();
        for (int i = 0; i < 5; i++) {
            Chat c = new Chat();
            c.setUser(testUser);
            c.setChatPartner(users.get(i + 5));
            this.chatRepository.save(c);
            for (int j = 0; j < 50; j++) {
                ChatMessage message = new ChatMessage();
                message.setMessage(f.shakespeare().romeoAndJulietQuote());
                if (j % 2 == 0) {
                    message.setFromId(c.getUser().getId());
                    message.setToId(c.getChatPartner().getId());
                } else {
                    message.setToId(c.getUser().getId());
                    message.setFromId(c.getChatPartner().getId());
                }
                message.setSentDate(LocalDateTime.now());
                message.setChat(c);

                this.chatMessageRepository.save(message);
            }


        }
    }

    private void loadTags(int numberOfTags) throws FileNotFoundException {
        File text = new File(ImageDataPaths.assetAbsoluteLocation + ImageDataPaths.tagLocation);
        List<String> tags = new LinkedList<>();
        Scanner scanner = new Scanner(text);
        while (scanner.hasNext() && numberOfTags > 0) {
            numberOfTags--;
            Tag t = new Tag(scanner.nextLine());
            if (!tags.contains(t.getName())) {
                tags.add(t.getName());
                tagRepository.save(t);
            }
        }
    }

    //make sure db is empty before running to avoid Unique key constraint issues
    private void loadProfiles(int numberOfProfiles) {
        List<Tag> tags = tagRepository.findAll();
        log.info(ImageDataPaths.assetAbsoluteLocation + ImageDataPaths.artistProfileLocation);
        try (Stream<Path> walk = Files.walk(Paths.get(ImageDataPaths.assetAbsoluteLocation + ImageDataPaths.artistProfileLocation), 1)) {

            List<String> result = walk.filter(Files::isDirectory).map(Path::toString).collect(Collectors.toList());
            int limit = numberOfProfiles;
            if (numberOfProfiles > result.size() - 1) {
                limit = result.size() - 1;
            }
            result.subList(1, limit).forEach(
                folder -> {
                    log.info(folder.toString());
                    File fldr = new File(folder);
                    Artist a = generateArtistProfile(fldr.getName());

                    artistService.saveArtist(a);

                    log.info("Saved artist: " + a.getUserName());
                    File artistProfileDir = new File(folder);
                    File[] artistProfileDirectoryListing = artistProfileDir.listFiles();
                    if (artistProfileDirectoryListing != null) {
                        for (File artworkFile : artistProfileDirectoryListing) {
                            if (artworkFile.isFile()) {
                                String description = new Faker().harryPotter().quote();
                                if (description.length() > 50) {
                                    description = description.substring(0, 50);
                                }

                                String name = artworkFile.getName();
                                if (name.length() > 50) {
                                    name = name.substring(0, 50);
                                }

                                List<Tag> selectedTags = new LinkedList<>();

                                // Generate random sequence of numbers from 0 to tags.size()-1
                                ArrayList numbers = new ArrayList();
                                for (int i = 0; i < tags.size(); i++) {
                                    numbers.add(i);
                                }

                                Collections.shuffle(numbers);

                                // Add a random number of tags to list by selecting the first few
                                Faker f = new Faker();
                                for (int j = 0; j < f.random().nextInt(0, 10); j++) {
                                    selectedTags.add(tags.get(j));
                                }

                                Artwork artwork = new Artwork();
                                artwork.setTags(selectedTags);
                                artwork.setDescription(description);
                                artwork.setArtist(a);
                                artwork.setFileType(FileType.JPG);
                                artwork.setImageUrl(artworkFile.toString().replace(ImageDataPaths.assetAbsoluteLocation, ""));
                                artwork.setName(name);
                                artworkRepo.save(artwork);

                                log.info("Saved artwork: " + artwork.getImageUrl());
                            }
                        }
                    } else {
                        log.info("Error saving  artwork: ");
                    }

                });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Artist generateArtistProfile(String username) {
        Faker faker = new Faker();
        Artist artist = new Artist();
        artist.setAdmin(false);
        artist.setUserName(username);
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

    private ApplicationUser generateUserProfile(String username) {
        Faker faker = new Faker();
        ApplicationUser user = new ApplicationUser();
        user.setAdmin(false);
        user.setUserName(username);
        user.setName(faker.name().firstName());
        user.setSurname(faker.name().lastName());
        user.setAddress(faker.address().fullAddress());
        user.setEmail(faker.internet().emailAddress());
        user.setPassword(passwordEncoder.encode(faker.internet().password(8, 15)));
        user.setUserRole(UserRole.User);
        return user;
    }

    private void generateArtistTestAccount(String username, String password) throws IOException {
        Artist artist = generateArtistProfile(username);
        artist.setEmail(username.toLowerCase() + "@test.com");
        artist.setPassword(passwordEncoder.encode(password));
        artistService.saveArtist(artist);
    }

    private void generateUserTestAccount(String username, String password) {
        ApplicationUser user = generateUserProfile(username);
        user.setEmail(username.toLowerCase() + "@test.com");
        user.setPassword(passwordEncoder.encode(password));
        userRepository.save(user);
    }

    private void generateUsers(int numberOfUsers) {
        for (int i = 0; i < numberOfUsers; i++) {
            Faker faker = new Faker();
            ApplicationUser user = generateUserProfile(faker.starTrek().character() + i);
            userRepository.save(user);
        }
    }

    private void generateTestCommissions() throws IOException {

        List<ApplicationUser> users = userRepository.findAll();
        List<Artist> artists = artistService.getAllArtists();

        Commission c = generateCommission2(artists.get(0), users.get(0));
        commissionService.saveCommission(c);
        Commission d = generateCommission1(artists.get(1), users.get(1));
        commissionService.saveCommission(d);
        Commission e = generateCommission3(artists.get(0), users.get(1));
        commissionService.saveCommission(e);
        Commission f = generateCommission4(artists.get(0), users.get(1));
        commissionService.saveCommission(f);
        Commission g = generateCommission5(artists.get(0), users.get(1));
        commissionService.saveCommission(g);


    }

    private Commission generateCommission1(Artist artist, ApplicationUser user) {

        Faker faker = new Faker();
        Commission commission = new Commission();
        commission.setStatus(CommissionStatus.IN_PROGRESS);
        commission.setArtist(artist);
        commission.setCustomer(user);
        commission.setTitle("Sample Commission");
        commission.setSketchesShown((int) (Math.random() * 10));
        commission.setFeedbackSent((int) (Math.random() * 6));
        commission.setPrice((int) (Math.random() * 10000));
        commission.setIssueDate(LocalDateTime.now());
        commission.setDeadlineDate(LocalDateTime.now().plusDays((int) (Math.random() * 100)));
        commission.setStatus(CommissionStatus.LISTED);
        String desc = faker.shakespeare().hamletQuote().toString();
        if (desc.length() > 50) {
            desc = desc.substring(0, 49);
        }
        commission.setInstructions(desc);
        Artwork a = new Artwork();
        List<Sketch> sketches = new LinkedList<Sketch>();
        for (int i = 4; i > 1; i--) {
            if (i == 4) {
                a.setArtist(artist);
                a.setName("Sample Commission Art");
                a.setDescription(faker.harryPotter().quote().toString());
                a.setImageUrl("data\\com\\adminSample Commission\\sketch" + i);
                a.setFileType(FileType.JPG);
            } else {
                Sketch k = new Sketch();
                k.setDescription("Sketch " + i);
                k.setFileType(FileType.JPG);
                k.setArtwork(a);
                k.setImageUrl("data\\com\\adminSample Commission\\sketch" + i);
                sketches.add(k);

            }
        }
        a.setSketches(sketches);
        commission.setArtwork(a);
        return commission;
    }

    private Commission generateCommission2(Artist artist, ApplicationUser user) throws IOException {

        Faker faker = new Faker();
        Commission commission = new Commission();
        commission.setStatus(CommissionStatus.IN_PROGRESS);
        commission.setArtist(artist);
        commission.setCustomer(user);
        commission.setTitle("Sample Commission");
        commission.setSketchesShown(2);
        commission.setFeedbackSent(1);
        commission.setPrice((int) (Math.random() * 10000));
        commission.setFeedbackRounds(4);
        commission.setIssueDate(LocalDateTime.now());
        commission.setDeadlineDate(LocalDateTime.now().plusDays((int) (Math.random() * 100)));
        commission.setStatus(CommissionStatus.IN_PROGRESS);
        String desc = faker.shakespeare().hamletQuote().toString();
        if (desc.length() > 50) {
            desc = desc.substring(0, 49);
        }
        commission.setInstructions(desc);
        List<Reference> references = new LinkedList<Reference>();
        Artwork a = new Artwork();
        List<Sketch> sketches = new LinkedList<Sketch>();
        for (int i = 5; i > 1; i--) {
            if (i == 4) {
                a.setArtist(artist);
                a.setName("Sample Commission Art");
                desc = faker.shakespeare().hamletQuote().toString();
                if (desc.length() > 50) {
                    desc = desc.substring(0, 49);
                }
                a.setDescription(desc);
                a.setImageUrl("data\\com\\adminSample Commission2\\b" + i);
                a.setFileType(FileType.JPG);
            } else {
                if (i % 2 != 0) {
                    Sketch k = new Sketch();
                    k.setFileType(FileType.JPG);
                    k.setArtwork(a);
                    k.setImageData(getImageBytes(urls[i]));
                    k.setDescription("Sketch " + i);
                    k.setImageUrl("data\\com\\adminSample Commission2\\b" + i);
                    if (i == 3) {
                        k.setCustomerFeedback("nice work " + i);
                    }
                    sketches.add(k);
                }

            }
        }
        Reference reference = new Reference();
        reference.setImageUrl("data\\com\\adminSample Commission2\\bReference");
        reference.setImageData(getImageBytes(urls[0]));
        reference.setCommission(commission);
        reference.setFileType(FileType.JPG);
        references.add(reference);
        a.setSketches(sketches);
        commission.setReferences(references);
        commission.setArtwork(a);
        return commission;
    }

    private Commission generateCommission3(Artist artist, ApplicationUser user) throws IOException {

        Faker faker = new Faker();
        Commission commission = new Commission();
        commission.setArtist(artist);
        commission.setCustomer(user);
        commission.setTitle("Sample Commission");
        commission.setSketchesShown(4);
        commission.setFeedbackSent(4);
        commission.setPrice((int) (Math.random() * 10000));
        commission.setFeedbackRounds(4);
        commission.setIssueDate(LocalDateTime.now().minusDays((int) (Math.random() * 100)));
        commission.setDeadlineDate(LocalDateTime.now());
        commission.setStatus(CommissionStatus.COMPLETED);
        String desc = faker.shakespeare().hamletQuote().toString();
        if (desc.length() > 50) {
            desc = desc.substring(0, 49);
        }
        commission.setInstructions(desc);
        List<Reference> references = new LinkedList<Reference>();
        Artwork a = new Artwork();
        List<Sketch> sketches = new LinkedList<Sketch>();
        ImageFileManager ifm = new ImageFileManager();
        String assetAbsoluteLocation = Path.of("").toAbsolutePath().toString().replace("\\backend", "") + "\\frontend\\src\\assets\\";
        ifm.createFolderIfNotExists(assetAbsoluteLocation+"data\\com\\adminSample Commission3\\");
        for (int i = 5; i > 1; i--) {
            if (i == 4) {
                a.setArtist(artist);
                a.setName("Sample Commission Art");
                desc = faker.shakespeare().hamletQuote().toString();
                if (desc.length() > 50) {
                    desc = desc.substring(0, 49);
                }
                a.setDescription(desc);
                a.setImageUrl("data\\com\\adminSample Commission3\\b" + i);
                a.setImageData(getImageBytes(urls[3]));
                a.setFileType(FileType.JPG);
            } else {
                Sketch k = new Sketch();
                k.setFileType(FileType.JPG);
                k.setArtwork(a);
                k.setImageData(getImageBytes(urls[i]));
                k.setDescription("Sketch " + i);
                k.setImageUrl("data\\com\\adminSample Commission3\\b" + i);
                k.setCustomerFeedback("looks great " + i);
                sketches.add(k);
            }
        }
        Reference reference = new Reference();
        reference.setImageUrl("data\\com\\adminSample Commission3\\bReference");
        reference.setImageData(getImageBytes(urls[0]));
        reference.setCommission(commission);
        reference.setFileType(FileType.JPG);
        references.add(reference);
        Reference reference1 = new Reference();
        reference1.setImageUrl("data\\com\\adminSample Commission3\\bReference1");
        reference1.setImageData(getImageBytes(urls[1]));
        reference1.setCommission(commission);
        reference1.setFileType(FileType.JPG);
        references.add(reference1);
        a.setSketches(sketches);
        Artwork artwork = new Artwork();
        artwork.setDescription(desc);
        artwork.setImageUrl("data\\com\\adminSample Commission3\\artwork");
        artwork.setImageData(getImageBytes(urls[3]));
        artwork.setFileType(FileType.JPG);
        ifm.writeReferenceDatagenImage(commission, reference, "data\\com\\adminSample Commission3\\bReference");
        ifm.writeReferenceDatagenImage(commission, reference1, "data\\com\\adminSample Commission3\\bReference1");
        commission.setArtwork(artwork);
        commission.setReferences(references);
        commission.setArtwork(a);
        return commission;
    }

    private Commission generateCommission4(Artist artist, ApplicationUser user) throws IOException {

        Faker faker = new Faker();
        Commission commission = new Commission();
        commission.setArtist(artist);
        commission.setCustomer(user);
        commission.setTitle("Sample Commission");
        commission.setSketchesShown(4);
        commission.setFeedbackSent(4);
        commission.setPrice((int) (Math.random() * 10000));
        commission.setFeedbackRounds(4);
        commission.setIssueDate(LocalDateTime.now());
        commission.setDeadlineDate(LocalDateTime.now().plusDays((int) (Math.random() * 100)));
        commission.setStatus(CommissionStatus.COMPLETED);
        String desc = faker.shakespeare().hamletQuote().toString();
        if (desc.length() > 50) {
            desc = desc.substring(0, 49);
        }
        commission.setInstructions(desc);
        List<Reference> references = new LinkedList<Reference>();
        Artwork a = new Artwork();
        List<Sketch> sketches = new LinkedList<Sketch>();
        ImageFileManager ifm = new ImageFileManager();
        String assetAbsoluteLocation = Path.of("").toAbsolutePath().toString().replace("\\backend", "") + "\\frontend\\src\\assets\\";
        ifm.createFolderIfNotExists(assetAbsoluteLocation+"data\\com\\adminSample Commission4\\");
        for (int i = 5; i > 1; i--) {
            if (i == 4) {
                a.setArtist(artist);
                a.setName("Sample Commission Art");
                desc = faker.shakespeare().hamletQuote().toString();
                if (desc.length() > 50) {
                    desc = desc.substring(0, 49);
                }
                a.setDescription(desc);
                a.setImageUrl("data\\com\\adminSample Commission4\\b" + i);
                a.setFileType(FileType.JPG);
            } else {
                Sketch k = new Sketch();
                k.setFileType(FileType.JPG);
                k.setArtwork(a);
                k.setImageData(getImageBytes(urls[i+3]));
                k.setDescription("Sketch " + i);
                k.setImageUrl("data\\com\\adminSample Commission4\\b" + i);
                k.setCustomerFeedback("looks good " + i);
                sketches.add(k);

            }
        }
        Reference reference = new Reference();
        reference.setImageUrl("data\\com\\adminSample Commission4\\bReference");
        reference.setImageData(getImageBytes(urls[1]));
        reference.setCommission(commission);
        reference.setFileType(FileType.JPG);
        references.add(reference);
        ifm.writeReferenceDatagenImage(commission, reference, "data\\com\\adminSample Commission4\\bReference");
        a.setSketches(sketches);
        commission.setArtistCandidates(null);
        commission.setReferences(references);
        commission.setArtwork(a);
        return commission;
    }

    private Commission generateCommission5(Artist artist, ApplicationUser user) {
        List<Artist> artists = artistService.getAllArtists();
        Faker faker = new Faker();
        Commission commission = new Commission();
        commission.setStatus(CommissionStatus.IN_PROGRESS);
        commission.setArtist(artist);
        commission.setCustomer(user);
        commission.setTitle("Sample Commission");
        commission.setSketchesShown(0);
        commission.setFeedbackSent(0);
        commission.setPrice((int) (Math.random() * 10000));
        commission.setIssueDate(LocalDateTime.now());
        commission.setDeadlineDate(LocalDateTime.now().plusDays((int) (Math.random() * 100)));
        commission.setStatus(CommissionStatus.LISTED);
        String desc = faker.shakespeare().hamletQuote().toString();
        if (desc.length() > 50) {
            desc = desc.substring(0, 49);
        }
        commission.setArtistCandidates(artists.subList(0,5));
        commission.setInstructions(desc);
        Artwork a = new Artwork();
        List<Sketch> sketches = new LinkedList<Sketch>();
        for (int i = 4; i > 1; i--) {
            if (i == 4) {
                a.setArtist(artist);
                a.setName("Sample Commission Art");
                a.setDescription(faker.harryPotter().quote().toString());
                a.setImageUrl("data\\com\\adminSample Commission5\\sketch" + i);
                a.setFileType(FileType.JPG);
            } else {
                Sketch k = new Sketch();
                k.setDescription("Sketch " + i);
                k.setFileType(FileType.JPG);
                k.setArtwork(a);
                k.setImageUrl("data\\com\\adminSample Commission5\\sketch" + i);
                sketches.add(k);

            }
        }
        a.setSketches(sketches);
        commission.setArtwork(a);
        return commission;
    }

    public static byte[] getImageBytes(String imageUrl) throws IOException {
        URL url = new URL(imageUrl);

        ByteArrayOutputStream output = new ByteArrayOutputStream();

        try (InputStream stream = url.openStream()) {
            byte[] buffer = new byte[4096];

            while (true) {
                int bytesRead = stream.read(buffer);
                if (bytesRead < 0) {
                    break;
                }
                output.write(buffer, 0, bytesRead);
            }
        }

        return output.toByteArray();
    }

    //old approach not working, issues with cloudflare protection 403 error.
    private void downloadSamplePicture(int numberOfImages) throws IOException {
        // only  incr. when actually saving
        int counter = 0;
        while (numberOfImages > 0) {
            numberOfImages--;
            log.info("Images loading..." + counter);
            URL url = new URL("https://www.artstation.com/artwork/q9mA4D");
            HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
            con.setRequestProperty("User-Agent", "psb");
            con.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
            //      con.setConnectTimeout(10000);
            con.connect();
            try (InputStream in = con.getInputStream()) {
                String filename = ImageDataPaths.artistProfileLocation + String.format("/artwork%s.png", counter);
                File file = new File(filename);
                if (!file.exists()) {
                    Files.copy(in, Paths.get(filename));
                    log.info("Saved Sample Image: " + filename);
                    counter++;

                }
            } catch (IOException e) {
                log.info(e.getMessage());
            }
        }
    }
}

