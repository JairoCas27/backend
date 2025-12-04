@echo off
echo ========================================
echo   GENERANDO TRAFICO DE PRUEBA
echo ========================================

echo Generando 20 peticiones de prueba...

for /l %%i in (1,1,20) do (
    echo Peticion %%i:
    
    echo Probando login...
    curl -s -o response.txt -w "Status: %%{http_code}, Time: %%{time_total}s\n" ^
        http://localhost:8080/api/auth/login ^
        -H "Content-Type: application/json" ^
        -d "{\"email\":\"test%%i@test.com\",\"contrasena\":\"test123\"}"
    
    type response.txt
    echo.
    
    timeout /t 1 /nobreak > nul
)

del response.txt 2>nul
echo.
echo ✓ Trafico generado
echo Verifica las metricas en: http://localhost:9090/graph
pause