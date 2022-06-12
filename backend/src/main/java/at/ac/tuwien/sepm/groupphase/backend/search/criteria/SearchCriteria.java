package at.ac.tuwien.sepm.groupphase.backend.search.criteria;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
@NoArgsConstructor
public class SearchCriteria {
    private String key;
    private String operation;
    private Object value;
    private String predicateType;

    public SearchCriteria(String key, String operation, Object value, String predicateType) {
        this.key = key;
        this.operation = operation;
        this.value = value;
        this.predicateType = predicateType;
        if (operation.equals("~")) {

            this.key = "lower(" + key + ')';
        }
        log.info(this.toString());
    }

    public boolean isOrPredicate() {
        if (predicateType.toLowerCase().equals("or")) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return "SearchCriteria{"
            + "key='" + key + '\''
            + ", operation='" + operation + '\''
            + ", value=" + value
            + ", predicateType='" + predicateType + '\'' + '}';
    }
}
