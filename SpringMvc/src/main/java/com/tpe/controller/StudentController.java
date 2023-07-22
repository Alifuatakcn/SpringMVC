package com.tpe.controller;

import com.tpe.domain.Student;
import com.tpe.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller//@RestController
@RequestMapping("/students")//http://localhost:8080/SpringMvc/students
//class level:tüm metodlar için geçerli
//method level:sadece o method için geçerli
public class StudentController {

    @Autowired
    private StudentService service;

    //controller requeste göre modelandview(data+view name) ya da String olarak sadece view name döner.
    @GetMapping("/hi")//http://localhost:8080/SpringMvc/students/hi
    public ModelAndView sayHi(){
        ModelAndView mav=new ModelAndView();
        mav.addObject("message","Hi;");
        mav.addObject("messagebody","I'm a Student Management System");
        mav.setViewName("hi");//hi.jsp
        return mav;
    }
    //view resolver mav içindeki model ı view name verilen dosyayı bulup içine bind eder.

    //1-Student Creation
    //http://localhost:8080/SpringMvc/students/new
    //kullanıcıdan bilgileri almak için form gösterelim
    @GetMapping("/new")
    public String sendStudentForm(@ModelAttribute("student")Student student){
        return "studentForm";
    }

    //@ModelAttribute view katmanı ile controller arasında data transferini sağlar.


    //student ı DB ye kaydedince tüm öğrencileri listeleyelim.
    //http://localhost:8080/SpringMvc/students/saveStudent+POST
    @PostMapping("/saveStudent")
    public String saveStudent(@ModelAttribute Student student){

        //service de student ı kaydet
        service.saveStudent(student);
        return "redirect:/students";//http://localhost:8080/SpringMvc/students/ bu isteğe yönlendirelim
    }





}
