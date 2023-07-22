package com.tpe.controller;

import com.tpe.domain.Student;
import com.tpe.exception.ResourceNotFoundException;
import com.tpe.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller//gelen request ler karşılanacak/maplenecek
//request lerin karşılanacağı class olduğunu anlaması için bu annotation kullanılmalı
// @RestController yani bu class ta bir rest service class oluşturulacak demiş olurduk, dinamik bir web projesi değil,
// sadece bir RestAPI/ API oluşturacağımız zaman, örneğini SpringBoot da göreceğiz.
@RequestMapping("/students")//http://localhost:8080/SpringMvc
//class level: tüm methodlar için geçerli
//method level: sadece o method için geçerli
//@RequestMapping() annotation u tomcat çalıştığında gelen basis url var. bu class içinde basis url ile birlikte gelen hangi requestleri karşılayacağımı belirtiyorum
//parametre olarak /students dediğimde basis url den sonra /students gelen olan requestler için geçerli oluyor
//class levelde kullanırsak tüm methodlar için geçerli olur.
//@RequestMapping() i method levelde kullanırsak: sadece o method için geçerli olur.

public class StudentController {//--------------------------------------------------------------------------------------

    //service class imi controller a enjekte ediyorum.
    //controller class service class ile iletişime geçer service class ise repository ile iletişime geçer => controller - service - repository ==> 3 Layer Architecture
    @Autowired
    private StudentService service;

    //response olarak sadece read işlemi yapacağım.
    //controller requeste göre modelandview
    @GetMapping("/hi")//read okuma işlemi yapacağız sadece
    //Controller responce olarak view ModelAndView(data ve görüntüleme dosyasının ismini ya da string tipinde sadece view name i) dönebilir
    //Controller, gelen request e göre modelandview(data+view name) ya da String tipinde sadece view name i de dönebilir.
    public ModelAndView sayHi() {//selamlama methodu
        ModelAndView mav = new ModelAndView();
        mav.addObject("message", "Hi;");
        mav.addObject("messagebody", "I'm a Student Management System ");
        mav.setViewName("hi");//hi.jsp dosyasını bulup, çözümleyip view katmanına verecek. hangi dosyayı döneceksek o dosyanın ismini veriyorum
        return mav;//model and view in kısaltılmışı : mav, abjemin adı, oluşturduğum objeyi return ediyorum.

        //view resolwer, mav(ModelAndView) içindeki model'i, dataları, view name i verilen dosyayı bulup içine bind eder(yerleştirir)
    }
//TODO------------------------------------------------------------------------------------------------------------------
//TODO 1-Student Creation----------------------------------------------------------------------------------------------------
    //http://localhost:8080/SpringMvc/students/new ==> bu request i şu method ile karşıla
    //kullanıcıdan bilgileri almak için form gösterelim
    //formu gönder-içindeki bilgileri al
    @GetMapping("/new")// /new ile bir form döndürüyoruz.
    public String sendStudentForm(@ModelAttribute("student") Student student) {//@ModelAttribute ile not aşağıda eklendi
        return "studentForm";
    }

//TODO------------------- save student -----------------------------------------------------------------------------
    //student i DB ye kaydedince response olarak tablo içinde tüm öğrencileri listeleyelim
//    @PostMapping("/saveStudent")
//student form submit edildiğinde action saveStudent olacak, method olarak ise post kullanılacak,
// yeni bir object oluşturacağımız zaman http methodlarından post method kullanıyoruz

//    public String saveStudent(@ModelAttribute Student student) {//geriye sadece bir dosya ismi döneceğim
//                                                                //service de student i kaydediyoruz return etmeden önce. @ModelAttribute Student student
//                                                                //service de student ı kaydet
//        service.saveStudent(student);
//
//        return "redirect:/students";//http://localhost:8080/SpringMvc/students => öğrenci kaydedildiğinde kullanıcı bu isteğe/requeste yönlendirilsin.
//                                                                                  //redirect(yönlendir) students
//
//
//    }
//TODO------------ hatalar kayıtta submit deyince ekranda yansın, görünsün istiyorum -----------------------------------
    @PostMapping("/saveStudent")
//student form submit edildiğinde action saveStudent olacak, method olarak ise post kullanılacak, yeni bir object oluşturacağımız zaman http methodlarından post method kullanıyoruz
    public String saveStudent(@Valid @ModelAttribute Student student, BindingResult bindingResult) {//geriye sadece bir dosya ismi döneceğim////hatalı işlemler olduğunda kontrol edebilmek için @Valid(bu objeyi validasyona tabi tut)
        //@Valid ile validasyona tabi tut eğer hata var ise BindingResult ile hataları al
        if (bindingResult.hasErrors()) {
            return "studentForm";//hata var ise studentForm u tekrar göster.
        }
        //service de student i kaydediyoruz return etmeden önce. @ModelAttribute Student student
        //service de student ı kaydet
        service.saveStudent(student);

        return "redirect:/students";//http://localhost:8080/SpringMvc/students => öğrenci kaydedildiğinde kullanıcı bu isteğe/requeste yönlendirilsin.
        //redirect(yönlendir) students
    }

//service ve repository katmanlarıma gidip öğrencilerimi save methodlarımı hazırladım
    //StudentController da service yardımıyla student i kaydedeceğiz. //burada da service class i controller a enjekte ediyourum => class level a bak
//TODO------------------------------------------------------------------------------------------------------------------
    //TODO 2-list all student---------------------------------------------
/*
http://localhost:8080/SpringMvc/students/ tüm öğrencileri görmek/listelemek için bu request i controller da karşılamamız gerekiyor.
request i karşıladığımda students dosyası içine öğrenci listesi koymuş olacağım, mevcut tüm öğrencileri listeleyerek student.jsp dosyasını hazır hale getirmiş olacağız.

save Student yaparken zaten bu request i karşılayacağım
yeni bir öğrenci DB ye kaydedildiğinde return/response olarak tekrar yönlendirelim saveStudent methodundaki
return "redirect:/students"; bu işlemi yapacak. her iki işlemde de aynı request i kullanmış olacağız.
yeniden yönlendirme aynı request e

//StudentRepository de findAll() method unu düzenledik
//findAll() methodunu StudentService class ta çağırdık
//şimdi burada request i karşılayalım. http://localhost:8080/SpringMvc/students/
hangi http methodu ile gelecek,
bu request/öğrencileri listele  karşılığında ne yapacağız: bir tablo göstereceğiz
POST methodunu yeni bir obje oluşturuyorsak/DB ye yeni bir data kaydedeceksek kullanıyoruz.
PUT, PATCH mevcut datayı güncelle
GET, görüntüleme
 */

    //http://localhost:8080/SpringMvc/students/ + GET(datayı görüntüleyeceğimiz için get method kullandık )
    @GetMapping//karşıla/datayı görüntüle//path girmedik çünkü /students ile gelenleri buraya ekleyecek
    public ModelAndView listAllStudents(){//ModelAndView kullandım neden: studend.jsp dosyamı return edeceğim ancak bu dosya içine DB den gelen öğrenci listemi koyacağım, hem dada hem view katmanı gelecek.
    List<Student> studentList = service.getAllStudent();
    ModelAndView mav = new ModelAndView();
    mav.addObject("students", studentList);//<c:forEach items="${students}" var="student" varStatus="status"> studends.jsp de bulunan items teki isim (students) ile attributeName aynı olması gerekiyor
                                                        // studends.jsp de forEach de bir liste var, bu listeye benim oluşturduğum student listesini ver
        mav.setViewName("students");//viewname ini verdik. students dediğimde students.jsp yi bulmuş olacak.
    return mav;
//student listesini ModelAndView objesi içerisine koydum, students değişkeninin değeri studentList olsun dedim.
    }

//TODO------------------------------------------------------------------------------------------------------------------
    //TODO 3- Update student ---------------------------------------------
    //http://localhost:8080/SpringMvc/students/update?id=1
    //update değerleri girmesi için hazır, save için formunu tekrar gösterip değerleri okusun, farklı bir form da oluşturabilirdik
    //kullanıcıya form gösteriyoruz
    //http methodum burada get olmalı, çünkü form gösteriyorum, bu yüzden @GetMappin
    //save methodundan farkı: ?id=1 requestte gelen parametreyi controller da okuyabilmem lazım ki öğrenciyi bulayım: @RequestParam("id)

    //1.yöntem
    @GetMapping("/update")
    public String showUpdateForm(@RequestParam("id") Long id, Model model){//query parametresi ise bu annotation ile oku, key değeri id olan parametreyi oku  => Long tipinde bir id değişkenine ata(Long id)
//parametre olarak boş bir model aldım ve değer olarak bulduğum öğrenciyi ekleyerek formu dolu olarak döndürüyor.
        Student foundStudent = service.getStudentById(id);
        model.addAttribute("student", foundStudent);//items teki attribute student olduğu için
        return "studentForm";
    }


    //2.yöntem
    //geriye String değil ModelAndview de döndürebilirdik
//    @GetMapping("/update")
//    public ModelAndView showUpdateForm(@RequestParam("id") Long id){//query parametresi ise bu annotation ile oku, key değeri id olan parametreyi oku  => Long tipinde bir id değişkenine ata(Long id)
//
//        Student foundStudent = service.getStudentById(id);
//        ModelAndView mav = new ModelAndView();
//        mav.addObject("student", foundStudent);
//        mav.setViewName("studentForm");
//        return mav;
//
//        //modelandview kullanma sebebim; geriye student döndüreceğim ama içine data ekleyeceğiz
//    }
/*
save işlemi yaparken boş form gösterdik kullanıcıya, update linkine tıkladığında öğrenciyi tekrar görmesi gerekir formda, dolu form göstermeliyim
parametre olarak bir Model al, bu model a bir attribute ekle, id si verilen öğrenciyi bulup vereceğim, getStudentById method ile bulup model içine ekliyorum,
 */


//TODO------------------------------------------------------------------------------------------------------------------
//TODO 4- delete student ---------------------------------------------
//http://localhost:8080/SpringMvc/students/delete/4 => request
//silme işleminden sonra tüm kayıtları listeleyelim: @GetMapping("/delete/{id})
    @GetMapping("/delete/{id}")
    public String deleteStudents(@PathVariable("id") Long id){
        service.deleteStudent(id);//andpoint teki pathvariable deki id yi verince siliyor
        return "redirect:/students";//tekrar /students, tüm öğrencilerin listelendiği sayfaya yönlendir demiş olduk.
    }

//@ExceptionHandler try-cacth mantığı ile çalışır. eğer exception yakalanırsa belirtilen işlemi yapar
    //exception yakalanırsa notFound.jsp dosyasını döneceğiz
    @ExceptionHandler(ResourceNotFoundException.class)
        public ModelAndView handleException(Exception ex){//exception u handle etme
        ModelAndView mav = new ModelAndView();//modelandview obj oluştur
        mav.addObject("message", ex.getMessage());//message değişkenini değer olarak ekle
        mav.setViewName("notFound");//exception yakalanırsa notFound.jsp dosyasını döneceğiz
        return mav;
}
//ExceptionHandler: belirtilen exception sınıf için bir metod belirlememizi sağlar
    //bu metod yakalanan exception için özel bir işlem içerir(notFound u içinde mesaj ile gösterir)

}











//TODO ------------------------------ NOTES ----------------------------------------------------------------------------
/*

Spring i karşılıyorum
views dosyası içinde hi.jsp dosyası var.
hi.jsp içinde  bizim tarayıcımıza <body> tag i içindeki değerler yansır.

<body>

${message} --> $ isareti değer verilmemiş, değişken kullanılacak anlamına gelir

${messagebody}

</body>



---------------------
@GetMapping(), @PutMapping(), @PostMapping() gibi annotationlar @RequestMapping() annotation ile kullanılır.
@RequestMapping() class level de kullandım. @GetMapping(), @PutMapping(), @PostMapping() ile tekrar method levelde kullanmama gerek yok.
 */

/*

TODO----------------------------------------------- @ModelAttribute ----------------------------------------------------
studentForm.jsp dosyasında <form:form modelAttribute="student" action="saveStudent" method="post">
action = saveStudent yeni bir requerst e gönderecek => post post methodu ile öğrenciyi karşılayacağız.

buradaki modelAttribute nedir?
kullanıcı, forma FirstName, LastName, Grade girdiğinde studend isimli bir "model" oluşturulacak
ve bu "model" içerisine bu bilgiler alınacak.

bu bilgileri controller da yani uygulamam içerisinde nasıl alacağım?
sendStudentForm methodum var. kullanıcı bu istekle geldiğinde form içinde bir tane  "@ModelAttribute" var.
bu model attribute un ismi "student"
bu model attribute u al, içerisindeki bilgileri Student class imdan bir obje(student) oluştur bu objenin değerlerini set et
form içerisindeki bilgileri @ModelAttribute annotation u kullanarak alıyoruz.
id, datetime otomatik oluşacaktı zaten.

--- @ModelAttribute : view katmanı ile controller arasında data taşınması/transferini sağlar.
formun(studentForm.jsp ile) içerisine girilen bilgileri uygulama yani controller içerisindeki student objemin içerisine yerleştiriyor.
student objesini yine @ModelAttribute kullanarak DB'ye kaydedebileceğiz.

//@ModelAttribute view katmanı ile controller arasında data transferini sağlar.
  @GetMapping("/new")
    public String sendStudentForm(@ModelAttribute("student")Student student){
        return "studentForm";
    }
TODO------------------------------------------------------------ Add Student -------------------------------------------
(<form:form modelAttribute="student" action="saveStudent" method="post">)
//formu doldurup submit e tıkladığımda studentForm.jsp de ki action save (action="saveStudent")
  submit e tıkladığımda //http://localhost:8080/SpringMvc/students/saveStudent request ine yönlendirecek.
  http://localhost:8080/SpringMvc/students/saveStudent request i/bu isteği POST methodu ile karşılamamız gerekiyor( method="post">)).

  yeni bir kayıt yapacağız
  yeni bir obje oluşturacağımzı zaman http methodlarından POST methodunu kullanıyoruz, formda belirtilen method da POST

    //student i DB ye kaydedince response olarak tablo içinde tüm öğrencileri listeleyelim
    @PostMapping("/saveStudent")//student form submit edildiğinde action saveStudent olacak, method olarak ise post kullanılacak,
    yeni bir object oluşturacağımız zaman http methodlarından post method kullanıyoruz
    public String saveStudent(@ModelAttribute Student student){
        //service de student i kaydediyoruz
        return "redirect:/students";
    }
 */


//submit> saveStudent(action) > student i al ve kaydet> service nin savestudent methodu> repository nin saveStudent i updateOrsave ==> öğrencileri gösterdi.
//öğrencinin var olduğunu nereden biliyor/anlıyor?
//studentForm.jsp ye gidiyorum. orada id yok. yeni obje yeni id oluşuyor. studentForm.jsp de hidden path var, gizli path id yi veriyor fakat kullanıcıya göstermiyor.
//Student class ta setId methodunu silmeyeceğiz, kullanacağız demiştik, burada kullanıyoruz, uygulama kullanıyor
//saveOrUpdate kullanarak hem save hem update işlemini gerçekleştirmiş oluyoruz. update ve save methodunu ayrı ayrı da yapabiliriz.