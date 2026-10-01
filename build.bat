@echo off
rem Builds PhytMod.jar from source. Requires a JDK (javac and jar) on the PATH.
setlocal
cd /d "%~dp0"

if exist build rmdir /s /q build
mkdir build

rem compile
javac -nowarn -encoding UTF-8 -cp lib\optimization.jar -d build src\lateblight\*.java || exit /b 1

rem copy resources (icons, help pages) next to the classes
xcopy /q /y src\lateblight\*.gif build\lateblight\ >nul
xcopy /q /y /i src\lateblight\docs build\lateblight\docs >nul

rem bundle the optimization library into the jar
pushd build
jar xf ..\lib\optimization.jar
if exist META-INF rmdir /s /q META-INF
popd

jar cfe PhytMod.jar lateblight.Phyt_model -C build . || exit /b 1
echo Built PhytMod.jar - run it with:  java -jar PhytMod.jar
