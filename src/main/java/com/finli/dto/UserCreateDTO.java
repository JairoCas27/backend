package com.finli.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateDTO {
    // Datos Personales
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private Integer edad;

    // Credenciales
    private String email;
    private String password;

    // Configuración
    private String rol;
    private Integer subscriptionId; // El ID de la suscripción (1, 2, 3 o 4)

    // Estado del usuario (opcional, para desactivar)
    private Integer estadoUsuarioId;
}
