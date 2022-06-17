package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;

import java.util.List;

public interface ChatService {

    List<ApplicationUser> getChatListForUser(long id);
}
