package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtworkDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artwork;
import at.ac.tuwien.sepm.groupphase.backend.service.ArtistService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ArtistService.class, SketchMapper.class, TagMapper.class})
public abstract class ArtworkMapper {

    @Mapping(source = "artist.id", target = "artistId")
    @Mapping(source = "sketches", target = "sketchesDtos")
    @Mapping(source = "tags", target = "tagsDtos")
    public abstract ArtworkDto artworkToArtworkDto(Artwork a);

    @Mapping(source = "artistId", target = "artist")
    @Mapping(source = "sketchesDtos", target = "sketches")
    @Mapping(source = "tagsDtos", target = "tags")
    public abstract Artwork artworkDtoToArtwork(ArtworkDto artworkDto);

    // TODO: (Ask Daniel why this is necessary) uncommenting this to see if it's needed
    //@BeforeMapping
    //protected void urlDefault(ArtworkDto artworkDto, @MappingTarget Artwork a) {
    //    a.setImageUrl("default");
    //}

}
