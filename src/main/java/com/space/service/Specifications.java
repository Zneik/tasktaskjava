package com.space.service;

import com.space.model.Ship;
import com.space.model.ShipType;
import org.springframework.data.jpa.domain.Specification;

import java.util.Date;

public class Specifications {
    public static Specification<Ship> filterByCrewSize(Integer minCrewSize, Integer maxCrewSize) {
        return (root, query, criteriaBuilder) -> {
            if (minCrewSize == null && maxCrewSize == null) return null;
            if (minCrewSize == null) return criteriaBuilder.lessThanOrEqualTo(root.get("crewSize"), maxCrewSize);
            if (maxCrewSize == null) return criteriaBuilder.greaterThanOrEqualTo(root.get("crewSize"), minCrewSize);
            return criteriaBuilder.between(root.get("crewSize"), minCrewSize, maxCrewSize);
        };
    }

    public static Specification<Ship> filterByIsUsed(Boolean isUsed) {
        return (root, query, criteriaBuilder) -> {
            if (isUsed == null) return null;
            return criteriaBuilder.equal(root.get("isUsed"), isUsed);
        };
    }

    public static Specification<Ship> filterByName(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null) return null;
            return criteriaBuilder.like(root.get("name"), "%" + name + "%");
        };
    }

    public static Specification<Ship> filterByPlanet(String planet) {
        return (root, query, criteriaBuilder) -> {
            if (planet == null) return null;
            return criteriaBuilder.like(root.get("planet"), "%" + planet + "%");
        };
    }

    public static Specification<Ship> filterByProdDate(Long after, Long before) {
        return (root, query, criteriaBuilder) -> {
            if (after == null && before == null) return null;
            if (after == null) return criteriaBuilder.lessThanOrEqualTo(root.get("prodDate"),
                    new Date(before));
            if (before == null) return criteriaBuilder.greaterThanOrEqualTo(root.get("prodDate"),
                    new Date(after));
            return criteriaBuilder.between(
                    root.get("prodDate"),
                    new Date(after),
                    new Date(before)
            );
        };
    }

    public static Specification<Ship> filterByRating(Double minRating, Double maxRating) {
        return (root, query, criteriaBuilder) -> {
            if (minRating == null && maxRating == null) return null;
            if (minRating == null) return criteriaBuilder.lessThanOrEqualTo(root.get("rating"), maxRating);
            if (maxRating == null) return criteriaBuilder.greaterThanOrEqualTo(root.get("rating"), minRating);
            return criteriaBuilder.between(root.get("rating"), minRating, maxRating);
        };
    }

    public static Specification<Ship> filterBySpeed(Double minSpeed, Double maxSpeed) {
        return (root, query, criteriaBuilder) -> {
            if (minSpeed == null && maxSpeed == null) return null;
            if (minSpeed == null) return criteriaBuilder.lessThanOrEqualTo(root.get("speed"), maxSpeed);
            if (maxSpeed == null) return criteriaBuilder.greaterThanOrEqualTo(root.get("speed"), minSpeed);
            return criteriaBuilder.between(root.get("speed"), minSpeed, maxSpeed);
        };
    }

    public static Specification<Ship> filterByShipType(ShipType shipType) {
        return (root, query, criteriaBuilder) -> {
            if (shipType == null) return null;
            return criteriaBuilder.equal(root.get("shipType"), shipType);
        };
    }
}
