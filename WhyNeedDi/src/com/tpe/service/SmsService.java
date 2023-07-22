package com.tpe.service;

import com.tpe.domain.Message;
import com.tpe.repository.DbRepository;
import com.tpe.repository.Repo;

public class SmsService implements MessageService{

    private Repo repo;

    public SmsService(Repo repo) {
        this.repo = repo;
    }

    public void sendMessage(Message message){
        System.out.println("Ben bir SMS servisiyim. Mesajınız: "+message.getMessage());

    }

    @Override
    public void saveMessage(Message message) {
        //Repo repo=new DbRepository();
        repo.save(message);


    }

}
