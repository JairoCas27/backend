@echo off
echo ========================================
echo   MONITOREO SIMPLE PARA FINLI - WINDOWS
echo ========================================

echo 1. Deteniendo contenedores existentes...
docker stop finli-prometheus finli-grafana 2>nul
docker rm finli-prometheus finli-grafana 2>nul

echo 2. Iniciando Prometheus...
docker run -d ^
  --name finli-prometheus ^
  -p 9090:9090 ^
  -v %cd%\prometheus-simple.yml:/etc/prometheus/prometheus.yml ^
  prom/prometheus:latest

echo 3. Esperando 5 segundos para Prometheus...
timeout /t 5 /nobreak > nul

echo 4. Iniciando Grafana...
docker run -d ^
  --name finli-grafana ^
  -p 3000:3000 ^
  -e GF_SECURITY_ADMIN_PASSWORD=admin ^
  grafana/grafana:latest

echo 5. Esperando 10 segundos para Grafana...
timeout /t 10 /nobreak > nul

echo.
echo ========================================
echo        ¡MONITOREO INICIADO!
echo ========================================
echo.
echo URLs IMPORTANTES:
echo.
echo 1. TU APLICACION SPRING BOOT:
echo    http://localhost:8080
echo    Metricas: http://localhost:8080/actuator/prometheus
echo.
echo 2. PROMETHEUS:
echo    http://localhost:9090
echo    Targets: http://localhost:9090/targets
echo.
echo 3. GRAFANA:
echo    http://localhost:3000
echo    Usuario: admin
echo    Contraseña: admin
echo.
echo ========================================
echo   CONFIGURACION MANUAL EN GRAFANA
echo ========================================
echo.
echo 1. Abre http://localhost:3000
echo 2. Inicia sesion con admin/admin
echo 3. Ve a Configuration -> Data Sources
echo 4. Agrega Prometheus
echo 5. En URL usa: http://host.docker.internal:9090
echo 6. Guarda y prueba
echo.
pause