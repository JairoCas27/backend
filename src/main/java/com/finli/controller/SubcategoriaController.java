package com.finli.controller;

import com.finli.model.Subcategoria;
import com.finli.model.Categoria;
import com.finli.service.SubcategoriaService;
import com.finli.service.CategoriaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subcategorias")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SubcategoriaController {

    private final SubcategoriaService subService;
    private final CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<List<Subcategoria>> listarTodos() {
        return ResponseEntity.ok(subService.listarTodos());
    }

    @GetMapping("/categoria/{idCategoria}")
    public ResponseEntity<List<Subcategoria>> listarPorCategoria(@PathVariable Integer idCategoria) {
        return categoriaService.buscarPorId(idCategoria)
                .map(categoria -> ResponseEntity.ok(subService.listarPorCategoria(categoria)))
                .orElse(ResponseEntity.notFound().build());
    }
}
