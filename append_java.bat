@echo off
setlocal enabledelayedexpansion

REM Set the text you want to append before the extension
set "append=Service"

for /R %%f in (*.java) do (
    set "filename=%%~nxf"
    echo !filename! | findstr /I "Controller Repository Model Service" >nul
    if errorlevel 1 (
        set "name=%%~nf"
        set "ext=%%~xf"
        ren "%%f" "!name!!append!!ext!"
        echo Renamed: %%~nxf -> !name!!append!!ext!
    )
)

endlocal
