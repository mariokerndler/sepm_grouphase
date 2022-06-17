package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtworkDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ChatMessageDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artwork;
import at.ac.tuwien.sepm.groupphase.backend.entity.Chat;
import at.ac.tuwien.sepm.groupphase.backend.entity.ChatMessage;
import at.ac.tuwien.sepm.groupphase.backend.service.ArtistService;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.springframework.web.bind.annotation.PostMapping;

@Mapper(componentModel = "spring" )
public abstract class ChatMessageMapper {

    public abstract ChatMessageDto chatMessageToChatMessageDto(ChatMessage message);
    public abstract ChatMessage  chatMessageDtoToChatMessage(ChatMessage message);

}
