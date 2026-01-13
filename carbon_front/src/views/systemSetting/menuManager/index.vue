<template>
  <div class="root">
    <div class="divBox">
      <div class="content-container">
        <div class="container">
          <div style="width: 420px" class="selectbox-root">
            <span class="selectbox-hint" style="width: 80px">搜索</span>
            <div class="selectbox-deliver" />
            <el-input
              v-model="searchKeyword"
              placeholder="菜单名称 / URL"
              clearable
              size="medium"
              @keyup.enter.native="getList"
              @clear="getList"
            />
          </div>
          <button style="margin-left: 16px" class="light-green-btn" @click="getList">查询</button>
          <div style="flex-grow: 1" />
          <button class="normal-white-btn" style="width: 120px" @click="openAdd(null)">新增菜单</button>
        </div>

        <el-table
          :header-cell-style="{
            background: '#F2F5F7',
            border: '0px solid #DDDDDD',
            color: '#242B35',
            height: '64px',
          }"
          :data="list"
          row-key="id"
          default-expand-all
          :tree-props="{ children: 'children' }"
          stripe
          :row-style="{ height: '64px' }"
          style="width: 100%"
        >
          <el-table-column min-width="10" />
          <el-table-column label="菜单名称" prop="menuName" min-width="220" />
          <el-table-column label="URL" prop="menuUrl" min-width="260" show-overflow-tooltip />
          <el-table-column label="图标" prop="menuIcon" min-width="120" show-overflow-tooltip />
          <el-table-column label="排序" prop="orderNum" min-width="80" />
          <el-table-column label="状态" min-width="110">
            <template slot-scope="scope">
              <el-switch
                v-model="scope.row.__statusOn"
                active-text="启用"
                inactive-text="禁用"
                @change="(v) => onToggleStatus(scope.row, v)"
              />
            </template>
          </el-table-column>
          <el-table-column label="操作" min-width="240" fixed="right">
            <template slot-scope="scope">
              <a class="list-blue-text" @click="openEdit(scope.row)">编辑</a>
              <a style="margin-left: 10px" class="list-green-text" @click="openAdd(scope.row)">新增子菜单</a>
              <a style="margin-left: 10px" class="list-red-text" @click="onDelete(scope.row)">删除</a>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="560px">
      <el-form :model="form" label-position="left" label-width="90px">
        <el-form-item label="菜单名称">
          <el-input v-model="form.menuName" size="medium" />
        </el-form-item>
        <el-form-item label="菜单URL">
          <el-input v-model="form.menuUrl" size="medium" />
        </el-form-item>
        <el-form-item label="图标">
          <el-input v-model="form.menuIcon" size="medium" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="form.orderNum" :min="0" :step="1" style="width: 100%" />
        </el-form-item>
        <el-form-item label="父级ID">
          <el-input v-model="form.parentId" size="medium" :disabled="true" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button class="normal-white-btn" @click="dialogVisible = false">取消</el-button>
        <el-button class="light-green-btn" @click="onSubmit">保存</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import * as systemAdminApi from '@/api/systemadmin'

export default {
  name: 'menuManager',
  data() {
    return {
      searchKeyword: '',
      list: [],
      dialogVisible: false,
      dialogTitle: '',
      mode: 'add',
      form: {
        id: null,
        parentId: 0,
        menuName: '',
        menuUrl: '',
        menuIcon: '',
        orderNum: 0,
        status: 0,
      },
    }
  },
  mounted() {
    this.getList()
  },
  methods: {
    normalizeTree(nodes) {
      const walk = (arr) => {
        if (!Array.isArray(arr)) return
        arr.forEach((n) => {
          n.__statusOn = n.status === 0 || n.status === '0'
          if (Array.isArray(n.children) && n.children.length) {
            walk(n.children)
          }
        })
      }
      walk(nodes)
      return nodes
    },
    getList() {
      const param = {}
      if (this.searchKeyword) {
        param.keyword = this.searchKeyword
      }
      systemAdminApi.getMenuList(param).then((res) => {
        this.list = this.normalizeTree(Array.isArray(res) ? res : [])
      })
    },
    openAdd(parent) {
      this.mode = 'add'
      this.dialogTitle = parent ? '新增子菜单' : '新增菜单'
      this.form = {
        id: null,
        parentId: parent && parent.id != null ? parent.id : 0,
        menuName: '',
        menuUrl: '',
        menuIcon: '',
        orderNum: 0,
        status: 0,
      }
      this.dialogVisible = true
    },
    openEdit(row) {
      this.mode = 'edit'
      this.dialogTitle = '编辑菜单'
      this.form = {
        id: row.id,
        parentId: row.parentId == null ? 0 : row.parentId,
        menuName: row.menuName || '',
        menuUrl: row.menuUrl || '',
        menuIcon: row.menuIcon || '',
        orderNum: row.orderNum == null ? 0 : row.orderNum,
        status: row.status == null ? 0 : row.status,
      }
      this.dialogVisible = true
    },
    onSubmit() {
      if (!this.form.menuName) {
        this.$message.warning('菜单名称不能为空')
        return
      }
      const payload = { ...this.form }
      const req = this.mode === 'add' ? systemAdminApi.addMenu(payload) : systemAdminApi.editMenu(payload)
      req.then(() => {
        this.$message.success('保存成功')
        this.dialogVisible = false
        this.getList()
      })
    },
    onToggleStatus(row, v) {
      const status = v ? 0 : 1
      systemAdminApi.editMenuStatus({ id: row.id, status }).then(() => {
        row.status = status
        this.$message.success('状态已更新')
      })
    },
    onDelete(row) {
      this.$confirm('是否删除该菜单？').then(() => {
        systemAdminApi.delMenu(row.id).then(() => {
          this.$message.success('删除成功')
          this.getList()
        })
      })
    },
  },
}
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
  padding: 20px 20px 10px 20px;
  display: flex;
  flex-direction: row;
  align-items: center;
}
.content-container {
  display: flex;
  flex-direction: column;
  width: 100%;
  padding: 0 20px 20px 20px;
}
</style>
