package com.finli.service;

import com.finli.dto.TransaccionRequest;
import com.finli.dto.TransaccionResponse;
import com.finli.model.Categoria;
import com.finli.model.MedioPago;
import com.finli.model.Subcategoria;
import com.finli.model.Transaccion;
import com.finli.repository.CategoriaRepository;
import com.finli.repository.MedioPagoRepository;
import com.finli.repository.SubcategoriaRepository;
import com.finli.repository.TransaccionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.StringUtils;
import com.google.common.base.Preconditions;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransaccionService {

    private final TransaccionRepository transaccionRepo;
    private final CategoriaRepository categoriaRepo;
    private final SubcategoriaRepository subcategoriaRepo;
    private final MedioPagoRepository medioPagoRepo;
    private final MedioPagoService medioPagoService;

    /* ---------- Crear transacción ---------- */
    public Transaccion crearTransaccion(TransaccionRequest dto) {
        Preconditions.checkArgument(StringUtils.isNotBlank(dto.getEtiqueta()), "Etiqueta no puede estar vacía");
        Preconditions.checkNotNull(dto.getMonto(), "Monto no puede ser null");
        Preconditions.checkNotNull(dto.getFecha(), "Fecha no puede ser null");
        Preconditions.checkNotNull(dto.getIdUsuario(), "idUsuario es obligatorio");
        Preconditions.checkNotNull(dto.getIdMedioPago(), "idMedioPago es obligatorio");
        Preconditions.checkNotNull(dto.getIdCategoria(), "idCategoria es obligatorio");
        Preconditions.checkNotNull(dto.getIdSubcategoria(), "idSubcategoria es obligatorio");

        // ✅ Restar saldo ANTES de guardar la transacción
        medioPagoService.restarSaldo(dto.getIdMedioPago(), dto.getMonto());

        Transaccion t = Transaccion.builder()
                .nombreTransaccion(dto.getEtiqueta())
                .descripcionTransaccion(dto.getEtiqueta())
                .monto(dto.getMonto())
                .fecha(dto.getFecha())
                .idUsuario(dto.getIdUsuario())
                .idMedioPago(dto.getIdMedioPago())
                .idCategoria(dto.getIdCategoria())
                .idSubcategoria(dto.getIdSubcategoria())
                .imagen(dto.getImagen())
                .tipo("GASTO")
                .build();

        return transaccionRepo.save(t);
    }

    /* ---------- Listar transacciones por usuario ---------- */
public List<TransaccionResponse> listarPorUsuario(Integer idUsuario) {
    List<Transaccion> transacciones = transaccionRepo.findByIdUsuario(idUsuario);

    return transacciones.stream().map(t -> {
        Categoria cat = categoriaRepo.findById(t.getIdCategoria()).orElse(null);
        Subcategoria sub = subcategoriaRepo.findById(t.getIdSubcategoria()).orElse(null);
        MedioPago medio = medioPagoRepo.findById(t.getIdMedioPago()).orElse(null);

        return TransaccionResponse.builder()
                .idTransaccion(t.getIdTransaccion())
                .nombre(t.getNombreTransaccion())
                .categoria(cat != null ? cat.getNombreCategoria() : "Desconocido")
                .subcategoria(sub != null ? sub.getNombreSubcategoria() : "Desconocido")
                .mediopago(medio != null ? medio.getNombreMedioPago() : "Desconocido")
                .monto(t.getMonto())
                .fecha(t.getFecha())
                .descripcion(t.getDescripcionTransaccion())
                .imagen(t.getImagen())
                .build();
    }).collect(Collectors.toList());
}

    /* ---------- Eliminar transacción ---------- */
    public void eliminarTransaccion(Integer id) {
        transaccionRepo.deleteById(id);
    }
}
