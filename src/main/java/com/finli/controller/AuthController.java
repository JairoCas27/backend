package com.finli.controller;

import com.finli.dto.LoginRequest;
import com.finli.dto.PasswordResetRequest;
import com.finli.dto.RegistroRequest;
import com.finli.model.Usuario;
import com.finli.service.ServicioAutenticacion;
import lombok.RequiredArgsConstructor;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final ServicioAutenticacion servicio;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest dto) {
        try {
            Usuario u = servicio.login(dto.getEmail(), dto.getContrasena());
            // antes: return ResponseEntity.ok(u);
            return ResponseEntity.ok(servicio.toResponse(u));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
        
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegistroRequest dto) {
        try {
            Usuario u = servicio.registrar(dto);
            return ResponseEntity.ok("Usuario creado ID: " + u.getId());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ENDPOINT: SOLICITAR RECUPERACIÓN
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        if (email == null || email.isBlank()) {
            return ResponseEntity.badRequest().body("El campo 'email' es obligatorio.");
        }
        
        try {
            // Llama al método creado en el ServicioAutenticacion
            servicio.iniciarRecuperacion(email);
            return ResponseEntity.ok("Se ha enviado un código de recuperación a su correo.");
        } catch (RuntimeException e) {
            // Manejamos errores del servicio ( Correo no registrado, fallo de envío)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                  .body("Error al iniciar la recuperación: " + e.getMessage());
        }
    }

    // RESTABLECER CONTRASEÑA 

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody PasswordResetRequest request) {
        try {
            // Llama al método del servicio
            servicio.restablecerContrasena(request);
            return ResponseEntity.ok("Contraseña restablecida exitosamente.");
        } catch (RuntimeException e) {
            // Captura errores de token inválido, expirado o correo no coincidente
            return ResponseEntity.badRequest().body("Error al restablecer: " + e.getMessage());
        } catch (Exception e) {
            // Otros errores (fallo de base de datos)
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error interno: " + e.getMessage());
        }
    }
}