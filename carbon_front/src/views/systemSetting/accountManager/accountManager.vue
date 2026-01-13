<template>
  <div class="root">
    <div class="divBox">
      <div class="content-container">
        <div class="container">
          <el-row style="flex-grow: 1">
            <el-col :span="6">
              <div class="selectbox-root">
                <a class="selectbox-hint cursor-mi" style="width: 100px">账户类型</a>
                <div class="selectbox-deliver" />
                <el-cascader
                  style="width: 120px"
                  placeholder="全部"
                  class="selectbox-input"
                  v-model="selectedType"
                  :options="accountTypeDict"
                  clearable
                  @change="onClickSearch"
                />
              </div>
            </el-col>
            <el-col :span="5">
              <div style="margin-left: 16px" class="selectbox-root">
                <a class="selectbox-hint cursor-mi" style="width: 80px">状态</a>
                <div class="selectbox-deliver" />
                <el-cascader
                  placeholder="全部"
                  class="selectbox-input"
                  v-model="selectedStatus"
                  :options="accountStatusDict"
                  clearable
                  @change="onClickSearch"
                />
              </div>
            </el-col>
            <el-col :span="5">
              <div style="margin-left: 16px" class="selectbox-root">
                <a class="selectbox-hint cursor-mi" style="width: 110px">产品版本</a>
                <div class="selectbox-deliver" />
                <el-cascader
                  placeholder="全部"
                  class="selectbox-input"
                  v-model="selectedVersion"
                  :options="productVerssionDict"
                  clearable
                  @change="onClickSearch"
                />
              </div>
            </el-col>
            <el-col :span="8">
              <div style="margin-left: 10px; margin-right: 10px" class="selectbox-root">
                <a class="selectbox-hint cursor-mi" style="width: 140px">用户搜索</a>
                <div class="selectbox-deliver" />
                <el-input
                  v-model="searchKeyWord"
                  placeholder="输入用户名或公司名"
                  clearable
                  size="medium"
                  @clear="onClickSearch"
                  @keyup.enter.native="onClickSearch"
                />
              </div>
            </el-col>
          </el-row>
          <button class="light-green-btn" @click="onClickSearch">查询</button>
        </div>

        <div>
          <button style="margin-top: 0px; margin-bottom: 20px; width: 103px" class="normal-white-btn" @click="addUser">
            +新增用户
          </button>
          <el-table
            :header-cell-style="{
              background: '#F2F5F7',
              border: '0px solid #DDDDDD',
              color: '#242B35',
              height: '64px',
            }"
            :show-header="true"
            :data="list"
            :row-style="{ height: '64px' }"
            :cell-style="cellStyle"
            stripe
            style="width: 100%; cursor: default"
            @row-click="clickItem"
          >
            <el-table-column min-width="20" />
            <el-table-column label="用户账号" align="left" prop="accountName" min-width="120" />
            <el-table-column :show-overflow-tooltip="true" prop="username" label="用户名称" align="left" min-width="100" />
            <el-table-column align="left" prop="accountRole.roleNames[0]" label="用户角色" min-width="80" />
            <el-table-column align="left" prop="accountTypeName" label="账户类型" min-width="100" />
            <el-table-column align="left" prop="productVersionName" label="产品版本" min-width="100" />
            <el-table-column align="left" prop="accountStatusName" label="状态" min-width="60" />
            <el-table-column align="left" prop="createdTime" label="开户时间" min-width="120" />
            <el-table-column align="left" prop="validityPeriod" label="账户有效期" min-width="120" />
            <el-table-column align="left" prop="remarks" label="备注" min-width="60" />
            <el-table-column label="操作" min-width="150" align="center">
              <template slot-scope="scope">
                <a class="list-blue-text" @click.stop="onEdit(scope.row)">编辑</a>
                <a style="margin-left: 10px" class="list-red-text" @click.stop="onClickDelete(scope.row.id)">删除</a>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>

      <div style="margin-top: 30px; margin-bottom: 10px" class="pageBox">
        <div style="flex-grow: 1" />
        <el-pagination
          style="margin: auto"
          background
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="current"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
        />
      </div>

      <el-dialog top="20px" title="用户信息" :visible.sync="readFormVisible" width="50%">
        <el-row>
          <el-col :span="4"><span class="table-text">用户账号:<span style="color: red">*</span></span></el-col>
          <el-col :span="8">
            <el-input v-model="addForm.accountName" autocomplete="off" disabled size="medium" />
          </el-col>
          <el-col :span="4"><span style="margin-left: 10px" class="table-text">用户姓名:</span></el-col>
          <el-col :span="8">
            <el-input v-model="addForm.username" autocomplete="off" disabled size="medium" />
          </el-col>
          <div style="clear: both; height: 20px" />
          <el-col :span="4"><span class="table-text">联系电话:<span style="color: red">*</span></span></el-col>
          <el-col :span="8">
            <el-input v-model="addForm.phone" autocomplete="off" size="medium" disabled />
          </el-col>
          <el-col :span="4"><span style="margin-left: 10px" class="table-text">电子邮箱:</span></el-col>
          <el-col :span="8">
            <el-input v-model="addForm.email" autocomplete="off" disabled size="medium" />
          </el-col>
          <div style="clear: both; height: 20px" />
          <el-col :span="4"><span class="table-text">登录密码:</span></el-col>
          <el-col :span="8">
            <el-input v-model="password" type="password" autocomplete="off" disabled size="medium" />
          </el-col>
          <el-col :span="4"><span style="margin-left: 10px" class="table-text">确认密码:</span></el-col>
          <el-col :span="8">
            <el-input v-model="addForm.password" type="password" autocomplete="off" disabled size="medium" />
          </el-col>
          <div style="clear: both; height: 20px" />
          <el-col :span="4"><span class="table-text">用户角色:<span style="color: red">*</span></span></el-col>
          <el-col :span="8">
            <el-select v-model="selectedRole" placeholder="请选择角色" size="medium" disabled style="width: 100%">
              <el-option v-for="(item, index) in roleOptions" :key="index" :label="item.label" :value="item.value" />
            </el-select>
          </el-col>
          <el-col :span="4"><span style="margin-left: 10px" class="table-text">所属租户:</span></el-col>
          <el-col :span="8">
            <el-input v-model="tenant.tenantName" autocomplete="off" disabled size="medium" />
          </el-col>
          <div style="clear: both; height: 20px" />
          <el-col :span="4"><span class="table-text">所属行业:</span></el-col>
          <el-col :span="8">
            <el-input v-model="tenant.industry" disabled autocomplete="off" size="medium" />
          </el-col>
          <el-col :span="4"><span style="margin-left: 10px" class="table-text">企业地址:</span></el-col>
          <el-col :span="8">
            <el-input v-model="tenant.address" autocomplete="off" disabled size="medium" />
          </el-col>
          <div style="clear: both; height: 20px" />
          <el-col :span="4"><span class="table-text">企业电话:</span></el-col>
          <el-col :span="8">
            <el-input v-model="tenant.telephone" autocomplete="off" disabled size="medium" />
          </el-col>
          <el-col :span="4"><span style="margin-left: 10px" class="table-text">企业传真:</span></el-col>
          <el-col :span="8">
            <el-input v-model="tenant.faxNumber" autocomplete="off" disabled size="medium" />
          </el-col>
          <div style="clear: both; height: 20px" />
          <el-col :span="4"><span class="table-text">账户类型:<span style="color: red">*</span></span></el-col>
          <el-col :span="8">
            <el-select v-model="addForm.accountType" placeholder="" size="medium" style="width: 100%" disabled>
              <el-option v-for="(item, index) in accountTypeDict" :key="index" :label="item.label" :value="item.value" />
            </el-select>
          </el-col>
          <el-col :span="4"><span style="margin-left: 10px" class="table-text">产品版本:<span style="color: red">*</span></span></el-col>
          <el-col :span="8">
            <el-select v-model="addForm.productVersion" placeholder="" disabled size="medium" style="width: 100%">
              <el-option v-for="(item, index) in productVerssionDict" :key="index" :label="item.label" :value="item.value" />
            </el-select>
          </el-col>
          <div style="clear: both; height: 20px" />
          <el-col :span="4"><span class="table-text">开户日期:</span></el-col>
          <el-col :span="8">
            <el-date-picker v-model="addForm.createdTime" type="datetime" placeholder="选择日期时间" align="right" style="width: 100%" disabled size="medium" />
          </el-col>
          <el-col :span="4"><span style="margin-left: 10px" class="table-text">账户有效日期:</span></el-col>
          <el-col :span="8">
            <el-date-picker v-model="addForm.validityPeriod" type="datetime" placeholder="选择日期时间" align="right" style="width: 100%" disabled size="medium" />
          </el-col>
          <div style="clear: both; height: 20px" />
          <el-col :span="4"><span class="table-text">账户状态:<span style="color: red">*</span></span></el-col>
          <el-col :span="8">
            <el-select v-model="addForm.accountStatusName" placeholder="启用/禁用" style="width: 100%" disabled size="medium">
              <el-option v-for="(item, index) in optionStatus" :label="item.label" :value="item.value" :key="index" />
            </el-select>
          </el-col>
          <el-col :span="4"><span style="margin-left: 10px" class="table-text">企业介绍:</span></el-col>
          <el-col :span="8">
            <el-input v-model="addForm.remarks" autocomplete="off" disabled size="medium" />
          </el-col>
        </el-row>
        <div slot="footer" class="dialog-footer" align="center">
          <el-button @click="readFormVisible = false" class="light-green-btn">确定</el-button>
        </div>
      </el-dialog>

      <el-dialog top="20px" title="编辑用户信息" :visible.sync="editFormVisible" width="50%">
        <el-row>
          <el-col :span="4"><span class="table-text">用户账号:<span style="color: red">*</span></span></el-col>
          <el-col :span="8">
            <el-input v-model="addForm.accountName" autocomplete="off" size="medium" />
          </el-col>
          <el-col :span="4"><span style="margin-left: 10px" class="table-text">用户姓名:</span></el-col>
          <el-col :span="8">
            <el-input v-model="addForm.username" autocomplete="off" size="medium" />
          </el-col>
          <div style="clear: both; height: 20px" />
          <el-col :span="4"><span class="table-text">联系电话:<span style="color: red">*</span></span></el-col>
          <el-col :span="8">
            <el-input v-model="addForm.phone" autocomplete="off" size="medium" />
          </el-col>
          <el-col :span="4"><span style="margin-left: 10px" class="table-text">电子邮箱:</span></el-col>
          <el-col :span="8">
            <el-input v-model="addForm.email" autocomplete="off" size="medium" />
          </el-col>
          <div style="clear: both; height: 20px" />
          <el-col :span="4"><span class="table-text">登录密码:</span></el-col>
          <el-col :span="8">
            <el-input v-model="password" type="password" autocomplete="off" size="medium" />
          </el-col>
          <el-col :span="4"><span style="margin-left: 10px" class="table-text">确认密码:</span></el-col>
          <el-col :span="8">
            <el-input v-model="addForm.password" type="password" autocomplete="off" @blur="onInputBlur" size="medium" />
          </el-col>
          <el-col :span="20">
            <div style="text-align: right; color: red" v-if="passwordTip">输入密码不一致</div>
          </el-col>
          <div style="clear: both; height: 20px" />
          <el-col :span="4"><span class="table-text">用户角色:<span style="color: red">*</span></span></el-col>
          <el-col :span="8">
            <el-select v-model="selectedRole" placeholder="请选择角色" size="medium" style="width: 100%">
              <el-option v-for="(item, index) in roleOptions" :key="index" :label="item.label" :value="item.value" />
            </el-select>
          </el-col>
          <el-col :span="4"><span style="margin-left: 10px" class="table-text">所属租户:</span></el-col>
          <el-col :span="8">
            <el-select v-model="tenant.id" placeholder="请选择租户" size="medium" style="width: 100%" @change="updateTenant">
              <el-option v-for="(item, index) in tenantList" :key="index" :label="item.tenantName" :value="item.id" />
            </el-select>
          </el-col>
          <div style="clear: both; height: 20px" />
          <el-col :span="4"><span class="table-text">所属行业:</span></el-col>
          <el-col :span="8">
            <el-input v-model="tenant.industry" autocomplete="off" size="medium" />
          </el-col>
          <el-col :span="4"><span style="margin-left: 10px" class="table-text">企业地址:</span></el-col>
          <el-col :span="8">
            <el-input v-model="tenant.address" autocomplete="off" size="medium" />
          </el-col>
          <div style="clear: both; height: 20px" />
          <el-col :span="4"><span class="table-text">企业电话:</span></el-col>
          <el-col :span="8">
            <el-input v-model="tenant.telephone" autocomplete="off" size="medium" />
          </el-col>
          <el-col :span="4"><span style="margin-left: 10px" class="table-text">企业传真:</span></el-col>
          <el-col :span="8">
            <el-input v-model="tenant.faxNumber" autocomplete="off" size="medium" />
          </el-col>
          <div style="clear: both; height: 20px" />
          <el-col :span="4"><span class="table-text">账户类型:<span style="color: red">*</span></span></el-col>
          <el-col :span="8">
            <el-select v-model="addForm.accountType" size="medium" style="width: 100%">
              <el-option v-for="(item, index) in accountTypeDict" :key="index" :label="item.label" :value="item.value" />
            </el-select>
          </el-col>
          <el-col :span="4"><span style="margin-left: 10px" class="table-text">产品版本:<span style="color: red">*</span></span></el-col>
          <el-col :span="8">
            <el-select v-model="addForm.productVersion" size="medium" style="width: 100%">
              <el-option v-for="(item, index) in productVerssionDict" :key="index" :label="item.label" :value="item.value" />
            </el-select>
          </el-col>
          <div style="clear: both; height: 20px" />
          <el-col :span="4"><span class="table-text">开户日期:</span></el-col>
          <el-col :span="8">
            <el-date-picker v-model="addForm.createdTime" type="datetime" placeholder="选择日期时间" align="right" disabled style="width: 100%" size="medium" />
          </el-col>
          <el-col :span="4"><span style="margin-left: 10px" class="table-text">账户有效日期:</span></el-col>
          <el-col :span="8">
            <el-date-picker v-model="addForm.validityPeriod" type="datetime" placeholder="选择日期时间" align="right" style="width: 100%" size="medium" />
          </el-col>
          <div style="clear: both; height: 20px" />
          <el-col :span="4"><span class="table-text">账户状态:<span style="color: red">*</span></span></el-col>
          <el-col :span="8">
            <el-select v-model="addForm.accountStatus" placeholder="全部" style="width: 100%" size="medium">
              <el-option v-for="(item, index) in accountStatusDict" :label="item.label" :value="item.value" :key="index" />
            </el-select>
          </el-col>
          <el-col :span="4"><span style="margin-left: 10px" class="table-text">企业介绍:</span></el-col>
          <el-col :span="8">
            <el-input v-model="addForm.remarks" autocomplete="off" size="medium" />
          </el-col>
        </el-row>
        <div slot="footer" class="dialog-footer" align="center">
          <el-button @click="onCancel">取 消</el-button>
          <el-button @click="update" type="primary">保存</el-button>
        </div>
      </el-dialog>

      <el-dialog title="用户信息" :visible.sync="addFormVisible" width="40%">
        <el-form :model="form" label-position="left" label-width="80px">
          <el-form-item label="账户名">
            <span class="require">*</span>
            <el-input v-model="form.accountName" autocomplete="off" style="width: 80%" size="medium" />
          </el-form-item>
          <el-form-item label="账户状态">
            <span class="require">*</span>
            <el-select v-model="form.accountStatus" placeholder="全部" style="width: 80%" size="medium">
              <el-option v-for="(item, index) in accountStatusDict" :label="item.label" :value="item.value" :key="index" />
            </el-select>
          </el-form-item>
          <el-form-item label="邮箱">
            <el-input v-model="form.email" autocomplete="off" style="width: 80%; margin-left: 10px" size="medium" />
          </el-form-item>
          <el-form-item label="账户角色">
            <el-select v-model="selectedRole" placeholder="请选择角色" style="width: 80%; margin-left: 10px" size="medium">
              <el-option v-for="(item, index) in roleOptions" :key="index" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
          <el-form-item label="账户密码">
            <span class="require">*</span>
            <el-input v-model="form.password" type="password" show-password style="width: 80%" autocomplete="off" size="medium" />
          </el-form-item>
          <el-form-item label="手机号">
            <span class="require">*</span>
            <el-input v-model="form.phone" autocomplete="off" style="width: 80%" size="medium" maxlength="11" />
          </el-form-item>
          <el-form-item label="备注">
            <el-input v-model="form.remarks" autocomplete="off" style="width: 80%; margin-left: 10px" size="medium" />
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer" align="center">
          <el-button @click="addFormVisible = false" class="normal-white-btn">取 消</el-button>
          <el-button type="primary" @click="addSubmit" class="light-green-btn">确 定</el-button>
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import {
  accountList,
  getTenantInfo,
  adminList,
  updatedAccout,
  addAccount,
  delAccout,
  getTenantList,
} from "@/api/systemadmin";
import md5 from "js-md5";
import Cookies from "js-cookie";
import { getProductVerssionDict, getAccountStatusDict, getAccountTypeDict } from "@/config/dictHelper";
import { cursor } from "@/libs/element-table";

export default {
  name: "accountManager",
  data() {
    return {
      searchKeyWord: "",
      selectedVersion: [],
      selectedType: [],
      selectedStatus: [],

      list: [],
      total: 0,
      current: 1,
      pageSize: 10,

      passwordTip: false,
      isSaved: false,

      tenant: {
        address: "",
        faxNumber: "",
        id: 0,
        industry: "",
        telephone: "",
        tenantName: "",
      },
      tenantList: [],

      roleOptions: [],
      selectedRole: "",

      addFormVisible: false,
      readFormVisible: false,
      editFormVisible: false,

      password: "",
      addForm: {
        accountName: "",
        accountStatus: "",
        accountStatusName: "",
        email: "",
        id: "",
        password: "",
        phone: "",
        username: "",
        remarks: "",
        accountType: "",
        productVersion: "",
        createdTime: "",
        validityPeriod: "",
        tenantId: "",
        accountRole: { roleIds: [], roleNames: [] },
      },
      form: {
        accountName: "",
        accountStatus: "",
        email: "",
        id: 0,
        password: "",
        phone: "",
        remarks: "",
        username: "",
        roleIds: [],
      },

      productVerssionDict: [],
      accountStatusDict: [],
      accountTypeDict: [],
      optionStatus: [
        { label: "全部", value: "" },
        { label: "启用", value: 1 },
        { label: "禁用", value: 0 },
      ],
    };
  },
  mounted() {
    this.formatStatus(getAccountStatusDict(this.$store));
    this.formatType(getAccountTypeDict(this.$store));
    this.formatProduct(getProductVerssionDict(this.$store));
    this.getTenantList();
    this.getRoleList();
    this.getList(1);
  },
  methods: {
    cellStyle({ column }) {
      if (column.label !== "操作") {
        return cursor({ column });
      }
      return "";
    },
    addUser() {
      this.addFormVisible = true;
      this.readFormVisible = false;
      this.selectedRole = "";
      this.form = {
        accountName: "",
        accountStatus: "",
        email: "",
        id: 0,
        password: "",
        phone: "",
        remarks: "",
        username: "",
        roleIds: [],
      };
    },
    onClickDelete(id) {
      this.$confirm("确认删除当前账户？").then(() => {
        delAccout(id)
          .then(() => {
            this.$message.success("删除成功");
            this.getList(1);
          })
          .catch((err) => {
            this.$message.error((err && err.msg) || "删除失败");
          });
      });
    },
    clickItem(row) {
      this.readFormVisible = true;
      this.tenant = {
        address: "",
        faxNumber: "",
        id: 0,
        industry: "",
        telephone: "",
        tenantName: "",
      };
      this.selectedRole = row && row.accountRole && row.accountRole.roleIds ? row.accountRole.roleIds[0] : "";
      this.addForm = { ...row };

      if (row && row.tenantId && row.tenantId !== "--") {
        getTenantInfo(row.tenantId)
          .then((res) => {
            this.tenant = { ...(res || {}) };
          })
          .catch(() => {});
      }
    },
    onEdit(row) {
      this.tenant = {
        address: "",
        faxNumber: "",
        id: 0,
        industry: "",
        telephone: "",
        tenantName: "",
      };
      this.isSaved = false;
      this.passwordTip = false;
      this.password = "";
      this.editFormVisible = true;
      this.readFormVisible = false;
      this.addForm = { ...row, password: "" };
      this.selectedRole = row && row.accountRole && row.accountRole.roleIds ? row.accountRole.roleIds[0] : "";

      if (row && row.tenantId && row.tenantId !== 0 && row.tenantId !== "--") {
        getTenantInfo(row.tenantId)
          .then((res) => {
            this.tenant = { ...(res || {}) };
          })
          .catch(() => {});
      }
    },
    updateTenant() {
      const hit = this.tenantList.find((v) => v.id === this.tenant.id);
      if (hit) {
        this.tenant = { ...this.tenant, ...hit };
      }
    },
    onCancel() {
      if (this.addForm.password) {
        if (this.isSaved) {
          this.editFormVisible = false;
        } else {
          this.$message.warning("您还未保存密码");
          this.isSaved = true;
        }
      } else {
        this.editFormVisible = false;
      }
    },
    update() {
      if (this.passwordTip) {
        this.$message.warning("请检查两次输入的密码是否一致");
        return;
      }

      this.addForm.roleIds = [this.selectedRole];

      if (!this.addForm.accountName || !this.addForm.phone || !this.selectedRole || !this.addForm.accountType) {
        this.$message.warning("必填项不能为空");
        return;
      }

      const data = {
        accountName: this.addForm.accountName,
        accountStatus: this.addForm.accountStatus,
        accountType: this.addForm.accountType,
        createdTime: this.addForm.createdTime,
        email: this.addForm.email,
        id: this.addForm.id,
        password: this.addForm.password ? md5(this.addForm.password) : "",
        phone: this.addForm.phone,
        productVersion: this.addForm.productVersion,
        remarks: this.addForm.remarks,
        roleIds: this.addForm.roleIds,
        tenantId: this.tenant.id,
        username: this.addForm.username,
        validityPeriod: this.addForm.validityPeriod,
      };

      updatedAccout(data)
        .then(() => {
          const javaInfoStr = Cookies.get("JavaInfo");
          if (javaInfoStr) {
            const javaInfo = JSON.parse(javaInfoStr);
            if (javaInfo && javaInfo.accountId === this.addForm.id) {
              window.localStorage.setItem("accountName", this.addForm.accountName);
              this.$store.dispatch("user/updateName", this.addForm.accountName);
            }
          }
          this.$message.success("保存成功");
          this.isSaved = true;
          this.editFormVisible = false;
          this.getList(1);
        })
        .catch((err) => {
          this.$message.error((err && err.msg) || "保存失败");
        });
    },
    addSubmit() {
      if (!this.selectedRole) {
        this.$message.warning("用户角色不能为空");
        return;
      }
      const createdAccountName = this.form.accountName;
      this.form.roleIds = [Number(this.selectedRole)];
      if (!this.form.password || !this.form.accountName || !this.form.accountStatus || !this.form.phone) {
        this.$message.warning("必填项不能为空");
        return;
      }

      addAccount(this.form)
        .then(() => {
          this.$message.success("添加成功");
          this.addFormVisible = false;
          this.searchKeyWord = createdAccountName || "";
          this.selectedType = [];
          this.selectedStatus = [];
          this.selectedVersion = [];
          this.getList(1);
        })
        .catch((err) => {
          this.$message.error((err && err.msg) || "添加失败");
        });
    },
    onInputBlur() {
      this.passwordTip = this.password !== this.addForm.password;
    },
    getRoleList() {
      const data = { asc: true, size: 50, current: 1 };
      adminList(data)
        .then((res) => {
          const records = (res && res.records) || [];
          this.roleOptions = records.map((v) => ({ label: v.roleName, value: v.id }));
        })
        .catch(() => {});
    },
    getTenantList() {
      getTenantList()
        .then((res) => {
          this.tenantList = Array.isArray(res) ? res : [];
        })
        .catch(() => {});
    },
    buildSearchParams() {
      return {
        async: true,
        keywords: this.searchKeyWord,
        accountType: (this.selectedType && this.selectedType[0]) || "",
        accountStatus: (this.selectedStatus && this.selectedStatus[0]) || "",
        productVersion: (this.selectedVersion && this.selectedVersion[0]) || "",
        asc: true,
        size: this.pageSize,
        current: this.current,
      };
    },
    onClickSearch() {
      this.current = 1;
      this.getList(1);
    },
    getList(page) {
      this.current = page;
      const search = this.buildSearchParams();
      accountList(search)
        .then((res) => {
          const records = Array.isArray(res && res.records)
            ? res.records
            : Array.isArray(res && res.data && res.data.records)
              ? res.data.records
              : [];
          const total =
            res && res.total != null
              ? Number(res.total)
              : res && res.data && res.data.total != null
                ? Number(res.data.total)
                : 0;
          const current =
            res && res.current != null
              ? Number(res.current)
              : res && res.data && res.data.current != null
                ? Number(res.data.current)
                : page;

          this.list = records;
          this.total = Number.isFinite(total) ? total : 0;
          this.current = Number.isFinite(current) ? current : page;
          this.list = this.list.map((v) => {
            const row = { ...v };
            Object.keys(row).forEach((k) => {
              if (!row[k] || row[k] === " ") row[k] = "--";
            });
            return row;
          });
        })
        .catch((err) => {
          this.list = [];
          this.total = 0;
          this.$message.error((err && err.msg) || "加载失败");
        });
    },
    handleSizeChange(val) {
      this.pageSize = val;
      this.getList(1);
    },
    handleCurrentChange(val) {
      this.getList(val);
    },
    formatProduct(data) {
      const list = data || [];
      this.productVerssionDict = list.map((v) => ({
        label: v.name,
        value: v.name === "全部" ? "" : v.value,
      }));
    },
    formatStatus(data) {
      const list = data || [];
      this.accountStatusDict = list.map((v) => ({
        label: v.name,
        value: v.name === "全部" ? "" : v.value,
      }));
    },
    formatType(data) {
      const list = data || [];
      this.accountTypeDict = list.map((v) => ({
        label: v.name,
        value: v.name === "全部" ? "" : v.value,
      }));
    },
  },
};
</script>
<style lang="scss" scoped>
.root {
  background: #f2f5f7;
}

.divBox {
  margin: 20px;
  background: #ffffff;
  box-shadow: 0px 2px 8px 0px #eaf0f3;
  border-radius: 8px;
}

.container {
  margin: 20px 0px 20px 0px;
  display: flex;
  flex-direction: row;
}

.content-container {
  display: flex;
  flex-direction: column;
  width: 100%;
  padding: 0 20px 20px 20px;
}

>>> .el-cascader .el-input .el-input__inner,
>>> .el-cascader .el-input.is-focus .el-input__inner {
  border-color: transparent;
}

.table-text {
  position: relative;
  top: 10px;
}

.require {
  color: red;
  font-size: 16px;
}

.cursor-mi {
  cursor: default;
}
</style>
