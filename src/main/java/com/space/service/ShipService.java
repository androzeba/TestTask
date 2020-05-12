package com.space.service;

import com.space.model.Ship;
import com.space.model.ShipFilterParam;
import org.springframework.data.domain.Page;


public interface ShipService {

    Page<Ship> findAll(ShipFilterParam shipFilterParam);

    Ship findById(Long id);

    Integer getCount();

    void createShip(Ship ship);

    void updateShip(Ship ship);

    void deleteShip(Long id);



}
