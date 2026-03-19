package com.example.libreria.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Entity
@Table(name = "baldas")
public class Balda {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "numero", nullable = false)
    private Integer numero;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_estanteria", nullable = false)
    private Estanteria idEstanteria;

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

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Estanteria getIdEstanteria() {
        return idEstanteria;
    }

    public void setIdEstanteria(Estanteria idEstanteria) {
        this.idEstanteria = idEstanteria;
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