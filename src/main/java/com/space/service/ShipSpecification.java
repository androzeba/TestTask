package com.space.service;

import com.space.model.Ship;
import com.space.model.ShipFilterParam;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.sql.Date;
import java.util.Objects;

@Component
public class ShipSpecification {

    public Specification<Ship> getShipSpec(ShipFilterParam shipFilterParam) {
        return new Specification<Ship>() {
            @Override
            public Predicate toPredicate(Root<Ship> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                Predicate pr = cb.conjunction();
                if (!StringUtils.isEmpty(shipFilterParam.getName())) {
                    pr = cb.and(pr, cb.like(root.get("name"), "%" + shipFilterParam.getName() + "%"));
                }
                if (!StringUtils.isEmpty(shipFilterParam.getPlanet())) {
                    pr = cb.and(pr, cb.like(root.get("planet"), "%" + shipFilterParam.getPlanet() + "%"));
                }
                if (Objects.nonNull(shipFilterParam.getShipType())) {
                    pr = cb.and(pr, cb.equal(root.get("shipType"), shipFilterParam.getShipType()));
                }
                if (Objects.nonNull(shipFilterParam.isUsed())) {
                    pr = cb.and(pr, cb.equal(root.get("isUsed"), shipFilterParam.isUsed()));
                }
                if (Objects.nonNull(shipFilterParam.getMinSpeed())
                        && Objects.nonNull(shipFilterParam.getMaxSpeed())
                        && (shipFilterParam.getMinSpeed() <= shipFilterParam.getMaxSpeed())) {
                    pr = cb.and(pr, cb.between(root.get("speed"), shipFilterParam.getMinSpeed(), shipFilterParam.getMaxSpeed()));
                }
                if (Objects.nonNull(shipFilterParam.getMinCrewSize())
                        && Objects.nonNull(shipFilterParam.getMaxCrewSize())
                        && (shipFilterParam.getMinCrewSize() <= shipFilterParam.getMaxCrewSize())) {
                    pr = cb.and(pr, cb.between(root.get("crewSize"), shipFilterParam.getMinCrewSize(), shipFilterParam.getMaxCrewSize()));
                }
                if (Objects.nonNull(shipFilterParam.getMinRating())
                        && Objects.nonNull(shipFilterParam.getMaxRating())
                        && (shipFilterParam.getMinRating() <= shipFilterParam.getMaxRating())) {
                    pr = cb.and(pr, cb.between(root.get("rating"), shipFilterParam.getMinRating(), shipFilterParam.getMaxRating()));
                }
                if (Objects.nonNull(shipFilterParam.getAfter())
                        && Objects.nonNull(shipFilterParam.getBefore())
                        && (new Date(shipFilterParam.getAfter()).before(new Date(shipFilterParam.getBefore())))) {
                    pr = cb.and(pr, cb.between(root.get("prodDate"), new Date(shipFilterParam.getAfter()), new Date(shipFilterParam.getBefore())));
                }
                return pr;
            }
        };
    }
}
