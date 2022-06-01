package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtistDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.service.ArtistService;
import at.ac.tuwien.sepm.groupphase.backend.service.ArtworkService;
import at.ac.tuwien.sepm.groupphase.backend.service.CommissionService;
import at.ac.tuwien.sepm.groupphase.backend.service.ReviewService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = {HasIdMapper.class, ArtworkService.class, CommissionService.class, ReviewService.class})
public abstract class ArtistMapper {

    @Autowired
    ArtistService artistService;

    @Mapping(source = "artworks", target = "artworksIds")
    @Mapping(source = "commissions", target = "commissionsIds")
    @Mapping(source = "reviews", target = "reviewsIds")
    public abstract ArtistDto artistToArtistDto(Artist artist);

    @Mapping(source = "artworksIds", target = "artworks")
    @Mapping(source = "commissionsIds", target = "commissions")
    @Mapping(source = "reviewsIds", target = "reviews")
    public abstract Artist artistDtoToArtist(ArtistDto artistDto);

    @Named("idToArtist")
    public Artist idToArtist(Long id) {
        return artistService.findArtistById(id);
    }

    // TODO: Set empty lists null ?
}
