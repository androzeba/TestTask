package com.space.controller;

import com.space.model.Ship;
import com.space.model.ShipFilterParam;
import com.space.model.ShipType;
import com.space.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/rest")
public class ShipController {

    @Autowired
    private ShipService shipService;

    private Integer count = 0;

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getCount() {
        return count;
    }

    @GetMapping("/ships")
    public ResponseEntity<List<Ship>> getAllShips(
            @RequestParam(name = "pageNumber", required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", required = false) Integer pageSize,
            @RequestParam(name = "order", required = false) String order,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "planet", required = false) String planet,
            @RequestParam(name = "shipType", required = false) ShipType shipType,
            @RequestParam(name = "after", required = false) Long after,
            @RequestParam(name = "before", required = false) Long before,
            @RequestParam(name = "isUsed", required = false) Boolean isUsed,
            @RequestParam(name = "minSpeed", required = false) Double minSpeed,
            @RequestParam(name = "maxSpeed", required = false) Double maxSpeed,
            @RequestParam(name = "minCrewSize", required = false) Integer minCrewSize,
            @RequestParam(name = "maxCrewSize", required = false) Integer maxCrewSize,
            @RequestParam(name = "minRating", required = false) Double minRating,
            @RequestParam(name = "maxRating", required = false) Double maxRating
            ) {
        String sortOrder;
        if (pageNumber == null) pageNumber = 0;
        if (pageSize == null) pageSize = 3;
        if (order == null || order.equals("")) {
            sortOrder = ShipOrder.ID.getFieldName();
        }
        else {
            sortOrder = ShipOrder.valueOf(order).getFieldName();
        }
        if (!Objects.nonNull(minSpeed)) minSpeed = 0.01;
        if (!Objects.nonNull(maxSpeed)) maxSpeed = 0.99;
        if (!Objects.nonNull(minCrewSize)) minCrewSize = 1;
        if (!Objects.nonNull(maxCrewSize)) maxCrewSize = 9999;
        if (!Objects.nonNull(minRating)) minRating = 0.0;
        if (!Objects.nonNull(maxRating)) maxRating = Double.MAX_VALUE;
        if (!Objects.nonNull(after)) after = Date.valueOf("2800-01-01").getTime();
        if (!Objects.nonNull(before)) before = Date.valueOf("3020-01-01").getTime();
        ShipFilterParam shipFilterParam = new ShipFilterParam(
                pageNumber,
                pageSize,
                sortOrder,
                after,
                before,
                minSpeed,
                maxSpeed,
                minCrewSize,
                maxCrewSize,
                minRating,
                maxRating,
                name,
                planet,
                shipType,
                isUsed);
        Page<Ship> allShips = this.shipService.findAll(shipFilterParam);
        return new ResponseEntity<>(allShips.getContent(), HttpStatus.OK);
    }

    @GetMapping("/ships/count")
    public ResponseEntity<Integer> getShipsCount(
            @RequestParam(name = "pageNumber", required = false) Integer pageNumber,
            @RequestParam(name = "pageSize", required = false) Integer pageSize,
            @RequestParam(name = "order", required = false) String order,
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "planet", required = false) String planet,
            @RequestParam(name = "shipType", required = false) ShipType shipType,
            @RequestParam(name = "after", required = false) Long after,
            @RequestParam(name = "before", required = false) Long before,
            @RequestParam(name = "isUsed", required = false) Boolean isUsed,
            @RequestParam(name = "minSpeed", required = false) Double minSpeed,
            @RequestParam(name = "maxSpeed", required = false) Double maxSpeed,
            @RequestParam(name = "minCrewSize", required = false) Integer minCrewSize,
            @RequestParam(name = "maxCrewSize", required = false) Integer maxCrewSize,
            @RequestParam(name = "minRating", required = false) Double minRating,
            @RequestParam(name = "maxRating", required = false) Double maxRating
    ) {
        String sortOrder;
        if (pageNumber == null) pageNumber = 0;
        if (pageSize == null) pageSize = 3;
        if (order == null || order.equals("")) {
            sortOrder = ShipOrder.ID.getFieldName();
        }
        else {
            sortOrder = ShipOrder.valueOf(order).getFieldName();
        }
        if (!Objects.nonNull(minSpeed)) minSpeed = 0.01;
        if (!Objects.nonNull(maxSpeed)) maxSpeed = 0.99;
        if (!Objects.nonNull(minCrewSize)) minCrewSize = 1;
        if (!Objects.nonNull(maxCrewSize)) maxCrewSize = 9999;
        if (!Objects.nonNull(minRating)) minRating = 0.0;
        if (!Objects.nonNull(maxRating)) maxRating = Double.MAX_VALUE;
        if (!Objects.nonNull(after)) after = Date.valueOf("2800-01-01").getTime();
        if (!Objects.nonNull(before)) before = Date.valueOf("3020-01-01").getTime();
        ShipFilterParam shipFilterParam = new ShipFilterParam(
                pageNumber,
                pageSize,
                sortOrder,
                after,
                before,
                minSpeed,
                maxSpeed,
                minCrewSize,
                maxCrewSize,
                minRating,
                maxRating,
                name,
                planet,
                shipType,
                isUsed);
        Page<Ship> allShips = this.shipService.findAll(shipFilterParam);
        setCount(Math.toIntExact(allShips.getTotalElements()));
        return new ResponseEntity<>(getCount(), HttpStatus.OK);
    }

    @GetMapping("/ships/{id}")
    public ResponseEntity<Ship> getShipById(@PathVariable("id") String id) {
        try {
            Long result = Long.parseLong(id);
            if (result > 0) {
                Ship ship = this.shipService.findById(result);
                if (ship == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(ship, HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    @PostMapping("/ships")
    @ResponseBody
    public ResponseEntity<Ship> createShip(@RequestBody Ship ship) {
        if (ship == null) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (StringUtils.isEmpty(ship.getName()) || ship.getName().length() > 50
                || StringUtils.isEmpty(ship.getPlanet()) || ship.getPlanet().length() > 50
                || !Objects.nonNull(ship.getSpeed()) || Math.round(ship.getSpeed() * 100)/100.0 < 0.01 || Math.round(ship.getSpeed() * 100)/100.0 > 0.99
                || !Objects.nonNull(ship.getCrewSize()) || ship.getCrewSize() < 1 || ship.getCrewSize() > 9999
                || !Objects.nonNull(ship.getProdDate()) || ship.getProdDate().getTime() < 0
                || ship.getProdDate().before(Date.valueOf("2800-01-01")) || ship.getProdDate().after(Date.valueOf("3020-01-01"))) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (ship.getUsed() == null) ship.setUsed(false);
        ship.setRating(calculateRating(ship));
        this.shipService.createShip(ship);
        return new ResponseEntity<>(ship, HttpStatus.OK);
    }


    @PostMapping("/ships/{id}")
    @ResponseBody
    public ResponseEntity<Ship> updateShip(@PathVariable("id") String id, @RequestBody Ship updatedShip) {
        if (!Objects.nonNull(updatedShip)) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        Ship ship;
        try {
            Long shipId = Long.parseLong(id);
            if (shipId > 0) {
                ship = this.shipService.findById(shipId);
                if (ship == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
            else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (Objects.nonNull(updatedShip.getName())) {
            if (updatedShip.getName().equals("") || updatedShip.getName().length() > 50) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            else ship.setName(updatedShip.getName());
        }
        if (Objects.nonNull(updatedShip.getPlanet())) {
            if (updatedShip.getPlanet().equals("") || updatedShip.getPlanet().length() > 50) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            else ship.setPlanet(updatedShip.getPlanet());
        }
        if (Objects.nonNull(updatedShip.getProdDate())) {
            if (updatedShip.getProdDate().before(Date.valueOf("2800-01-01")) || updatedShip.getProdDate().after(Date.valueOf("3020-01-01"))) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            else ship.setProdDate(updatedShip.getProdDate());
        }
        if (Objects.nonNull(updatedShip.getSpeed())) {
            if (Math.round(updatedShip.getSpeed() * 100)/100.0 < 0.01 || Math.round(updatedShip.getSpeed() * 100)/100.0 > 0.99) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            else ship.setSpeed(Math.round(updatedShip.getSpeed() * 100)/100.0);
        }
        if (Objects.nonNull(updatedShip.getCrewSize())) {
            if (updatedShip.getCrewSize() < 1 || updatedShip.getCrewSize() > 9999) return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            else ship.setCrewSize(updatedShip.getCrewSize());
        }
        if (Objects.nonNull(updatedShip.getUsed())) ship.setUsed(updatedShip.getUsed());
        if (Objects.nonNull(updatedShip.getShipType())) ship.setShipType(updatedShip.getShipType());
        ship.setRating(calculateRating(ship));
        this.shipService.updateShip(ship);
        return new ResponseEntity<>(ship, HttpStatus.OK);
    }

    @DeleteMapping("/ships/{id}")
    public ResponseEntity<Ship> deleteShip(@PathVariable ("id") String id) {
        try {
            Long shipId = Long.parseLong(id);
            if (shipId > 0) {
                Ship ship = this.shipService.findById(shipId);
                if (ship == null) return new ResponseEntity<>(HttpStatus.NOT_FOUND);
                this.shipService.deleteShip(shipId);
                return new ResponseEntity<>(HttpStatus.OK);
            }
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }



    Double calculateRating(Ship ship) {
        Double rating;
        double k;
        if (ship.getUsed())  k = 0.5; else k = 1.0;
        Calendar now = new GregorianCalendar();
        int currentYear = now.get(Calendar.YEAR) + 999;
        now.setTime(ship.getProdDate());
        int prodYear = now.get(Calendar.YEAR);
        rating = (80 * ship.getSpeed() * k) / (currentYear - prodYear + 1);
        return Math.round(rating * 100) / 100.0;
    }



}
