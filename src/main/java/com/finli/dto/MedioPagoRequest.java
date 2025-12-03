package com.finli.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MedioPagoRequest {
    private String nombreMedioPago;
    private Double montoInicial;
}