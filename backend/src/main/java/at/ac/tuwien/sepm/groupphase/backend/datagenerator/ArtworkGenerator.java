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
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedList;

@Profile("generateData")
@Component
public class ArtworkGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private  final ArtworkRepository artworkRepo;
    private static final int NUMBER_OF_ARTWORKS_TO_GENERATE = 6;
    private  static final String dir="./data";
    private  final ArtistRepository artistRepo;
    public ArtworkGenerator(ArtworkRepository artworkRepo, ArtistRepository artistRepo) {
        this.artworkRepo = artworkRepo;
        this.artistRepo=artistRepo;
    }

    @PostConstruct
    private void generateArtworks(){


    }

    }
