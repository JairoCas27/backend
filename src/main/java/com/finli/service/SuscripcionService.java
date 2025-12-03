package com.finli.service;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.finli.dto.SuscripcionResponse;
import com.finli.model.EstadoSuscripcion;
import com.finli.model.Suscripcion;
import com.finli.model.TipoSuscripcion;
import com.finli.model.Usuario;
import com.finli.repository.EstadoSuscripcionRepository;
import com.finli.repository.SuscripcionRepository;
import com.finli.repository.TipoSuscripcionRepository;
import com.finli.repository.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SuscripcionService {

    private final SuscripcionRepository suscripcionRepository;
    private final TipoSuscripcionRepository tipoSuscripcionRepository;
    private final EstadoSuscripcionRepository estadoSuscripcionRepository;
    private final UsuarioRepository usuarioRepository;

    /**
     *    Crear una suscripción gratuita automáticamente
     *    cuando se registra un nuevo usuario.
     */
    public void crearSuscripcionGratuita(Integer idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        TipoSuscripcion tipo = tipoSuscripcionRepository.findById(4)
                .orElseThrow(() -> new RuntimeException("Tipo de suscripción gratuita no encontrada"));

        EstadoSuscripcion estado = estadoSuscripcionRepository.findById(1)
                .orElseThrow(() -> new RuntimeException("Estado de suscripción activa no encontrado"));

        Suscripcion suscripcion = Suscripcion.builder()
                .usuario(usuario)
                .tipoSuscripcion(tipo)
                .estadoSuscripcion(estado)
                .fechaInicio(LocalDate.now())
                .fechaFin(null) // gratuito → sin límite
                .build();

        suscripcionRepository.save(suscripcion);
    }

    /**
     * 📌 Cambiar el tipo de suscripción del usuario (cuando compra un plan)
     */
    public SuscripcionResponse cambiarSuscripcion(Integer idUsuario, Integer idTipoSuscripcion) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Suscripcion suscripcion = suscripcionRepository.findByUsuario(usuario)
                .orElseThrow(() -> new RuntimeException("Suscripción no encontrada para el usuario"));

        TipoSuscripcion nuevoTipo = tipoSuscripcionRepository.findById(idTipoSuscripcion)
                .orElseThrow(() -> new RuntimeException("Tipo de suscripción no encontrado"));

        EstadoSuscripcion estadoActivo = estadoSuscripcionRepository.findById(1)
                .orElseThrow(() -> new RuntimeException("Estado activo no encontrado"));

        suscripcion.setTipoSuscripcion(nuevoTipo);
        suscripcion.setEstadoSuscripcion(estadoActivo);
        suscripcion.setFechaInicio(LocalDate.now());

        // 🔧 Configurar fecha fin según tipo
        switch (idTipoSuscripcion) {
            case 1: // Mensual
                suscripcion.setFechaFin(LocalDate.now().plusMonths(1));
                break;
            case 2: // Anual
                suscripcion.setFechaFin(LocalDate.now().plusYears(1));
                break;
            case 3: // De por vida
                suscripcion.setFechaFin(null);
                break;
            case 4: // Gratuito
                suscripcion.setFechaFin(null);
                break;
        }

        Suscripcion actualizada = suscripcionRepository.save(suscripcion);

// 🔥 Cargar las relaciones antes de acceder (evita proxy)
actualizada.getEstadoSuscripcion().getNombreEstado();
actualizada.getTipoSuscripcion().getNombreTipoSuscripcion();

return SuscripcionResponse.builder()
        .idSuscripcion(actualizada.getIdSuscripcion())
        .tipoSuscripcion(actualizada.getTipoSuscripcion().getNombreTipoSuscripcion())
        .estadoSuscripcion(actualizada.getEstadoSuscripcion().getNombreEstado())
        .fechaInicio(actualizada.getFechaInicio())
        .fechaFin(actualizada.getFechaFin())
        .build();
    }

    /**
     * 📌 Ver la suscripción actual de un usuario
     */
    public Optional<Suscripcion> obtenerPorUsuario(Integer idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return suscripcionRepository.findByUsuario(usuario);
    }

    /**
     * ⚙️ (Opcional futuro) Revisar y expirar suscripciones caducadas
     *    Esto lo puedes luego programar con @Scheduled
     */

     @Scheduled(cron = "0 0 0 * * *") // Todos los días a medianoche

    public void verificarSuscripcionesExpiradas() {
        suscripcionRepository.findByFechaFinBefore(LocalDate.now()).forEach(s -> {
            if (s.getFechaFin() != null) {
                EstadoSuscripcion expirada = estadoSuscripcionRepository.findById(4)
                        .orElseThrow(() -> new RuntimeException("Estado Expirada no encontrado"));

                TipoSuscripcion gratuita = tipoSuscripcionRepository.findById(4)
                        .orElseThrow(() -> new RuntimeException("Tipo Gratuito no encontrado"));

                s.setEstadoSuscripcion(expirada);
                s.setTipoSuscripcion(gratuita);
                s.setFechaFin(null);
                suscripcionRepository.save(s);
            }
        });
    }
}