package com.finli.service;

import com.finli.dto.IngresoRequest;
import com.finli.model.Ingreso;
import com.finli.repository.IngresoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class IngresoService {

    private final IngresoRepository repo;
    private final MedioPagoService medioPagoService;

    public List<Ingreso> listarTodos() {
        return repo.findAll();
    }

    public List<Ingreso> listarPorUsuario(Integer idUsuario) {
        return repo.findByIdUsuario(idUsuario);
    }

    public Ingreso crear(IngresoRequest dto) {
    Ingreso ing = Ingreso.builder()
                         .idUsuario(dto.getIdUsuario())
                         .idMedioPago(dto.getIdMedioPago())
                         .nombreIngreso(dto.getNombreIngreso())
                         .montoIngreso(dto.getMontoIngreso())
                         .descripcion(dto.getDescripcion())
                         .fechaIngreso(dto.getFechaIngreso())
                         .build();

    Ingreso guardado = repo.save(ing);

    // ✅ Sumar saldo al medio de pago
    medioPagoService.sumarSaldo(dto.getIdMedioPago(), dto.getMontoIngreso());

    return guardado;
}

    public void eliminar(Integer id) {
        repo.deleteById(id);
    }
}