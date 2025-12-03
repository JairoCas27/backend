package com.finli.controller;

import com.finli.model.Categoria;
import com.finli.model.FuenteCategoria;
import com.finli.service.CategoriaService;
import com.finli.service.FuenteCategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CategoriaController {

    private final CategoriaService categoriaService;
    private final FuenteCategoriaService fuenteService;


    @GetMapping
    public ResponseEntity<List<Categoria>> listarTodos() {
        return ResponseEntity.ok(categoriaService.listarTodos());
    }

    @GetMapping("/fuente/{idFuente}")
    public ResponseEntity<List<Categoria>> listarPorFuente(@PathVariable Integer idFuente) {
        return fuenteService.buscarPorId(idFuente)
                .map(fuente -> ResponseEntity.ok(categoriaService.listarPorFuente(fuente)))
                .orElse(ResponseEntity.notFound().build());
    }
}