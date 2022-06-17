package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.entity.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.entity.Chat;
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

    public ChatServiceImpl(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
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
}
