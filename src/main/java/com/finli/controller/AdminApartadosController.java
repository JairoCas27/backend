package com.finli.controller;

import com.finli.dto.CategoriaDTO;
import com.finli.dto.SubcategoriaDTO;
import com.finli.dto.MedioPagoDTO;
import com.finli.service.AdministradorService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminApartadosController {

    @Autowired
    private AdministradorService administradorService;

    // --- 1. LISTAR CATEGORÍAS PREDETERMINADAS (GET) ---
    // Endpoint: /api/admin/categories
    @GetMapping("/categories")
    public ResponseEntity<List<CategoriaDTO>> listarCategorias() {
        List<CategoriaDTO> categorias = administradorService.listarCategoriasPredeterminadas();
        return ResponseEntity.ok(categorias);
    }

    // --- 2. LISTAR SUBCATEGORÍAS POR CATEGORÍA (GET) ---
    // Endpoint: /api/admin/categories/{id}/subcategories
    @GetMapping("/categories/{id}/subcategories")
    public ResponseEntity<List<SubcategoriaDTO>> listarSubcategoriasPorCategoria(@PathVariable Integer id) {
        try {
            // Llamamos al método nuevo que devuelve el DTO de subcategorías
            List<SubcategoriaDTO> subcategorias = administradorService.obtenerSubcategoriasPorCategoria(id);
            return ResponseEntity.ok(subcategorias);
        } catch (RuntimeException e) {
            // Manejamos el caso de que el ID de categoría no exista
            return ResponseEntity.notFound().build();
        }
    }

    /* ------ EDITAR CATEGORÍA (PUT) ------ */
    @PutMapping("/categories/{id}")
    public ResponseEntity<CategoriaDTO> actualizarCategoria(@PathVariable Integer id,
            @RequestBody CategoriaDTO dto) {
        CategoriaDTO updated = administradorService.actualizarCategoriaPredeterminada(id, dto);
        return ResponseEntity.ok(updated);
    }

    /* ------ ELIMINAR CATEGORÍA (DELETE) ------ */
    @DeleteMapping("/categories/{id}")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable Integer id) {
        administradorService.eliminarCategoriaPredeterminada(id);
        return ResponseEntity.noContent().build(); // 204
    }

    // --- 3. LISTAR MEDIOS DE PAGO PREDETERMINADOS (GET) ---
    // Endpoint: /api/admin/payment-methods
    @GetMapping("/payment-methods")
    public ResponseEntity<List<MedioPagoDTO>> listarMediosPagoPredeterminados() {
        // Llama al nuevo método del servicio que filtra por usuario=null y asigna
        // íconos
        List<MedioPagoDTO> medios = administradorService.loadDefaultPaymentMethods();
        return ResponseEntity.ok(medios);
    }

    @PostMapping("/payment-methods")
    public ResponseEntity<MedioPagoDTO> crearMedioPago(@RequestBody MedioPagoDTO dto) {
        MedioPagoDTO creado = administradorService.crearMedioPagoPredeterminado(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/payment-methods/{id}")
    public ResponseEntity<MedioPagoDTO> actualizarMedioPago(@PathVariable Integer id,
            @RequestBody MedioPagoDTO dto) {
        log.warn("🔹 Controlador PUT id={} body={}", id, dto);
        return ResponseEntity.ok(administradorService.actualizarMedioPagoPredeterminado(id, dto));
    }

    @DeleteMapping("/payment-methods/{id}")
    public ResponseEntity<Void> eliminarMedioPago(@PathVariable Integer id) {
        administradorService.eliminarMedioPagoPredeterminado(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }

    @DeleteMapping("/subcategories/{id}")
public ResponseEntity<Void> eliminarSubcategoria(@PathVariable Integer id) {
    administradorService.eliminarSubcategoriaPredeterminada(id);
    return ResponseEntity.noContent().build();
}

@PutMapping("/subcategories/{id}")
public ResponseEntity<SubcategoriaDTO> actualizarSubcategoria(
        @PathVariable Integer id,
        @RequestBody SubcategoriaDTO dto) {
    SubcategoriaDTO updated = administradorService.actualizarSubcategoriaPredeterminada(id, dto);
    return ResponseEntity.ok(updated);
}

@PostMapping("/subcategories")
public ResponseEntity<SubcategoriaDTO> crearSubcategoria(@RequestBody SubcategoriaDTO dto) {
    SubcategoriaDTO creada = administradorService.crearSubcategoriaPredeterminada(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(creada);
}

@PostMapping("/categories")
public ResponseEntity<CategoriaDTO> crearCategoria(@RequestBody CategoriaDTO dto) {
    CategoriaDTO creada = administradorService.crearCategoriaPredeterminada(dto);
    return ResponseEntity.status(HttpStatus.CREATED).body(creada);
}

}
