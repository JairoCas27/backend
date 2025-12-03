package com.finli.dto;

import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SuscripcionResponse {
    private Integer idSuscripcion;
    private String tipoSuscripcion;
    private String estadoSuscripcion;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
}