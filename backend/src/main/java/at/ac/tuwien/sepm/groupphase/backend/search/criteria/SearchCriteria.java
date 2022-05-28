package at.ac.tuwien.sepm.groupphase.backend.search.criteria;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SearchCriteria {
    private String key;
    private String operation;
    private Object value;
    private String predicateType;

    public boolean isOrPredicate() {
        if (predicateType.toLowerCase().equals("or")) {
            return true;
        }
        return false;
    }
}
