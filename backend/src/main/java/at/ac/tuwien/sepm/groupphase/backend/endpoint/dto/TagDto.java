package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TagDto {

    private Long id;
    private String name;

    @Override
    public String toString() {
        return "TagDto{" +
            "id=" + id +
            ", name='" + name + '\'' +
            '}';
    }
}
