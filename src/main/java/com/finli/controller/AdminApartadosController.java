package com.finli.controller;

import com.finli.dto.CategoriaDTO;
import com.finli.dto.SubcategoriaDTO;
import com.finli.dto.MedioPagoDTO;
import com.finli.service.AdministradorService;
import io.micrometer.core.annotation.Timed;
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

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminApartadosController {

    @Autowired
    private AdministradorService administradorService;

    // --- 1. LISTAR CATEGORÍAS PREDETERMINADAS (GET) ---
    @GetMapping("/categories")
    @Timed(value = "finli.admin.categorias.listar", description = "Tiempo de listado de categorías")
    public ResponseEntity<List<CategoriaDTO>> listarCategorias() {
        List<CategoriaDTO> categorias = administradorService.listarCategoriasPredeterminadas();
        return ResponseEntity.ok(categorias);
    }

    // --- 2. LISTAR SUBCATEGORÍAS POR CATEGORÍA (GET) ---
    @GetMapping("/categories/{id}/subcategories")
    @Timed(value = "finli.admin.subcategorias.listar", description = "Tiempo de listado de subcategorías por categoría")
    public ResponseEntity<List<SubcategoriaDTO>> listarSubcategoriasPorCategoria(@PathVariable Integer id) {
        try {
            List<SubcategoriaDTO> subcategorias = administradorService.obtenerSubcategoriasPorCategoria(id);
            return ResponseEntity.ok(subcategorias);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /* ------ EDITAR CATEGORÍA (PUT) ------ */
    @PutMapping("/categories/{id}")
    @Timed(value = "finli.admin.categorias.actualizar", description = "Tiempo de actualización de categoría")
    public ResponseEntity<CategoriaDTO> actualizarCategoria(@PathVariable Integer id,
            @RequestBody CategoriaDTO dto) {
        CategoriaDTO updated = administradorService.actualizarCategoriaPredeterminada(id, dto);
        return ResponseEntity.ok(updated);
    }

    /* ------ ELIMINAR CATEGORÍA (DELETE) ------ */
    @DeleteMapping("/categories/{id}")
    @Timed(value = "finli.admin.categorias.eliminar", description = "Tiempo de eliminación de categoría")
    public ResponseEntity<Void> eliminarCategoria(@PathVariable Integer id) {
        administradorService.eliminarCategoriaPredeterminada(id);
        return ResponseEntity.noContent().build();
    }

    // --- 3. LISTAR MEDIOS DE PAGO PREDETERMINADOS (GET) ---
    @GetMapping("/payment-methods")
    @Timed(value = "finli.admin.medios_pago.listar", description = "Tiempo de listado de medios de pago")
    public ResponseEntity<List<MedioPagoDTO>> listarMediosPagoPredeterminados() {
        List<MedioPagoDTO> medios = administradorService.loadDefaultPaymentMethods();
        return ResponseEntity.ok(medios);
    }

    @PostMapping("/payment-methods")
    @Timed(value = "finli.admin.medios_pago.crear", description = "Tiempo de creación de medio de pago")
    public ResponseEntity<MedioPagoDTO> crearMedioPago(@RequestBody MedioPagoDTO dto) {
        MedioPagoDTO creado = administradorService.crearMedioPagoPredeterminado(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    @PutMapping("/payment-methods/{id}")
    @Timed(value = "finli.admin.medios_pago.actualizar", description = "Tiempo de actualización de medio de pago")
    public ResponseEntity<MedioPagoDTO> actualizarMedioPago(@PathVariable Integer id,
            @RequestBody MedioPagoDTO dto) {
        log.warn("🔹 Controlador PUT id={} body={}", id, dto);
        return ResponseEntity.ok(administradorService.actualizarMedioPagoPredeterminado(id, dto));
    }

    @DeleteMapping("/payment-methods/{id}")
    @Timed(value = "finli.admin.medios_pago.eliminar", description = "Tiempo de eliminación de medio de pago")
    public ResponseEntity<Void> eliminarMedioPago(@PathVariable Integer id) {
        administradorService.eliminarMedioPagoPredeterminado(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/subcategories/{id}")
    @Timed(value = "finli.admin.subcategorias.eliminar", description = "Tiempo de eliminación de subcategoría")
    public ResponseEntity<Void> eliminarSubcategoria(@PathVariable Integer id) {
        administradorService.eliminarSubcategoriaPredeterminada(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/subcategories/{id}")
    @Timed(value = "finli.admin.subcategorias.actualizar", description = "Tiempo de actualización de subcategoría")
    public ResponseEntity<SubcategoriaDTO> actualizarSubcategoria(
            @PathVariable Integer id,
            @RequestBody SubcategoriaDTO dto) {
        SubcategoriaDTO updated = administradorService.actualizarSubcategoriaPredeterminada(id, dto);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/subcategories")
    @Timed(value = "finli.admin.subcategorias.crear", description = "Tiempo de creación de subcategoría")
    public ResponseEntity<SubcategoriaDTO> crearSubcategoria(@RequestBody SubcategoriaDTO dto) {
        SubcategoriaDTO creada = administradorService.crearSubcategoriaPredeterminada(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @PostMapping("/categories")
    @Timed(value = "finli.admin.categorias.crear", description = "Tiempo de creación de categoría")
    public ResponseEntity<CategoriaDTO> crearCategoria(@RequestBody CategoriaDTO dto) {
        CategoriaDTO creada = administradorService.crearCategoriaPredeterminada(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }
}