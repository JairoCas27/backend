package com.finli.repository;

import com.finli.model.TipoSuscripcion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface TipoSuscripcionRepository extends JpaRepository<TipoSuscripcion, Integer> {
    Optional<TipoSuscripcion> findByNombreTipoSuscripcion(String nombreTipoSuscripcion);
}
