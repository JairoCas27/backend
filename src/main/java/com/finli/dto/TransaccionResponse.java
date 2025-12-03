package com.finli.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransaccionResponse {
    private Integer idTransaccion;       
    private String nombre;               
    private String tipo;                 
    private BigDecimal monto;            
    private LocalDateTime fecha;         
    private String categoria;            
    private String subcategoria;         
    private String mediopago;            
    private String descripcion;          
    private String imagen;               
}
