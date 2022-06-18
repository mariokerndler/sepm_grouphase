package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ApplicationUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ChatDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ChatMessageDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ChatMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ChatMessageMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Chat;
import at.ac.tuwien.sepm.groupphase.backend.entity.ChatMessage;
import at.ac.tuwien.sepm.groupphase.backend.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.annotation.security.PermitAll;
import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@RestController
@RequestMapping(value = "api/v1/chats")
public class ChatEndpoint {
    private final ChatService chatService;
    private final UserMapper userMapper;
    private final ChatMessageMapper chatMessageMapper;
    private final ChatMapper chatMapper;

    @Autowired
    public ChatEndpoint(ChatService chatService, UserMapper userMapper, ChatMessageMapper chatMessageMapper, ChatMapper chatMapper) {
        this.userMapper = userMapper;
        this.chatService = chatService;
        this.chatMessageMapper = chatMessageMapper;
        this.chatMapper = chatMapper;
    }


    @PermitAll
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    @Operation(summary = "getChatListForUser")
    public List<ApplicationUserDto> getChatListForUser(@PathVariable Long id) {
        log.info("getChatListForUser '{}'", id);
        try {
            List<ApplicationUserDto> chatsPartners = (List<ApplicationUserDto>) chatService.getChatListForUser(id).stream().map(userMapper::userToUserDto).collect(Collectors.toList());

            return chatsPartners;
        } catch (Exception n) {
            log.error(n.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, n.getMessage());
        }
    }

    @PermitAll
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/history")
    @Operation(summary = "getMessageHistory")
    public List<ChatMessageDto> getMessageHistory(
        @RequestParam(defaultValue = "", name = "userId") String userId,
        @RequestParam(defaultValue = "", name = "participantId") String chatPartnerId
    ) {
        log.info("Fetching message history for user with id '{}'", userId);
        try {

            List<ChatMessageDto> chatsPartners = chatService.getChatMessageHistory(userId, chatPartnerId).stream().map(chatMessageMapper::chatMessageToChatMessageDto).collect(Collectors.toList());

            return chatsPartners;
        } catch (Exception n) {
            log.error(n.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, n.getMessage());
        }
    }

    @PermitAll
    @PostMapping("/chat")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Post Chat")
    public void postChat(@RequestBody ChatDto chatDto) {
        log.debug("Endpoint: postChat()." + chatDto.toString());
        try {
            Chat c = this.chatMapper.chatDtoToChat(chatDto);
            chatService.postChat(c);
        } catch (Exception v) {
            log.error(v.getMessage());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, v.getMessage());
        }
    }

    @PermitAll
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Post chatMessage")
    public void postChatMessage(@RequestBody ChatMessageDto chatMessageDto) {
        log.debug("Endpoint: postChatMessage()." + chatMessageDto.toString());
        try {
            ChatMessage chatMessage = chatMessageMapper.chatMessageDtoToChatMessage(chatMessageDto);
            chatService.postChatMessage(chatMessage);
        } catch (Exception v) {
            log.error(v.getMessage());
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, v.getMessage());
        }
    }
}
