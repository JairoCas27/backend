package com.finli.controller;

import com.finli.dto.IngresoRequest;
import com.finli.model.Ingreso;
import com.finli.service.IngresoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ingresos")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class IngresoController {

    private final IngresoService service;

    @GetMapping
    public List<Ingreso> listar() {
        return service.listarTodos();
    }

    @GetMapping("/usuario/{idUsuario}")
    public List<Ingreso> listarPorUsuario(@PathVariable Integer idUsuario) {
        return service.listarPorUsuario(idUsuario);
    }

    @PostMapping
    public ResponseEntity<Ingreso> crear(@Valid @RequestBody IngresoRequest dto) {
        Ingreso guardado = service.crear(dto);
        return ResponseEntity.ok(guardado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}