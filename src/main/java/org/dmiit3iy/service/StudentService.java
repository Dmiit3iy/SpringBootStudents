package org.dmiit3iy.service;


import org.dmiit3iy.model.Student;

import java.util.List;

public interface StudentService {
    void add(Student student);

    List<Student> get();

    Student get(long id);

    List<Student> get(int age);

    Student delete(long id);

    void update(Student student);
}
