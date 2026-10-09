param(
    [string]$JavaHome = (Join-Path $PSScriptRoot '..\.tools\jdk-17'),
    [string]$AndroidSdk = (Join-Path $PSScriptRoot '..\.tools\android-sdk'),
    [string]$DetektJar = (Join-Path $PSScriptRoot '..\.tools\detekt-cli-1.23.8-all.jar'),
    [string]$Python = 'python'
)
$ErrorActionPreference = 'Stop'
$projectRoot = [IO.Path]::GetFullPath((Join-Path $PSScriptRoot '..'))
$env:JAVA_HOME = [IO.Path]::GetFullPath($JavaHome)
$env:ANDROID_HOME = [IO.Path]::GetFullPath($AndroidSdk)
if (!(Test-Path -LiteralPath (Join-Path $env:JAVA_HOME 'bin\java.exe'))) { throw 'JDK 17 missing.' }
if (!(Test-Path -LiteralPath (Join-Path $env:ANDROID_HOME 'platforms\android-32\android.jar'))) { throw 'Android SDK platform 32 missing.' }
if (!(Test-Path -LiteralPath $DetektJar)) { throw 'Detekt CLI 1.23.8 jar missing.' }
Push-Location $projectRoot
try {
    New-Item -ItemType Directory -Force reports\static, reports\environment | Out-Null
    $ErrorActionPreference = 'Continue' # Capture native stderr; validate the exit code explicitly.
    & software\compose-tetris\gradlew.bat --project-dir software\compose-tetris :app:testDebugUnitTest :app:lintDebug :app:assembleDebug --no-daemon --console=plain 2>&1 | Tee-Object reports\environment\gradle-verification.log
    $ErrorActionPreference = 'Stop'
    if ($LASTEXITCODE -ne 0) { throw "Gradle failed ($LASTEXITCODE); no successful run recorded." }
    $ErrorActionPreference = 'Continue'
    & (Join-Path $env:JAVA_HOME 'bin\java.exe') -jar $DetektJar --input software/compose-tetris/app/src/main/java --build-upon-default-config --report xml:reports/static/detekt.xml --report html:reports/static/detekt.html --report txt:reports/static/detekt.txt --base-path . 2>&1 | Tee-Object reports\environment\detekt-run.log
    $ErrorActionPreference = 'Stop'
    # Detekt CLI 1.23.8: exit 2 means the report's findings exceed the threshold.
    # Execution/argument errors must not be treated as successful analysis.
    if ($LASTEXITCODE -notin @(0,2)) { throw "Detekt execution failed ($LASTEXITCODE)." }
    & $Python tools\collect_results.py
    if ($LASTEXITCODE -ne 0) { throw 'Result collection failed.' }
} finally {
    Pop-Location
}
