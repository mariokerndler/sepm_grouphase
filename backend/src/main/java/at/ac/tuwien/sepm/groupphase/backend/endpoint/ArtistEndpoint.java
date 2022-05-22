package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtistDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ArtistMapper;
import at.ac.tuwien.sepm.groupphase.backend.service.ArtistService;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.security.PermitAll;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/artist")
public class ArtistEndpoint {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private final ArtistService artistService;
    private final ArtistMapper artistMapper;

    @Autowired
    public ArtistEndpoint(ArtistService artistService, ArtistMapper artistMapper) {
        this.artistService = artistService;
        this.artistMapper = artistMapper;
    }

    /*@PermitAll
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{id}")
    @Operation(summary = "Get Detailed informations about a specific user")
    public ArtistDto findById(@PathVariable Long id) {
        LOGGER.debug("Get /User/{}", id);
        try {
            Artist artist = artistService.findArtistById(id);
            LOGGER.info(applicationUser.getUserName());
            ArtistDto artistDto = artistMapper.artistToArtistDto(artist);
            LOGGER.info(artistDto.toString());
            return artistDto;
        } catch (NotFoundException n) {
            LOGGER.error(n.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, n.getMessage());
        }
    }*/

    // TODO: ArtistDto includes infinite loop because of circular dependency between Entities (e.g. Artwork and Artist).
    //  Configure Mapper to avoid this or remove entities that are responsible
    @PermitAll
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    @Operation(summary = "Get all artists")
    @Transactional
    public List<ArtistDto> getAllArtists() {
        LOGGER.debug("Get /Artist");
        return artistService.getAllArtists().stream().map(u -> artistMapper.artistToArtistDto(u)).collect(Collectors.toList());
    }

    @PermitAll
    @ResponseStatus(HttpStatus.OK)
    @PostMapping
    @Operation(summary = "Post new artist")
    @Transactional
    public ArtistDto saveArtist(@RequestBody ArtistDto artistDto) {
        try {
            return artistMapper.artistToArtistDto(artistService.saveArtist(artistMapper.artistDtoToArtist(artistDto)));
        } catch (Exception e) {
            LOGGER.error(e.getMessage() + artistDto.toString());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        }
    }

    //TODO: if uncommented, swap user for artist
    /*@PermitAll
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get Detailed informations about a specific user")
    public void updateUser(@RequestBody ApplicationUserDto userDto) {
        LOGGER.debug("Post /User/{}", userDto);
        try {
            userService.updateUser(userDto);
        } catch (Exception v) {
            LOGGER.error(v.getMessage());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, v.getMessage());
        }

    }*/
}
