package com.space.service;

import com.space.model.Ship;
import com.space.model.ShipFilterParam;
import com.space.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.Predicate;
import java.sql.Date;
import java.util.Objects;


@Service
public class ShipServiceImpl implements ShipService {

    @Autowired
    ShipRepository shipRepository;

    @Override
    public Page<Ship> findAll(ShipFilterParam shipFilterParam) {
        Pageable pageable = PageRequest.of(shipFilterParam.getPageNumber(), shipFilterParam.getPageSize(), Sort.by(shipFilterParam.getOrder()));
        Page<Ship> allShips = shipRepository.findAll((Specification<Ship>) (root, cq, cb) -> {
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
        }, pageable);
        return allShips;
    }

    @Override
    public Ship findById(Long id) {
        if (shipRepository.findById(id).isPresent()) return shipRepository.findById(id).get();
        return null;
    }

    @Override
    public Integer getCount() {
        return (int) shipRepository.count();
    }

    @Override
    public void createShip(Ship ship) {
        shipRepository.save(ship);
    }

    @Override
    public void updateShip(Ship ship) {
        shipRepository.save(ship);
    }

    @Override
    public void deleteShip(Long id) {
        shipRepository.deleteById(id);
    }
}
