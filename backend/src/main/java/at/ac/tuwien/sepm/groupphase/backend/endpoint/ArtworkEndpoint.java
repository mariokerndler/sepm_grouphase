package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtistDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtworkDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ArtworkMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artwork;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.service.ArtworkService;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.security.PermitAll;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/artwork")
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
    @GetMapping("/{id}")
    @Operation(summary = "getAllArtworksByArtist")
    public List<ArtworkDto> getAllArtworksByArtist(@PathVariable Long id ){
        LOGGER.info("Get /Artist");
        try {
            List<Artwork> artworks=artworkService.findArtworksByArtist(id);
            LOGGER.info(artworks.get(0).getImageUrl());
            List<ArtworkDto> artworksDto= artworks.stream().map(a -> artworkMapper.artworkToArtworkDto(a)).collect(Collectors.toList());

            LOGGER.info(artworksDto.get(0).toString());
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
