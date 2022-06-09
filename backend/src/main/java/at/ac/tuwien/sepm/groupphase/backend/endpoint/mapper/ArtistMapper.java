package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtistDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.service.ArtistService;
import at.ac.tuwien.sepm.groupphase.backend.service.ArtworkService;
import at.ac.tuwien.sepm.groupphase.backend.service.CommissionService;
import at.ac.tuwien.sepm.groupphase.backend.service.ReviewService;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = {HasIdMapper.class, ProfilePictureMapper.class, ArtworkService.class, CommissionService.class, ReviewService.class})
public abstract class ArtistMapper {

    @Autowired
    ArtistService artistService;

    @Mapping(source = "artworks", target = "artworksIds")
    @Mapping(source = "commissions", target = "commissionsIds")
    @Mapping(source = "reviews", target = "reviewsIds")
    @Mapping(source = "profilePicture", target = "profilePictureDto")
    public abstract ArtistDto artistToArtistDto(Artist artist);

    @Mapping(source = "artworksIds", target = "artworks")
    @Mapping(source = "commissionsIds", target = "commissions")
    @Mapping(source = "reviewsIds", target = "reviews")
    @Mapping(source = "profilePictureDto", target = "profilePicture", qualifiedByName = "ProfilePictureWithoutArtistId")
    public abstract Artist artistDtoToArtist(ArtistDto artistDto);

    @Named("idToArtist")
    public Artist idToArtist(Long id) {
        return artistService.findArtistById(id);
    }

    @AfterMapping
    public Artist setUserForChildren(@MappingTarget Artist.ArtistBuilder<?, ?> artistBuilder) {
        Artist artist = artistBuilder.build();
        if (artist.getProfilePicture() != null) {
            artist.getProfilePicture().setApplicationUser(artist);
        }
        return artist;
    }

    // TODO: Set empty lists null ?
}
