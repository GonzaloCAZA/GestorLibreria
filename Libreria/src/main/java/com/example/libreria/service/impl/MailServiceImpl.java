package com.example.libreria.service.impl;

import com.example.libreria.service.MailService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;
    private final String fromAddress;

    public MailServiceImpl(
            JavaMailSender mailSender,
            @Value("${app.mail.from:no-reply@libreria.local}") String fromAddress
    ) {
        this.mailSender = mailSender;
        this.fromAddress = fromAddress;
    }

    @Override
    public void sendPasswordResetCode(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        message.setTo(to);
        message.setSubject("Recuperacion de cuenta");
        message.setText(
                "Has solicitado cambiar tu contrasena.\n\n" +
                "Tu codigo de recuperacion es: " + code + "\n\n" +
                "Si no has sido tu, ignora este correo."
        );
        mailSender.send(message);
    }
}
