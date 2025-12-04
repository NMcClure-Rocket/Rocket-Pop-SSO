# Start Frontend Development Server
Write-Host "Starting Rocket Pop SSO Frontend..." -ForegroundColor Cyan
Write-Host "Frontend will be available at http://localhost:42067" -ForegroundColor Green
Write-Host ""

Set-Location -Path "$PSScriptRoot\frontend"
npm run dev
