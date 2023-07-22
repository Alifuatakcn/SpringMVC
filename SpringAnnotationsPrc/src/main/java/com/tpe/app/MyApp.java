package com.tpe.app;

import com.tpe.domain.Course;
import com.tpe.service.CourseService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class MyApp {
    public static void main(String[] args) {

        AnnotationConfigApplicationContext contex=new AnnotationConfigApplicationContext(ApplicationContext.class);
        Course course=new Course();
        course.setName("Spring MVC");

        CourseService service=contex.getBean("zoomService", CourseService.class);
        service.teachCourse(course);
        service.saveCourse(course);


    }
}
