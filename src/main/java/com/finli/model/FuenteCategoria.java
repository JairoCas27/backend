package com.finli.model;

import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.lang3.StringUtils;
import com.google.common.base.Preconditions;

@Entity
@Table(name = "fuentecategoria")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FuenteCategoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_fuente")
    private Integer idFuente;

    @Column(name = "nombre_fuente", nullable = false, unique = true, length = 50)
    private String nombreFuente;

    @PrePersist
    @PreUpdate
    private void validar() {
        Preconditions.checkArgument(StringUtils.isNotBlank(nombreFuente), "nombre_fuente no puede estar vacío");
    }
}
