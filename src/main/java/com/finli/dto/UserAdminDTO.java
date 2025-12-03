package com.finli.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAdminDTO {
    private Integer id;
    private String name;             
    private String email;            
    private String subscriptionType; 
    private String registrationDate; 
    private String photo;            
    
    // --- NUEVO CAMPO ---
    private String status; // Aquí guardaremos "Activo" o "Inactivo"
}