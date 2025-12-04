@echo off
echo ========================================
echo   VERIFICACION DE METRICAS
echo ========================================

echo 1. Verificando que la aplicacion este corriendo...
curl -s http://localhost:8080/actuator/health > nul
if %errorlevel% neq 0 (
    echo ERROR: La aplicacion Spring Boot no esta corriendo en puerto 8080
    echo Ejecuta primero: mvn spring-boot:run
    pause
    exit /b 1
)
echo ✓ Aplicacion Spring Boot OK

echo 2. Verificando metricas disponibles...
curl -s http://localhost:8080/actuator/prometheus | find "finli_" > metrics.temp
if %errorlevel% equ 0 (
    echo ✓ Metricas personalizadas encontradas:
    type metrics.temp | find "finli_"
    del metrics.temp
) else (
    echo ✗ No se encontraron metricas 'finli_'
    echo Verifica que hayas agregado @Timed a los controladores
)

echo 3. Verificando Prometheus...
curl -s http://localhost:9090/-/healthy > nul
if %errorlevel% equ 0 (
    echo ✓ Prometheus esta corriendo
) else (
    echo ✗ Prometheus no responde
)

echo 4. Verificando Grafana...
curl -s http://localhost:3000/api/health > nul
if %errorlevel% equ 0 (
    echo ✓ Grafana esta corriendo
) else (
    echo ✗ Grafana no responde
)

echo.
echo ========================================
echo   COMANDOS UTILES
echo ========================================
echo.
echo Ver metricas: curl http://localhost:8080/actuator/prometheus
echo Ver targets: curl http://localhost:9090/api/v1/targets
echo Ver logs Prometheus: docker logs finli-prometheus
echo Ver logs Grafana: docker logs finli-grafana
echo.
pause