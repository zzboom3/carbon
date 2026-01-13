# 后端不完整实现清单（自动扫描）

## 说明

本清单用于定位“接口存在但功能缺失/占位/断点”的代码位置，优先级以页面能用为准。

## carbon-assets

### 区块链/上链断点

- carbon_back/carbon-assets/src/main/java/com/carbon/assets/service/impl/CarbonBlockChainMsgProduce.java
  - syncAddCarbonOffset：真实调用被注释/返回 null
  - registerAdmin/registerUser/addCarbonQuotas/addCarbonOffset：空实现
  - queryAssetsTokenList/queryAssetsTokenInfo：返回 null

### 项目文档/内容相关空实现

- carbon_back/carbon-assets/src/main/java/com/carbon/assets/service/impl/CarbonMetaregistryDocServiceImpl.java
  - uploadCarbonMetaRegistryDoc：空实现
  - isDocRegistration：空实现
- carbon_back/carbon-assets/src/main/java/com/carbon/assets/service/impl/CarbonMetaregistryDocContentServiceImpl.java
  - getMetaregistryContentByRefId：返回 null（且存在注释掉的实现）
- carbon_back/carbon-assets/src/main/java/com/carbon/assets/controller/CarbonMetaregistryEsController.java
  - getByRef：查不到时直接 ApiResult.ok() 且注释掉补内容逻辑

### 统计接口占位

- carbon_back/carbon-assets/src/main/java/com/carbon/assets/controller/CarbonCreditAssetsController.java
  - assetsTotal：固定返回 0

## carbon-system

### H5 文章/搜索服务缺失

- carbon_back/carbon-system/src/main/java/com/carbon/system/controller/CarbonH5ArticleController.java
  - 多个端点已暴露
- carbon_back/carbon-system/src/main/java/com/carbon/system/service/impl/CarbonH5ArticleServiceImpl.java
  - getCarbonH5ArticleById/searchByKeyword/searchMethodologyByKeyword/searchArticleByKeyword：均返回 null（且存在注释掉的实现）

## carbon-auth

### 权限校验缺失

- carbon_back/carbon-auth/src/main/java/com/carbon/auth/service/impl/LoginServiceImpl.java
  - checkPermission：几乎只校验登录，未按 URL 做权限匹配

