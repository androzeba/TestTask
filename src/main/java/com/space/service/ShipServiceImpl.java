package com.space.service;

import com.space.model.Ship;
import com.space.model.ShipFilterParam;
import com.space.repository.ShipRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class ShipServiceImpl implements ShipService {

    @Autowired
    private ShipRepository shipRepository;

    @Autowired
    private ShipSpecification shipSpecification;

    @Override
    public Page<Ship> findAll(ShipFilterParam shipFilterParam) {
        Pageable pageable = PageRequest.of(shipFilterParam.getPageNumber(), shipFilterParam.getPageSize(), Sort.by(shipFilterParam.getOrder()));
        return shipRepository.findAll(shipSpecification.getShipSpec(shipFilterParam), pageable);
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
