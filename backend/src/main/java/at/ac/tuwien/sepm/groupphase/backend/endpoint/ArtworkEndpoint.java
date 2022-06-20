package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtworkDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TagSearchDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ArtworkMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artwork;
import at.ac.tuwien.sepm.groupphase.backend.search.ArtistSpecification;
import at.ac.tuwien.sepm.groupphase.backend.search.GenericSpecificationBuilder;
import at.ac.tuwien.sepm.groupphase.backend.search.TagSpecification;
import at.ac.tuwien.sepm.groupphase.backend.service.ArtworkService;
import at.ac.tuwien.sepm.groupphase.backend.utils.enums.SearchOperation;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.security.PermitAll;
import javax.transaction.Transactional;
import javax.validation.Valid;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Transactional
@Slf4j
@Validated
@RestController
@RequestMapping(value = "api/v1/artworks")
public class ArtworkEndpoint {

    private final ArtworkService artworkService;
    private final ArtworkMapper artworkMapper;

    @Autowired
    public ArtworkEndpoint(ArtworkService artworkService, ArtworkMapper artworkMapper) {
        this.artworkService = artworkService;
        this.artworkMapper = artworkMapper;
    }

    //TODO: implementation arguably belongs to service class (feel free to move it :))
    //see https://www.baeldung.com/rest-api-query-search-language-more-operations
    @PermitAll
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    @ResponseBody
    @Transactional
    @Operation(summary = "searchArtworks with searchDto searchOperations:id>12,name=*a etc, tagIds as List")
    public List<ArtworkDto> search(@RequestParam(name = "randomSeed", defaultValue = "0") int randomSeed,
                                   @RequestParam(name = "tagIds", required = false) List<String> tagIds,
                                   @RequestParam(name = "artistIds", required = false) List<String> artistIds,
                                   @RequestParam(name = "pageNr", defaultValue = "0") int pageNr,
                                   @RequestParam(name = "searchOperations", defaultValue = "") String searchOperations) {
        log.info("A user is searching for an artwork.");
        TagSearchDto tagSearchDto = new TagSearchDto(tagIds, artistIds, searchOperations.toLowerCase(), pageNr, randomSeed);
        String search = tagSearchDto.getSearchOperations();

        GenericSpecificationBuilder builder = new GenericSpecificationBuilder();
        String operationSetExper = String.join("|", SearchOperation.SIMPLE_OPERATION_SET);
        Pattern pattern = Pattern.compile(
            "(\\w+?)(" + operationSetExper + ")(\\p{Punct}?)(\\w+?)(\\p{Punct}?),"); //regex not really flexible?
        Matcher matcher = pattern.matcher(search + ",");
        while (matcher.find()) {
            builder.with(
                matcher.group(1),
                matcher.group(2),
                matcher.group(4),
                matcher.group(3),
                matcher.group(5));
        }

        Specification<Artwork> spec = builder.build();

        if (tagSearchDto.getTagIds() != null) {
            if (tagSearchDto.getTagIds().size() > 0) {
                if (spec == null) {
                    spec = TagSpecification.filterByTags(tagSearchDto.getTagIds().get(0));
                }
                for (String tag : tagSearchDto.getTagIds()) {
                    spec = spec.and(TagSpecification.filterByTags(tag).and(spec));
                }
            }
        }

        if (tagSearchDto.getArtistIds() != null) {
            if (tagSearchDto.getArtistIds().size() > 0) {
                if (spec == null) {
                    spec = ArtistSpecification.filterBy(tagSearchDto.getArtistIds().get(0));
                }
                for (String artist : tagSearchDto.getArtistIds()) {
                    spec = spec.and(ArtistSpecification.filterBy(artist).and(spec));
                }
            }
        }

        Pageable page = PageRequest.of(tagSearchDto.getPageNr(), 50);
        return artworkService.searchArtworks(spec, page, tagSearchDto.getRandomSeed()).stream().map(artworkMapper::artworkToArtworkDto).collect(Collectors.toList());
    }


    @PermitAll
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    @Operation(summary = "getAllArtworksByArtist")
    public List<ArtworkDto> getAllArtworksByArtist(@PathVariable Long id) {
        log.info("A user is fetching all artworks by artist with id '{}'", id);
        try {
            List<Artwork> artworks = artworkService.findArtworksByArtist(id);

            return artworks.stream().map(artworkMapper::artworkToArtworkDto).collect(Collectors.toList());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PermitAll
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping()
    @Operation(summary = "Delete artwork")
    public void deleteArtwork(@Valid @RequestBody ArtworkDto artworkDto) {
        log.info("A user is trying to delete an artwork.");
        try {
            artworkService.deleteArtwork(artworkMapper.artworkDtoToArtwork(artworkDto));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PermitAll
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Post artwork")
    public void postArtwork(@Valid @RequestBody ArtworkDto artworkDto) {
        log.debug("A user is trying to create a new artwork.");

        Artwork artwork = artworkMapper.artworkDtoToArtwork(artworkDto);
        artworkService.saveArtwork(artwork);
    }
}
