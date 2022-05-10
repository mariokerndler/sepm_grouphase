package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.repository.ArtworkRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.invoke.MethodHandles;

@Profile("generateData")
@Component
public class ArtworkGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private  final ArtworkRepository artworkRepo;
    private static final int NUMBER_OF_ARTWORKS_TO_GENERATE = 5;

    public ArtworkGenerator(ArtworkRepository artworkRepo) {
        this.artworkRepo = artworkRepo;
    }

    @PostConstruct
    private void generateArtworks(){
        if(artworkRepo.findAll().size()> NUMBER_OF_ARTWORKS_TO_GENERATE+ 1){

        }
    }
}
