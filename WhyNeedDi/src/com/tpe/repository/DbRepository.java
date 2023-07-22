package com.tpe.repository;

import com.tpe.domain.Message;

public class DbRepository implements Repo{
    @Override
    public void save(Message message) {
        System.out.println("Mesaj DB ye kaydediliyor.");
    }
}
