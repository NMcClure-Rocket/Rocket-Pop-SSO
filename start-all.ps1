# Start both Frontend and Backend servers
Write-Host "Starting Rocket Pop SSO - Full Stack" -ForegroundColor Magenta
Write-Host "========================================" -ForegroundColor Magenta
Write-Host ""

# Start backend as a background job
Write-Host "Starting Backend Server..." -ForegroundColor Yellow
$backendJob = Start-Job -ScriptBlock {
    Set-Location $using:PSScriptRoot
    .\mvnw.cmd spring-boot:run
}
Write-Host "Backend job started (Job ID: $($backendJob.Id))" -ForegroundColor Gray

# Wait for backend to start
Write-Host "Waiting for backend to initialize..." -ForegroundColor Yellow
Start-Sleep -Seconds 15

# Start frontend as a background job
Write-Host "Starting Frontend Server..." -ForegroundColor Yellow
$frontendJob = Start-Job -ScriptBlock {
    Set-Location "$using:PSScriptRoot\frontend"
    npm run dev
}
Write-Host "Frontend job started (Job ID: $($frontendJob.Id))" -ForegroundColor Gray

Write-Host ""
Write-Host "========================================" -ForegroundColor Magenta
Write-Host "Both servers are running as background jobs!" -ForegroundColor Green
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
Write-Host "Job Management:" -ForegroundColor Cyan
Write-Host "  View jobs:        Get-Job" -ForegroundColor White
Write-Host "  Stop backend:     Stop-Job -Id $($backendJob.Id)" -ForegroundColor White
Write-Host "  Stop frontend:    Stop-Job -Id $($frontendJob.Id)" -ForegroundColor White
Write-Host "  Stop all jobs:    Get-Job | Stop-Job" -ForegroundColor White
Write-Host "  View output:      Receive-Job -Id <JobId> -Keep" -ForegroundColor White
Write-Host ""
Write-Host "Open http://localhost:42067 in your browser to access the app." -ForegroundColor Cyan
Write-Host ""
