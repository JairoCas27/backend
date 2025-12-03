package com.finli.repository;

import com.finli.model.EstadoSuscripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EstadoSuscripcionRepository extends JpaRepository<EstadoSuscripcion, Integer> {
    Optional<EstadoSuscripcion> findByNombreEstado(String nombreEstado);
}
