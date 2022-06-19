package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    @Query(nativeQuery = true, value = "select * from CHAT_MESSAGE where CHAT_MESSAGE.CHAT=:chatId")
    List<ChatMessage> getChatMessagesByChatId(@Param("chatId") String chatId);
}