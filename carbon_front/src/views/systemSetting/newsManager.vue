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
              placeholder="标题关键字"
              clearable
              size="medium"
              @keyup.enter.native="getList(1)"
              @clear="getList(1)"
            />
          </div>
          <button style="margin-left: 16px" class="light-green-btn" @click="getList(1)">查询</button>
          <div style="flex-grow: 1" />
          <button class="normal-white-btn" style="width: 120px" @click="openSync">同步文章</button>
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
          <el-table-column label="标题" prop="title" min-width="240" show-overflow-tooltip />
          <el-table-column label="作者" prop="author" min-width="120" show-overflow-tooltip />
          <el-table-column label="状态" min-width="120">
            <template slot-scope="scope">
              <el-switch
                v-model="scope.row.__statusOn"
                active-text="启用"
                inactive-text="禁用"
                @change="(v) => onToggleStatus(scope.row, v)"
              />
            </template>
          </el-table-column>
          <el-table-column label="创建时间" prop="createdTime" min-width="160" show-overflow-tooltip />
          <el-table-column label="操作" min-width="160" fixed="right">
            <template slot-scope="scope">
              <a class="list-red-text" @click="deleteArticle(scope.row)">删除</a>
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

    <el-dialog title="同步文章" :visible.sync="syncVisible" width="560px">
      <el-form :model="syncForm" label-position="left" label-width="120px">
        <el-form-item label="分页页码">
          <el-input-number v-model="syncForm.page" :min="1" :step="1" style="width: 100%" />
        </el-form-item>
        <el-form-item label="分页大小">
          <el-input-number v-model="syncForm.size" :min="1" :step="1" style="width: 100%" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button class="normal-white-btn" @click="syncVisible = false">取消</el-button>
        <el-button class="light-green-btn" @click="doSync">开始同步</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { getCarbonArticles, DelArticle, changeCarbonStatusById, syncArticle, addArticleToSql } from '@/api/article'

export default {
  name: 'newsManager',
  data() {
    return {
      keyword: '',
      list: [],
      total: 0,
      current: 1,
      pageSize: 10,
      pageCount: 1,

      syncVisible: false,
      syncForm: { page: 1, size: 20 },
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
    normalize(records) {
      return (records || []).map((r) => {
        const status = r.status == null ? 0 : r.status
        return { ...r, __statusOn: status === 0 || status === '0' }
      })
    },
    getList(page) {
      const param = { current: page, size: this.pageSize }
      if (this.keyword) {
        param.title = this.keyword
      }
      getCarbonArticles(param).then((res) => {
        this.list = this.normalize((res && res.records) || [])
        this.total = parseInt((res && res.total) || 0)
        this.current = parseInt((res && res.current) || page)
        this.pageCount = Math.ceil(this.total / this.pageSize)
      })
    },
    onToggleStatus(row, v) {
      const status = v ? 0 : 1
      changeCarbonStatusById({ id: row.id, status }).then(() => {
        row.status = status
        this.$message.success('状态已更新')
      })
    },
    deleteArticle(row) {
      this.$confirm('是否删除该文章？').then(() => {
        DelArticle(row.id).then(() => {
          this.$message.success('删除成功')
          this.getList(1)
        })
      })
    },
    openSync() {
      this.syncVisible = true
    },
    doSync() {
      const payload = { page: this.syncForm.page, size: this.syncForm.size }
      syncArticle(payload).then((res) => {
        return addArticleToSql(res)
      }).then(() => {
        this.$message.success('同步完成')
        this.syncVisible = false
        this.getList(1)
      }).catch(() => {
        this.$message.error('同步失败')
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
