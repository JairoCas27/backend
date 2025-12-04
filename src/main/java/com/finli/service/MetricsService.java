package com.finli.service;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;  // ¡Cambiado!
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class MetricsService {
    
    private final MeterRegistry meterRegistry;
    private Counter loginExitosoCounter;
    private Counter loginFallidoCounter;
    private Counter transaccionCreadaCounter;
    private Counter usuarioRegistradoCounter;
    private Counter transaccionEliminadaCounter;
    private AtomicInteger usuariosActivos = new AtomicInteger(0);
    private Timer transaccionTimer;
    
    public MetricsService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }
    
    @PostConstruct
    public void init() {
        // Contadores personalizados
        loginExitosoCounter = Counter.builder("finli.auth.login.exitoso")
                .description("Total de logins exitosos")
                .register(meterRegistry);
                
        loginFallidoCounter = Counter.builder("finli.auth.login.fallido")
                .description("Total de logins fallidos")
                .register(meterRegistry);
                
        transaccionCreadaCounter = Counter.builder("finli.transacciones.total")
                .description("Total de transacciones creadas")
                .register(meterRegistry);
                
        transaccionEliminadaCounter = Counter.builder("finli.transacciones.eliminadas")
                .description("Total de transacciones eliminadas")
                .register(meterRegistry);
                
        usuarioRegistradoCounter = Counter.builder("finli.usuarios.registrados")
                .description("Total de usuarios registrados")
                .register(meterRegistry);
        
        // Gauge para usuarios activos
        Gauge.builder("finli.usuarios.activos", usuariosActivos, AtomicInteger::get)
                .description("Número de usuarios activos en la sesión")
                .register(meterRegistry);
        
        // Timer personalizado para transacciones
        transaccionTimer = Timer.builder("finli.transacciones.procesamiento")
                .description("Tiempo de procesamiento de transacciones")
                .publishPercentiles(0.5, 0.95, 0.99)
                .register(meterRegistry);
    }
    
    public void incrementLoginExitoso() {
        loginExitosoCounter.increment();
    }
    
    public void incrementLoginFallido() {
        loginFallidoCounter.increment();
    }
    
    public void incrementTransaccionCreada() {
        transaccionCreadaCounter.increment();
    }
    
    public void incrementTransaccionEliminada() {
        transaccionEliminadaCounter.increment();
    }
    
    public void incrementUsuarioRegistrado() {
        usuarioRegistradoCounter.increment();
    }
    
    public void setUsuariosActivos(int count) {
        usuariosActivos.set(count);
    }
    
    public void recordTransaccionTime(long durationInMillis) {
        transaccionTimer.record(durationInMillis, TimeUnit.MILLISECONDS);
    }
}