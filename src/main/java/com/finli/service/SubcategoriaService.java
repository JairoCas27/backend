package com.finli.service;

import com.finli.model.Subcategoria;
import com.finli.model.Categoria;
import com.finli.repository.SubcategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubcategoriaService {

    private final SubcategoriaRepository repository;

    public List<Subcategoria> listarTodos() {
        return repository.findAll();
    }

    public Optional<Subcategoria> buscarPorId(Integer id) {
        return repository.findById(id);
    }

    public List<Subcategoria> listarPorCategoria(Categoria categoria) {
        return repository.findByCategoria(categoria);
    }

    public Subcategoria guardar(Subcategoria subcategoria) {
        return repository.save(subcategoria);
    }
}
