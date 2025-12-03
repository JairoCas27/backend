package com.finli.model;

import com.fasterxml.jackson.annotation.JsonIgnore; 
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "estadousuario")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstadoUsuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estado") 
    private Integer idEstado; 

    @Column(name = "nombre_estado", nullable = false, unique = true, length = 50)
    private String nombreEstado;

    @OneToMany(mappedBy = "estadoUsuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore 
    private java.util.List<Usuario> usuarios;
    
}