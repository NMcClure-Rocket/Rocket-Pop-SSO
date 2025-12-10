# Test Backend API Endpoints
Write-Host "Testing Rocket Pop SSO Backend API" -ForegroundColor Magenta
Write-Host "========================================" -ForegroundColor Magenta
Write-Host ""

$backendUrl = "http://localhost:42068"

# Test 1: Ping endpoint
Write-Host "Test 1: Checking if backend is online..." -ForegroundColor Cyan
try {
    $pingResponse = Invoke-RestMethod -Uri "$backendUrl/ping" -Method Get
    Write-Host "✓ Backend is online!" -ForegroundColor Green
    Write-Host "  Status: $($pingResponse.status)" -ForegroundColor White
    Write-Host "  Message: $($pingResponse.message)" -ForegroundColor White
    Write-Host ""
} catch {
    Write-Host "✗ Backend is not responding" -ForegroundColor Red
    Write-Host "  Make sure the backend is running on port 42068" -ForegroundColor Yellow
    Write-Host "  Run: .\start-backend.ps1" -ForegroundColor Yellow
    Write-Host ""
    exit 1
}

# Test 2: Login with admin user
Write-Host "Test 2: Testing login endpoint (admin user)..." -ForegroundColor Cyan
try {
    $loginBody = @{
        username = "admin"
        password = "admin123"
    } | ConvertTo-Json

    $loginResponse = Invoke-RestMethod -Uri "$backendUrl/login" -Method Post -Body $loginBody -ContentType "application/json"
    Write-Host "✓ Login successful!" -ForegroundColor Green
    Write-Host "  Token received (first 50 chars): $($loginResponse.token.Substring(0, [Math]::Min(50, $loginResponse.token.Length)))..." -ForegroundColor White
    Write-Host ""
    
    $adminToken = $loginResponse.token
    
    # Test 3: Get user info
    Write-Host "Test 3: Testing user info endpoint..." -ForegroundColor Cyan
    $headers = @{
        Authorization = "Bearer $adminToken"
    }
    $userInfoResponse = Invoke-RestMethod -Uri "$backendUrl/user/info" -Method Get -Headers $headers
    Write-Host "✓ User info retrieved!" -ForegroundColor Green
    Write-Host "  Username: $($userInfoResponse.username)" -ForegroundColor White
    Write-Host "  Email: $($userInfoResponse.email)" -ForegroundColor White
    Write-Host "  Title: $($userInfoResponse.title)" -ForegroundColor White
    Write-Host ""
    
    # Test 4: Get all users (admin endpoint)
    Write-Host "Test 4: Testing admin endpoint (get all users)..." -ForegroundColor Cyan
    $usersResponse = Invoke-RestMethod -Uri "$backendUrl/admin/user/getall" -Method Get -Headers $headers
    Write-Host "✓ Admin endpoint accessible!" -ForegroundColor Green
    Write-Host "  Total users in system: $($usersResponse.Count)" -ForegroundColor White
    Write-Host ""
    
} catch {
    Write-Host "✗ Login test failed" -ForegroundColor Red
    Write-Host "  Error: $($_.Exception.Message)" -ForegroundColor Yellow
    Write-Host ""
}

# Test 5: Login with regular user
Write-Host "Test 5: Testing login with regular user..." -ForegroundColor Cyan
try {
    $loginBody = @{
        username = "testuser"
        password = "user123"
    } | ConvertTo-Json

    $loginResponse = Invoke-RestMethod -Uri "$backendUrl/login" -Method Post -Body $loginBody -ContentType "application/json"
    Write-Host "✓ User login successful!" -ForegroundColor Green
    Write-Host "  Token received (first 50 chars): $($loginResponse.token.Substring(0, [Math]::Min(50, $loginResponse.token.Length)))..." -ForegroundColor White
    Write-Host ""
} catch {
    Write-Host "✗ User login test failed" -ForegroundColor Red
    Write-Host "  Error: $($_.Exception.Message)" -ForegroundColor Yellow
    Write-Host ""
}

Write-Host "========================================" -ForegroundColor Magenta
Write-Host "Backend API Testing Complete!" -ForegroundColor Green
Write-Host ""
Write-Host "Available Endpoints:" -ForegroundColor Cyan
Write-Host "  Public:" -ForegroundColor Yellow
Write-Host "    GET  /ping                              - Health check" -ForegroundColor White
Write-Host "    POST /login                             - User authentication" -ForegroundColor White
Write-Host ""
Write-Host "  User Endpoints (requires token):" -ForegroundColor Yellow
Write-Host "    GET  /user/info                         - Get user info" -ForegroundColor White
Write-Host "    POST /user/updatepassword               - Change password" -ForegroundColor White
Write-Host ""
Write-Host "  Admin Endpoints (requires admin token):" -ForegroundColor Yellow
Write-Host "    POST /admin/user/create                 - Create user/manager" -ForegroundColor White
Write-Host "    POST /admin/adminuser/create            - Create admin user" -ForegroundColor White
Write-Host "    PUT  /admin/user/edit                   - Edit user" -ForegroundColor White
Write-Host "    POST /admin/user/delete/{username}      - Delete user" -ForegroundColor White
Write-Host "    GET  /admin/user/getall?username=       - Get all users" -ForegroundColor White
Write-Host "    GET  /admin/user/get/{username}         - Get specific user" -ForegroundColor White
Write-Host ""
