package com.finli.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.finli.dto.PasswordResetRequest;
import com.finli.dto.RegistroRequest;
import com.finli.dto.UsuarioResponse;
import com.finli.model.EstadoUsuario;
import com.finli.model.PasswordResetToken;
import com.finli.model.Suscripcion;
import com.finli.model.Usuario;
import com.finli.repository.EstadoUsuarioRepository;
import com.finli.repository.PasswordResetTokenRepository;
import com.finli.repository.UsuarioRepository;
import com.google.common.base.Preconditions;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ServicioAutenticacion {

    private final UsuarioRepository repo;
    private final EstadoUsuarioRepository estadoUsuarioRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailService emailService;
    private final SuscripcionService suscripcionService;

    /* ========== METODO BUSCAR POR CORREO ========== */
    public Optional<Usuario> buscarPorCorreo(String correo) {
        Preconditions.checkArgument(StringUtils.isNotBlank(correo), "El correo no puede estar vacío");
        return repo.findByCorreo(correo);
    }

    public Usuario registrar(RegistroRequest dto) {
        if (!dto.getContrasena().equals(dto.getConfirmarContrasena())) {
            throw new RuntimeException("Las contraseñas no coinciden");
        }
        if (repo.existsByCorreo(dto.getEmail())) {
            throw new RuntimeException("Correo ya registrado");
        }

        EstadoUsuario estadoPorDefecto = estadoUsuarioRepository.findById(1)
                .orElseThrow(() -> new RuntimeException("Error: Estado de usuario por defecto (ID 1) no encontrado."));

        Usuario u = Usuario.builder()
                .nombre(dto.getNombre())
                .apellidoPaterno(dto.getApellidoPaterno())
                .apellidoMaterno(dto.getApellidoMaterno())
                .edad(dto.getEdad())
                .correo(dto.getEmail())
                .contrasena(BCrypt.hashpw(dto.getContrasena(), BCrypt.gensalt()))
                .estadoUsuario(estadoPorDefecto)
                .build();

        Usuario nuevoUsuario = repo.save(u);

        suscripcionService.crearSuscripcionGratuita(nuevoUsuario.getId());
        
        // ENVIAR CORREO DE BIENVENIDA DESPUÉS DEL REGISTRO
        enviarCorreoBienvenida(nuevoUsuario);

        return nuevoUsuario;
    }

    private void enviarCorreoBienvenida(Usuario usuario) {
        try {
            String subject = "🎉 ¡Bienvenido a FinLi! - Tu aliado financiero";
            String htmlMsg = buildWelcomeEmailHtml(
                usuario.getNombre(), 
                usuario.getApellidoPaterno(), 
                usuario.getCorreo()
            );
            
            emailService.sendHtmlEmail(usuario.getCorreo(), subject, htmlMsg);
            System.out.println("📧 Correo de bienvenida enviado a: " + usuario.getCorreo());
            
        } catch (Exception e) {
            System.err.println("⚠️ No se pudo enviar el correo de bienvenida: " + e.getMessage());
            // No lanzamos excepción para no interrumpir el flujo de registro
        }
    }

    private String buildWelcomeEmailHtml(String nombre, String apellido, String email) {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    body {
                        font-family: Arial, sans-serif;
                        background-color: #f8f9fa;
                        margin: 0;
                        padding: 20px;
                    }
                    .container {
                        max-width: 600px;
                        margin: 0 auto;
                        background: white;
                        border-radius: 12px;
                        box-shadow: 0 8px 32px rgba(12, 37, 24, 0.08);
                        overflow: hidden;
                    }
                    .header {
                        background: #0ea46f;
                        padding: 30px;
                        text-align: center;
                        color: white;
                    }
                    .logo {
                        font-size: 28px;
                        font-weight: bold;
                        margin-bottom: 10px;
                    }
                    .content {
                        padding: 40px;
                    }
                    .greeting {
                        color: #6c757d;
                        margin-bottom: 20px;
                        line-height: 1.6;
                    }
                    .message {
                        background: #d7ffe3;
                        border: 2px solid #0ea46f;
                        border-radius: 8px;
                        padding: 25px;
                        text-align: center;
                        margin: 30px 0;
                    }
                    .info-box {
                        background: #f8f9fa;
                        border-left: 4px solid #0ea46f;
                        padding: 20px;
                        margin: 25px 0;
                        border-radius: 4px;
                    }
                    .footer {
                        background: #f8f9fa;
                        padding: 25px;
                        text-align: center;
                        border-top: 1px solid #e9ecef;
                        color: #838e98;
                        font-size: 12px;
                    }
                    .highlight {
                        color: #0ea46f;
                        font-weight: bold;
                    }
                    .button {
                        display: inline-block;
                        background-color: #0ea46f;
                        color: white;
                        padding: 12px 24px;
                        text-decoration: none;
                        border-radius: 8px;
                        margin-top: 20px;
                        font-weight: bold;
                    }
                    .user-info {
                        background: #e9f7ef;
                        border-radius: 8px;
                        padding: 15px;
                        margin: 20px 0;
                    }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <div class="logo">FINLI</div>
                        <div>Tu aliado en finanzas personales</div>
                    </div>
                    
                    <div class="content">
                        <div class="greeting">
                            Hola <span class="highlight">%s %s</span>,
                        </div>
                        
                        <div class="greeting">
                            ¡Te damos la más cordial bienvenida a FinLi! Estamos emocionados de que te unas a nuestra comunidad 
                            y comiences tu viaje hacia una mejor gestión financiera.
                        </div>
                        
                        <div class="message">
                            <h3 style="color: #0ea46f; margin-bottom: 10px;">🎉 ¡Registro Exitoso!</h3>
                            <p style="color: #6c757d; line-height: 1.6;">
                                Tu cuenta ha sido creada correctamente y ya puedes comenzar a disfrutar de todos los beneficios 
                                que FinLi tiene para ofrecerte.
                            </p>
                        </div>
                        
                        <div class="user-info">
                            <h4 style="color: #0ea46f; margin-bottom: 10px;">📋 Información de tu cuenta:</h4>
                            <p style="color: #6c757d; line-height: 1.6;">
                                <strong>Nombre:</strong> %s %s<br>
                                <strong>Email:</strong> %s<br>
                                <strong>Estado de cuenta:</strong> <span style="color: #0ea46f;">Activa</span><br>
                                <strong>Tipo de suscripción:</strong> <span style="color: #0ea46f;">Gratuita</span>
                            </p>
                        </div>
                        
                        <div class="info-box">
                            <h3 style="color: #0ea46f; margin-bottom: 10px;">🚀 ¿Qué puedes hacer ahora?</h3>
                            <ul style="color: #6c757d; padding-left: 20px; line-height: 1.6;">
                                <li>Comienza a registrar tus ingresos y gastos</li>
                                <li>Establece tus metas financieras</li>
                                <li>Visualiza tus estadísticas y progreso</li>
                                <li>Explora nuestros recursos educativos</li>
                                <li>Actualiza a una suscripción premium cuando estés listo</li>
                            </ul>
                        </div>
                        
                        <div style="text-align: center; margin-top: 30px;">
                            <a href="#" class="button">Comenzar a usar FinLi</a>
                        </div>
                        
                        <div style="color: #6c757d; font-size: 14px; margin-top: 20px;">
                            <strong>📞 Soporte:</strong> Si tienes alguna pregunta, nuestro equipo de soporte está disponible en 
                            <span class="highlight">soporte@finli.com</span>
                        </div>
                    </div>
                    
                    <div class="footer">
                        © 2024 FinLi. Todos los derechos reservados.<br>
                        Este es un mensaje automático, por favor no respondas a este correo.<br>
                        Protegiendo tu seguridad financiera.
                    </div>
                </div>
            </body>
            </html>
            """.formatted(nombre, apellido, nombre, apellido, email);
    }

    public Usuario login(String email, String rawPassword) {
        Usuario u = repo.findByCorreo(email)
                .orElseThrow(() -> new RuntimeException("Credenciales inválidas"));

        if (!BCrypt.checkpw(rawPassword, u.getContrasena())) {
            throw new RuntimeException("Credenciales inválidas");
        }

        int estado = u.getEstadoUsuario().getIdEstado();

        if (estado == 2) {
            throw new RuntimeException("Tu cuenta está SUSPENDIDA.");
        }

        if (estado == 5) {
            throw new RuntimeException("Tu cuenta está BLOQUEADA.");
        }

        if (u.getSuscripciones() != null && !u.getSuscripciones().isEmpty()) {
            Suscripcion sus = u.getSuscripciones().get(0);
            int estadoSus = sus.getEstadoSuscripcion().getIdEstadoSuscripcion();

            if (estadoSus == 2) {
                throw new RuntimeException("Tu suscripción está SUSPENDIDA.");
            }

            if (estadoSus == 3) {
                throw new RuntimeException("Tu suscripción fue CANCELADA.");
            }

            if (estadoSus == 4) {
                System.out.println("⚠ AVISO: Suscripción expirada, login permitido como GRATUITO.");
            }
        }

        return u;
    }

    @Transactional
    public void iniciarRecuperacion(String email) {
        try {
            System.out.println("🔍 Buscando usuario con email: " + email);
            
            Usuario usuario = repo.findByCorreo(email)
                    .orElseThrow(() -> new RuntimeException("Correo no registrado."));

            System.out.println("✅ Usuario encontrado: " + usuario.getNombre());

            String token = String.valueOf((int) (Math.random() * 900000) + 100000);
            System.out.println("🔑 Token generado: " + token);

            // Eliminar tokens anteriores
            passwordResetTokenRepository.deleteByUsuarioId(usuario.getId());
            System.out.println("🗑️ Tokens anteriores eliminados");

            PasswordResetToken resetToken = new PasswordResetToken(token, usuario);
            passwordResetTokenRepository.save(resetToken);
            System.out.println("💾 Token guardado en base de datos");

            String subject = "🔐 Código de Recuperación - FinLi";
            String htmlMsg = buildRecoveryEmailHtml(usuario.getNombre(), token);

            System.out.println("📧 Enviando correo HTML a: " + email);
            emailService.sendHtmlEmail(email, subject, htmlMsg);
            System.out.println("✅ Proceso de recuperación completado exitosamente");

        } catch (Exception e) {
            System.err.println("❌ Error en iniciarRecuperacion: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Error al iniciar el proceso de recuperación: " + e.getMessage(), e);
        }
    }

    private String buildRecoveryEmailHtml(String nombre, String token) {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    body {
                        font-family: Arial, sans-serif;
                        background-color: #f8f9fa;
                        margin: 0;
                        padding: 20px;
                    }
                    .container {
                        max-width: 600px;
                        margin: 0 auto;
                        background: white;
                        border-radius: 12px;
                        box-shadow: 0 8px 32px rgba(12, 37, 24, 0.08);
                        overflow: hidden;
                    }
                    .header {
                        background: #0ea46f;
                        padding: 30px;
                        text-align: center;
                        color: white;
                    }
                    .logo {
                        font-size: 28px;
                        font-weight: bold;
                        margin-bottom: 10px;
                    }
                    .content {
                        padding: 40px;
                    }
                    .greeting {
                        color: #6c757d;
                        margin-bottom: 20px;
                        line-height: 1.6;
                    }
                    .token-container {
                        background: #d7ffe3;
                        border: 2px dashed #0ea46f;
                        border-radius: 8px;
                        padding: 25px;
                        text-align: center;
                        margin: 30px 0;
                    }
                    .token {
                        font-size: 32px;
                        font-weight: bold;
                        color: #0ea46f;
                        letter-spacing: 8px;
                        font-family: 'Courier New', monospace;
                    }
                    .instructions {
                        background: #f8f9fa;
                        border-left: 4px solid #0ea46f;
                        padding: 20px;
                        margin: 25px 0;
                        border-radius: 4px;
                    }
                    .warning {
                        background: #fff3f3;
                        border: 1px solid #ff6b6b;
                        border-radius: 8px;
                        padding: 15px;
                        margin: 20px 0;
                        text-align: center;
                        color: #ff6b6b;
                    }
                    .footer {
                        background: #f8f9fa;
                        padding: 25px;
                        text-align: center;
                        border-top: 1px solid #e9ecef;
                        color: #838e98;
                        font-size: 12px;
                    }
                    .highlight {
                        color: #0ea46f;
                        font-weight: bold;
                    }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <div class="logo">FINLI</div>
                        <div>Tu aliado en finanzas personales</div>
                    </div>
                    
                    <div class="content">
                        <div class="greeting">
                            Hola <span class="highlight">%s</span>,
                        </div>
                        
                        <div class="greeting">
                            Hemos recibido una solicitud para restablecer tu contraseña. 
                            Utiliza el siguiente código de verificación para continuar con el proceso.
                        </div>
                        
                        <div class="token-container">
                            <div style="color: #6c757d; font-size: 14px; margin-bottom: 10px;">
                                TU CÓDIGO DE VERIFICACIÓN
                            </div>
                            <div class="token">%s</div>
                            <div style="color: #838e98; font-size: 12px; margin-top: 10px;">
                                Válido por 10 minutos
                            </div>
                        </div>
                        
                        <div class="instructions">
                            <h3 style="color: #0ea46f; margin-bottom: 10px;">📋 Instrucciones:</h3>
                            <ul style="color: #6c757d; padding-left: 20px; line-height: 1.6;">
                                <li>Ingresa este código en la página de recuperación de contraseña</li>
                                <li>El código expirará en 10 minutos por seguridad</li>
                                <li>No compartas este código con nadie</li>
                            </ul>
                        </div>
                        
                        <div class="warning">
                            ⚠️ Si no solicitaste este cambio, ignora este mensaje.
                        </div>
                        
                        <div style="color: #6c757d; font-size: 14px; margin-top: 20px;">
                            ¿Necesitas ayuda? Contáctanos en 
                            <span class="highlight">soporte@finli.com</span>
                        </div>
                    </div>
                    
                    <div class="footer">
                        © 2024 FinLi. Todos los derechos reservados.<br>
                        Este es un mensaje automático, por favor no respondas a este correo.<br>
                        Protegiendo tu seguridad financiera.
                    </div>
                </div>
            </body>
            </html>
            """.formatted(nombre, token);
    }

    @Transactional
    public void restablecerContrasena(PasswordResetRequest request) {
        try {
            System.out.println("🔍 Validando token: " + request.getToken());
            
            PasswordResetToken tokenEntity = passwordResetTokenRepository.findByToken(request.getToken())
                    .orElseThrow(() -> new RuntimeException("Código de recuperación inválido o no encontrado."));

            if (!tokenEntity.getUsuario().getCorreo().equalsIgnoreCase(request.getEmail())) {
                throw new RuntimeException("El código no corresponde al correo proporcionado.");
            }

            if (tokenEntity.getExpiryDate().isBefore(LocalDateTime.now())) {
                passwordResetTokenRepository.delete(tokenEntity);
                throw new RuntimeException("El código de recuperación ha expirado. Por favor, solicite uno nuevo.");
            }

            Usuario usuario = tokenEntity.getUsuario();
            String nuevaContrasenaCodificada = BCrypt.hashpw(request.getNuevaContrasena(), BCrypt.gensalt());
            usuario.setContrasena(nuevaContrasenaCodificada);

            repo.save(usuario);
            passwordResetTokenRepository.delete(tokenEntity);
            
            // ENVIAR CORREO DE CONFIRMACIÓN DE CAMBIO DE CONTRASEÑA
            enviarCorreoConfirmacionCambioContrasena(usuario);
            
            System.out.println("✅ Contraseña restablecida exitosamente para: " + request.getEmail());

        } catch (Exception e) {
            System.err.println("❌ Error en restablecerContrasena: " + e.getMessage());
            throw e;
        }
    }

    private void enviarCorreoConfirmacionCambioContrasena(Usuario usuario) {
        try {
            String subject = "✅ Confirmación de cambio de contraseña - FinLi";
            String htmlMsg = buildConfirmationEmailHtml(usuario.getNombre());
            
            emailService.sendHtmlEmail(usuario.getCorreo(), subject, htmlMsg);
            System.out.println("📧 Correo de confirmación enviado a: " + usuario.getCorreo());
            
        } catch (Exception e) {
            System.err.println("⚠️ No se pudo enviar el correo de confirmación: " + e.getMessage());
            // No lanzamos excepción para no interrumpir el flujo principal
        }
    }

    private String buildConfirmationEmailHtml(String nombre) {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <meta charset="UTF-8">
                <style>
                    body {
                        font-family: Arial, sans-serif;
                        background-color: #f8f9fa;
                        margin: 0;
                        padding: 20px;
                    }
                    .container {
                        max-width: 600px;
                        margin: 0 auto;
                        background: white;
                        border-radius: 12px;
                        box-shadow: 0 8px 32px rgba(12, 37, 24, 0.08);
                        overflow: hidden;
                    }
                    .header {
                        background: #0ea46f;
                        padding: 30px;
                        text-align: center;
                        color: white;
                    }
                    .logo {
                        font-size: 28px;
                        font-weight: bold;
                        margin-bottom: 10px;
                    }
                    .content {
                        padding: 40px;
                    }
                    .greeting {
                        color: #6c757d;
                        margin-bottom: 20px;
                        line-height: 1.6;
                    }
                    .message {
                        background: #d7ffe3;
                        border: 2px solid #0ea46f;
                        border-radius: 8px;
                        padding: 25px;
                        text-align: center;
                        margin: 30px 0;
                    }
                    .footer {
                        background: #f8f9fa;
                        padding: 25px;
                        text-align: center;
                        border-top: 1px solid #e9ecef;
                        color: #838e98;
                        font-size: 12px;
                    }
                    .highlight {
                        color: #0ea46f;
                        font-weight: bold;
                    }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <div class="logo">FINLI</div>
                        <div>Tu aliado en finanzas personales</div>
                    </div>
                    
                    <div class="content">
                        <div class="greeting">
                            Hola <span class="highlight">%s</span>,
                        </div>
                        
                        <div class="greeting">
                            Te confirmamos que tu contraseña ha sido cambiada exitosamente.
                        </div>
                        
                        <div class="message">
                            <h3 style="color: #0ea46f; margin-bottom: 10px;">🔒 Contraseña Actualizada</h3>
                            <p style="color: #6c757d; line-height: 1.6;">
                                Tu contraseña ha sido actualizada correctamente. Ahora puedes iniciar sesión con tu nueva contraseña.
                            </p>
                            <p style="color: #6c757d; line-height: 1.6;">
                                Si no realizaste este cambio, por favor contacta a soporte inmediatamente.
                            </p>
                        </div>
                        
                        <div style="color: #6c757d; font-size: 14px; margin-top: 20px;">
                            ¿Necesitas ayuda? Contáctanos en 
                            <span class="highlight">soporte@finli.com</span>
                        </div>
                    </div>
                    
                    <div class="footer">
                        © 2024 FinLi. Todos los derechos reservados.<br>
                        Este es un mensaje automático, por favor no respondas a este correo.<br>
                        Protegiendo tu seguridad financiera.
                    </div>
                </div>
            </body>
            </html>
            """.formatted(nombre);
    }

    public UsuarioResponse toResponse(Usuario u) {
        Suscripcion sus = (u.getSuscripciones() == null || u.getSuscripciones().isEmpty())
                ? null
                : u.getSuscripciones().get(0);

        return UsuarioResponse.builder()
                .id(u.getId())
                .email(u.getCorreo())
                .nombre(u.getNombre())
                .apellidoPaterno(u.getApellidoPaterno())
                .apellidoMaterno(u.getApellidoMaterno())
                .edad(u.getEdad())
                .estadoUsuario(
                    UsuarioResponse.EstadoUsuarioResponse.builder()
                            .idEstado(u.getEstadoUsuario().getIdEstado())
                            .nombreEstado(u.getEstadoUsuario().getNombreEstado())
                            .build()
                )
                .tipoSuscripcion(
                    sus != null ? sus.getTipoSuscripcion().getNombreTipoSuscripcion() : "Gratuito"
                )
                .estadoSuscripcion(
                    sus != null ? sus.getEstadoSuscripcion().getNombreEstado() : "Ninguno"
                )
                .idEstadoSuscripcion(
                    sus != null ? sus.getEstadoSuscripcion().getIdEstadoSuscripcion() : 1
                )
                .fechaFinSuscripcion(
                    sus != null ? sus.getFechaFin() : null
                )
                .build();
    }
}