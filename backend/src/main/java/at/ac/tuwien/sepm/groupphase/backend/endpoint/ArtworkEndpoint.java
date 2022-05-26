package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtworkDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TagSearchDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ArtworkMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artwork;
import at.ac.tuwien.sepm.groupphase.backend.search.GenericSpecificationBuilder;
import at.ac.tuwien.sepm.groupphase.backend.search.TagSpecification;
import at.ac.tuwien.sepm.groupphase.backend.service.ArtworkService;
import at.ac.tuwien.sepm.groupphase.backend.utils.SearchOperation;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    //TODO: implementation arguably belongs to service class
    //see https://www.baeldung.com/rest-api-query-search-language-more-operations
    @PermitAll
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    @ResponseBody
    @Transactional
    @Operation(summary = "searchArtworks with searchDto searchOperations:id>12,name=*a etc, tagIds as List")
    public List<ArtworkDto> search(@RequestBody TagSearchDto tagSearchDto  ) {
        LOGGER.info("Search artworks by criteria " + tagSearchDto.toString());
        String search= tagSearchDto.getSearchOperations();

        Pageable page= PageRequest.of(tagSearchDto.getPageNr(), 50);
        GenericSpecificationBuilder builder = new GenericSpecificationBuilder();
        String operationSetExper = String.join("|",SearchOperation.SIMPLE_OPERATION_SET);
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

        if(tagSearchDto.getTagIds()!=null) {
            if (spec == null) {
                spec = TagSpecification.filterByTags(tagSearchDto.getTagIds().get(0));
            }
            for (String tag : tagSearchDto.getTagIds()) {
                spec.and(TagSpecification.filterByTags(tag));
            }
            log.info(tagSearchDto.getSearchOperations());
        }
        return artworkService.searchArtworks(spec,page).stream().map(a -> artworkMapper.artworkToArtworkDto(a)).collect(Collectors.toList());

    }


    @PermitAll
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    @Operation(summary = "getAllArtworksByArtist")
    public List<ArtworkDto> getAllArtworksByArtist(@PathVariable Long id ){
        LOGGER.info("Get artworks by artist with id " + id);
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
    @DeleteMapping()
    @Operation(summary = "Delete artwork")
    public void deleteArtwork(@RequestBody ArtworkDto artworkDto ){
        LOGGER.info("Delete artwork " + artworkDto.getName());
        try {

            artworkService.deleteArtwork(artworkMapper.artworkDtoToArtwork(artworkDto));

        } catch (Exception n) {
            LOGGER.error(n.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, n.getMessage());
        }
    }

    @PermitAll
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Post artwork")
    public void postArtwork(@RequestBody ArtworkDto artworkDto ) {
        LOGGER.info("Post artwork " + artworkDto.getName());
        try {
            artworkService.saveArtwork(artworkMapper.artworkDtoToArtwork(artworkDto));
        } catch (Exception v) {
            LOGGER.error(v.getMessage());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, v.getMessage());
        }

    }
}
