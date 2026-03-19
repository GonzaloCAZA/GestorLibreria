package com.example.libreria.domain;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "prestamo_libros")
public class PrestamoLibro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario idUsuario;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_libro", nullable = false)
    private Libro idLibro;

    @Column(name = "fecha_prestamo", nullable = false)
    private LocalDate fechaPrestamo;

    @ColumnDefault("(`fecha_prestamo` + interval 1 month)")
    @Column(name = "fecha_devolucion_prevista")
    private LocalDate fechaDevolucionPrevista;

    @Column(name = "fecha_devolucion_real")
    private LocalDate fechaDevolucionReal;

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

    public Usuario getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Usuario idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Libro getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(Libro idLibro) {
        this.idLibro = idLibro;
    }

    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(LocalDate fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public LocalDate getFechaDevolucionPrevista() {
        return fechaDevolucionPrevista;
    }

    public void setFechaDevolucionPrevista(LocalDate fechaDevolucionPrevista) {
        this.fechaDevolucionPrevista = fechaDevolucionPrevista;
    }

    public LocalDate getFechaDevolucionReal() {
        return fechaDevolucionReal;
    }

    public void setFechaDevolucionReal(LocalDate fechaDevolucionReal) {
        this.fechaDevolucionReal = fechaDevolucionReal;
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