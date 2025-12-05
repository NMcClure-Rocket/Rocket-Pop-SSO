# Start Frontend Development Server
Write-Host "Starting Rocket Pop SSO Frontend..." -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Frontend: http://localhost:42067" -ForegroundColor Green
Write-Host "Backend API: http://localhost:42068" -ForegroundColor Yellow
Write-Host ""
Write-Host "Note: Make sure backend is running on port 42068" -ForegroundColor Yellow
Write-Host ""

Set-Location -Path "$PSScriptRoot\frontend"
npm run dev
