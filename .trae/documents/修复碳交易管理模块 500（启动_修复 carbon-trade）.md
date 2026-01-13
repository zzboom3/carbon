## 现象定位
- 碳交易管理模块页面首屏会自动请求 `/trade/**` 接口（例如：
  - 供需行情：`POST /trade/carbonTradeQuote/getPageList`（buy/sell tab mounted 都会调）
  - 询报价：`POST /trade/carbonTradePrice/getPageList`
  这些在前端路由 [carbonDealManagement.js](file:///d:/desktop/Carbon_manage_system/carbon_official/carbon_front/src/router/modules/carbonDealManagement.js) 和页面组件中可见。
- 网关已配置把 `/trade/**` 转发到 `serviceId: carbon-trade`（[gate application.yml](file:///d:/desktop/Carbon_manage_system/carbon_official/carbon_back/carbon-gate/src/main/resources/application.yml#L63-L69)）。
- `carbon-trade` 服务自身端口是 `9005`，context-path 是 `/trade`（[trade application.yml](file:///d:/desktop/Carbon_manage_system/carbon_official/carbon_back/carbon-trade/src/main/resources/application.yml#L1-L6)）。
- 因此 500 的高概率根因是：**carbon-trade 没跑起来/未注册到 Nacos，或运行中因数据库/ES/RocketMQ 依赖报错导致接口 500**（其它模块正常说明网关/鉴权/系统/资产链路已基本恢复）。

## 修复目标（只动碳交易模块）
- `/trade` 相关页面打开不再 500：供需行情、询报价、交易账户/交易所等页面能够正常拉取列表。

## 实施步骤（你确认后我将立即执行）
1. **先把 carbon-trade 服务跑起来并确认被网关发现**
   - 启动 `carbon_back/carbon-trade`（端口 9005，context `/trade`）。
   - 验证网关侧：`GET http://localhost:9091/trade/v2/api-docs` 返回 200。

2. **对照前端首屏接口逐个验证**
   - `POST /trade/carbonTradeQuote/getPageList`（采购/供应）
   - `POST /trade/carbonTradePrice/getPageList`
   - 如果仍 500：抓取 `carbon-trade` 的报错栈，精确定位是 DB 表/字段、ES、RocketMQ 还是参数校验。

3. **只在 carbon-trade 内做最小修复（按报错类型选择）**
   - 若是“服务未注册/路由不到”：修复 nacos 配置或启动参数。
   - 若是“数据库异常/表缺失/字段不匹配”：只修 mapper/entity/sql（trade 模块内），确保分页查询可跑。
   - 若是“ES/RocketMQ 依赖导致接口失败”：把相关依赖改为可降级（仅对 search/异步能力降级，不影响列表查询接口）。

4. **回归确认**
   - 在前端依次打开：供需行情、询报价、交易账户、交易所管理，确认 Network 无 500。

## 交付物
- carbon-trade 服务可稳定启动。
- `/trade` 模块页面不再报 `Request failed with status code 500`。
- 附带一组最小冒烟请求，确保后续不回归。