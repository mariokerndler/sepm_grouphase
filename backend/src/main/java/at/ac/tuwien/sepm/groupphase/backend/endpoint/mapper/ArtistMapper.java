package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtistDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artist;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artwork;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.LinkedList;

@Mapper(componentModel = "spring")
public abstract class ArtistMapper {

    public abstract ArtistDto artistToArtistDto(Artist artist);


    public abstract Artist artistDtoToArtist(ArtistDto artistDto);

    @AfterMapping
    protected void addArtworkIds(Artist a, @MappingTarget ArtistDto artistDto) {
        artistDto.setArtworksIds(new LinkedList<>());
        for (Artwork aw : a.getArtworks()) {
            artistDto.addArtworkId(aw.getId());
        }
    }
}
