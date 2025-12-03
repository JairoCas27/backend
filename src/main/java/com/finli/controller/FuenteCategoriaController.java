package com.finli.controller;

import com.finli.model.FuenteCategoria;
import com.finli.service.FuenteCategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/fuentes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FuenteCategoriaController {

    private final FuenteCategoriaService service;

    @GetMapping
    public ResponseEntity<List<FuenteCategoria>> listarTodos() {
        return ResponseEntity.ok(service.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FuenteCategoria> buscarPorId(@PathVariable Integer id) {
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
