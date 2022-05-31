package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtworkDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artwork;
import at.ac.tuwien.sepm.groupphase.backend.service.ArtistService;
import org.mapstruct.BeforeMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = ArtistService.class)
public abstract class ArtworkMapper {

    @Mapping(source = "artist.id", target = "artistId")
    public abstract ArtworkDto artworkToArtworkDto(Artwork a);

    @Mapping(source = "artistId", target = "artist")
    public abstract Artwork artworkDtoToArtwork(ArtworkDto artworkDto);

    // TODO: Ask Daniel why this is necessary
    @BeforeMapping
    protected void urlDefault(ArtworkDto artworkDto, @MappingTarget Artwork a) {
        a.setImageUrl("default");
    }

}
