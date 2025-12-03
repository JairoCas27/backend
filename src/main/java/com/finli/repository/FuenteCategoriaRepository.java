package com.finli.repository;

import com.finli.model.FuenteCategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional; // <-- Importante: No olvides este import

@Repository
public interface FuenteCategoriaRepository extends JpaRepository<FuenteCategoria, Integer> {
    
    // Este método nos permitirá buscar el ID de la fuente "Sistema" o "Usuario" por su nombre
    Optional<FuenteCategoria> findByNombreFuente(String nombreFuente);
}