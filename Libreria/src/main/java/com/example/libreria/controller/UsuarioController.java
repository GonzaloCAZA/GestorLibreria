package com.example.libreria.controller;

import com.example.libreria.dto.common.PageResponse;
import com.example.libreria.dto.usuario.UsuarioCreateRequest;
import com.example.libreria.dto.usuario.UsuarioResponse;
import com.example.libreria.dto.usuario.UsuarioUpdateRequest;
import com.example.libreria.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("hasRole('ADMIN')")
@Transactional(readOnly = true)
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public ResponseEntity<PageResponse<UsuarioResponse>> findAll(
            @RequestParam(required = false) String rol,
            @RequestParam(required = false) Boolean moroso,
            Pageable pageable
    ) {
        if (rol != null && !rol.isBlank()) {
            return ResponseEntity.ok(PageResponse.from(usuarioService.findByRol(rol, pageable), UsuarioResponse::from));
        }
        if (moroso != null) {
            return ResponseEntity.ok(PageResponse.from(usuarioService.findByMoroso(moroso, pageable), UsuarioResponse::from));
        }
        return ResponseEntity.ok(PageResponse.from(usuarioService.findAll(pageable), UsuarioResponse::from));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(UsuarioResponse.from(usuarioService.findById(id)));
    }

    @GetMapping("/mail/{mail}")
    public ResponseEntity<UsuarioResponse> findByMail(@PathVariable String mail) {
        return ResponseEntity.ok(UsuarioResponse.from(usuarioService.findByMail(mail)));
    }

    @PostMapping
    @Transactional
    public ResponseEntity<UsuarioResponse> create(@Valid @RequestBody UsuarioCreateRequest request) {
        return ResponseEntity.ok(UsuarioResponse.from(usuarioService.save(request)));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<UsuarioResponse> update(@PathVariable Long id, @Valid @RequestBody UsuarioUpdateRequest request) {
        return ResponseEntity.ok(UsuarioResponse.from(usuarioService.update(id, request)));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        usuarioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
