package com.finli.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoriaDTO {
    private Integer id;
    private String label;      // Nombre de la categoría
    private String icon;       // Clase del icono (ej: 'bi-house')
    private String color;      // Color para el badge (ej: 'success', 'primary')
    private Long subcategoriesCount; // Cantidad de subcategorías
}