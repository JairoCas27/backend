package com.finli.repository;

import com.finli.model.Subcategoria;
import com.finli.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubcategoriaRepository extends JpaRepository<Subcategoria, Integer> {
    
    // Método para encontrar todas las subcategorías de una categoría padre específica
    List<Subcategoria> findByCategoria(Categoria categoria);
    
    // Método para contar cuántas subcategorías tiene una categoría (útil para validaciones rápidas)
    long countByCategoria(Categoria categoria);
}
