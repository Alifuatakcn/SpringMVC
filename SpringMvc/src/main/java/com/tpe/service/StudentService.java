package com.tpe.service;

import com.tpe.domain.Student;

import java.util.List;

public interface StudentService {

    void saveStudent(Student student);
    List<Student> getAllStudent();
    Student getStudentById(Long id);
    void deleteStudent(Long id);
}
