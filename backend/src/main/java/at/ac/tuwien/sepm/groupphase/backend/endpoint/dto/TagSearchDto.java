package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class TagSearchDto {
   private List<String> tagIds;
    private String searchOperations;
    private  int pageNr=0;
    private int  randomSeed=0;
    @Override
    public String toString() {
        return "TagSearchDto{" +
            "tagIds=" + tagIds +
            ", searchOperations='" + searchOperations + '\'' +
            '}';
    }
}
