# Start both Frontend and Backend servers
Write-Host "Starting Rocket Pop SSO - Full Stack" -ForegroundColor Magenta
Write-Host "========================================" -ForegroundColor Magenta
Write-Host ""

# Start backend in a new window
Write-Host "Starting Backend Server..." -ForegroundColor Yellow
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$PSScriptRoot'; .\start-backend.ps1"

# Wait for backend to start
Write-Host "Waiting for backend to initialize..." -ForegroundColor Yellow
Start-Sleep -Seconds 5

# Start frontend in a new window
Write-Host "Starting Frontend Server..." -ForegroundColor Yellow
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$PSScriptRoot'; .\start-frontend.ps1"

Write-Host ""
Write-Host "========================================" -ForegroundColor Magenta
Write-Host "✓ Both servers are starting..." -ForegroundColor Green
Write-Host ""
Write-Host "URLs:" -ForegroundColor Cyan
Write-Host "  Frontend:     http://localhost:42067" -ForegroundColor White
Write-Host "  Backend API:  http://localhost:42068" -ForegroundColor White
Write-Host "  Health Check: http://localhost:42068/ping" -ForegroundColor White
Write-Host "  H2 Console:   http://localhost:42068/h2-console" -ForegroundColor White
Write-Host ""
Write-Host "Sample Users:" -ForegroundColor Cyan
Write-Host "  Admin    - username: admin      password: admin123" -ForegroundColor White
Write-Host "  User     - username: testuser   password: user123" -ForegroundColor White
Write-Host "  Manager  - username: manager1   password: manager123" -ForegroundColor White
Write-Host ""
Write-Host "Both servers are running in separate windows." -ForegroundColor Yellow
Write-Host "Open http://localhost:42067 in your browser to access the app." -ForegroundColor Cyan
Write-Host ""
