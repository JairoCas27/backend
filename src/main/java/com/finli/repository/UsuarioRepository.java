package com.finli.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.finli.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    
    Optional<Usuario> findByCorreo(String correo);
    boolean existsByCorreo(String correo);

    Page<Usuario> findByEstadoUsuario_IdEstado(Integer idEstado, Pageable pageable);
    
    // Contar usuarios por estado
    Long countByEstadoUsuario_IdEstado(Integer idEstado);

    // --- NUEVO: Interfaz para capturar los datos crudos de la base de datos ---
    public interface UserAdminProjection {
        Integer getId();
        String getNombre();
        String getApellido();
        String getEmail();
        String getSuscripcion();
        String getEstado(); // <--- NUEVO: Captura "Activo", "Inactivo", etc.
    }

    // --- MODIFICADO: Agregamos la columna de estado y el JOIN correspondiente ---
    @Query(value = """
        SELECT 
            u.id AS id, 
            u.nombre AS nombre,
            u.apellido_Paterno AS apellido, 
            u.correo AS email,
            COALESCE(ts.nombre_tiposuscripcion, 'Sin suscripción') AS suscripcion,
            COALESCE(eu.nombre_estado, 'Desconocido') AS estado
        FROM usuarios u
        LEFT JOIN suscripciones s ON u.id = s.id_usuario AND s.id_estadosuscripcion = 1
        LEFT JOIN tiposuscripcion ts ON s.id_tiposuscripcion = ts.id_tiposuscripcion
        LEFT JOIN estadousuario eu ON u.id_estadoUsuario = eu.id_estado
        WHERE 
            (:keyword IS NULL OR :keyword = '') OR 
            (LOWER(CONCAT(u.nombre, ' ', u.apellido_Paterno)) LIKE LOWER(CONCAT('%', :keyword, '%'))) OR 
            (LOWER(u.correo) LIKE LOWER(CONCAT('%', :keyword, '%')))
        """, nativeQuery = true)
    List<UserAdminProjection> obtenerDatosAdmin(@Param("keyword") String keyword);

    // Nuevo: 3 usuarios más recientes (devuelve Object[], luego mapeamos)
    @Query(value = """
        SELECT u.id,
               CONCAT(u.nombre, ' ', u.apellido_Paterno),
               u.correo,
               COALESCE(ts.nombre_tiposuscripcion, 'Sin suscripción'),
               COALESCE(eu.nombre_estado, 'Desconocido'),
               DATE(u.fecha_registro),
               u.foto
        FROM usuarios u
        LEFT JOIN suscripciones s ON u.id = s.id_usuario AND s.id_estadosuscripcion = 1
        LEFT JOIN tiposuscripcion ts ON s.id_tiposuscripcion = ts.id_tiposuscripcion
        LEFT JOIN estadousuario eu ON u.id_estadoUsuario = eu.id_estado
        ORDER BY u.fecha_registro DESC
        LIMIT 3
        """, nativeQuery = true)
    List<Object[]> findLatestUsersForHomeRaw();

    // CORREGIDO: Cambiar LocalDate por LocalDateTime
    @Query("SELECT COUNT(u) FROM Usuario u WHERE u.fechaRegistro BETWEEN :inicio AND :fin")
    Integer countByFechaRegistroBetween(@Param("inicio") LocalDateTime inicio, @Param("fin") LocalDateTime fin);
    
    // Nuevo método para contar usuarios con suscripción activa
    @Query("SELECT COUNT(DISTINCT u) FROM Usuario u JOIN u.suscripciones s WHERE s.estadoSuscripcion.idEstadoSuscripcion = 1")
    Long countUsuariosConSuscripcionActiva();
}