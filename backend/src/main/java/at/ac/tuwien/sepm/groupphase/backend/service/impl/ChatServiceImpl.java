package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Chat;
import at.ac.tuwien.sepm.groupphase.backend.entity.ChatMessage;
import at.ac.tuwien.sepm.groupphase.backend.repository.ChatMessageRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.ChatRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.ChatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

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
        List<ApplicationUser> partners = new LinkedList<>();
        for (Chat a : chats) {
            partners.add(a.getChatPartner());
            partners.add(a.getUser());
        }
        return partners.stream().filter(a -> a.getId() != id).collect(Collectors.toList());

    }

    @Override
    public List<ChatMessage> getChatMessageHistory(String userId, String participantId) {
        Chat c = this.chatRepository.getAllByUserAndChatPartner(userId, participantId);

        return this.chatMessageRepository.getChatMessagesByChatId(String.valueOf(c.getId()));
    }

    @Override
    public void postChatMessage(ChatMessage chatMessage) {
        if (chatMessage.getSentDate() == null) {
            chatMessage.setSentDate(LocalDateTime.now());
        }
        Chat c = this.chatRepository.getAllByUserAndChatPartner(String.valueOf(chatMessage.getFromId()),
            String.valueOf(chatMessage.getToId()));
        if (c == null) {
            this.chatRepository.getAllByUserAndChatPartner(String.valueOf(chatMessage.getToId()),
                String.valueOf(chatMessage.getFromId()));
        }
        chatMessage.setChat(c);

        this.chatMessageRepository.save(chatMessage);

    }

    @Transactional
    @Override
    public void postChat(Chat chat) {

        Chat c2 = new Chat(chat.getUser(), chat.getChatPartner());
        //this.chatRepository.save(c2);
        this.chatRepository.save(chat);
    }


}
