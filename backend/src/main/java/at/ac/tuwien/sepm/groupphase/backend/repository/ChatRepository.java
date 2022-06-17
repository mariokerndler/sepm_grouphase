package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.entity.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChatRepository extends JpaRepository<Chat, Long> {

    @Query(nativeQuery = true, value = "SELECT * FROM Chat WHERE Chat.user_Id=:userId")
    List<Chat> getAllByUserId(@Param("userId") String userId);
}