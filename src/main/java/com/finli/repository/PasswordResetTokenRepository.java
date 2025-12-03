package com.finli.repository;

import com.finli.model.PasswordResetToken;
import com.finli.model.Usuario; 
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> { 

    Optional<PasswordResetToken> findByToken(String token);

    void deleteByUsuarioId(Integer usuarioId);

    void deleteByUsuario(Usuario usuario);
}