# Historical graduation project: initialize a NEW demo database only.
# Configure credentials locally using mysql_config_editor; never commit them.
param(
    [string]$MySQLExe = 'mysql',
    [string]$LoginPath = 'straw-archive'
)

$ErrorActionPreference = 'Stop'
$OutputEncoding = [Console]::OutputEncoding = [Text.Encoding]::UTF8
$DBName = 'straw_management'
$SQLDir = Join-Path $PSScriptRoot 'sql'
$SQLFiles = @(
    'schema.sql',
    'migration.sql',
    'test_users.sql',
    'sample_data.sql',
    'additional_data.sql',
    'notification_migration.sql',
    'faq_migration.sql',
    'enterprise_role.sql'
)

if (-not (Get-Command $MySQLExe -ErrorAction SilentlyContinue)) {
    throw 'MySQL client not found. Add it to PATH or specify -MySQLExe.'
}
foreach ($name in $SQLFiles) {
    if (-not (Test-Path -LiteralPath (Join-Path $SQLDir $name))) {
        throw "Missing SQL file: $name"
    }
}

# --login-path reads credentials from the local MySQL client configuration.
$mysqlArgs = @("--login-path=$LoginPath", '--default-character-set=utf8mb4', '--batch')
$checkSql = "SELECT COUNT(*) FROM information_schema.SCHEMATA WHERE SCHEMA_NAME = '$DBName';"
$existing = & $MySQLExe @mysqlArgs --skip-column-names --execute=$checkSql
if ($LASTEXITCODE -ne 0) { throw 'Connection failed. Check the local MySQL login path.' }
if ((($existing -join '').Trim()) -ne '0') {
    throw "Database $DBName already exists. No changes made. Use a fresh demo environment."
}

$confirm = Read-Host "Create NEW demo database $DBName and import demo data? Type CREATE to continue"
if ($confirm -cne 'CREATE') { Write-Host 'Cancelled.'; exit 0 }

# One client session keeps USE from schema.sql active for all subsequent scripts.
$sqlParts = foreach ($name in $SQLFiles) {
    Get-Content -LiteralPath (Join-Path $SQLDir $name) -Raw -Encoding UTF8
}
($sqlParts -join "`n`n") | & $MySQLExe @mysqlArgs
if ($LASTEXITCODE -ne 0) {
    throw 'Import failed. Partial data may remain; inspect the demo database before retrying.'
}
Write-Host 'Demo database initialized. See the startup guide for demo accounts.'
