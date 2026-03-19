package com.example.libreria.service;

public interface MailService {

    void sendPasswordResetCode(String to, String code);
}
