package com.finli.service;

import com.finli.model.Categoria;
import com.finli.model.FuenteCategoria;
import com.finli.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository repository;

    public List<Categoria> listarTodos() {
        return repository.findAll();
    }

    public Optional<Categoria> buscarPorId(Integer id) {
        return repository.findById(id);
    }

    public List<Categoria> listarPorFuente(FuenteCategoria fuente) {
        return repository.findByFuente(fuente);
    }

    public Categoria guardar(Categoria categoria) {
        return repository.save(categoria);
    }
}
