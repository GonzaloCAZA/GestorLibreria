package com.example.libreria.security.dto;

import com.example.libreria.util.Rol;

public class AuthResponse {

    private Long id;
    private String mail;
    private String rol;
    private String message;

    public AuthResponse(Long id, String mail, Rol rol, String usuarioRegistradoCorrectamente) {
    }

    public AuthResponse(Long id, String mail, String rol, String message) {
        this.id = id;
        this.mail = mail;
        this.rol = rol;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
