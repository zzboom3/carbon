# Carbon 管理系统（Carbon Manage System）

面向“碳资产开发、碳资产管理、碳交易管理”的一体化后台管理平台，采用前后端分离与微服务架构，通过网关统一对外提供 API，并支持消息驱动流程与区块链存证能力。

> 本仓库包含完整前端（`carbon_front`）与后端微服务（`carbon_back`）代码。

## 功能概览

- 碳资产管理：碳配额、碳信用、资产详情、资产评估、数据记录（新增/查看/编辑）
- 碳资产开发：项目立项/实施/进度管理、项目文档、CCER 申请项目、方法学管理、注册表/项目库
- 碳交易管理：交易所/交易账户、询报价、供需行情、合同/履约（按服务实现）
- 资讯与运营：碳资讯、碳盘查、客户管理（CRM）
- 系统管理：租户/账号/角色/菜单/字典/权限、审批、OSS、数据看板等

更详细的模块清单与路由/接口映射见：[项目功能文档.md](./项目功能文档.md)

## 架构与模块

### 前端

- Vue 2 + Element UI（基于 vue-element-admin 风格）
- 路由：`carbon_front/src/router`
- API：`carbon_front/src/api`

### 后端（微服务）

后端采用 Spring Boot + MyBatis-Plus，主要服务如下（默认端口以启动脚本为准）：

| 服务 | 端口 | 网关前缀 | 说明 |
|---|---:|---|---|
| carbon-auth | 9001 | `/authCenter` | 登录/注册/找回、微信登录、登录态校验 |
| carbon-system | 9002 | `/system` | 菜单/权限/角色/字典/租户、资讯、审批、OSS、看板等 |
| carbon-assets | 9003 | `/assets` | 项目/方法学/注册表、碳配额/碳信用、中和资产等 |
| carbon-trade | 9005 | `/trade` | 行情/报价、价格、合同/履约、行情检索等 |
| carbon-gate | 9091 | 网关入口 | Zuul 路由转发、跨域/过滤、AI 接口 |
| carbon-mq | 9006 | - | RocketMQ/Canal/飞书等消息消费与异步流程 |
| carbon-chainmaker | 9007 | - | ChainMaker 区块链交互（上链/查询/存证） |

网关路由配置位于：`carbon_back/carbon-gate/src/main/resources/application.yml`

### 依赖组件（按需启用）

- MySQL：核心业务数据存储
- Redis：缓存/会话/验证码等
- Nacos：服务注册与配置中心
- RocketMQ：异步消息与流程驱动
- Elasticsearch：方法学/注册表/行情检索（未启动可能仅产生告警，不一定影响 CRUD）
- ChainMaker：区块链存证与查询（按业务流程启用）

## 目录结构

```text
.
├─ carbon_front/                 # 前端（Vue2）
├─ carbon_back/                  # 后端（Spring Boot 微服务）
│  ├─ carbon-auth/               # 认证中心
│  ├─ carbon-system/             # 系统管理
│  ├─ carbon-assets/             # 碳资产/项目/方法学
│  ├─ carbon-trade/              # 碳交易
│  ├─ carbon-gate/               # 网关
│  ├─ carbon-mq/                 # 消息消费/异步流程
│  ├─ carbon-chainmaker/         # 区块链交互
│  └─ tools/                     # 启动与本地配置脚本
├─ start-all.ps1                 # 一键启动（Windows/PowerShell）
└─ 项目功能文档.md               # 功能与模块映射说明
```

## 快速开始（Windows）

### 1) 环境要求

- JDK 8（后端 `java.version=1.8`）
- Maven（仓库内可能已自带，具体以 `carbon_back` 工具脚本为准）
- Node.js（前端 `package.json` 声明 `node >= 8.9`，建议使用更高版本以获得更好兼容性）
- 可用的基础设施：MySQL / Redis / Nacos / RocketMQ（按你的部署环境配置地址与端口）

### 2) 一键启动

仓库根目录执行（PowerShell）：

```powershell
.\start-all.ps1
```

如需改为本机/容器地址，建议显式传参（示例）：

```powershell
.\start-all.ps1 `
  -MySqlHost "127.0.0.1" -MySqlPort "3306" -MySqlDb "carbon" -MySqlUser "root" -MySqlPassword "********" `
  -RedisHost "127.0.0.1" -RedisPort "6379" `
  -NacosAddr "127.0.0.1:8848" `
  -RocketMqNamesrv "127.0.0.1:9876"
```

启动完成后默认访问：

- 前端：`http://localhost:9528/`
- 网关：`http://localhost:9091/`

### 3) 手动启动（可选）

前端：

```powershell
cd .\carbon_front
npm install
npm run dev
```

后端（示例：启动 carbon-assets）：

```powershell
cd .\carbon_back
.\tools\start-backend.ps1 -Services "carbon-assets"
```

## 权限与菜单说明

- 侧边栏菜单来自后端：`POST /system/sysMenu/userMenus`
- 后端菜单字段 `menuUrl` 与前端路由 `path` 匹配后生成可见菜单

## 常见问题

- **启动时出现 Elasticsearch 连接告警**：如本地未部署 ES，部分检索能力不可用，但通常不影响核心 CRUD；如需检索功能请部署 ES 并配置连接。
- **端口被占用**：建议以管理员权限运行 PowerShell，或手动释放占用端口后再启动。
- **环境参数不一致**：请优先通过 `start-all.ps1` 的参数覆盖默认地址，避免脚本内默认值与本机环境不一致。

## 文档

- [项目功能文档.md](./项目功能文档.md)：模块功能、前端路由与后端服务/接口对照

## 贡献

欢迎以 Issue / PR 的方式提交改进建议（功能完善、文档补充、稳定性与可维护性优化等）。

## License

当前仓库未附带 LICENSE 文件。如需开源发布，建议补充合适的开源协议（如 Apache-2.0 / MIT 等）并在 README 中声明。

