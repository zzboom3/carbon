<template>
  <div class="root">
    <div class="divBox">
      <div class="content-container">
        <div class="container">
          <div style="width: 420px" class="selectbox-root">
            <span class="selectbox-hint" style="width: 80px">搜索</span>
            <div class="selectbox-deliver" />
            <el-input
              v-model="keyword"
              placeholder="租户名称/机构编码"
              clearable
              size="medium"
              @keyup.enter.native="getList(1)"
              @clear="getList(1)"
            />
          </div>
          <button style="margin-left: 16px" class="light-green-btn" @click="getList(1)">查询</button>
          <div style="flex-grow: 1" />
          <button class="normal-white-btn" style="width: 120px" @click="openAdd">新增租户</button>
        </div>

        <el-table
          :header-cell-style="{
            background: '#F2F5F7',
            border: '0px solid #DDDDDD',
            color: '#242B35',
            height: '64px',
          }"
          :data="list"
          stripe
          :row-style="{ height: '64px' }"
          style="width: 100%"
        >
          <el-table-column min-width="10" />
          <el-table-column label="租户名称" prop="tenantName" min-width="160" show-overflow-tooltip />
          <el-table-column label="机构编码" prop="orgName" min-width="140" show-overflow-tooltip />
          <el-table-column label="行业" prop="industry" min-width="120" show-overflow-tooltip />
          <el-table-column label="联系人" prop="contactsName" min-width="100" show-overflow-tooltip />
          <el-table-column label="联系人电话" prop="contactsPhone" min-width="120" show-overflow-tooltip />
          <el-table-column label="创建时间" prop="createdTime" min-width="140" show-overflow-tooltip />
          <el-table-column label="操作" min-width="180" fixed="right">
            <template slot-scope="scope">
              <a class="list-blue-text" @click="openEdit(scope.row)">编辑</a>
              <a style="margin-left: 10px" class="list-red-text" @click="deleteTenant(scope.row)">删除</a>
            </template>
          </el-table-column>
        </el-table>

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
      </div>
    </div>

    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="640px">
      <el-form :model="form" label-position="left" label-width="110px">
        <el-form-item label="租户名称">
          <el-input v-model="form.tenantName" size="medium" />
        </el-form-item>
        <el-form-item label="机构编码">
          <el-input v-model="form.orgName" size="medium" :disabled="mode === 'edit'" />
        </el-form-item>
        <el-form-item label="行业">
          <el-input v-model="form.industry" size="medium" />
        </el-form-item>
        <el-form-item label="联系人姓名">
          <el-input v-model="form.contactsName" size="medium" />
        </el-form-item>
        <el-form-item label="联系人电话">
          <el-input v-model="form.contactsPhone" size="medium" />
        </el-form-item>
        <el-form-item label="联系人邮箱">
          <el-input v-model="form.contactsEmail" size="medium" />
        </el-form-item>
        <el-form-item label="地址">
          <el-input v-model="form.address" size="medium" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="form.remarks" type="textarea" size="medium" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button class="normal-white-btn" @click="dialogVisible = false">取消</el-button>
        <el-button class="light-green-btn" @click="submit">保存</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import * as systemAdminApi from '@/api/systemadmin'

export default {
  name: 'tenantMgtManager',
  data() {
    return {
      keyword: '',
      list: [],
      total: 0,
      current: 1,
      pageSize: 10,
      pageCount: 1,

      dialogVisible: false,
      dialogTitle: '',
      mode: 'add',
      form: {
        id: null,
        tenantName: '',
        orgName: '',
        industry: '',
        contactsName: '',
        contactsPhone: '',
        contactsEmail: '',
        address: '',
        remarks: '',
      },
    }
  },
  mounted() {
    this.getList(1)
  },
  methods: {
    handleSizeChange(val) {
      this.pageSize = val
      this.getList(1)
    },
    handleCurrentChange(val) {
      this.current = val
      this.getList(val)
    },
    getList(page) {
      const param = { current: page, size: this.pageSize }
      if (this.keyword) {
        param.tenantName = this.keyword
        param.orgName = this.keyword
      }
      systemAdminApi.getTenantPageList(param).then((res) => {
        this.list = (res && res.records) || []
        this.total = parseInt((res && res.total) || 0)
        this.current = parseInt((res && res.current) || page)
        this.pageCount = Math.ceil(this.total / this.pageSize)
      })
    },
    openAdd() {
      this.mode = 'add'
      this.dialogTitle = '新增租户'
      this.form = {
        id: null,
        tenantName: '',
        orgName: '',
        industry: '',
        contactsName: '',
        contactsPhone: '',
        contactsEmail: '',
        address: '',
        remarks: '',
      }
      this.dialogVisible = true
    },
    openEdit(row) {
      this.mode = 'edit'
      this.dialogTitle = '编辑租户'
      this.form = {
        id: row.id,
        tenantName: row.tenantName || '',
        orgName: row.orgName || '',
        industry: row.industry || '',
        contactsName: row.contactsName || '',
        contactsPhone: row.contactsPhone || '',
        contactsEmail: row.contactsEmail || '',
        address: row.address || '',
        remarks: row.remarks || '',
      }
      this.dialogVisible = true
    },
    submit() {
      if (!this.form.tenantName || !this.form.orgName) {
        this.$message.warning('租户名称和机构编码不能为空')
        return
      }
      const payload = { ...this.form }
      const req = this.mode === 'add' ? systemAdminApi.addTenantUser(payload) : systemAdminApi.updatedTenant(payload)
      req.then(
        () => {
          this.$message.success('保存成功')
          this.dialogVisible = false
          this.getList(1)
        },
        (err) => {
          this.$message.error((err && err.msg) || '保存失败')
        }
      )
    },
    deleteTenant(row) {
      this.$confirm('是否删除该租户？').then(() => {
        systemAdminApi.delTenantById(row.id).then(() => {
          this.$message.success('删除成功')
          this.getList(1)
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
.dialog-footer {
  text-align: center;
}
</style>
