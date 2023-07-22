package com.tpe.service;

import com.tpe.domain.Message;
import com.tpe.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.Properties;

@Component//bu classtan bean oluşturulsun, default:mailService
@Scope("prototype")//defaultta:Singleton
public class MailService implements MessageService{

    @PostConstruct//constructor metodu çağrıldıktan sonra
    public void init(){
        System.out.println("MailService objesi oluşturuluyor");
    }

    @PreDestroy
    public void destroy(){
        System.out.println("MailService objesi imha edildi");
    }


//    @Autowired//field injection
//    @Qualifier("fileRepository")
//    private Repository repo;

    //setter injection
//    private Repository repo;
//    @Autowired
//    @Qualifier("fileRepository")
//    public void setRepo(Repository repo) {
//        this.repo = repo;
//    }

    //constructor injection:daha güvenli,daha anlaşılır,test etmesi daha kolay
    private Repository repo;

    @Autowired//classın sadece bir tane constructorı var ise, zorunlu değil
    public MailService(@Qualifier("fileRepository") Repository repo) {
        this.repo = repo;
    }

    @Override
    public void sendMessage(Message message) {
        System.out.println("Ben bir Mail servisiyim. Mesajınız: "+message.getMessage());
    }

    @Override
    public void saveMessage(Message message) {
        //reponun objesine ihtiyacımız var
        repo.save(message);

    }
}
