package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtistDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ArtistMapper;
import at.ac.tuwien.sepm.groupphase.backend.service.ArtistService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.security.PermitAll;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping(value = "api/v1/artists")
public class ArtistEndpoint {

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
        log.debug("Get /Artist");
        return artistService.getAllArtists().stream().map(artistMapper::artistToArtistDto).collect(Collectors.toList());
    }

    @PermitAll
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @Operation(summary = "Post new artist")
    @Transactional
    public ArtistDto saveArtist(@RequestBody ArtistDto artistDto) {
        try {
            return artistMapper.artistToArtistDto(artistService.saveArtist(artistMapper.artistDtoToArtist(artistDto)));
        } catch (Exception e) {
            log.error(e.getMessage() + artistDto.toString());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        }
    }


    @PermitAll
    @ResponseStatus(HttpStatus.OK)
    @PutMapping
    @Operation(summary = "Update artist")
    @Transactional
    public void updateArtist(@RequestBody ArtistDto artistDto) {
        try {
            log.debug("put artist " + artistDto);
            artistService.updateArtist(artistMapper.artistDtoToArtist(artistDto));
        } catch (Exception e) {
            log.error(e.getMessage() + " : " + artistDto.toString());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        }
    }


    @PermitAll
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{id}")
    @Operation(summary = "Get  artist by id")
    @Transactional
    public ArtistDto getArtistById(@PathVariable Long id) {
        try {
            log.info(String.valueOf(id));
            return artistMapper.artistToArtistDto(artistService.findArtistById(id));
        } catch (Exception e) {
            log.error(e.getMessage() + id.toString());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        }
    }

    @PermitAll
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Delete artist by id")
    @Transactional
    public void deleteArtistById(@PathVariable Long id) {
        try {
            log.info("Delete artist with id: " + String.valueOf(id));
            artistService.deleteArtistById(id);
        } catch (Exception e) {
            log.error(e.getMessage() + id.toString());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, e.getMessage());
        }
    }


}
