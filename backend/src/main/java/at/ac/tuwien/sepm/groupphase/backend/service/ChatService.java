package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.ChatMessage;

import java.util.Collection;
import java.util.List;

public interface ChatService {

    List<ApplicationUser> getChatListForUser(long id);

    List<ChatMessage> getChatMessageHistory(String  userId, String participantId);
}
