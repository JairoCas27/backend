package com.finli.service;

import com.finli.model.Usuario;
import com.finli.model.Suscripcion; // ya usado
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.xssf.model.ThemesTable;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

@Service
public class ExcelExportService {

    // 1. ENCABEZADOS
    private static final List<String> HEADERS = Arrays.asList(
            "ID", "Nombre", "Apellido Paterno", "Apellido Materno",
            "Correo", "Edad", "Rol", "Suscripción Actual", "Estado");

    // Colores (hex)
    private static final String VERDE_CLARO_HEX = "d7ffe3"; // --verdeCla
    private static final String VERDE_OSCURO_HEX = "0ea46f"; // --verdeOs
    private static final String BLANCO_HEX = "ffffff"; // --blanco
    private static final String AMARILLO_HEX = "ffd000"; // --amarrillo
    private static final String PLOMO_HEX = "f7f7f7"; // --plomo

    public byte[] exportUsersToExcel(List<Usuario> usuarios) throws IOException {
        try (XSSFWorkbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            XSSFSheet sheet = workbook.createSheet("Reporte_Usuarios_FinLi");

            // Crear estilos reutilizables
            XSSFCellStyle titleStyle = createTitleStyle(workbook);
            XSSFCellStyle headerStyle = createHeaderStyle(workbook);
            XSSFCellStyle evenRowStyle = createRowStyle(workbook, BLANCO_HEX);
            XSSFCellStyle oddRowStyle = createRowStyle(workbook, PLOMO_HEX);
            XSSFCellStyle yellowHighlightStyle = createHighlightStyle(workbook);

            int currentRow = 0;

            // 1) Insertar logo (si existe) y título grande alineado
            insertLogoIfExists(workbook, sheet);

            // Título (merged across columns 1..HEADERS.size()-1)
            XSSFRow titleRow = sheet.createRow(currentRow++);
            titleRow.setHeightInPoints(36);
            XSSFCell titleCell = titleRow.createCell(1);
            titleCell.setCellValue("Reporte de Usuarios - FinLi");
            titleCell.setCellStyle(titleStyle);

            // Merge del título para que sea amplio (col 1 a last header column)
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 1, HEADERS.size() - 1));

            // Espacio pequeño
            sheet.createRow(currentRow++);

            // 2) Fila de encabezados
            XSSFRow headerRow = sheet.createRow(currentRow++);
            headerRow.setHeightInPoints(20);
            for (int col = 0; col < HEADERS.size(); col++) {
                XSSFCell cell = headerRow.createCell(col);
                cell.setCellValue(HEADERS.get(col));
                cell.setCellStyle(headerStyle);
            }

            // Freeze pane para mantener encabezado visible (filas arriba del contenido)
            sheet.createFreezePane(0, currentRow);

            // 3) Filas de datos (comenzando en currentRow)
            int rowIdx = currentRow;
            for (Usuario user : usuarios) {
                XSSFRow row = sheet.createRow(rowIdx);
                XSSFCellStyle rowStyle = (rowIdx % 2 == 0) ? evenRowStyle : oddRowStyle;

                // ID
                XSSFCell c0 = row.createCell(0);
                c0.setCellValue(user.getId());
                c0.setCellStyle(rowStyle);

                // Nombre
                XSSFCell c1 = row.createCell(1);
                c1.setCellValue(orEmpty(user.getNombre()));
                c1.setCellStyle(rowStyle);

                // Apellido Paterno
                XSSFCell c2 = row.createCell(2);
                c2.setCellValue(orEmpty(user.getApellidoPaterno()));
                c2.setCellStyle(rowStyle);

                // Apellido Materno
                XSSFCell c3 = row.createCell(3);
                c3.setCellValue(orEmpty(user.getApellidoMaterno()));
                c3.setCellStyle(rowStyle);

                // Correo
                XSSFCell c4 = row.createCell(4);
                c4.setCellValue(orEmpty(user.getCorreo()));
                c4.setCellStyle(rowStyle);

                // Edad
                XSSFCell c5 = row.createCell(5);
                if (user.getEdad() != null) {
                    c5.setCellValue(user.getEdad());
                } else {
                    c5.setCellValue("");
                }
                c5.setCellStyle(rowStyle);

                // Rol
                XSSFCell c6 = row.createCell(6);
                c6.setCellValue(orEmpty(user.getRol()));
                c6.setCellStyle(rowStyle);

                // Suscripción Actual
                String suscripcionActual = "Gratuito";
                if (user.getSuscripciones() != null) {
                    suscripcionActual = user.getSuscripciones().stream()
                            .filter(s -> s.getEstadoSuscripcion() != null
                                    && s.getEstadoSuscripcion().getIdEstadoSuscripcion() == 1)
                            .map(s -> s.getTipoSuscripcion() != null
                                    ? s.getTipoSuscripcion().getNombreTipoSuscripcion()
                                    : "Gratuito")
                            .findFirst()
                            .orElse("Gratuito");
                }
                XSSFCell c7 = row.createCell(7);
                c7.setCellValue(suscripcionActual);
                c7.setCellStyle(rowStyle);

                // Estado
                String estado = (user.getEstadoUsuario() != null)
                        ? user.getEstadoUsuario().getNombreEstado()
                        : "Desconocido";
                XSSFCell c8 = row.createCell(8);
                c8.setCellValue(estado);
                c8.setCellStyle(rowStyle);

                rowIdx++;
            }

            // Autoajustar columnas (ligeramente) — notar que autosize puede tardar con muchas filas
            for (int i = 0; i < HEADERS.size(); i++) {
                sheet.autoSizeColumn(i);
                int currentWidth = sheet.getColumnWidth(i);
                // añadir un padding extra para estética
                sheet.setColumnWidth(i, Math.min(10000, currentWidth + 600));
            }

            // Escritura final
            workbook.write(out);
            return out.toByteArray();
        }
    }

    // ============================
    // Métodos auxiliares de estilo
    // ============================

    private XSSFCellStyle createTitleStyle(XSSFWorkbook workbook) {
        XSSFCellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeightInPoints((short) 16);
        font.setBold(true);
        font.setFontName("Footlight MT Light"); // si existe en el sistema
        style.setFont(font);
        // fondo verde claro
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFillForegroundColor(new XSSFColor(hexToRgb(VERDE_CLARO_HEX), new DefaultIndexedColorMap()));
        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        addThinBorders(style);
        return style;
    }

    private XSSFCellStyle createHeaderStyle(XSSFWorkbook workbook) {
        XSSFCellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontName("Footlight MT Light");
        font.setFontHeightInPoints((short) 11);
        font.setColor(IndexedColors.WHITE.getIndex()); // texto blanco
        style.setFont(font);

        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFillForegroundColor(new XSSFColor(hexToRgb(VERDE_OSCURO_HEX), new DefaultIndexedColorMap()));

        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(true);

        addThinBorders(style);
        return style;
    }

    private XSSFCellStyle createRowStyle(XSSFWorkbook workbook, String bgHex) {
        XSSFCellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontName("Footlight MT Light");
        font.setFontHeightInPoints((short) 10);
        style.setFont(font);

        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFillForegroundColor(new XSSFColor(hexToRgb(bgHex), new DefaultIndexedColorMap()));

        style.setAlignment(HorizontalAlignment.LEFT);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        addThinBorders(style);
        return style;
    }

    private XSSFCellStyle createHighlightStyle(XSSFWorkbook workbook) {
        XSSFCellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontName("Footlight MT Light");
        style.setFont(font);
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setFillForegroundColor(new XSSFColor(hexToRgb(AMARILLO_HEX), new DefaultIndexedColorMap()));
        addThinBorders(style);
        return style;
    }

    private void addThinBorders(XSSFCellStyle style) {
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
    }

    // ============================
    // Insertar logo desde classpath: static/images/LogoFinLi.png
    // ============================
    private void insertLogoIfExists(XSSFWorkbook workbook, XSSFSheet sheet) {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("static/images/LogoFinLi.png")) {
            if (is == null) return; // no encontrado: se continúa sin logo

            byte[] bytes = toByteArray(is);
            int pictureIdx = workbook.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);

            CreationHelper helper = workbook.getCreationHelper();
            Drawing<?> drawing = sheet.createDrawingPatriarch();
            ClientAnchor anchor = helper.createClientAnchor();

            // Columna/ fila donde comienza el logo (col 0, row 0),
            // y expandirlo para que ocupe aprox hasta la fila 1-2 visualmente.
            anchor.setCol1(0);
            anchor.setRow1(0);
            anchor.setCol2(1);
            anchor.setRow2(2);

            Picture pict = drawing.createPicture(anchor, pictureIdx);
            // ajustar tamaño relativo: se puede afinar (0.8f - 1.5f)
            pict.resize(0.5);

        } catch (Exception e) {
            // si algo falla con el logo, no interrumpe la generación del excel
            // (podrías registrar la excepción si lo deseas)
        }
    }

    // ============================
    // Utilitarios
    // ============================
    private byte[] toByteArray(InputStream is) throws IOException {
        try (ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {
            IOUtils.copy(is, buffer);
            return buffer.toByteArray();
        }
    }

    private byte[] hexToRgb(String hex) {
        // devuelve array RGB para XSSFColor (orden R,G,B). hex sin '#'
        int r = Integer.parseInt(hex.substring(0, 2), 16);
        int g = Integer.parseInt(hex.substring(2, 4), 16);
        int b = Integer.parseInt(hex.substring(4, 6), 16);
        return new byte[]{(byte) r, (byte) g, (byte) b};
    }

    private String orEmpty(String s) {
        return s == null ? "" : s;
    }
}
