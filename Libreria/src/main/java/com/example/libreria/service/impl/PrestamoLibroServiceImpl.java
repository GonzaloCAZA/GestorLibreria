package com.example.libreria.service.impl;

import com.example.libreria.domain.Libro;
import com.example.libreria.domain.PrestamoLibro;
import com.example.libreria.domain.Usuario;
import com.example.libreria.repository.PrestamoLibroRepository;
import com.example.libreria.service.PrestamoLibroService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class PrestamoLibroServiceImpl implements PrestamoLibroService {

    private final PrestamoLibroRepository prestamoLibroRepository;

    public PrestamoLibroServiceImpl(PrestamoLibroRepository prestamoLibroRepository) {
        this.prestamoLibroRepository = prestamoLibroRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrestamoLibro> findAll() {
        return prestamoLibroRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PrestamoLibro> findAll(Pageable pageable) {
        return prestamoLibroRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public PrestamoLibro findById(Long id) {
        return prestamoLibroRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Prestamo no encontrado con id: " + id));
    }

    @Override
    public PrestamoLibro save(PrestamoLibro prestamoLibro) {
        return prestamoLibroRepository.save(prestamoLibro);
    }

    @Override
    public PrestamoLibro update(Long id, PrestamoLibro prestamoLibro) {
        PrestamoLibro prestamoExistente = findById(id);
        prestamoExistente.setIdUsuario(prestamoLibro.getIdUsuario());
        prestamoExistente.setIdLibro(prestamoLibro.getIdLibro());
        prestamoExistente.setFechaPrestamo(prestamoLibro.getFechaPrestamo());
        prestamoExistente.setFechaDevolucionPrevista(prestamoLibro.getFechaDevolucionPrevista());
        prestamoExistente.setFechaDevolucionReal(prestamoLibro.getFechaDevolucionReal());
        prestamoExistente.setCreado(prestamoLibro.getCreado());
        prestamoExistente.setActualizado(prestamoLibro.getActualizado());
        return prestamoLibroRepository.save(prestamoExistente);
    }

    @Override
    public PrestamoLibro devolver(Long id, LocalDate fechaDevolucionReal) {
        PrestamoLibro prestamoExistente = findById(id);
        prestamoExistente.setFechaDevolucionReal(fechaDevolucionReal);
        return prestamoLibroRepository.save(prestamoExistente);
    }

    @Override
    public void deleteById(Long id) {
        PrestamoLibro prestamoLibro = findById(id);
        prestamoLibroRepository.delete(prestamoLibro);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrestamoLibro> findByUsuario(Usuario usuario) {
        return prestamoLibroRepository.findByIdUsuario(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrestamoLibro> findByLibro(Libro libro) {
        return prestamoLibroRepository.findByIdLibro(libro);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrestamoLibro> findByFechaPrestamo(LocalDate fechaPrestamo) {
        return prestamoLibroRepository.findByFechaPrestamo(fechaPrestamo);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PrestamoLibro> findByFechaPrestamo(LocalDate fechaPrestamo, Pageable pageable) {
        return prestamoLibroRepository.findByFechaPrestamo(fechaPrestamo, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrestamoLibro> findByFechaDevolucionPrevista(LocalDate fechaDevolucionPrevista) {
        return prestamoLibroRepository.findByFechaDevolucionPrevista(fechaDevolucionPrevista);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PrestamoLibro> findByFechaDevolucionPrevista(LocalDate fechaDevolucionPrevista, Pageable pageable) {
        return prestamoLibroRepository.findByFechaDevolucionPrevista(fechaDevolucionPrevista, pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrestamoLibro> findByFechaDevolucionReal(LocalDate fechaDevolucionReal) {
        return prestamoLibroRepository.findByFechaDevolucionReal(fechaDevolucionReal);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PrestamoLibro> findByFechaDevolucionReal(LocalDate fechaDevolucionReal, Pageable pageable) {
        return prestamoLibroRepository.findByFechaDevolucionReal(fechaDevolucionReal, pageable);
    }
}
