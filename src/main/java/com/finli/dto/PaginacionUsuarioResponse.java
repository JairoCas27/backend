package com.finli.dto; 

// <--- Ahora SÍ se usa aquí
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginacionUsuarioResponse {

    // Nombre CLAVE: 'users' (El frontend lo espera)
    private List<UsuarioResponse> users; 
    
    // Nombre CLAVE: 'totalCount' (El frontend lo espera)
    private long totalCount;            
}