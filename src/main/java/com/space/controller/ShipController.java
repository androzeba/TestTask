package com.space.controller;

import com.space.model.Ship;
import com.space.model.ShipFilterParam;
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
    

    @GetMapping("/ships")
    public ResponseEntity<List<Ship>> getAllShips(
            @RequestParam(name = "isUsed", required = false) Boolean isUsed,
            @ModelAttribute("shipFilterParam") ShipFilterParam shipFilterParam
            ) {
        shipFilterParam.setUsed(isUsed);
        filterParamCorrection(shipFilterParam);
        Page<Ship> allShips = this.shipService.findAll(shipFilterParam);
        return new ResponseEntity<>(allShips.getContent(), HttpStatus.OK);
    }

    @GetMapping("/ships/count")
        public ResponseEntity<Integer> getShipsCount(
            @RequestParam(name = "isUsed", required = false) Boolean isUsed,
            @ModelAttribute ("shipFilterParam") ShipFilterParam shipFilterParam
    ) {
        shipFilterParam.setUsed(isUsed);
        filterParamCorrection(shipFilterParam);
        Page<Ship> allShips = this.shipService.findAll(shipFilterParam);
        return new ResponseEntity<>(Math.toIntExact(allShips.getTotalElements()), HttpStatus.OK);
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

    private Double calculateRating(Ship ship) {
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

    private void filterParamCorrection(ShipFilterParam shipFilterParam) {
        if (shipFilterParam.getPageNumber() == null) shipFilterParam.setPageNumber(0);
        if (shipFilterParam.getPageSize() == null) shipFilterParam.setPageSize(3);
        if (shipFilterParam.getOrder() == null || shipFilterParam.getOrder().equals("")) {
            shipFilterParam.setOrder(ShipOrder.ID.getFieldName());
        }
        else {
            shipFilterParam.setOrder(ShipOrder.valueOf(shipFilterParam.getOrder()).getFieldName());
        }
        if (!Objects.nonNull(shipFilterParam.getMinSpeed())) shipFilterParam.setMinSpeed(0.01);
        if (!Objects.nonNull(shipFilterParam.getMaxSpeed())) shipFilterParam.setMaxSpeed(0.99);
        if (!Objects.nonNull(shipFilterParam.getMinCrewSize())) shipFilterParam.setMinCrewSize(1);
        if (!Objects.nonNull(shipFilterParam.getMaxCrewSize())) shipFilterParam.setMaxCrewSize(9999);
        if (!Objects.nonNull(shipFilterParam.getMinRating())) shipFilterParam.setMinRating(0.0);
        if (!Objects.nonNull(shipFilterParam.getMaxRating())) shipFilterParam.setMaxRating(Double.MAX_VALUE);
        if (!Objects.nonNull(shipFilterParam.getAfter())) shipFilterParam.setAfter(Date.valueOf("2800-01-01").getTime());
        if (!Objects.nonNull(shipFilterParam.getBefore())) shipFilterParam.setBefore(Date.valueOf("3020-01-01").getTime());
    }



}
