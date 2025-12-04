@echo off
echo ========================================
echo   REPARACION COMPLETA PARA WINDOWS
echo ========================================

echo 1. Parando todo...
docker-compose down 2>nul
docker stop finli-prometheus finli-grafana 2>nul
docker rm finli-prometheus finli-grafana 2>nul

echo 2. Limpiando...
docker system prune -f

echo 3. Obteniendo IP local...
for /f "tokens=2 delims=:" %%i in ('ipconfig ^| findstr "IPv4" ^| findstr /v "192.168.0."') do set LOCAL_IP=%%i
set LOCAL_IP=%LOCAL_IP: =%
echo Tu IP local es: %LOCAL_IP%

echo 4. Actualizando configuracion...
(
echo global:
echo   scrape_interval: 15s
echo   evaluation_interval: 15s
echo.
echo scrape_configs:
echo   - job_name: 'finli-app'
echo     metrics_path: '/actuator/prometheus'
echo     static_configs:
echo       - targets: ['%LOCAL_IP%:8080']
echo         labels:
echo           application: 'FinLi Backend'
echo           environment: 'windows-local'
) > prometheus-fixed.yml

echo 5. Iniciando Prometheus...
start /B docker run -d --name finli-prometheus -p 9090:9090 -v %cd%\prometheus-fixed.yml:/etc/prometheus/prometheus.yml prom/prometheus:latest

echo 6. Iniciando Grafana...
start /B docker run -d --name finli-grafana -p 3000:3000 -e GF_SECURITY_ADMIN_PASSWORD=admin grafana/grafana:latest

echo.
echo Esperando 15 segundos...
timeout /t 15 /nobreak > nul

echo.
echo ========================================
echo   ¡CONFIGURACION COMPLETA!
echo ========================================
echo.
echo 1. Asegurate de que tu app Spring Boot este corriendo en:
echo    http://localhost:8080
echo.
echo 2. Accede a Grafana: http://localhost:3000
echo    Usuario: admin
echo    Contraseña: admin
echo.
echo 3. Configura el Data Source con URL:
echo    OPCION 1: http://%LOCAL_IP%:9090
echo    OPCION 2: http://localhost:9090
echo    OPCION 3: http://host.docker.internal:9090
echo.
echo 4. Importa dashboards con IDs: 11378 y 4701
echo.
pause