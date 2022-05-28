package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class TagDto {

    private Long id;

    @NotBlank
    @Size(max = 35)
    private String name;

    @Override
    public String toString() {
        return "TagDto{" +
            "id=" + id +
            ", name='" + name + '\'' +
            '}';
    }
}
