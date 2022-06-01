package at.ac.tuwien.sepm.groupphase.backend.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, name = "published_at")
    private LocalDateTime publishedAt;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, length = 500)
    private String summary;

    @Column(nullable = false, length = 10000)
    private String text;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Message message)) {
            return false;
        }
        return Objects.equals(id, message.id)
            && Objects.equals(publishedAt, message.publishedAt)
            && Objects.equals(title, message.title)
            && Objects.equals(summary, message.summary)
            && Objects.equals(text, message.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, publishedAt, title, summary, text);
    }

    @Override
    public String toString() {
        return "Message{"
            + "id=" + id
            + ", publishedAt=" + publishedAt
            + ", title='" + title + '\''
            + ", summary='" + summary + '\''
            + ", text='" + text + '\''
            + '}';
    }


    public static final class MessageBuilder {
        private Long id;
        private LocalDateTime publishedAt;
        private String title;
        private String summary;
        private String text;

        private MessageBuilder() {
        }

        public static MessageBuilder aMessage() {
            return new MessageBuilder();
        }

        public MessageBuilder withId(Long id) {
            this.id = id;
            return this;
        }

        public MessageBuilder withPublishedAt(LocalDateTime publishedAt) {
            this.publishedAt = publishedAt;
            return this;
        }

        public MessageBuilder withTitle(String title) {
            this.title = title;
            return this;
        }

        public MessageBuilder withSummary(String summary) {
            this.summary = summary;
            return this;
        }

        public MessageBuilder withText(String text) {
            this.text = text;
            return this;
        }

        public Message build() {
            Message message = new Message();
            message.setId(id);
            message.setPublishedAt(publishedAt);
            message.setTitle(title);
            message.setSummary(summary);
            message.setText(text);
            return message;
        }
    }
}