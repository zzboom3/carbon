## 问题结论
- `/sysTenant` 实际是“账户管理”页：[accountManager.vue](file:///d:/desktop/Carbon_manage_system/carbon_official/carbon_front/src/views/systemSetting/accountManager/accountManager.vue)。
- 新增用户成功后虽会调用 `getList(1)` 刷新，但会继续带着当前筛选条件（账户类型/状态/版本/关键词），如果新增用户不满足筛选，就会“新增了但列表不出现”。
- 分页组件同时传了 `:total` 和 `:page-count`（[accountManager.vue:L109-L119](file:///d:/desktop/Carbon_manage_system/carbon_official/carbon_front/src/views/systemSetting/accountManager/accountManager.vue#L109-L119)），会让分页展示看起来像“前端算死/写死”。

## 修复目标
- 新增用户后：列表能立即看到新增记录（至少在无筛选状态下）。
- 分页：只由后端返回的 `total` 驱动，不再由 `pageCount` 强行控制。

## 修改方案（仅改前端该页面）
1. **新增成功后强制回到“无筛选”并刷新第一页**
   - 在 `addSubmit()` 成功后清空 `searchKeyWord/selectedType/selectedStatus/selectedVersion`，然后 `getList(1)`。
   - 同时补上“角色必选”校验（避免 roleIds 为空但前端误判成功）。

2. **修复分页绑定方式**
   - 移除 `el-pagination` 的 `:page-count` 传参，保留 `:total/:page-size/:current-page`。
   - 同步删除 data 里的 `pageCount` 以及 `getList()` 里对 `pageCount` 的计算赋值。

3. **回归验证**
   - 在页面新增用户后确认：总数 `total` 增加、第一页列表出现新记录。
   - 改页大小/翻页后确认：分页展示与列表数据一致。

## 涉及文件
- [accountManager.vue](file:///d:/desktop/Carbon_manage_system/carbon_official/carbon_front/src/views/systemSetting/accountManager/accountManager.vue)