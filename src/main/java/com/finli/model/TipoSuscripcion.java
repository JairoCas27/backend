package com.finli.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tiposuscripcion")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TipoSuscripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tiposuscripcion")
    private Integer idTipoSuscripcion;

    @Column(name = "nombre_tiposuscripcion", nullable = false, unique = true, length = 50)
    private String nombreTipoSuscripcion;
}
