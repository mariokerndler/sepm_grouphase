package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtistDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ArtistMapper;
import at.ac.tuwien.sepm.groupphase.backend.service.ArtistService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@Validated
@RequestMapping(value = "api/v1/artists")
public class ArtistEndpoint {

    private final ArtistService artistService;
    private final ArtistMapper artistMapper;

    @Autowired
    public ArtistEndpoint(ArtistService artistService, ArtistMapper artistMapper) {
        this.artistService = artistService;
        this.artistMapper = artistMapper;
    }

    @PermitAll
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    @Operation(summary = "Get all artists")
    @Transactional
    public List<ArtistDto> getAllArtists() {
        log.info("A user requested all artists.");
        return artistService.getAllArtists().stream().map(artistMapper::artistToArtistDto).collect(Collectors.toList());
    }

    @PermitAll
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @Operation(summary = "Post new artist")
    @Transactional
    public ArtistDto saveArtist(@Valid @RequestBody ArtistDto artistDto) {
        log.info("A user is trying to save and artist.");

        return artistMapper.artistToArtistDto(artistService.saveArtist(artistMapper.artistDtoToArtist(artistDto)));
    }

    @PermitAll
    @ResponseStatus(HttpStatus.OK)
    @PutMapping
    @Operation(summary = "Update artist")
    @Transactional
    public void updateArtist(@Valid @RequestBody ArtistDto artistDto) {
        log.info("A user is trying to update an artist.");

        artistService.updateArtist(artistMapper.artistDtoToArtist(artistDto));
    }

    @PermitAll
    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/{id}")
    @Operation(summary = "Get artist by id")
    @Transactional
    public ArtistDto getArtistById(@PathVariable Long id) {
        log.info("A user is requesting an artist with id '{}'", id);
        return artistMapper.artistToArtistDto(artistService.findArtistById(id));
    }

    @PermitAll
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Delete artist by id")
    @Transactional
    public void deleteArtistById(@PathVariable Long id) {
        log.info("A user is deleting an artist with id '{}'", id);

        artistService.deleteArtistById(id);
    }
}
