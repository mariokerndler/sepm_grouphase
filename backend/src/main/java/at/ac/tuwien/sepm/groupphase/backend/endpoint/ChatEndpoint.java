package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ApplicationUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ArtworkDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ArtworkMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.entity.Artwork;
import at.ac.tuwien.sepm.groupphase.backend.service.ArtworkService;
import at.ac.tuwien.sepm.groupphase.backend.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
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


    @Autowired
    public ChatEndpoint(ChatService chatService,UserMapper userMapper ) {
        this.userMapper=userMapper;
        this.chatService = chatService;

    }


    @PermitAll
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{id}")
    @Operation(summary = "getChatListForUser")
    public List<ApplicationUserDto> getChatListForUser(@PathVariable Long id) {
        log.info("A user is fetching all artworks by artist with id '{}'", id);
        try {
            List<ApplicationUserDto> chatsPartners = (List<ApplicationUserDto>) chatService.getChatListForUser(id).stream().map(userMapper::userToUserDto).collect(Collectors.toList());

            return chatsPartners;
        } catch (Exception n) {
            log.error(n.getMessage());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, n.getMessage());
        }
    }
}
