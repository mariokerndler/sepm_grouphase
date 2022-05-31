package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtworkRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Slf4j
@Profile("generateData")
@Component
public class ArtworkGenerator {

    private static final int NUMBER_OF_ARTWORKS_TO_GENERATE = 6;
    private static final String dir = "./data";
    private final ArtworkRepository artworkRepo;
    private final ArtistRepository artistRepo;

    public ArtworkGenerator(ArtworkRepository artworkRepo, ArtistRepository artistRepo) {
        this.artworkRepo = artworkRepo;
        this.artistRepo = artistRepo;
    }

    @PostConstruct
    private void generateArtworks() {


    }

}
