@echo off
echo ========================================
echo   MusicFlow Backend 启动脚本
echo ========================================

REM 检查Java是否安装
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo 错误: 未找到Java，请先安装Java 17或更高版本
    pause
    exit /b 1
)

REM 检查Maven是否安装
mvn -version >nul 2>&1
if %errorlevel% neq 0 (
    echo 错误: 未找到Maven，请先安装Maven 3.6或更高版本
    pause
    exit /b 1
)

echo 正在启动MusicFlow后端服务...
echo.

REM 编译并启动应用
mvn clean spring-boot:run

if %errorlevel% neq 0 (
    echo.
    echo 启动失败，请检查错误信息
    pause
    exit /b 1
)

pause