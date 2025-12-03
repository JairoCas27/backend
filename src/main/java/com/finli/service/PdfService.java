package com.finli.service;

import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.finli.model.Suscripcion;
import com.finli.model.TipoSuscripcion;
import com.finli.model.Usuario;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

@Service
public class PdfService {

    // Definición de colores
    private static final BaseColor VERDE_OSCURO = new BaseColor(14, 164, 111);   // #0ea46f
    private static final BaseColor GRIS_OSCURO = new BaseColor(108, 117, 125);   // #6c757d
    private static final BaseColor GRIS_CLARO = new BaseColor(131, 142, 152);    // #838e98
    private static final BaseColor VERDE_CLARO = new BaseColor(215, 255, 227);   // #d7ffe3
    private static final BaseColor ROJO = new BaseColor(255, 107, 107);          // #ff6b6b

    public String generarReciboPDF(Usuario usuario, TipoSuscripcion tipo, Suscripcion suscripcion, String last4Digits) {
        try {
            String filePath = "recibo_" + usuario.getId() + "_" + System.currentTimeMillis() + ".pdf";

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            // Fuentes personalizadas
            Font titleFont = new Font(Font.FontFamily.HELVETICA, 20, Font.BOLD, VERDE_OSCURO);
            Font subtitleFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD, GRIS_OSCURO);
            Font normalFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL, GRIS_OSCURO);
            Font labelFont = new Font(Font.FontFamily.HELVETICA, 11, Font.BOLD, GRIS_OSCURO);
            Font valueFont = new Font(Font.FontFamily.HELVETICA, 11, Font.NORMAL, GRIS_CLARO);
            Font successFont = new Font(Font.FontFamily.HELVETICA, 12, Font.BOLD, VERDE_OSCURO);

            // Agregar logo (asegúrate de tener el logo en src/main/resources/static/images/)
            try {
                Image logo = Image.getInstance(new ClassPathResource("static/images/LogoFinLi.png").getURL());
                logo.scaleToFit(80, 80);
                logo.setAlignment(Element.ALIGN_CENTER);
                document.add(logo);
            } catch (Exception e) {
                System.out.println("Logo no encontrado, continuando sin logo...");
            }

            document.add(new Paragraph("\n"));

            // Título principal
            Paragraph title = new Paragraph("FinLi \n RECIBO DE SUSCRIPCIÓN", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            document.add(new Paragraph("\n"));

            // Línea decorativa
            addSeparator(document);

            // Información del usuario en una tabla con estilo
            PdfPTable userTable = new PdfPTable(2);
            userTable.setWidthPercentage(100);
            userTable.setSpacingBefore(10f);
            userTable.setSpacingAfter(10f);

            // Encabezado de sección
            PdfPCell headerCell = new PdfPCell(new Paragraph("INFORMACIÓN DEL USUARIO", subtitleFont));
            headerCell.setColspan(2);
            headerCell.setBackgroundColor(VERDE_CLARO);
            headerCell.setBorderWidth(0);
            headerCell.setPadding(8f);
            headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            userTable.addCell(headerCell);

            addTableRow(userTable, "Nombre:", usuario.getNombre(), labelFont, valueFont);
            addTableRow(userTable, "Correo:", usuario.getCorreo(), labelFont, valueFont);

            document.add(userTable);

            // Información de la suscripción
            PdfPTable subscriptionTable = new PdfPTable(2);
            subscriptionTable.setWidthPercentage(100);
            subscriptionTable.setSpacingBefore(10f);
            subscriptionTable.setSpacingAfter(10f);

            PdfPCell subHeaderCell = new PdfPCell(new Paragraph("DETALLES DE LA SUSCRIPCIÓN", subtitleFont));
            subHeaderCell.setColspan(2);
            subHeaderCell.setBackgroundColor(VERDE_CLARO);
            subHeaderCell.setBorderWidth(0);
            subHeaderCell.setPadding(8f);
            subHeaderCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            subscriptionTable.addCell(subHeaderCell);

            addTableRow(subscriptionTable, "Plan:", tipo.getNombreTipoSuscripcion(), labelFont, valueFont);
            addTableRow(subscriptionTable, "Fecha inicio:", suscripcion.getFechaInicio().toString(), labelFont, valueFont);
            
            String fechaFin = suscripcion.getFechaFin() != null ? 
                suscripcion.getFechaFin().toString() : "Ilimitada";
            addTableRow(subscriptionTable, "Fecha fin:", fechaFin, labelFont, valueFont);
            
            addTableRow(subscriptionTable, "Tarjeta:", "**** **** **** " + last4Digits, labelFont, valueFont);

            document.add(subscriptionTable);

            // Estado de la operación
            Paragraph status = new Paragraph("✓ PAGADO", successFont);
            status.setAlignment(Element.ALIGN_RIGHT);
            document.add(status);

            document.add(new Paragraph("\n"));

            // Fecha de operación
            String fechaOperacion = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
            Paragraph fecha = new Paragraph("Fecha de operación: " + fechaOperacion, normalFont);
            fecha.setAlignment(Element.ALIGN_CENTER);
            document.add(fecha);

            // Pie de página
            Paragraph footer = new Paragraph("\nGracias por confiar en FINLI", 
                new Font(Font.FontFamily.HELVETICA, 10, Font.ITALIC, GRIS_CLARO));
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

            document.close();
            return filePath;

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("No se pudo crear el PDF.");
        }
    }

    private void addSeparator(Document document) throws DocumentException {
        PdfPTable separator = new PdfPTable(1);
        separator.setWidthPercentage(100);
        PdfPCell cell = new PdfPCell();
        cell.setFixedHeight(2f);
        cell.setBackgroundColor(VERDE_OSCURO);
        cell.setBorderWidth(0);
        separator.addCell(cell);
        document.add(separator);
        document.add(new Paragraph("\n"));
    }

    private void addTableRow(PdfPTable table, String label, String value, Font labelFont, Font valueFont) {
        PdfPCell labelCell = new PdfPCell(new Paragraph(label, labelFont));
        labelCell.setBorderWidth(0);
        labelCell.setPadding(5f);
        labelCell.setBackgroundColor(new BaseColor(248, 249, 250));

        PdfPCell valueCell = new PdfPCell(new Paragraph(value, valueFont));
        valueCell.setBorderWidth(0);
        valueCell.setPadding(5f);

        table.addCell(labelCell);
        table.addCell(valueCell);
    }
}