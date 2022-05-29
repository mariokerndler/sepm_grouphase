package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtistDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.service.ArtworkService;
import at.ac.tuwien.sepm.groupphase.backend.service.CommissionService;
import at.ac.tuwien.sepm.groupphase.backend.service.ReviewService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {HasIdMapper.class, ArtworkService.class, CommissionService.class, ReviewService.class})
public abstract class ArtistMapper {

    @Mapping(source = "artworks", target = "artworksIds")
    @Mapping(source = "commissions", target = "commissionsIds")
    @Mapping(source = "reviews", target = "reviewsIds")
    public abstract ArtistDto artistToArtistDto(Artist artist);

    @Mapping(source = "artworksIds", target = "artworks")
    @Mapping(source = "commissionsIds", target = "commissions")
    @Mapping(source = "reviewsIds", target = "reviews")
    public abstract Artist artistDtoToArtist(ArtistDto artistDto);

    // TODO: Set empty lists null ?
}
