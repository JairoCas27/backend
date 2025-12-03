package com.finli.service;

import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.MultiPartEmail;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Value("${mail.smtp.host}")
    private String host;

    @Value("${mail.smtp.port}")
    private int port;

    @Value("${mail.smtp.username}")
    private String username;

    @Value("${mail.smtp.password}")
    private String password;

    @Value("${mail.smtp.starttls.enable}")
    private boolean startTlsEnable;

    @Value("${mail.smtp.auth}")
    private boolean auth;

    // 📌 ENVÍO DE EMAIL SIMPLE (TEXTO PLANO)
    public void sendEmail(String toEmail, String subject, String msg) {
        try {
            MultiPartEmail email = new MultiPartEmail();
            email.setHostName(host);
            email.setSmtpPort(port);
            email.setAuthentication(username, password);
            email.setStartTLSEnabled(startTlsEnable);
            email.setFrom(username);
            email.setSubject(subject);
            email.setMsg(msg);
            email.addTo(toEmail);

            email.send();
            System.out.println("✅ Correo de texto enviado a: " + toEmail);

        } catch (Exception e) {
            System.err.println("❌ Error al enviar correo simple a " + toEmail + ": " + e.getMessage());
            throw new RuntimeException("Fallo al enviar correo simple.", e);
        }
    }

    // 📌 ENVÍO DE EMAIL HTML (PARA RECUPERACIÓN DE CONTRASEÑA)
    public void sendHtmlEmail(String toEmail, String subject, String htmlMsg) {
        try {
            HtmlEmail email = new HtmlEmail();
            email.setHostName(host);
            email.setSmtpPort(port);
            email.setAuthentication(username, password);
            email.setStartTLSEnabled(startTlsEnable);
            email.setFrom(username, "FinLi - Tu Aliado Financiero");
            email.setSubject(subject);
            email.setHtmlMsg(htmlMsg);
            
            // También agregamos una versión de texto plano por compatibilidad
            String textMsg = "FinLi - Recuperación de Contraseña\n\n" +
                            "Por favor, vea este correo en un cliente que soporte HTML para obtener la información completa.";
            email.setTextMsg(textMsg);
            
            email.addTo(toEmail);

            email.send();
            System.out.println("✅ Correo HTML enviado correctamente a: " + toEmail);

        } catch (Exception e) {
            System.err.println("❌ Error al enviar correo HTML a " + toEmail + ": " + e.getMessage());
            throw new RuntimeException("Fallo al enviar correo HTML.", e);
        }
    }

    // 📌 ENVÍO DE EMAIL CON PDF ADJUNTO (RECIBO DE PAGO) - TEXTO PLANO
    public void sendEmailWithAttachment(String toEmail, String subject, String msg, String filePath) {
        try {
            MultiPartEmail email = new MultiPartEmail();
            email.setHostName(host);
            email.setSmtpPort(port);
            email.setAuthentication(username, password);
            email.setStartTLSEnabled(startTlsEnable);
            email.setFrom(username);
            email.setSubject(subject);
            email.setMsg(msg);
            email.addTo(toEmail);

            // Crear adjunto
            EmailAttachment attachment = new EmailAttachment();
            attachment.setPath(filePath);
            attachment.setDisposition(EmailAttachment.ATTACHMENT);
            attachment.setDescription("Recibo de suscripción");
            attachment.setName("Recibo_FinLi.pdf");

            // Adjuntar
            email.attach(attachment);

            // Enviar
            email.send();

            System.out.println("📨 Email con recibo enviado correctamente a: " + toEmail);

        } catch (Exception e) {
            System.err.println("❌ Error al enviar correo con PDF a " + toEmail + ": " + e.getMessage());
            throw new RuntimeException("Fallo al enviar correo con adjunto.", e);
        }
    }

    // 📌 ENVÍO DE EMAIL HTML CON PDF ADJUNTO (PARA FUTURAS MEJORAS)
    public void sendHtmlEmailWithAttachment(String toEmail, String subject, String htmlMsg, String filePath) {
        try {
            HtmlEmail email = new HtmlEmail();
            email.setHostName(host);
            email.setSmtpPort(port);
            email.setAuthentication(username, password);
            email.setStartTLSEnabled(startTlsEnable);
            email.setFrom(username, "FinLi - Tu Aliado Financiero");
            email.setSubject(subject);
            email.setHtmlMsg(htmlMsg);
            
            // Versión de texto plano
            String textMsg = "FinLi - Comprobante\n\n" +
                            "Por favor, vea este correo en un cliente que soporte HTML para obtener la información completa.";
            email.setTextMsg(textMsg);
            
            email.addTo(toEmail);

            // Crear y adjuntar PDF
            EmailAttachment attachment = new EmailAttachment();
            attachment.setPath(filePath);
            attachment.setDisposition(EmailAttachment.ATTACHMENT);
            attachment.setDescription("Recibo de suscripción");
            attachment.setName("Recibo_FinLi.pdf");
            email.attach(attachment);

            email.send();
            System.out.println("✅ Correo HTML con adjunto enviado a: " + toEmail);

        } catch (Exception e) {
            System.err.println("❌ Error al enviar correo HTML con adjunto a " + toEmail + ": " + e.getMessage());
            throw new RuntimeException("Fallo al enviar correo HTML con adjunto.", e);
        }
    }
}