package com.space.controller;

import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.service.ShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("rest")
public class ShipController {
    final private ShipService shipService;

    @Autowired
    public ShipController(ShipService shipService) {
        this.shipService = shipService;
    }

    @GetMapping("ships")
    public List<Ship> getShips(
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
            @RequestParam(name = "maxRating", required = false) Double maxRating,
            @RequestParam(name = "order", defaultValue = "ID") ShipOrder order,
            @RequestParam(name = "pageNumber", defaultValue = "0") Integer pageNumber,
            @RequestParam(name = "pageSize", defaultValue = "3") Integer pageSize
    ) {
        return shipService.getShips(
                name,
                planet,
                shipType,
                after,
                before,
                isUsed,
                minSpeed,
                maxSpeed,
                minCrewSize,
                maxCrewSize,
                minRating,
                maxRating,
                order,
                pageNumber,
                pageSize
        );
    }

    @GetMapping("ships/count")
    public Integer getShipsCount(
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
        return shipService.getShipsCount(
                name,
                planet,
                shipType,
                after,
                before,
                isUsed,
                minSpeed,
                maxSpeed,
                minCrewSize,
                maxCrewSize,
                minRating,
                maxRating
        );
    }

    @GetMapping("ships/{id}")
    public Ship getShip(@PathVariable("id") String id) {
        return shipService.getShip(id);
    }

    @PostMapping("ships")
    public Ship createShip(@RequestBody Ship ship) {
        return shipService.createShip(ship);
    }

    @PostMapping("ships/{id}")
    public Ship updateShip(@PathVariable("id") String id, @RequestBody Ship ship) {
        return shipService.updateShip(id, ship);
    }

    @DeleteMapping("ships/{id}")
    public void deleteShip(@PathVariable("id") String id) {
        shipService.deleteShip(id);
    }
}
