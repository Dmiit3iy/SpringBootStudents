package org.dmiit3iy.service;

import org.dmiit3iy.model.Car;

import java.util.List;

public interface CarService {
    void add(long idStudent,Car car);

    List<Car> get();

    Car get(long id);

    List<Car> get(String brand);

    Car delete(long id);

    void update(Car car);
}
