package com.finli.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordResetRequest {

    // El correo para identificar al usuario
    private String email; 
    
    // El código de 6 dígitos enviado por correo
    private String token; 
    
    // La nueva contraseña que el usuario quiere usar
    private String nuevaContrasena; 
}