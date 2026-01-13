<template>
  <div class="root">
    <div class="divBox">
      <div class="content-container">
        <div class="container">
          <div style="width: 390px" class="selectbox-root">
            <span class="selectbox-hint" style="width: 60px">搜索</span>
            <div class="selectbox-deliver" />
            <el-input
              v-model="searchKeyWord"
              placeholder="输入名称"
              clearable
              size="medium"
              @keyup.enter.native="searchUser"
            />
          </div>
          <button style="margin-left: 16px" class="light-green-btn" @click="searchUser">
            查询
          </button>
        </div>

        <div>
          <button style="margin-top: 0px; margin-bottom: 20px; width: 103px" class="normal-white-btn" @click="addUser">
            添加角色
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
            stripe
            :row-style="{ height: '64px' }"
            :cell-style="cellStyle"
            style="width: 100%"
          >
            <el-table-column min-width="10" />
            <el-table-column label="序号" align="left" min-width="60">
              <template slot-scope="scope">
                <span>{{ getCurListNo(scope.$index) }}</span>
              </template>
            </el-table-column>
            <el-table-column :show-overflow-tooltip="true" align="left" prop="roleName" label="角色昵称" min-width="100" />
            <el-table-column align="left" prop="roleCode" label="角色编码" min-width="100" />
            <el-table-column align="left" prop="statusName" label="状态" min-width="90" />
            <el-table-column align="left" prop="createdTime" label="创建时间" min-width="100" />
            <el-table-column align="left" prop="updatedTime" label="更新时间" min-width="100" />
            <el-table-column label="操作" min-width="130" fixed="right">
              <template slot-scope="scope">
                <template v-if="scope.row.isDel">
                  <span>-</span>
                </template>
                <template v-else>
                  <a @click="editUser(scope.row)" class="list-blue-text">编辑</a>
                  <a style="margin-left: 10px" @click="delUser(scope.row.id)" class="list-red-text">删除</a>
                </template>
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>

      <div style="margin-top: 30px; margin-bottom: 10px" class="pageBox">
        <div style="flex-grow: 1" />
        <a style="margin: auto" class="pageBox-total-num">共{{ total }}条</a>
        <el-pagination
          style="margin: auto"
          background
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="current"
          :page-size="pageSize"
          :page-count="pageCount"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
        />
      </div>

      <el-drawer title="角色权限配置" :visible.sync="dialogFormVisible" style="font-size: 16px">
        <div class="my_table">
          <el-tree
            :data="data"
            show-checkbox
            node-key="id"
            :default-expanded-keys="[2, 3]"
            :default-checked-keys="userMenu"
            :props="defaultProps"
            ref="tree"
          />
        </div>
        <el-divider />
        <button
          style="margin-top: 20px; margin-left: 120px; margin-bottom: 20px; width: 80px"
          class="normal-white-btn"
          @click="dialogFormVisible = false"
        >
          取消
        </button>
        <button
          style="margin-top: 20px; margin-left: 15px; margin-bottom: 20px; width: 80px"
          class="light-green-btn"
          @click="saveChange"
        >
          保存
        </button>
      </el-drawer>

      <el-dialog title="添加角色" :visible.sync="addUserShow" width="500px">
        <el-form :model="addForm" label-position="left">
          <el-form-item label="角色名称">
            <el-input v-model="addForm.roleName" size="medium" style="width: 350px" />
          </el-form-item>
          <el-form-item label="角色编码">
            <el-input v-model="addForm.roleCode" size="medium" style="width: 350px" />
          </el-form-item>
          <el-form-item label="角色状态">
            <el-select v-model="addForm.status" placeholder="请选择角色状态" size="medium">
              <el-option label="启用" value="0" />
              <el-option label="禁用" value="1" />
            </el-select>
          </el-form-item>
          <el-form-item label="角色描述">
            <el-input v-model="addForm.remarks" type="textarea" style="width: 350px" />
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click="addUserShow = false" class="normal-white-btn">取 消</el-button>
          <el-button type="primary" @click="addSubmit" class="light-green-btn">确 定</el-button>
        </div>
      </el-dialog>
    </div>
  </div>
</template>
<script>
import * as systemAdminApi from "@/api/systemadmin.js";
import * as roleApi from "@/api/role.js";
import { setListNo } from "@/libs/public";
import { cursor } from "@/libs/element-table";

export default {
  name: "adminList",
  data() {
    return {
      searchKeyWord: "",
      addUserShow: false,
      list: [],
      total: 0,
      current: 1,
      pageCount: 1,
      pageSize: 10,

      dialogFormVisible: false,
      userMenu: [],
      data: [],
      defaultProps: { children: "children", label: "menuName" },

      addForm: { roleName: "", roleCode: "", status: "", remarks: "" },
      editForm: { roleId: 0, menuIds: [] },
    };
  },
  mounted() {
    this.getList(1);
    systemAdminApi.getMenuList({}).then((res) => {
      this.data = res || [];
    });
  },
  methods: {
    cellStyle(data) {
      return cursor(data);
    },
    getCurListNo(index) {
      const curNo = parseInt(index, 10) + 1;
      const page = this.current - 1;
      return setListNo(page, this.pageSize, curNo);
    },
    handleSizeChange(val) {
      this.pageSize = val;
      this.getList(this.current);
    },
    handleCurrentChange(val) {
      this.current = val;
      this.getList(val);
    },
    getUserMenu(data) {
      this.userMenu = [];
      if (Array.isArray(data)) {
        data.forEach((v) => {
          if (v && v.menuId != null) {
            this.userMenu.push(Number(v.menuId));
          }
        });
      }

      this.$nextTick(() => {
        if (this.$refs.tree) {
          this.$refs.tree.setCheckedKeys(this.userMenu);
        }
      });
    },
    editUser(row) {
      this.dialogFormVisible = true;
      this.userMenu = [];
      this.editForm.roleId = row.id;

      roleApi.getRoleMenu(row.id).then((res) => {
        this.getUserMenu(res);
        sessionStorage.setItem("roleMenu" + this.editForm.roleId, JSON.stringify(this.userMenu));
      });
    },
    saveChange() {
      const keys = this.$refs.tree ? this.$refs.tree.getCheckedKeys() : [];
      this.$message({ type: "success", duration: 5000, message: "权限配置中..." });
      this.editForm.menuIds = Array.isArray(keys) ? keys : [];
      systemAdminApi.editRolePermissions(this.editForm).then(
        () => {
          this.$message.success("保存成功");
          sessionStorage.removeItem("roleMenu" + this.editForm.roleId);
          sessionStorage.setItem("roleMenu" + this.editForm.roleId, JSON.stringify(this.editForm.menuIds));
          this.dialogFormVisible = false;
        },
        () => {
          this.$message.error("保存失败");
        }
      );
    },
    addUser() {
      this.addForm = { roleName: "", roleCode: "", status: "", remarks: "" };
      this.addUserShow = true;
    },
    addSubmit() {
      if (!this.addForm.roleName || !this.addForm.roleCode) {
        this.$message.warning("角色名称和角色编码不能为空！");
        return;
      }
      if (!this.addForm.status) {
        this.addForm.status = "0";
      }
      roleApi.addRole(this.addForm).then(
        (res) => {
          this.$message.success("添加成功");
          this.addUserShow = false;
          this.getList(1);
        },
        (err) => {
          this.$message.error((err && err.msg) || "添加失败");
        }
      );
    },
    delUser(id) {
      this.$confirm("是否删除该角色？").then(() => {
        roleApi.delRole(id).then(() => {
          this.$message.success("删除数据成功");
          this.getList(this.current);
        });
      });
    },
    searchUser() {
      const para = {
        asc: true,
        current: 1,
        roleName: this.searchKeyWord,
        size: this.pageSize,
        sortField: "",
        status: "",
      };
      roleApi
        .getRoleList(para)
        .then((data) => {
          this.list = data.records;
          this.total = data.total;
          this.current = data.current;
          this.pageCount = Math.ceil(parseInt(this.total) / this.pageSize);
          this.list.forEach((v) => {
            v.id = v.id ? v.id : "--";
            v.roleName = v.roleName ? v.roleName : "--";
          });
        })
        .catch(() => {});
    },
    getList(page) {
      const para = {
        asc: true,
        current: page,
        size: this.pageSize,
      };
      roleApi
        .getRoleList(para)
        .then((data) => {
          this.list = data.records;
          this.total = data.total;
          this.current = data.current;
          this.pageCount = Math.ceil(parseInt(this.total) / this.pageSize);
          this.list.forEach((v) => {
            Object.keys(v).forEach((k) => {
              v[k] = v[k] ? v[k] : "--";
              if (v[k] === " ") v[k] = "--";
            });
            v.statusName = v.status === "0" || v.status === 0 ? "启用" : "禁用";
          });
        })
        .catch(() => {});
    },
  },
};
</script>
<style lang="scss" scoped>
.dialog-footer {
  text-align: center;
}

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
  margin: 10px 0px 20px 0px;
  display: flex;
  flex-direction: row;
}

.content-container {
  display: flex;
  flex-direction: column;
  width: 100%;
}

::v-deep .el-cascader .el-input .el-input__inner,
::v-deep .el-cascader .el-input.is-focus .el-input__inner {
  border-color: transparent;
}

.my_table ::v-deep .el-table__row > td {
  border: none;
}
</style>
