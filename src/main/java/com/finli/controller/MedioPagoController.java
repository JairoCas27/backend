package com.finli.controller;

import com.finli.dto.MedioPagoRequest;
import com.finli.model.MedioPago;
import com.finli.model.Usuario;
import com.finli.repository.MedioPagoRepository;
import com.finli.service.ServicioAutenticacion;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/medios-pago")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class MedioPagoController {

    private final MedioPagoRepository repo;
    private final ServicioAutenticacion authService;

    // 🔍 Listar solo los medios de pago del usuario logueado
    @GetMapping
    public ResponseEntity<List<MedioPago>> listarPorUsuario(@RequestParam String email) {
        Usuario usuario = authService.buscarPorCorreo(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return ResponseEntity.ok(repo.findByUsuario(usuario));
    }

    // ➕ Agregar nuevo medio de pago
    @PostMapping
    public ResponseEntity<MedioPago> crear(@RequestParam String email,
                                           @RequestBody MedioPagoRequest dto) {
        Usuario usuario = authService.buscarPorCorreo(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        MedioPago nuevo = MedioPago.builder()
                .nombreMedioPago(dto.getNombreMedioPago())
                .montoInicial(dto.getMontoInicial())
                .fechaCreacion(LocalDateTime.now())
                .usuario(usuario)
                .build();

        return ResponseEntity.ok(repo.save(nuevo));
    }

    // ✏️ Editar medio de pago (solo si pertenece al usuario)
    @PutMapping("/{id}")
    public ResponseEntity<MedioPago> actualizar(@PathVariable Integer id,
                                                @RequestParam String email,
                                                @RequestBody MedioPagoRequest dto) {
        Usuario usuario = authService.buscarPorCorreo(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        MedioPago mp = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Medio de pago no encontrado"));

        if (!mp.getUsuario().getId().equals(usuario.getId())) {
            throw new RuntimeException("No autorizado");
        }

        mp.setNombreMedioPago(dto.getNombreMedioPago());
        mp.setMontoInicial(dto.getMontoInicial());

        return ResponseEntity.ok(repo.save(mp));
    }

    // 🗑️ Eliminar medio de pago (solo si pertenece al usuario)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id,
                                         @RequestParam String email) {
        Usuario usuario = authService.buscarPorCorreo(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        MedioPago mp = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Medio de pago no encontrado"));

        if (!mp.getUsuario().getId().equals(usuario.getId())) {
            throw new RuntimeException("No autorizado");
        }

        repo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}