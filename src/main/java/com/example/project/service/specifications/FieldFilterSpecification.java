package com.example.project.service.specifications;

import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;


public class FieldFilterSpecification {

    public static <T> Specification<T> filterEquals(Class<T> clazz, String field, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(fieldToPath(field, root), value);
    }

    public static <T, V> Specification<T> filterEqualsV(Class<T> clazz, String field, V value) {
        if (value == null) {
            return null;
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(fieldToPath(field, root), value);
    }

    public static <T, V extends Comparable<V>> Specification<T> greaterThan(Class<T> clazz, String field, V value) {
        if (value == null) {
            return null;
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(fieldToPath(field, (Root<V>) root), value);
    }

    public static <T> Specification<T> filterEquals(Class<T> clazz, String field, Long value) {
        if (value == null) {
            return null;
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(fieldToPath(field, root), value);
    }

    public static <T> Specification<T> filterContainsText(Class<T> clazz, String field, String value) {
        if (value == null || value.isEmpty()) {
            return null;
        }
        return (root, query, criteriaBuilder) -> criteriaBuilder.like(
                criteriaBuilder.lower(fieldToPath(field, (Root<String>) root)),
                "%" + value.toLowerCase() + "%"
        );
    }

//    private static <T> Path<T> fieldToPath(String field, Root<T> root) {
//        String[] parts = field.split("\\.");
//        Path<T> res = root;
//        for (String p : parts) {
//            res = res.get(p);
//        }
//        return res;
//    }
private static <T, V> Path<V> fieldToPath(String field, Root<T> root) {
    Path<?> path = root;
    for (String p : field.split("\\.")) {
        path = path.get(p);
    }
    @SuppressWarnings("unchecked")
    Path<V> result = (Path<V>) path;
    return result;
}


    public static <T, V extends Comparable<? super V>> Specification<T> filterBetween(Class<T> clazz, String field, V from, V to) {
        return (root, query, cb) -> {
            Path<V> path = fieldToPath(field, root);
            if (from != null && to != null) {
                return cb.between(path, from, to);
            } else if (from != null) {
                return cb.greaterThanOrEqualTo(path, from);
            } else if (to != null) {
                return cb.lessThanOrEqualTo(path, to);
            } else {
                return null;
            }
        };
    }

}

