package com.example.libreria.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "mail", nullable = false, length = 254)
    private String mail;

    @Lob
    @Column(name = "rol", nullable = false)
    private String rol;

    @ColumnDefault("0")
    @Column(name = "moroso", nullable = false)
    private Boolean moroso;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "creado")
    private Instant creado;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "actualizado")
    private Instant actualizado;

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

    public Boolean getMoroso() {
        return moroso;
    }

    public void setMoroso(Boolean moroso) {
        this.moroso = moroso;
    }

    public Instant getCreado() {
        return creado;
    }

    public void setCreado(Instant creado) {
        this.creado = creado;
    }

    public Instant getActualizado() {
        return actualizado;
    }

    public void setActualizado(Instant actualizado) {
        this.actualizado = actualizado;
    }

}