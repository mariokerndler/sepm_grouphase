package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtworkDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ChatDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artwork;
import at.ac.tuwien.sepm.groupphase.backend.entity.Chat;
import at.ac.tuwien.sepm.groupphase.backend.service.TagService;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;

@Mapper(componentModel = "spring",uses = UserService.class)
public abstract class ChatMapper {

    @Mapping(source = "userId",target = "user")
    @Mapping(source = "chatPartnerId",target = "chatPartner")
    public abstract Chat ChatDtoToChat(ChatDto chatDto);


}
