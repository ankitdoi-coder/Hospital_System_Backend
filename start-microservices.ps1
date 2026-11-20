# Healthcare Microservices Launch Script
Write-Host "==========================================================" -ForegroundColor Cyan
Write-Host "       Starting Healthcare Microservices Suite            " -ForegroundColor Cyan
Write-Host "==========================================================" -ForegroundColor Cyan

# 1. Start Eureka Server (Port 8761)
Write-Host "[1/4] Starting Eureka Server (Port 8761)..." -ForegroundColor Green
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd 'eureka-server'; mvn spring-boot:run"
Start-Sleep -Seconds 15

# 2. Start API Gateway (Port 8080)
Write-Host "[2/4] Starting API Gateway (Port 8080)..." -ForegroundColor Green
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd 'api-gateway'; mvn spring-boot:run"
Start-Sleep -Seconds 10

# 3. Start User Service (Port 8081)
Write-Host "[3/4] Starting User Service (Port 8081)..." -ForegroundColor Green
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd 'user-service'; mvn spring-boot:run"
Start-Sleep -Seconds 10

# 4. Start Appointment Service Instance 1 (Port 8082)
Write-Host "[4/4] Starting Appointment Service Instance 1 (Port 8082)..." -ForegroundColor Green
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd 'appointment-service'; mvn spring-boot:run"

# Optional: Start Appointment Service Instance 2 (Port 8085) for Load Balancing Demo
Write-Host "[BONUS] Starting Appointment Service Instance 2 (Port 8085) for Load Balancing..." -ForegroundColor Yellow
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd 'appointment-service'; mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8085"

Write-Host "All services launched! Eureka dashboard: http://localhost:8761" -ForegroundColor Cyan
Write-Host "API Gateway entry point: http://localhost:8080" -ForegroundColor Cyan
