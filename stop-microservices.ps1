# Healthcare Microservices Shutdown Script
Write-Host "==========================================================" -ForegroundColor Yellow
Write-Host "       Stopping Healthcare Microservices Suite            " -ForegroundColor Yellow
Write-Host "==========================================================" -ForegroundColor Yellow

$ports = @(8761, 8080, 8081, 8082, 8085)

foreach ($port in $ports) {
    try {
        $tcpConnection = Get-NetTCPConnection -LocalPort $port -ErrorAction SilentlyContinue
        if ($tcpConnection) {
            $pidsToKill = $tcpConnection | Select-Object -ExpandProperty OwningProcess -Unique
            foreach ($p in $pidsToKill) {
                if ($p -gt 0) {
                    Write-Host "Stopping process on port $port (PID: $p)..." -ForegroundColor Red
                    Stop-Process -Id $p -Force -ErrorAction SilentlyContinue
                }
            }
        } else {
            Write-Host "Port $port is already free." -ForegroundColor Gray
        }
    } catch {
        Write-Host "Could not query port $port: $_" -ForegroundColor DarkGray
    }
}

Write-Host "All microservices stopped successfully!" -ForegroundColor Green
