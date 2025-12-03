package com.finli.dto;

import java.time.LocalDate;

import com.finli.model.EstadoUsuario;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioResponse {
    
    private Integer id;
    private String email;   // mismo valor que correo
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private Integer edad;

    // Campo CLAVE: Necesario para mostrar el estado en el frontend
    private EstadoUsuarioResponse estadoUsuario; 
    private String tipoSuscripcion;
    private String estadoSuscripcion;
    private Integer idEstadoSuscripcion;
    private LocalDate fechaFinSuscripcion;


    // --- CLASE INTERNA PARA EL ESTADO ---
    // Esto es un DTO anidado para estructurar la respuesta del estado.
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class EstadoUsuarioResponse {
        private Integer idEstado;
        private String nombreEstado;
    }
}