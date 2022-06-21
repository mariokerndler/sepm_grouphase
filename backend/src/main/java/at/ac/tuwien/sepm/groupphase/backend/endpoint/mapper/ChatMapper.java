package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ChatDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Chat;
import at.ac.tuwien.sepm.groupphase.backend.entity.Chat.ChatBuilder;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
@Slf4j
@Mapper(componentModel = "spring", uses ={ UserService.class,UserMapper.class})
public abstract class ChatMapper {
    @Autowired
    UserService userService;


    public abstract Chat chatDtoToChat(ChatDto chatDto);


    @AfterMapping
    public Chat jpaPls(ChatDto chatDto, @MappingTarget Chat.ChatBuilder chatBuilder ){
        Chat c= chatBuilder.build();
        log.info("maaaaaaping");
        log.info(chatDto.getUserId().toString());
        ApplicationUser user= userService.findUserById(chatDto.getUserId());
        log.info(user.getUserName());
        log.info(chatDto.getChatPartnerId().toString());
        ApplicationUser user2= userService.findUserById(chatDto.getChatPartnerId());
        log.info(user2.getUserName());
        c.setUser(user);
        c.setChatPartner(user2);
        return  c;
    }

    @Named("mapEntity")
    public ApplicationUser mapUserEntity(Long userId) {
        try {
            return userService.findUserById(userId);
        } catch (NotFoundException e) {
            throw new NotFoundException("Cant find user with given id");
        }
    }

}
