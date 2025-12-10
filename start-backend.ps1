# Start Backend Spring Boot Server
Write-Host "Starting Rocket Pop SSO Backend..." -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Backend API: http://localhost:42068" -ForegroundColor Green
Write-Host "Health Check: http://localhost:42068/ping" -ForegroundColor Green
Write-Host "H2 Console: http://localhost:42068/h2-console" -ForegroundColor Yellow
Write-Host ""
Write-Host "Sample Users:" -ForegroundColor Magenta
Write-Host "  Admin    - username: admin      password: admin123" -ForegroundColor White
Write-Host "  User     - username: testuser   password: user123" -ForegroundColor White
Write-Host "  Manager  - username: manager1   password: manager123" -ForegroundColor White
Write-Host ""

Set-Location -Path $PSScriptRoot
.\mvnw.cmd spring-boot:run
