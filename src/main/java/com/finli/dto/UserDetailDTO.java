package com.finli.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailDTO {
    private Integer id;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private Integer edad;
    private String email;
    private String rol;
    private Integer subscriptionId; // Para pre-seleccionar el combo de suscripción
    // NO incluimos el password aquí, por seguridad nunca se envía al frontend.
}