package com.finli.model;

import jakarta.persistence.*;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.lang3.StringUtils;
import com.google.common.base.Preconditions;

import java.util.List;

@Entity
@Table(name = "categorias")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    private Integer idCategoria;

    @Column(name = "nombre_categoria", nullable = false, length = 100)
    private String nombreCategoria;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_fuente", nullable = false)
    @JsonIgnore
    private FuenteCategoria fuente;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_usuario", nullable = true)
    @JsonIgnore
    private Usuario usuario;

    @OneToMany(mappedBy = "categoria", fetch = FetchType.EAGER)
    @JsonIgnore
    private List<Subcategoria> subcategorias;

    @PrePersist
    @PreUpdate
    private void validar() {
        Preconditions.checkArgument(StringUtils.isNotBlank(nombreCategoria), "nombre_categoria no puede estar vacío");
        Preconditions.checkNotNull(fuente, "fuente es obligatoria");
    }
}
