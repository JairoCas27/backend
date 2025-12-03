package com.finli.controller;

import com.finli.dto.TransaccionRequest;
import com.finli.dto.TransaccionResponse;
import com.finli.model.Transaccion;
import com.finli.service.TransaccionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/transacciones")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Slf4j
public class TransaccionController {

    private final TransaccionService transaccionService;

    /* ---------- Crear nueva transacción ---------- */
    @PostMapping
    public ResponseEntity<Transaccion> crear(@RequestBody TransaccionRequest dto) {
        Transaccion t = transaccionService.crearTransaccion(dto);
        return ResponseEntity.ok(t);
    }

    /* ---------- Listar transacciones de un usuario ---------- */
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<TransaccionResponse>> listarPorUsuario(@PathVariable Integer idUsuario) {
        return ResponseEntity.ok(transaccionService.listarPorUsuario(idUsuario));
    }

    /* ---------- Eliminar transacción ---------- */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        transaccionService.eliminarTransaccion(id);
        return ResponseEntity.noContent().build();
    }
}
