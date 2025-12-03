package com.finli.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "suscripciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Suscripcion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_suscripcion")
    private Integer idSuscripcion;

    // 🔗 Relación con Usuario
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    // 🔗 Relación con TipoSuscripcion
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_tiposuscripcion", nullable = false)
    private TipoSuscripcion tipoSuscripcion;

    // 🔗 Relación con EstadoSuscripcion
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_estadosuscripcion", nullable = false)
    private EstadoSuscripcion estadoSuscripcion;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;
}
