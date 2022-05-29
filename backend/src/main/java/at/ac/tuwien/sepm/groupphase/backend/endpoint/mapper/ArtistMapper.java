package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtistDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = HasIdMapper.class)
public abstract class ArtistMapper {

    @Mapping(source = "artworks", target = "artworksIds")
    @Mapping(source = "commissions", target = "commissionsIds")
    @Mapping(source = "reviews", target = "reviewsIds")
    public abstract ArtistDto artistToArtistDto(Artist artist);

    public abstract Artist artistDtoToArtist(ArtistDto artistDto);

    /*@AfterMapping
    protected void addArtworkIds(Artist a, @MappingTarget ArtistDto aDto){
        aDto.setArtworksIds(new LinkedList<>());
        for(Artwork aw: a.getArtworks()){
            aDto.addArtworkId(aw.getId());
        }
    }*/

    /*@Named("ArtworkToId")
    public static Long ArtworkToId(Artwork a) {
        return a.getId();
    }*/

    // TODO: Set empty lists null ?
}
