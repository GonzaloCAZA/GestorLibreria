package com.example.libreria.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Entity
@Table(name = "salas")
public class Sala {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "nombre", nullable = false, length = 64)
    private String nombre;

    @Column(name = "maximo_personas", nullable = false)
    private Integer maximoPersonas;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_piso", nullable = false)
    private Piso idPiso;

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

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getMaximoPersonas() {
        return maximoPersonas;
    }

    public void setMaximoPersonas(Integer maximoPersonas) {
        this.maximoPersonas = maximoPersonas;
    }

    public Piso getIdPiso() {
        return idPiso;
    }

    public void setIdPiso(Piso idPiso) {
        this.idPiso = idPiso;
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