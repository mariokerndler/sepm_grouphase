package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtworkDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artwork;
import at.ac.tuwien.sepm.groupphase.backend.entity.Commission;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.service.ArtistService;
import at.ac.tuwien.sepm.groupphase.backend.service.CommissionService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = {ArtistService.class, SketchMapper.class, TagMapper.class})
public abstract class ArtworkMapper {

    @Autowired
    CommissionService commissionService;

    @Mapping(source = "artist.id", target = "artistId")
    @Mapping(source = "sketches", target = "sketchesDtos")
    @Mapping(source = "tags", target = "tagsDtos")
    @Mapping(source = "commission.id", target = "commissionId")
    public abstract ArtworkDto artworkToArtworkDto(Artwork a);

    @Mapping(source = "artistId", target = "artist")
    @Mapping(source = "sketchesDtos", target = "sketches")
    @Mapping(source = "tagsDtos", target = "tags")
    @Mapping(source = "commissionId", target = "commission", qualifiedByName = "idToCommission")
    public abstract Artwork artworkDtoToArtwork(ArtworkDto artworkDto);


    @Named("idToCommission")
    public Commission idToCommission(Long id) {
        if (id == null) {
            return null;
        }
        try {
            return commissionService.findById(id);
        } catch (NotFoundException e) {
            return null;
        }
    }

}
