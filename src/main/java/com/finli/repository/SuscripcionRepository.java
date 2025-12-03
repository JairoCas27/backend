package com.finli.repository;

import com.finli.model.Suscripcion;
import com.finli.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.time.LocalDate;
import java.util.List;

public interface SuscripcionRepository extends JpaRepository<Suscripcion, Integer> {
    Optional<Suscripcion> findByUsuario(Usuario usuario);

    List<Suscripcion> findByFechaFinBefore(LocalDate fecha); // para detectar expiradas
}
