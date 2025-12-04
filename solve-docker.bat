@echo off
echo ==================================================
echo SOLUCIÓN DEFINITIVA PARA DOCKER
echo ==================================================

echo 1. Limpiando contenedores previos...
docker-compose down 2>nul
docker system prune -f 2>nul

echo 2. Probando conexión a Docker Hub...
docker pull openjdk:17-alpine

if %errorlevel% neq 0 (
    echo ERROR: No se puede conectar a Docker Hub
    echo Verifica tu conexión a Internet o configuración de Docker
    pause
    exit /b 1
)

echo 3. Creando Dockerfile simple...
(
echo # Dockerfile simple con imagen verificada
echo FROM openjdk:17-alpine
echo WORKDIR /app
echo COPY target/finli-backend-*.jar app.jar
echo EXPOSE 8080
echo ENTRYPOINT ["java", "-jar", "app.jar"]
) > Dockerfile-simple

echo 4. Creando docker-compose.yml...
(
echo services:
echo   finli-app:
echo     build:
echo       context: .
echo       dockerfile: Dockerfile-simple
echo     container_name: finli-app
echo     ports:
echo       - "8080:8080"
echo     environment:
echo       - SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/FinLiApp?sslMode=DISABLED^&serverTimezone=UTC^&allowPublicKeyRetrieval=true
echo       - SPRING_DATASOURCE_USERNAME=root
echo       - SPRING_DATASOURCE_PASSWORD=Kaiser2702
echo     extra_hosts:
echo       - "host.docker.internal:host-gateway"
echo     networks:
echo       - monitoring
echo     restart: unless-stopped
echo.
echo   prometheus:
echo     image: prom/prometheus:latest
echo     container_name: finli-prometheus
echo     volumes:
echo       - ./prometheus.yml:/etc/prometheus/prometheus.yml
echo     ports:
echo       - "9090:9090"
echo     networks:
echo       - monitoring
echo     depends_on:
echo       - finli-app
echo     restart: unless-stopped
echo.
echo   grafana:
echo     image: grafana/grafana:latest
echo     container_name: finli-grafana
echo     environment:
echo       - GF_SECURITY_ADMIN_PASSWORD=admin
echo     ports:
echo       - "3000:3000"
echo     networks:
echo       - monitoring
echo     depends_on:
echo       - prometheus
echo     restart: unless-stopped
echo.
echo networks:
echo   monitoring:
echo     driver: bridge
) > docker-compose.yml

echo 5. Creando prometheus.yml...
(
echo global:
echo   scrape_interval: 15s
echo   evaluation_interval: 15s
echo.
echo scrape_configs:
echo   - job_name: 'finli-app'
echo     metrics_path: '/actuator/prometheus'
echo     static_configs:
echo       - targets: ['host.docker.internal:8080']
echo     scrape_interval: 10s
) > prometheus.yml

echo 6. Construyendo aplicación FinLi...
echo (La aplicación ya está construida por start-all.bat)

echo 7. Construyendo imagen Docker...
docker-compose build finli-app

if %errorlevel% neq 0 (
    echo ERROR en la construcción de la imagen Docker
    echo.
    echo POSIBLES SOLUCIONES:
    echo 1. Verifica que Docker Desktop esté corriendo
    echo 2. Verifica tu conexión a Internet
    echo 3. Prueba cambiar la imagen a: eclipse-temurin:17-jre-alpine
    pause
    exit /b 1
)

echo 8. Iniciando servicios...
docker-compose up -d

echo 9. Esperando que servicios inicien...
timeout /t 30 /nobreak > nul

echo 10. Verificando servicios...
docker-compose ps

echo.
echo ==================================================
echo SOLUCIÓN COMPLETADA
echo ==================================================
echo.
echo URLs disponibles:
echo.
echo Aplicación FinLi:    http://localhost:8080
echo Health Check:        http://localhost:8080/actuator/health
echo Métricas:           http://localhost:8080/actuator/prometheus
echo Prometheus:         http://localhost:9090
echo Grafana:            http://localhost:3000 (admin/admin)
echo.
echo Para ver logs:      docker-compose logs -f finli-app
echo Para detener:       docker-compose down
echo.
pause