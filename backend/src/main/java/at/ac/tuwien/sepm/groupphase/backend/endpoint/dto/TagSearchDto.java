package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TagSearchDto {
    private List<String> tagIds = new LinkedList<>();
    private String searchOperations = "";
    private int pageNr = 0;
    private int randomSeed = 0;

    @Override
    public String toString() {
        return "TagSearchDto{"
            + "tagIds=" + tagIds
            + ", searchOperations='" + searchOperations + '\''
            + ", pageNr=" + pageNr
            + ", randomSeed=" + randomSeed
            + '}';
    }
}
