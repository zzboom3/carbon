# 启动步骤（常用）

项目根目录：`d:\desktop\Carbon_manage_system\carbon_official`

## 最简一键启动（推荐）

```powershell
cd d:\desktop\Carbon_manage_system\carbon_official
powershell -ExecutionPolicy Bypass -File ".\start-all.ps1"
```

MySQL（本项目默认使用虚拟机 Docker MySQL：`192.168.100.128:3333`，root/root）：

首次启动前端会自动执行 `npm install`，可能需要几分钟；可查看 `carbon_front\logs\frontend.out.log / frontend.err.log`。

如果端口被占用（9528/9001/9002/9003/9005/9091），建议“以管理员身份运行 PowerShell”再执行一键脚本（脚本会尝试自动结束占用端口的进程）。

如果你要启用“注册/忘记密码邮箱验证码”，需要在同一个 PowerShell 窗口里先设置邮箱环境变量，然后再启动（不要把授权码写进仓库文件）：

```powershell
$env:CARBON_MAIL_USERNAME="你的邮箱地址"
$env:CARBON_MAIL_PASSWORD="你的SMTP授权码"
powershell -ExecutionPolicy Bypass -File ".\start-all.ps1"
```

也可以在项目根目录创建一个仅本机使用的脚本（不会进版本库）：

```powershell
copy carbon_back\tools\mail.local.ps1.example carbon_back\tools\mail.local.ps1
notepad carbon_back\tools\mail.local.ps1
```

把你的邮箱和授权码填进去。启动时脚本会自动读取该文件并注入到进程环境。

## 0. 必备环境

- JDK 已安装并可用（`java -version`）
- Node.js + npm 已安装并可用（`node -v`、`npm -v`）

依赖中间件使用固定虚拟机：
- Nacos：`192.168.100.128:8848`
- Redis：`192.168.100.128:6379`
- RocketMQ namesrv：`192.168.100.128:9876`

MySQL 二选一：
- 本机 MySQL 服务 `MySQL80`（端口 3306，账号/密码 root/root）
- 或虚拟机 Docker MySQL（`192.168.100.128:3333`，账号/密码 root/root）

## 1.（首次/异常时）重置本机 MySQL root 密码为 root

如果后端启动日志出现 `Access denied for user 'root'@'localhost'`，执行以下命令（会重置 root 密码为 root）：

```powershell
以“管理员身份运行 PowerShell”

cd d:\desktop\Carbon_manage_system\carbon_official

Stop-Service MySQL80

$mysqld = "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysqld.exe"
$defaultsFile = "C:\ProgramData\MySQL\MySQL Server 8.0\my.ini"
$initFile = "d:\desktop\Carbon_manage_system\carbon_official\carbon_back\tools\mysql-reset-root.sql"

$p = Start-Process -FilePath $mysqld -ArgumentList @(
  "--defaults-file=$defaultsFile",
  "--init-file=$initFile",
  "--console"
) -PassThru

Start-Sleep -Seconds 6
Stop-Process -Id $p.Id -Force
Start-Service MySQL80
```

也可以直接一键脚本（推荐）：

```powershell
以“管理员身份运行 PowerShell”
cd d:\desktop\Carbon_manage_system\carbon_official
powershell -ExecutionPolicy Bypass -File "carbon_back\tools\bootstrap-mysql.ps1"
```

## 2.（首次/换环境时）导入数据库

```powershell
cd d:\desktop\Carbon_manage_system\carbon_official

$mysql = "C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe"
& $mysql "-hlocalhost" "-P3306" "-uroot" "-proot" "-e" "CREATE DATABASE IF NOT EXISTS carbon DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;"
Get-Content -Raw "carbon_back\carbon.sql" | & $mysql "-hlocalhost" "-P3306" "-uroot" "-proot" "carbon"
```

如果你用的是虚拟机 Docker MySQL（端口 3333），把 `-h/-P` 换成：

```powershell
& $mysql "-h192.168.100.128" "-P3333" "-uroot" "-proot" "-e" "CREATE DATABASE IF NOT EXISTS carbon DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;"
Get-Content -Raw "carbon_back\carbon.sql" | & $mysql "-h192.168.100.128" "-P3333" "-uroot" "-proot" "carbon"
```

## 3. 后端：安装公共依赖（只需一次）

```powershell
cd d:\desktop\Carbon_manage_system\carbon_official\carbon_back\carbon-frame
..\apache-maven-3.8.8\bin\mvn.cmd -DskipTests install
```

## 4. 后端：一键启动（5 个服务）

```powershell
cd d:\desktop\Carbon_manage_system\carbon_official
.\carbon_back\tools\start-backend.ps1
```

后端端口：
- auth：9001
- system：9002
- assets：9003
- trade：9005
- gate：9091

日志目录：`carbon_back\logs\*.out.log / *.err.log`

## 5. 前端：一键启动

```powershell
cd d:\desktop\Carbon_manage_system\carbon_official
powershell -ExecutionPolicy Bypass -File "carbon_front\tools\start-frontend.ps1"
```

前端地址：`http://localhost:9528/`
