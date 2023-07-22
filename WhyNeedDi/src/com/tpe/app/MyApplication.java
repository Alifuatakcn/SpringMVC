package com.tpe.app;

import com.tpe.domain.Message;
import com.tpe.repository.DbRepository;
import com.tpe.repository.FileRepository;
import com.tpe.repository.Repo;
import com.tpe.service.MailService;
import com.tpe.service.MessageService;
import com.tpe.service.SmsService;

public class MyApplication {
    public static void main(String[] args) {

        Message message=new Message();
        message.setMessage("Siparişiniz kargoya verildi.");

        //maille mesaj gönderelim.
//        MailService mailService=new MailService();
//        mailService.sendMessage(message);

        //vazgeçtik, sms ile gönderelim.
//        SmsService smsService=new SmsService();
//        smsService.sendMessage(message);

        //bu böyle gitmez...interface çözüm olabilir mi?

        //MessageService service=new SmsService();
        //MessageService service=new MailService();
        //service.sendMessage(message);

        //run timeda hangi servisi kullanacağımızı belirtmek istesek

//        MessageService service=null;
//        String serviceName=null;
//
//        if(args.length>0){
//            serviceName=args[0];
//        }
//        if (serviceName.equalsIgnoreCase("MailService")){
//            service=new MailService();
//        }else{
//            service=new SmsService();
//        }
//        service.sendMessage(message);

        //mesajları kalıcı hale getirmek istersek
        Repo repo=new DbRepository();//veya DBrepo
        MessageService service=new MailService(repo);//DI
        service.sendMessage(message);
        service.saveMessage(message);//file a kaydetmek istersek

        MessageService service2=new SmsService(repo);//DI
        service2.saveMessage(message);



//ancak hala newleme yapıyoruz...



    }
}
