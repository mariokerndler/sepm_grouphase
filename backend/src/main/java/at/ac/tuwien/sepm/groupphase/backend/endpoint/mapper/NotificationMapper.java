package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.NotificationDto;
import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Notification;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = {UserService.class})
public abstract class NotificationMapper {

    @Autowired
    UserService userService;

    @Mapping(source = "user", target = "userId")
    public abstract NotificationDto notificationToNotificationDto(Notification notification);

    @Mapping(source = "userId", target = "user")
    public abstract Notification notificationDtoToNotification(NotificationDto notificationDto);

    @Named("idToUser")
    public ApplicationUser idToUser(Long id) {
        return userService.findUserById(id);
    }

    Long map(ApplicationUser value) {
        return value.getId();
    }
}
