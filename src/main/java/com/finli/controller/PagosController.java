package com.finli.controller;

import com.finli.dto.CompraRequest;
import com.finli.model.*;
import com.finli.repository.*;
import com.finli.service.EmailService;
import com.finli.service.PdfService;
import com.finli.service.SuscripcionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pagos")
@RequiredArgsConstructor
@CrossOrigin("*")
public class PagosController {

    private final UsuarioRepository usuarioRepository;
    private final TipoSuscripcionRepository tipoRepo;
    private final SuscripcionRepository susRepo;
    private final SuscripcionService suscripcionService;
    private final PdfService pdfService;
    private final EmailService emailService;

    // 🚀 RUTA PRINCIPAL ÚNICA
    @PostMapping("/confirmar")
public String procesarPago(@RequestBody CompraRequest request) {

    System.out.println("📩 Email recibido: " + request.getEmail());
    System.out.println("📦 Tipo de suscripción recibida: " + request.getNombreTipoSuscripcion());

    // 1. Buscar usuario por EMAIL
    Usuario usuario = usuarioRepository.findByCorreo(request.getEmail())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado con correo: " + request.getEmail()));

    // 2. Buscar tipo de suscripción por NOMBRE
    TipoSuscripcion tipo = tipoRepo.findByNombreTipoSuscripcion(request.getNombreTipoSuscripcion())
            .orElseThrow(() -> new RuntimeException("Tipo de suscripción no encontrada: " + request.getNombreTipoSuscripcion()));

    // 3. Actualizar / crear suscripción
    suscripcionService.cambiarSuscripcion(usuario.getId(), tipo.getIdTipoSuscripcion());

    Suscripcion suscripcion = susRepo.findByUsuario(usuario)
            .orElseThrow(() -> new RuntimeException("No se encontró suscripción del usuario."));

    // 4. Últimos 4 dígitos de tarjeta (sin romper)
    String cleanCard = request.getCardNumber().replace(" ", "");
    String last4 = cleanCard.substring(cleanCard.length() - 4);

    // 5. Generar PDF (el servicio debe manejar fechaFin null)
    String pdfPath = pdfService.generarReciboPDF(usuario, tipo, suscripcion, last4);

    // 6. Enviar correo con PDF adjunto
    emailService.sendEmailWithAttachment(
            request.getEmail(),
            "Recibo de Suscripción FinLi",
            "¡Gracias por tu compra! Adjuntamos tu recibo.",
            pdfPath
    );

    return "Pago procesado exitosamente. Recibo enviado.";
}

}
