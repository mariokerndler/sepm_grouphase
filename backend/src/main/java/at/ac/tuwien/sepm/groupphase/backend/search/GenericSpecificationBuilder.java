package at.ac.tuwien.sepm.groupphase.backend.search;

import at.ac.tuwien.sepm.groupphase.backend.search.criteria.SearchCriteria;
import at.ac.tuwien.sepm.groupphase.backend.utils.SearchOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
@Slf4j
public class GenericSpecificationBuilder<T> {
    private final List<SearchCriteria> params;

    public GenericSpecificationBuilder() {
        params = new ArrayList<SearchCriteria>();
    }

    public GenericSpecificationBuilder with(
        String key, String operation, Object value, String prefix, String suffix) {

        SearchOperation op = SearchOperation.getSimpleOperation(operation.charAt(0));
        if (op != null) {
            if (op == SearchOperation.EQUALITY) {
                boolean startWithAsterisk = prefix.contains("*");
                boolean endWithAsterisk = suffix.contains("*");

                if (startWithAsterisk && endWithAsterisk) {
                    op = SearchOperation.CONTAINS;
                } else if (startWithAsterisk) {
                    op = SearchOperation.ENDS_WITH;
                } else if (endWithAsterisk) {
                    op = SearchOperation.STARTS_WITH;
                }
            }
            params.add(new SearchCriteria(key, op.toString(), value, "and"));
        }
        return this;
    }

    public GenericSpecificationBuilder with(String key, String operation, Object value) {
        params.add(new SearchCriteria(key, operation, value, "and"));
        return this;
    }

    public Specification<T> build() {
        if (params.size() == 0) {
            return null;
        }

        List<Specification> specs = params.stream()
            .map(x -> getSpecification(x))
            .collect(Collectors.toList());

        Specification result = specs.get(0);

        for (int i = 1; i < params.size(); i++) {


            result = params.get(i - 1)
                .isOrPredicate()
                ? Specification.where(result)
                .and(specs.get(i))
                : Specification.where(result)
                .and(specs.get(i));
        }
        return result;
    }


    public Specification<T> getSpecification(SearchCriteria criteria) {
        Specification<T> specification = new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate predicate = advancedCriteria(criteria, root, criteriaBuilder);
                return predicate;
            }
        };
        return specification;
    }

    public Predicate advancedCriteria(SearchCriteria criteria, Root<?> root, CriteriaBuilder builder) {
        switch (SearchOperation.valueOf(criteria.getOperation())) {
            case EQUALITY:
                return builder.equal(root.get(criteria.getKey()), criteria.getValue());
            case NEGATION:
                return builder.notEqual(root.get(criteria.getKey()), criteria.getValue());
            case GREATER_THAN:
                return builder.greaterThan(root.get(
                    criteria.getKey()), criteria.getValue().toString());
            case LESS_THAN:
                return builder.lessThan(root.get(
                    criteria.getKey()), criteria.getValue().toString());
            case LIKE:
                return builder.like(root.get(
                    criteria.getKey()), criteria.getValue().toString());
            case STARTS_WITH:
                return builder.like(root.get(criteria.getKey()), criteria.getValue() + "%");
            case ENDS_WITH:
                return builder.like(root.get(criteria.getKey()), "%" + criteria.getValue());
            case CONTAINS:
                return builder.like(root.get(
                    criteria.getKey()), "%" + criteria.getValue() + "%");
            default:
                return null;
        }
    }

    public Predicate genericCriteria(SearchCriteria criteria, Root<?> root, CriteriaBuilder builder) {

        if (criteria.getOperation().equalsIgnoreCase(">")) {
            return builder.greaterThanOrEqualTo(
                root.<String>get(criteria.getKey()), criteria.getValue().toString());
        } else if (criteria.getOperation().equalsIgnoreCase("<")) {
            return builder.lessThanOrEqualTo(
                root.<String>get(criteria.getKey()), criteria.getValue().toString());
        } else if (criteria.getOperation().equalsIgnoreCase(":")) {
            if (root.get(criteria.getKey()).getJavaType() == String.class) {
                return builder.like(
                    root.<String>get(criteria.getKey()), "%" + criteria.getValue() + "%");
            } else {
                return builder.equal(root.get(criteria.getKey()), criteria.getValue());
            }
        }
        return null;
    }
}
