package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatDto {
    Long userId;
    Long chatPartnerId;

    @Override
    public String toString() {
        return "ChatDto{" +
            "userId=" + userId +
            ", chatPartnerId=" + chatPartnerId +
            '}';
    }
}
