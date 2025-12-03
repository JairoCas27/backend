package com.finli.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.StringUtils;
import com.google.common.base.Preconditions;

@Entity
@Table(name = "transacciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaccion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_transaccion")
    private Integer idTransaccion;

    @Column(name = "nombre_transaccion", nullable = false, length = 100)
    private String nombreTransaccion;

    @Column(name = "tipo", nullable = false, length = 50)
    private String tipo ="GASTO";

    @Column(name = "monto", nullable = false, precision = 10, scale = 2)
    private BigDecimal monto;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    @Column(name = "descripcion_transaccion", length = 255)
    private String descripcionTransaccion;

    @Column(name = "imagen")
    private String imagen;

    @Column(name = "id_usuario", nullable = false)
    private Integer idUsuario;

    @Column(name = "id_medio_pago", nullable = false)
    private Integer idMedioPago;

    @Column(name = "id_categoria", nullable = false)
    private Integer idCategoria;

    @Column(name = "id_subcategoria", nullable = false)
    private Integer idSubcategoria;

    /* ---------- Relaciones ---------- */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", insertable = false, updatable = false)
    @JsonIgnore
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_medio_pago", insertable = false, updatable = false)
    @JsonIgnore
    private MedioPago mediopago;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_categoria", insertable = false, updatable = false)
    @JsonIgnore
    private Categoria categoria;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_subcategoria", insertable = false, updatable = false)
    @JsonIgnore
    private Subcategoria subcategoria;
    

    /* ---------- Validaciones personalizadas ---------- */
    @PrePersist
    @PreUpdate
    private void validar() {
        Preconditions.checkArgument(idUsuario != null && idUsuario > 0, "idUsuario debe ser positivo");
        Preconditions.checkArgument(idMedioPago != null && idMedioPago > 0, "idMedioPago debe ser positivo");
        Preconditions.checkArgument(idCategoria != null && idCategoria > 0, "idCategoria debe ser positivo");
        Preconditions.checkArgument(idSubcategoria != null && idSubcategoria > 0, "idSubcategoria debe ser positivo");
        Preconditions.checkArgument(StringUtils.isNotBlank(nombreTransaccion), "nombreTransaccion no puede estar vacío");
        Preconditions.checkArgument(StringUtils.isNotBlank(tipo), "tipo no puede estar vacío");
        Preconditions.checkArgument(monto != null && monto.signum() >= 0, "monto debe ser mayor o igual a 0");
        Preconditions.checkArgument(fecha != null, "fecha es obligatoria");
    }
}
