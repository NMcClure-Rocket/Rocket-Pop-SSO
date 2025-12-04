# Start Backend Spring Boot Server
Write-Host "Starting Rocket Pop SSO Backend..." -ForegroundColor Cyan
Write-Host "Backend will be available at http://localhost:42068" -ForegroundColor Green
Write-Host ""

Set-Location -Path $PSScriptRoot
.\mvnw.cmd spring-boot:run
