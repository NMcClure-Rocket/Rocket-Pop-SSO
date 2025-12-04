# Start both Frontend and Backend servers
Write-Host "Starting Rocket Pop SSO - Full Stack" -ForegroundColor Magenta
Write-Host "========================================" -ForegroundColor Magenta
Write-Host ""

# Start backend in a new window
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$PSScriptRoot'; .\start-backend.ps1"

# Wait a moment for backend to start
Start-Sleep -Seconds 3

# Start frontend in a new window
Start-Process powershell -ArgumentList "-NoExit", "-Command", "cd '$PSScriptRoot'; .\start-frontend.ps1"

Write-Host ""
Write-Host "✓ Backend starting on http://localhost:42068" -ForegroundColor Green
Write-Host "✓ Frontend starting on http://localhost:42067" -ForegroundColor Green
Write-Host ""
Write-Host "Both servers will open in separate windows." -ForegroundColor Yellow
Write-Host "Access the application at: http://localhost:42067" -ForegroundColor Cyan
