package com.tpe.service;

import com.tpe.domain.Message;
import com.tpe.repository.DbRepository;
import com.tpe.repository.FileRepository;
import com.tpe.repository.Repo;

public class MailService implements MessageService{

    private Repo repo;//=new DbRepository();

    public MailService(Repo repo) {
        this.repo = repo;
    }

    public void sendMessage(Message message){

        System.out.println("Ben bir mail servisiyim. Mesajınız: "+message.getMessage());

    }

    @Override
    public void saveMessage(Message message) {
        //save metodunu kullanmak için objeye ihtiyaç var
        //Repo repo=new DbRepository();
        //Repo repo=new FileRepository();
        repo.save(message);

    }


}
