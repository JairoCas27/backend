package com.finli.repository;

import com.finli.model.Categoria;
import com.finli.model.FuenteCategoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {

    // Tu método existente (lo mantenemos)
    List<Categoria> findByFuente(FuenteCategoria fuente);

    // --- NUEVO: Interfaz para capturar los datos específicos que pide tu vista ---
    public interface CategoriaProjection {
        Integer getId();
        String getNombre();
        Long getCantidadSubcategorias();
    }

    // --- NUEVO: Consulta SQL para traer categorías del sistema y contar sus hijos ---
    // Filtramos por 'id_usuario IS NULL' para traer solo las predeterminadas del sistema
    @Query(value = """
        SELECT 
            c.id_categoria AS id,
            c.nombre_categoria AS nombre,
            COUNT(s.id_subcategoria) AS cantidadSubcategorias
        FROM categorias c
        LEFT JOIN subcategorias s ON c.id_categoria = s.id_categoria
        WHERE c.id_usuario IS NULL
        GROUP BY c.id_categoria, c.nombre_categoria
        """, nativeQuery = true)
    List<CategoriaProjection> obtenerCategoriasPredeterminadasConConteo();

    boolean existsByNombreCategoriaAndUsuarioIsNull(String nombre);
}
