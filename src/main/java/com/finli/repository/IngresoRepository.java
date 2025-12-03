package com.finli.repository;

import com.finli.model.Ingreso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngresoRepository extends JpaRepository<Ingreso, Integer> {

    /* query derivada por id de usuario */
    List<Ingreso> findByIdUsuario(Integer idUsuario);
}