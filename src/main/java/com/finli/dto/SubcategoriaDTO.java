package com.finli.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubcategoriaDTO {
    private Integer id;
    private String name;        // Nombre técnico (ej: 'alquiler')
    private String label;       // Nombre legible (ej: 'Alquiler')
    private String icon;        // Icono (ej: 'bi-house')
    private Integer categoriaId; // ID de la categoría padre
}
