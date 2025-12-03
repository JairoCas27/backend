package com.finli.model;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.StringUtils;
import com.google.common.base.Preconditions;

@Entity
@Table(name = "subcategorias")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subcategoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_subcategoria")
    private Integer idSubcategoria;

    @Column(name = "nombre_subcategoria", nullable = false, length = 100)
    private String nombreSubcategoria;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_categoria", nullable = false)
    @JsonIgnore
    private Categoria categoria;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_usuario", nullable = true)
    @JsonIgnore
    private Usuario usuario;

    @PrePersist
    @PreUpdate
    private void validar() {
        Preconditions.checkArgument(StringUtils.isNotBlank(nombreSubcategoria), "nombre_subcategoria no puede estar vacío");
        Preconditions.checkNotNull(categoria, "categoria es obligatoria");
    }
}
