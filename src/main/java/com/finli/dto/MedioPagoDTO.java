package com.finli.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedioPagoDTO {
    private Integer id;
    private String name;    // Nombre legible (ej: 'Efectivo')
    private String logo;    // URL o indicador de logo (ej: 'bi-cash')
}