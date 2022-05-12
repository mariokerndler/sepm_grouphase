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
    private  static final String path= "data/image";
    private  static final String dir="./data";
    private  final ArtistRepository artistRepo;
    public ArtworkGenerator(ArtworkRepository artworkRepo, ArtistRepository artistRepo) {
        this.artworkRepo = artworkRepo;
        this.artistRepo=artistRepo;
    }

    @PostConstruct
    private void generateArtworks(){
        createFolderIfNotExists();
        fetchSampleImageData();
        if(artworkRepo.findAll().size()> NUMBER_OF_ARTWORKS_TO_GENERATE+ 1){
            LOGGER.debug("Images already generated");
        }
        else{
            for (int i = 0; i < NUMBER_OF_ARTWORKS_TO_GENERATE; i++) {
                Artwork artwork = new Artwork(String.format("artwork%s",i+1),"okay dog pls",String.format(path+"/image%s",i), FileType.PNG,new Artist());
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
