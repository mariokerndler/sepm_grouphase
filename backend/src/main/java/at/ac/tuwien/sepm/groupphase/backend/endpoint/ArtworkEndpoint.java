package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtworkDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ArtworkMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artwork;
import at.ac.tuwien.sepm.groupphase.backend.search.GenericSpecificationBuilder;
import at.ac.tuwien.sepm.groupphase.backend.service.ArtworkService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.security.PermitAll;
import javax.transaction.Transactional;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/artwork")
@Slf4j
public class ArtworkEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ArtworkService artworkService;
    private final ArtworkMapper artworkMapper;
    @Autowired
    public ArtworkEndpoint(ArtworkService artworkService, ArtworkMapper artworkMapper) {
        this.artworkService = artworkService;
        this.artworkMapper = artworkMapper;
    }

    @PermitAll
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    @ResponseBody
    @Transactional
    @Operation(summary = "searchArtworks like this:http://localhost:8080/artwork?search=id>1")
    public List<ArtworkDto> search(@RequestParam(value = "search") String search) {
        GenericSpecificationBuilder builder = new GenericSpecificationBuilder();
        Pattern pattern = Pattern.compile("(\\w+?)(:|<|>)(\\w+?),");
        Matcher matcher = pattern.matcher(search + ",");
        log.info(search);
        while (matcher.find()) {
            log.info(matcher.group(1));
            builder.with(matcher.group(1), matcher.group(2), matcher.group(3));
        }

        Specification<Artwork> spec = builder.build();

        List<Artwork> artworks =artworkService.searchArtworks(spec);

        List<ArtworkDto> artworksDto= artworks.stream().map(a -> artworkMapper.artworkToArtworkDto(a)).collect(Collectors.toList());
        return artworksDto;
    }


    @PermitAll
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    @Operation(summary = "getAllArtworksByArtist")
    public List<ArtworkDto> getAllArtworksByArtist(@PathVariable Long id ){
        LOGGER.info("Get /Artist");
        try {
            List<Artwork> artworks=artworkService.findArtworksByArtist(id);

            List<ArtworkDto> artworksDto= artworks.stream().map(a -> artworkMapper.artworkToArtworkDto(a)).collect(Collectors.toList());


            return artworksDto;
        } catch (Exception n) {
            LOGGER.error(n.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, n.getMessage());
        }
    }

    @PermitAll
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/{id}")
    @Operation(summary = "getAllArtworksByArtist")
    public void deleteArtwork(@PathVariable Long id ){
        LOGGER.info("Get /Artist");
        try {

            LOGGER.info("Delete Artwork/"+id);
            artworkService.deleteArtwork(id);

        } catch (Exception n) {
            LOGGER.error(n.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, n.getMessage());
        }
    }
    @PermitAll
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get Detailed informations about a specific user")
    public void postArtwork(@RequestBody ArtworkDto artworkDto) {
        LOGGER.debug("Post /Artwork/{}", artworkDto.toString());
        try {
            artworkService.saveArtwork(artworkMapper.artworkDtoToArtwork(artworkDto));
        } catch (Exception v) {
            LOGGER.error(v.getMessage());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, v.getMessage());
        }

    }
}
