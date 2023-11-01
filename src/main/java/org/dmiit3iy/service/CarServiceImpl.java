package org.dmiit3iy.service;

import org.dmiit3iy.exceptions.ConstraintViolationException;
import org.dmiit3iy.model.Car;
import org.dmiit3iy.model.Student;
import org.dmiit3iy.repository.CarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarServiceImpl implements CarService {
    private CarRepository carRepository;

    private StudentService studentService;

    @Autowired
    public void setCarRepository(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Autowired
    public void setStudentService(StudentService studentService) {
        this.studentService = studentService;
    }

    @Override
    public void add(long idStudent, Car car) {
        try {
            Student student = this.studentService.get(idStudent);
            car.setStudent(student);
            this.carRepository.save(car);
        } catch (ConstraintViolationException e) {
            throw new IllegalArgumentException("Car has already added!");
        }
    }

    @Override
    public List<Car> get() {
        return this.carRepository.findAll();
    }

    @Override
    public Car get(long id) {
        return this.carRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("This car does not exists!"));
    }

    @Override
    public List<Car> get(String brand) {
        return this.carRepository.findByBrand(brand);
    }

    @Override
    public Car delete(long id) {
        Car car = this.get(id);
        this.carRepository.deleteById(id);
        return car;
    }

    @Override
    public void update(Car car) {
        try {
            Car old = this.get(car.getId());
            old.setYear(car.getYear());
            old.setPower(car.getPower());
            old.setBrand(car.getBrand());

            this.carRepository.save(old);
        } catch (ConstraintViolationException e) {
            throw new IllegalArgumentException("This student has already added!");
        }
    }
}
