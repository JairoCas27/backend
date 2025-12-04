@echo off
echo ==================================================
echo EJECUTANDO APLICACIÓN LOCAL + DOCKER PARA MONITOREO
echo ==================================================

echo 1. Deteniendo servicios Docker previos...
docker-compose down 2>nul

echo 2. Iniciando Prometheus y Grafana...
docker run -d --name finli-prometheus -p 9090:9090 -v %CD%/prometheus-local.yml:/etc/prometheus/prometheus.yml prom/prometheus
docker run -d --name finli-grafana -p 3000:3000 -e GF_SECURITY_ADMIN_PASSWORD=admin grafana/grafana

echo 3. Creando configuración de Prometheus para localhost...
(
echo global:
echo   scrape_interval: 15s
echo.
echo scrape_configs:
echo   - job_name: 'finli-app-local'
echo     metrics_path: '/actuator/prometheus'
echo     static_configs:
echo       - targets: ['localhost:8080']
echo     scrape_interval: 10s
) > prometheus-local.yml

echo 4. Actualizando Prometheus con nueva configuración...
docker kill -s HUP finli-prometheus

echo 5. Iniciando aplicación FinLi localmente...
echo.
echo ==================================================
echo EJECUTA ESTE COMANDO EN UNA NUEVA TERMINAL:
echo ==================================================
echo.
echo cd %CD%
echo java -jar target/finli-backend-1.0.0.jar
echo.
echo ==================================================
echo DESPUÉS DE INICIAR LA APLICACIÓN:
echo ==================================================
echo.
echo 1. Verifica la aplicación: http://localhost:8080/actuator/health
echo 2. Verifica métricas: http://localhost:8080/actuator/prometheus
echo 3. Prometheus: http://localhost:9090
echo 4. Grafana: http://localhost:3000 (admin/admin)
echo.
echo En Grafana, configura Prometheus con URL: http://localhost:9090
echo.
pausecurl http://localhost:8080/actuator/health
