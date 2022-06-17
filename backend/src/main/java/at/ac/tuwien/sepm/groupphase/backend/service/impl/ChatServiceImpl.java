package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Chat;
import at.ac.tuwien.sepm.groupphase.backend.entity.ChatMessage;
import at.ac.tuwien.sepm.groupphase.backend.repository.ChatMessageRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ChatRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Slf4j
@Service
public class ChatServiceImpl implements ChatService {
    private final ChatRepository chatRepository;
    private final ChatMessageRepository chatMessageRepository;
    public ChatServiceImpl(ChatRepository chatRepository, ChatMessageRepository chatMessageRepository) {
        this.chatRepository = chatRepository;
        this.chatMessageRepository = chatMessageRepository;
    }

    @Override
    public List<ApplicationUser> getChatListForUser(long id) {
        List<Chat> chats = this.chatRepository.getAllByUserId(String.valueOf(id));
        List<ApplicationUser> partners= new LinkedList<>();
        for( Chat a: chats){
            partners.add(a.getChatPartner());
        }
        return partners;
    }

    @Override
    public List<ChatMessage> getChatMessageHistory(String userId, String participantId) {
        Chat c = this.chatRepository.getAllByUserAndChatPartner(userId,participantId);
        return this.chatMessageRepository.getChatMessagesByChatId(String.valueOf(c.getId()));
    }


}
