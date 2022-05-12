package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.repository.ArtistRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ArtworkRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.lang.invoke.MethodHandles;

@Profile("generateData")
@Component
public class ArtworkGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private  final ArtworkRepository artworkRepo;
    private static final int NUMBER_OF_ARTWORKS_TO_GENERATE = 3;
    private  static final String path= "data/image";
    private  final ArtistRepository artistRepo;
    public ArtworkGenerator(ArtworkRepository artworkRepo, ArtistRepository artistRepo) {
        this.artworkRepo = artworkRepo;
        this.artistRepo=artistRepo;
    }

    /*
    @PostConstruct
    private void generateArtworks(){
        if(artworkRepo.findAll().size()> NUMBER_OF_ARTWORKS_TO_GENERATE+ 1){
            LOGGER.debug("Images already generated");
        }
        else{
            for (int i = 0; i < NUMBER_OF_ARTWORKS_TO_GENERATE; i++) {
                Artwork artwork = new Artwork(String.format("artwork%s",i+1),"okay dog pls",String.format(path+"/image%s",i), FileType.PNG,new Artist());
                artworkRepo.save(artwork);
            }
        }
    }*/
}
