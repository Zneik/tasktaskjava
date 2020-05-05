package com.space.service;

import com.space.controller.ShipOrder;
import com.space.exception.BadRequestException;
import com.space.exception.NotFoundException;
import com.space.model.Ship;
import com.space.model.ShipType;
import com.space.repository.ShipRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class ShipService {
    private final ShipRepository shipRepository;

    @Autowired
    public ShipService(ShipRepository shipRepository) {
        this.shipRepository = shipRepository;
    }

    public List<Ship> getShips(
            String name,
            String planet,
            ShipType shipType,
            Long after,
            Long before,
            Boolean isUsed,
            Double minSpeed,
            Double maxSpeed,
            Integer minCrewSize,
            Integer maxCrewSize,
            Double minRating,
            Double maxRating,
            ShipOrder order,
            Integer pageNumber,
            Integer pageSize) {
        Specification<Ship> specification = getShipSpecification(
                name, planet, shipType,
                after, before, isUsed,
                minSpeed, maxSpeed, minCrewSize,
                maxCrewSize, minRating, maxRating);

        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(order.getFieldName()));
        return shipRepository.findAll(specification, pageable).getContent();

    }

    private Specification<Ship> getShipSpecification(
            String name, String planet, ShipType shipType,
            Long after, Long before, Boolean isUsed,
            Double minSpeed, Double maxSpeed,
            Integer minCrewSize, Integer maxCrewSize,
            Double minRating, Double maxRating) {
        return Specification.where(Specifications.filterByName(name))
                .and(Specifications.filterByCrewSize(minCrewSize, maxCrewSize))
                .and(Specifications.filterByIsUsed(isUsed))
                .and(Specifications.filterByPlanet(planet))
                .and(Specifications.filterByProdDate(after, before))
                .and(Specifications.filterBySpeed(minSpeed, maxSpeed))
                .and(Specifications.filterByRating(minRating, maxRating))
                .and(Specifications.filterByShipType(shipType));
    }

    public Integer getShipsCount(
            String name,
            String planet,
            ShipType shipType,
            Long after,
            Long before,
            Boolean isUsed,
            Double minSpeed,
            Double maxSpeed,
            Integer minCrewSize,
            Integer maxCrewSize,
            Double minRating,
            Double maxRating
    ) {

        Specification<Ship> specification = getShipSpecification(name, planet, shipType,
                after, before, isUsed,
                minSpeed, maxSpeed, minCrewSize, maxCrewSize,
                minRating, maxRating);
        return shipRepository.findAll(specification).size();
    }

    public Long checkId(String id) {
        Long nId = null;
        try {
            nId = Long.parseLong(id);
        } catch (Throwable e) {
            throw new BadRequestException();
        }
        if (nId <= 0)
            throw new BadRequestException();
        return nId;
    }

    public Ship getShip(String id) {
        Long nId = checkId(id);
        Ship ship = shipRepository.findById(nId).orElseThrow(NotFoundException::new);
        return ship;
    }

    private Double calcRating(Ship ship) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(ship.getProdDate());
        int year = cal.get(Calendar.YEAR);

        BigDecimal raiting = new BigDecimal((80 * ship.getSpeed() * (ship.getUsed() ? 0.5 : 1)) / (3019 - year + 1));
        raiting = raiting.setScale(2, RoundingMode.HALF_UP);
        return raiting.doubleValue();
    }

    public Ship createShip(Ship ship) {
        Ship copy = new Ship();
        BeanUtils.copyProperties(ship, copy, "id", "rating");
        checkEmpty(copy);
        if (copy.getUsed() == null) copy.setUsed(false);
        checkName(copy.getName());
        checkPlanet(copy.getPlanet());
        checkSpeed(copy.getSpeed());
        checkCrewSize(copy.getCrewSize());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(copy.getProdDate());
        checkProdDate(copy.getProdDate());
        copy.setSpeed(roundSpeed(copy.getSpeed()));
        copy.setRating(calcRating(copy));
        Ship newShip = shipRepository.save(copy);
        return shipRepository.findById(newShip.getId()).orElseThrow(NotFoundException::new);
    }

    private Double roundSpeed(Double speed) {
        return new BigDecimal(speed).setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    private void checkProdDate(Date prodDate) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTime(prodDate);
        int year = calendar.get(Calendar.YEAR);
        if (year < 2800 || year > 3019)
            throw new BadRequestException();
    }

    private void checkCrewSize(Integer crewSize) {
        if (crewSize < 1 || crewSize > 9999)
            throw new BadRequestException();
    }

    private void checkSpeed(Double speed) {
        if (speed < 0.01 || speed > 0.99)
            throw new BadRequestException();
    }

    private void checkPlanet(String planet) {
        if (planet.isEmpty() || planet.length() > 50)
            throw new BadRequestException();
    }

    private void checkName(String name) {
        if (name.isEmpty() || name.length() > 50)
            throw new BadRequestException();
    }

    private void checkEmpty(Ship copy) {
        if (
                copy.getName() == null ||
                        copy.getPlanet() == null ||
                        copy.getShipType() == null ||
                        copy.getProdDate() == null ||
                        copy.getSpeed() == null ||
                        copy.getCrewSize() == null
        )
            throw new BadRequestException();
    }

    public Ship updateShip(String id, Ship ship) {
        final Long nId = checkId(id);
        Ship shipFromDb = shipRepository.findById(nId).orElseThrow(NotFoundException::new);
        if (ship.getName() != null) {
            checkName(ship.getName());
            shipFromDb.setName(ship.getName());
        }
        if (ship.getPlanet() != null) {
            checkPlanet(ship.getPlanet());
            shipFromDb.setPlanet(ship.getPlanet());
        }
        if (ship.getShipType() != null) {
            shipFromDb.setShipType(ship.getShipType());
        }
        if (ship.getProdDate() != null) {
            checkProdDate(ship.getProdDate());
            shipFromDb.setProdDate(ship.getProdDate());
        }
        if (ship.getUsed() != null) {
            shipFromDb.setUsed(ship.getUsed());
        }
        if (ship.getSpeed() != null) {
            checkSpeed(ship.getSpeed());
            shipFromDb.setSpeed(roundSpeed(ship.getSpeed()));
        }
        if (ship.getCrewSize() != null) {
            checkCrewSize(ship.getCrewSize());
            shipFromDb.setCrewSize(ship.getCrewSize());
        }
        shipFromDb.setRating(calcRating(shipFromDb));
        shipRepository.save(shipFromDb);
        return shipRepository.findById(nId).get();
    }

    public void deleteShip(String id) {
        final Long nId = checkId(id);
        Ship shipFromDb = shipRepository.findById(nId).orElseThrow(NotFoundException::new);
        shipRepository.delete(shipFromDb);
    }
}
