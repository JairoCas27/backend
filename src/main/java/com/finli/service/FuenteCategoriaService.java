package com.finli.service;

import com.finli.model.FuenteCategoria;
import com.finli.repository.FuenteCategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FuenteCategoriaService {

    private final FuenteCategoriaRepository repository;

    public List<FuenteCategoria> listarTodos() {
        return repository.findAll();
    }

    public Optional<FuenteCategoria> buscarPorId(Integer id) {
        return repository.findById(id);
    }

    public FuenteCategoria guardar(FuenteCategoria fuente) {
        return repository.save(fuente);
    }
}

