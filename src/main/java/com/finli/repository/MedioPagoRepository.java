package com.finli.repository;

import com.finli.model.MedioPago; // Nota: Cambié de 'entity' a 'model' asumiendo tu estructura
import com.finli.model.Usuario; // Nota: Mantenemos el import por si lo usas en otros métodos

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface MedioPagoRepository extends JpaRepository<MedioPago, Integer> {

    // Método para encontrar medios de pago asociados a un usuario específico
    // (Método original)
    List<MedioPago> findByUsuario(Usuario usuario);

    /**
     * NUEVO MÉTODO: Busca todos los medios de pago que son predeterminados del
     * sistema.
     * Esto significa que el campo 'id_usuario' de la tabla 'mediopago' es NULL.
     */
    List<MedioPago> findByUsuarioIsNull();

    @Query("SELECT mp FROM MedioPago mp WHERE mp.idMedioPago = :id")
    Optional<MedioPago> findByIdSinJoin(@Param("id") Integer id);

}
