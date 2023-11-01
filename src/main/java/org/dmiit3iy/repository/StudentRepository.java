package org.dmiit3iy.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.dmiit3iy.exceptions.ConstraintViolationException;
import org.dmiit3iy.model.Student;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class StudentRepository {
    @Value("${datasource.filenameStudents}")
    private String filename;
    private Map<Long, Student> studentHashMap = new HashMap<>();
    private ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void init() {
        try {
            ArrayList<Student> list = this.objectMapper.readValue(new File(filename), new TypeReference<>() {
            });
            this.studentHashMap = list.stream().collect(Collectors.toMap(Student::getId, x -> x));
        } catch (IOException ignored) {
        }
    }

    public void save(Student student) throws ConstraintViolationException {
        if (this.studentHashMap.values().stream().anyMatch(x -> x.getNum().equals(student.getNum()) && x.getFio()
                .equals(student.getFio()) && x.getAge() == student.getAge() && x.getSalary().equals(student.getSalary())
                && x.getId() != student.getId())) {
            throw new ConstraintViolationException("Duplicate entry");
        }
        if (student.getId() == 0) {
            long id = studentHashMap.keySet().stream().mapToLong(x -> x).max().orElse(0L) + 1;
            student.setId(id);
        }
        this.studentHashMap.put(student.getId(), student);
        this.save();
    }

    private void save() {
        try {
            this.objectMapper.writeValue(new File(this.filename), this.studentHashMap.values());
        } catch (IOException ignored) {
        }
    }

    public List<Student> findAll() {
        return new ArrayList<>(this.studentHashMap.values());
    }

    public Optional<Student> findById(long id) {
        return Optional.ofNullable(this.studentHashMap.get(id));
    }

    public List<Student> findByFio(String fio) {
        return this.studentHashMap.values().stream()
                .filter(x -> x.getFio().equals(fio)).collect(Collectors.toList());
    }

    public List<Student> findByAge(int age) {
        return this.studentHashMap.values().stream()
                .filter(x -> x.getAge() == age).collect(Collectors.toList());
    }

    public List<Student> findByNum(String num) {
        return this.studentHashMap.values().stream()
                .filter(x -> x.getNum().equals(num)).collect(Collectors.toList());
    }

    public void deleteById(long id) {
        this.studentHashMap.remove(id);
        this.save();
    }
}
