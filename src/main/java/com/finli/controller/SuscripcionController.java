package com.finli.controller;

import com.finli.dto.SuscripcionResponse;
import com.finli.model.Suscripcion;
import com.finli.service.SuscripcionService;
import io.micrometer.core.annotation.Timed;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/suscripciones")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SuscripcionController {

    private final SuscripcionService suscripcionService;

    /**
     * ✅ Crear una suscripción gratuita manualmente (para pruebas o integración)
     */
    @PostMapping("/crear-gratuita/{idUsuario}")
    @Timed(value = "finli.suscripciones.crear_gratuita", description = "Tiempo de creación de suscripción gratuita")
    public ResponseEntity<?> crearSuscripcionGratuita(@PathVariable Integer idUsuario) {
        try {
            suscripcionService.crearSuscripcionGratuita(idUsuario);
            return ResponseEntity.ok("Suscripción gratuita creada correctamente");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * ✅ Cambiar el tipo de suscripción (cuando el usuario compra un plan)
     */
    @PutMapping("/cambiar")
    @Timed(value = "finli.suscripciones.cambiar", description = "Tiempo de cambio de suscripción")
    public ResponseEntity<?> cambiarSuscripcion(
            @RequestParam Integer idUsuario,
            @RequestParam Integer idTipoSuscripcion) {
        try {
            SuscripcionResponse actualizada = suscripcionService.cambiarSuscripcion(idUsuario, idTipoSuscripcion);
            return ResponseEntity.ok(actualizada);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * ✅ Obtener suscripción actual por usuario
     */
    @GetMapping("/{idUsuario}")
    @Timed(value = "finli.suscripciones.obtener", description = "Tiempo de obtención de suscripción")
    public ResponseEntity<?> obtenerSuscripcion(@PathVariable Integer idUsuario) {
        try {
            return ResponseEntity.ok(suscripcionService.obtenerPorUsuario(idUsuario));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}