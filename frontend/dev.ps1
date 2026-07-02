# CineStream frontend'i SADECE bu terminalde Node 20 ile calistirir.
# Global Node surumunu DEGISTIRMEZ; diger projelerin etkilenmez.
# Kullanim:  .\dev.ps1            (dev sunucusu)
#            .\dev.ps1 build      (baska bir npm script'i)

$nodeVersion = "v20.19.0"
$nodeDir = Join-Path $env:NVM_HOME $nodeVersion

if (-not (Test-Path (Join-Path $nodeDir "node.exe"))) {
    Write-Error "Node $nodeVersion bulunamadi ($nodeDir). Once kur: nvm install 20.19.0"
    exit 1
}

# node20'yi PATH'in basina ekle (yalnizca bu oturum icin)
$env:Path = "$nodeDir;$env:Path"
Write-Host "Node $(node -v) kullaniliyor (yalnizca bu terminal)" -ForegroundColor Cyan

$task = if ($args.Count -gt 0) { $args } else { @("run", "dev") }
& npm @task
