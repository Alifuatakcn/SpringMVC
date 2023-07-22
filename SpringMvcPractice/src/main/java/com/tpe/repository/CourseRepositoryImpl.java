package com.tpe.repository;
import com.tpe.domain.Course;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public class CourseRepositoryImpl implements CourseRepository {
    @Autowired
    private SessionFactory sessionFactory;
    @Override
    public List<Course> save(Course course) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        session.saveOrUpdate(course);  //daha önce db de varsa update yoksa save işlemi yapar
        tx.commit();
        session.close();
       return save(course);
    }
    @Override
    public List<Course> getAll() {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        List<Course> courseList = session.createQuery("FROM Course", Course.class).getResultList();
        tx.commit();
        session.close();
        return courseList;
    }
    @Override
    public Optional<Course> findById(Long id) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        Course course = session.get(Course.class, id);
        Optional<Course> optCourse = Optional.ofNullable(course); //ofNullable course ı sarmalıyor
        //eğer null değer alırsa exception fırlatma diyor, null yerine boş bir optional döner
        tx.commit();
        session.close();
        return optCourse;
    }
    @Override
    public void delete(Long id) {
        Session session = sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        Course course=session.load(Course.class,id);
        session.delete(course);
        tx.commit();
        session.close();
    }
}