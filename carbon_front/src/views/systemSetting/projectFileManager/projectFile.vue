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
              placeholder="文档标题/项目备案号"
              clearable
              size="medium"
              @keyup.enter.native="getList(1)"
              @clear="getList(1)"
            />
          </div>
          <button style="margin-left: 16px" class="light-green-btn" @click="getList(1)">查询</button>
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
          <el-table-column label="项目备案号" prop="projectRefId" min-width="140" show-overflow-tooltip />
          <el-table-column label="项目名称" prop="projectRegistryName" min-width="200" show-overflow-tooltip />
          <el-table-column label="类型" prop="type" min-width="120" show-overflow-tooltip />
          <el-table-column label="标题" prop="title" min-width="260" show-overflow-tooltip />
          <el-table-column label="操作" min-width="120" fixed="right">
            <template slot-scope="scope">
              <a class="list-blue-text" @click="openUrl(scope.row.url)">打开</a>
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
  </div>
</template>
<script>
import { projectFileManager } from '@/api/systemSetting'

export default {
  name: 'projectFileManager',
  data() {
    return {
      keyword: '',
      list: [],
      total: 0,
      current: 1,
      pageSize: 10,
      pageCount: 1,
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
        param.title = this.keyword
        param.projectRefId = this.keyword
      }
      projectFileManager(param).then((res) => {
        this.list = (res && res.records) || []
        this.total = parseInt((res && res.total) || 0)
        this.current = parseInt((res && res.current) || page)
        this.pageCount = Math.ceil(this.total / this.pageSize)
      })
    },
    openUrl(url) {
      if (!url) return
      window.open(url)
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
