package com.finli.dto;

import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IngresoRequest {

    @NotNull
    private Integer idUsuario;

    @NotNull
    private Integer idMedioPago;

    @NotBlank
    @Size(max = 100)
    private String nombreIngreso;

    @NotNull
    @Positive
    private BigDecimal montoIngreso;

    @Size(max = 255)
    private String descripcion;

    @NotNull
    private LocalDate fechaIngreso;
}