package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SketchDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Sketch;
import at.ac.tuwien.sepm.groupphase.backend.service.ArtworkService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring", uses = {ArtworkService.class})
public abstract class SketchMapper {

    @Mapping(source = "artwork.id", target = "artworkId")
    public abstract SketchDto sketchToSketchDto(Sketch sketch);

    @Mapping(source = "artworkId", target = "artwork")
    public abstract Sketch sketchDtoToSketch(SketchDto sketchDto);

    @Named("SketchWithoutArtworkId")
    public abstract Sketch sketchDtoWithoutArtworkIdToSketch(SketchDto sketchDto);
}
