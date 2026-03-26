package com.example.libreria.dto.usuario;

import com.example.libreria.domain.Usuario;
import com.example.libreria.util.Rol;

import java.time.Instant;

public record UsuarioResponse(
        Long id,
        String mail,
        Rol rol,
        Boolean moroso,
        Instant creado,
        Instant actualizado
) {
    public static UsuarioResponse from(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getMail(),
                usuario.getRol(),
                usuario.getMoroso(),
                usuario.getCreado(),
                usuario.getActualizado()
        );
    }
}
