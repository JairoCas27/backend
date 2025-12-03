package com.finli.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "Ingresos")
@Data                   // getters, setters, toString, equals, hashCode
@NoArgsConstructor      // constructor sin args
@AllArgsConstructor     // constructor con todos los args
@Builder                // patrón builder
public class Ingreso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ingreso")
    private Integer idIngreso;

    @Column(name = "id_usuario", nullable = false)
    private Integer idUsuario;

    @Column(name = "id_medio_pago", nullable = false)
    private Integer idMedioPago;

    @Column(name = "nombre_ingreso", nullable = false, length = 100)
    private String nombreIngreso;

    @Column(name = "monto_ingreso", nullable = false, precision = 10, scale = 2)
    private BigDecimal montoIngreso;

    @Column(name = "descripcion", length = 255)
    private String descripcion;

    @Column(name = "fecha_ingreso", nullable = false)
    private LocalDate fechaIngreso;

    /* ---------- relaciones (solo lectura) ---------- */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", insertable = false, updatable = false)
    @JsonIgnore
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_medio_pago", insertable = false, updatable = false)
    @JsonIgnore
    private MedioPago medioPago;
}