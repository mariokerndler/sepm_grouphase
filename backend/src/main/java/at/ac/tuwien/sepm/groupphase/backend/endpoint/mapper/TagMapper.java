package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TagDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Tag;
import at.ac.tuwien.sepm.groupphase.backend.service.ArtistService;
import at.ac.tuwien.sepm.groupphase.backend.service.TagService;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class TagMapper {


    @Autowired
    protected TagService tagService;

    public abstract TagDto tagToTagDto(Tag a);
    public abstract Tag tagDtoToTag(TagDto aDto);
    @AfterMapping
    protected void tagPreMapping(Tag tag, @MappingTarget TagDto tagDto){

    }
    @AfterMapping
    protected void tagPostMapping(TagDto tagDto, @MappingTarget Tag tag){



    }

}
