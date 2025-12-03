package com.finli.service;

import com.finli.model.Usuario;
import com.finli.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public Usuario actualizarPerfil(Integer id, Usuario nuevosDatos) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (nuevosDatos.getNombre() != null)
            usuario.setNombre(nuevosDatos.getNombre());

        if (nuevosDatos.getApellidoPaterno() != null)
            usuario.setApellidoPaterno(nuevosDatos.getApellidoPaterno());

        if (nuevosDatos.getApellidoMaterno() != null)
            usuario.setApellidoMaterno(nuevosDatos.getApellidoMaterno());

        if (nuevosDatos.getCorreo() != null)
            usuario.setCorreo(nuevosDatos.getCorreo());

        if (nuevosDatos.getEdad() != null)
            usuario.setEdad(nuevosDatos.getEdad());

        // Actualizar contraseña (si envían la actual y la nueva)
        if (nuevosDatos.getContrasena() != null && !nuevosDatos.getContrasena().isBlank()) {
            usuario.setContrasena(BCrypt.hashpw(nuevosDatos.getContrasena(), BCrypt.gensalt()));
        }

        return usuarioRepository.save(usuario);
    }
}
