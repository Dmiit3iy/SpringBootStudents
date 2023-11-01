package org.dmiit3iy.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.dmiit3iy.exceptions.ConstraintViolationException;
import org.dmiit3iy.model.Car;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;


import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class CarRepository {
    @Value("${datasource.filenameCars}")
    private String filename;
    private Map<Long, Car> carHashMap = new HashMap<>();
    private ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void init() {
        try {
            ArrayList<Car> list = this.objectMapper.readValue(new File(filename), new TypeReference<>() {
            });
            this.carHashMap = list.stream().collect(Collectors.toMap(Car::getId, x -> x));
        } catch (IOException ignored) {
        }
    }

    public void save(Car car) throws ConstraintViolationException {
        if (this.carHashMap.values().stream().anyMatch(x -> x.getBrand()
                .equals(car.getBrand()) && x.getYear() == car.getYear()
                && x.getPower() == car.getPower() && x.getId() != car.getId()
                && x.getStudent().getId() == car.getStudent().getId())) {
            throw new ConstraintViolationException("Duplicate entry");
        }
        if (car.getId() == 0) {
            long id = carHashMap.keySet().stream().mapToLong(x -> x).max().orElse(0L) + 1;
            car.setId(id);
        }
        this.carHashMap.put(car.getId(), car);
        this.save();
    }



    private void save() {
        try {
            this.objectMapper.writeValue(new File(this.filename), this.carHashMap.values());
        } catch (IOException ignored) {
        }
    }

    public List<Car> findAll() {
        return new ArrayList<>(this.carHashMap.values());
    }

    public Optional<Car> findById(long id) {
        return Optional.ofNullable(this.carHashMap.get(id));
    }

    public List<Car> findByBrand(String brand) {
        return this.carHashMap.values().stream()
                .filter(x -> x.getBrand().equals(brand)).collect(Collectors.toList());
    }

    public List<Car> findByPower(int power) {
        return this.carHashMap.values().stream()
                .filter(x -> x.getPower() == power).collect(Collectors.toList());
    }

    public void deleteById(long id) {
        this.carHashMap.remove(id);
        this.save();
    }
}
