package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtworkDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artwork;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public abstract class ArtworkMapper {
    public abstract ArtworkDto artworkToArtworkDto(Artwork a);
    abstract Artwork artworkDtoToArtwork(ArtworkDto aDto);
    @AfterMapping
   protected void addArtistIdToArtworkDto(Artwork a, @MappingTarget ArtworkDto artworkDto){
            artworkDto.setArtistId (a.getArtist().getId());
    }

}
