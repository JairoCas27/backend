package com.finli.service;

import com.finli.model.MedioPago;
import com.finli.repository.MedioPagoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class MedioPagoService {

    private final MedioPagoRepository repo;

    @Transactional
    public void sumarSaldo(Integer idMedioPago, BigDecimal monto) {
        MedioPago mp = repo.findById(idMedioPago)
                .orElseThrow(() -> new RuntimeException("Medio de pago no encontrado"));

        mp.setMontoInicial(mp.getMontoInicial() + monto.doubleValue());
        repo.save(mp);
    }

    @Transactional
    public void restarSaldo(Integer idMedioPago, BigDecimal monto) {
        MedioPago mp = repo.findById(idMedioPago)
                .orElseThrow(() -> new RuntimeException("Medio de pago no encontrado"));

        if (mp.getMontoInicial() < monto.doubleValue()) {
            throw new RuntimeException("Saldo insuficiente en el medio de pago");
        }

        mp.setMontoInicial(mp.getMontoInicial() - monto.doubleValue());
        repo.save(mp);
    }
}