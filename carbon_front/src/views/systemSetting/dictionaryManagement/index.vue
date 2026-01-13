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
              placeholder="字典名称/编码"
              clearable
              size="medium"
              @keyup.enter.native="getList(1)"
              @clear="getList(1)"
            />
          </div>
          <button style="margin-left: 16px" class="light-green-btn" @click="getList(1)">查询</button>
          <div style="flex-grow: 1" />
          <button class="normal-white-btn" style="width: 120px" @click="openAddDict">新增字典</button>
        </div>

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
          style="width: 100%"
        >
          <el-table-column min-width="10" />
          <el-table-column label="字典名称" prop="dictName" min-width="160" show-overflow-tooltip />
          <el-table-column label="字典编码" prop="dictCode" min-width="120" show-overflow-tooltip />
          <el-table-column label="备注" prop="remarks" min-width="200" show-overflow-tooltip />
          <el-table-column label="操作" min-width="220" fixed="right">
            <template slot-scope="scope">
              <a class="list-blue-text" @click="openEditDict(scope.row)">编辑</a>
              <a style="margin-left: 10px" class="list-green-text" @click="openDictItems(scope.row)">字典项</a>
              <a style="margin-left: 10px" class="list-red-text" @click="deleteDict(scope.row)">删除</a>
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

    <el-dialog :title="dictDialogTitle" :visible.sync="dictDialogVisible" width="560px">
      <el-form :model="dictForm" label-position="left" label-width="90px">
        <el-form-item label="字典名称">
          <el-input v-model="dictForm.dictName" size="medium" />
        </el-form-item>
        <el-form-item label="字典编码">
          <el-input v-model="dictForm.dictCode" size="medium" :disabled="dictMode === 'edit'" />
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="dictForm.remarks" type="textarea" size="medium" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button class="normal-white-btn" @click="dictDialogVisible = false">取消</el-button>
        <el-button class="light-green-btn" @click="submitDict">保存</el-button>
      </div>
    </el-dialog>

    <el-drawer title="字典项管理" :visible.sync="itemDrawerVisible" size="60%">
      <div style="padding: 0 20px 20px 20px">
        <div style="display: flex; align-items: center; margin-bottom: 12px">
          <div style="flex-grow: 1" />
          <button class="normal-white-btn" style="width: 120px" @click="openAddItem">新增字典项</button>
        </div>
        <el-table
          :header-cell-style="{
            background: '#F2F5F7',
            border: '0px solid #DDDDDD',
            color: '#242B35',
            height: '64px',
          }"
          :data="itemList"
          stripe
          :row-style="{ height: '64px' }"
          style="width: 100%"
        >
          <el-table-column min-width="10" />
          <el-table-column label="名称" prop="itemCh" min-width="160" show-overflow-tooltip />
          <el-table-column label="值" prop="itemValue" min-width="160" show-overflow-tooltip />
          <el-table-column label="排序" prop="sortOrder" min-width="80" />
          <el-table-column label="操作" min-width="160" fixed="right">
            <template slot-scope="scope">
              <a class="list-blue-text" @click="openEditItem(scope.row)">编辑</a>
              <a style="margin-left: 10px" class="list-red-text" @click="deleteItem(scope.row)">删除</a>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-drawer>

    <el-dialog :title="itemDialogTitle" :visible.sync="itemDialogVisible" width="560px">
      <el-form :model="itemForm" label-position="left" label-width="90px">
        <el-form-item label="名称">
          <el-input v-model="itemForm.itemCh" size="medium" />
        </el-form-item>
        <el-form-item label="值">
          <el-input v-model="itemForm.itemValue" size="medium" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="itemForm.sortOrder" :min="0" :step="1" style="width: 100%" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button class="normal-white-btn" @click="itemDialogVisible = false">取消</el-button>
        <el-button class="light-green-btn" @click="submitItem">保存</el-button>
      </div>
    </el-dialog>
  </div>
  </template>
<script>
import * as systemAdminApi from '@/api/systemadmin'

export default {
  name: 'dictionaryManagement',
  data() {
    return {
      keyword: '',
      list: [],
      total: 0,
      current: 1,
      pageSize: 10,
      pageCount: 1,

      dictDialogVisible: false,
      dictDialogTitle: '',
      dictMode: 'add',
      dictForm: { id: null, dictName: '', dictCode: '', remarks: '' },

      itemDrawerVisible: false,
      currentDict: null,
      itemList: [],
      itemDialogVisible: false,
      itemDialogTitle: '',
      itemMode: 'add',
      itemForm: { id: null, dictCode: '', itemCh: '', itemValue: '', sortOrder: 0 },
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
      const param = {
        current: page,
        size: this.pageSize,
      }
      if (this.keyword) {
        param.dictName = this.keyword
        param.dictCode = this.keyword
      }
      systemAdminApi.getDictList(param).then((res) => {
        this.list = (res && res.records) || []
        this.total = parseInt((res && res.total) || 0)
        this.current = parseInt((res && res.current) || page)
        this.pageCount = Math.ceil(this.total / this.pageSize)
      })
    },
    openAddDict() {
      this.dictMode = 'add'
      this.dictDialogTitle = '新增字典'
      this.dictForm = { id: null, dictName: '', dictCode: '', remarks: '' }
      this.dictDialogVisible = true
    },
    openEditDict(row) {
      this.dictMode = 'edit'
      this.dictDialogTitle = '编辑字典'
      this.dictForm = {
        id: row.id,
        dictName: row.dictName || '',
        dictCode: row.dictCode || '',
        remarks: row.remarks || '',
      }
      this.dictDialogVisible = true
    },
    submitDict() {
      if (!this.dictForm.dictName || !this.dictForm.dictCode) {
        this.$message.warning('字典名称和字典编码不能为空')
        return
      }
      const payload = { ...this.dictForm }
      const req = this.dictMode === 'add' ? systemAdminApi.addDict(payload) : systemAdminApi.editDictList(payload)
      req.then(() => {
        this.$message.success('保存成功')
        this.dictDialogVisible = false
        this.getList(1)
      })
    },
    deleteDict(row) {
      this.$confirm('是否删除该字典？').then(() => {
        systemAdminApi.delDict(row.id).then(() => {
          this.$message.success('删除成功')
          this.getList(1)
        })
      })
    },
    openDictItems(row) {
      this.currentDict = row
      this.itemDrawerVisible = true
      this.fetchItems()
    },
    fetchItems() {
      if (!this.currentDict || !this.currentDict.dictCode) {
        this.itemList = []
        return
      }
      systemAdminApi
        .getSingleDictList({ current: 1, size: 200, dictCode: this.currentDict.dictCode })
        .then((res) => {
          this.itemList = (res && res.records) || []
        })
    },
    openAddItem() {
      if (!this.currentDict || !this.currentDict.dictCode) return
      this.itemMode = 'add'
      this.itemDialogTitle = '新增字典项'
      this.itemForm = { id: null, dictCode: this.currentDict.dictCode, itemCh: '', itemValue: '', sortOrder: 0 }
      this.itemDialogVisible = true
    },
    openEditItem(row) {
      this.itemMode = 'edit'
      this.itemDialogTitle = '编辑字典项'
      this.itemForm = {
        id: row.id,
        dictCode: row.dictCode,
        itemCh: row.itemCh || '',
        itemValue: row.itemValue || '',
        sortOrder: row.sortOrder == null ? 0 : row.sortOrder,
      }
      this.itemDialogVisible = true
    },
    submitItem() {
      if (!this.itemForm.itemCh || !this.itemForm.itemValue) {
        this.$message.warning('名称和值不能为空')
        return
      }
      const payload = { ...this.itemForm }
      const req = this.itemMode === 'add' ? systemAdminApi.addChildDict(payload) : systemAdminApi.editChildDict(payload)
      req.then(() => {
        this.$message.success('保存成功')
        this.itemDialogVisible = false
        this.fetchItems()
      })
    },
    deleteItem(row) {
      this.$confirm('是否删除该字典项？').then(() => {
        systemAdminApi.delChildDict(row.id).then(() => {
          this.$message.success('删除成功')
          this.fetchItems()
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
