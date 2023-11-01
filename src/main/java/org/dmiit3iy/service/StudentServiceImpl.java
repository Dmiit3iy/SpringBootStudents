package org.dmiit3iy.service;
import org.dmiit3iy.exceptions.ConstraintViolationException;
import org.dmiit3iy.model.Car;
import org.dmiit3iy.model.Student;
import org.dmiit3iy.repository.CarRepository;
import org.dmiit3iy.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {
    private StudentRepository studentRepository;

    private CarRepository carRepository;

    @Autowired
    public void setStudentRepository(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }


    @Autowired
    public void setCarRepository(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @Override
    public void add(Student student) {
        try {
            this.studentRepository.save(student);
        } catch (ConstraintViolationException e) {
            throw new IllegalArgumentException("This student has already added!");
        }
    }

    @Override
    public List<Student> get() {
        return this.studentRepository.findAll();
    }

    @Override
    public Student get(long id) {
        return this.studentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("This student does not exists!"));
    }

    @Override
    public List<Student> get(int age) {
        return this.studentRepository.findByAge(age);
    }

    @Override
    public Student delete(long id) {
        Student student = this.get(id);
        List<Car> carList = this.carRepository.findAll().stream()
                .filter(x -> x.getStudent().getId() == id).collect(Collectors.toList());
        this.studentRepository.deleteById(id);
        for (var car : carList) {
            carRepository.deleteById(car.getId());
        }
        return student;
    }

    @Override
    public void update(Student student) {
        try {
            this.get(student.getId());
            this.studentRepository.save(student);
        } catch (ConstraintViolationException e) {
            throw new IllegalArgumentException("This student has already added!");
        }
    }
}
