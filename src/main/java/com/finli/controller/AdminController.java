package com.finli.controller;

// NUEVA IMPORTACIÓN NECESARIA
import com.finli.dto.PaginacionUsuarioResponse;
// La importación de UsuarioResponse ya no es necesaria en el método listarUsuarios,
// pero la dejamos si se usa en otros métodos.


import com.finli.model.EstadoUsuario;
import com.finli.model.Usuario;
import com.finli.service.AdministradorService;
import com.finli.service.ExcelExportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AdminController {

    private final AdministradorService administradorService;
    private final ExcelExportService excelExportService;

    // =================================================================================
    // === LISTAR USUARIOS (REEMPLAZADO por Paginación y Filtro) ===
    // =================================================================================
    /**
     * Endpoint para obtener usuarios con paginación y filtrado por estado.
     * Endpoint: GET /api/admin/usuarios?page=1&limit=10&status=all
     */
    @GetMapping("/usuarios") // Mapeado a /api/admin/usuarios
    public ResponseEntity<PaginacionUsuarioResponse> getUsuariosPaginadosYFiltrados(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit,
            @RequestParam(defaultValue = "all") String status) {

        // Opcional: Validar que el estado del filtro sea uno de los permitidos
        if (!status.equalsIgnoreCase("all") && 
            !status.equalsIgnoreCase("active") && 
            !status.equalsIgnoreCase("inactive")) {
            status = "all";
        }
        
        // La llamada al servicio AdministradorService que creamos en el Paso 5.
        PaginacionUsuarioResponse response = administradorService.getUsuariosPaginadosYFiltrados(page, limit, status);

        return ResponseEntity.ok(response);
    }
    // =================================================================================
    // =================================================================================

    // CREAR CLIENTE (Se mantiene)
    @PostMapping("/usuarios")
    public ResponseEntity<Usuario> crearCliente(@RequestBody Usuario usuario) {
        Usuario nuevoUsuario = administradorService.guardarCliente(usuario);
        return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
    }

    // ACTUALIZAR USUARIO (Se mantiene)
    @PutMapping("/usuarios/{id}")
    public ResponseEntity<Usuario> actualizarUsuario(@PathVariable Integer id, @RequestBody Usuario usuario) {
        usuario.setId(id);

        Optional<Usuario> usuarioActualizado = administradorService.actualizarUsuario(usuario);

        return usuarioActualizado
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // LISTAR ESTADOS DE USUARIO (Se mantiene)
    @GetMapping("/estados-usuario")
    public ResponseEntity<List<EstadoUsuario>> listarEstados() {
        List<EstadoUsuario> estados = administradorService.listarTodosEstadosUsuario();
        return ResponseEntity.ok(estados);
    }

    // ELIMINAR USUARIO (ELIMINACIÓN LÓGICA - Se mantiene)
    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<Void> eliminarUsuario(@PathVariable Integer id) {
        boolean eliminado = administradorService.eliminarUsuarioLogico(id);

        if (eliminado) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // EXPORTAR A EXCEL (Se mantiene)
    @GetMapping("/usuarios/exportar")
    public ResponseEntity<byte[]> exportarUsuariosAExcel() {
        try {
            List<Usuario> usuarios = administradorService.obtenerListaDeUsuariosParaExportar();

            byte[] excelBytes = excelExportService.exportUsersToExcel(usuarios);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(
                    MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
            headers.setContentDispositionFormData("attachment",
                    "usuarios_finli_" + System.currentTimeMillis() + ".xlsx");
            headers.setContentLength(excelBytes.length);

            return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);

        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}