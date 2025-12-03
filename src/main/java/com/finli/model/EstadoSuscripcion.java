package com.finli.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "estadosuscripcion")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstadoSuscripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_estadosuscripcion")
    private Integer idEstadoSuscripcion;

    @Column(name = "nombre_estado", nullable = false, unique = true, length = 50)
    private String nombreEstado;
}
