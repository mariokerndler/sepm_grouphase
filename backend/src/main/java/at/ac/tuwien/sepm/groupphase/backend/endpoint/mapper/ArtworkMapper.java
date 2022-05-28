package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtworkDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artwork;
import at.ac.tuwien.sepm.groupphase.backend.service.ArtistService;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = ArtistService.class)
public abstract class ArtworkMapper {
    @Autowired
    protected ArtistService artistService;

    public abstract ArtworkDto artworkToArtworkDto(Artwork a);

    public abstract Artwork artworkDtoToArtwork(ArtworkDto artworkDto);

    @BeforeMapping
    protected void urlDefault(ArtworkDto artworkDto, @MappingTarget Artwork a) {
        a.setImageUrl("default");
    }

    @AfterMapping
    protected void addArtistIdToArtworkDto(Artwork a, @MappingTarget ArtworkDto artworkDto) {
        artworkDto.setArtistId(a.getArtist().getId());
    }

    //TODO: ArtworkD is this a typo?
    @AfterMapping
    protected void addArtistToArtworkD(ArtworkDto a, @MappingTarget Artwork artwork) {


        artwork.setArtist(artistService.findArtistById(a.getArtistId()));
    }
}
