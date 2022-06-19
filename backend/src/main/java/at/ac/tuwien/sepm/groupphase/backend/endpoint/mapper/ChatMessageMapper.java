package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ChatMessageDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ChatMessage;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class ChatMessageMapper {

    public abstract ChatMessageDto chatMessageToChatMessageDto(ChatMessage message);

    public abstract ChatMessage chatMessageDtoToChatMessage(ChatMessageDto message);

}
