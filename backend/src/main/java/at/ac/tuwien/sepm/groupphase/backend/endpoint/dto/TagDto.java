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

    @NotBlank(message = "Name cannot be blank or null.")
    @Size(max = 35, message = "Name has to be shorter than 35 characters.")
    private String name;

    public TagDto(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "TagDto{"
            + "id=" + id
            + ", name='" + name + '\'' + '}';
    }
}
