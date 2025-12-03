package com.finli.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransaccionRequest {
    private String etiqueta;          // se usa para nombre_transaccion y descripcion_transaccion
    private BigDecimal monto;
    private LocalDateTime fecha;
    private Integer idUsuario;
    private Integer idMedioPago;
    private Integer idCategoria;
    private Integer idSubcategoria;
    private String imagen;            // opcional
}
