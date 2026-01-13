<template>
  <div class="root">
    <div class="divBox">
      <div class="content-container">
        <div class="container">
          <div style="width: 360px" class="selectbox-root">
            <span class="selectbox-hint" style="width: 80px">项目领域</span>
            <div class="selectbox-deliver" />
            <el-input v-model="projectScopeCode" placeholder="领域编码(可选)" clearable size="medium" @keyup.enter.native="getList(1)" />
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
          @row-click="openDetail"
        >
          <el-table-column min-width="10" />
          <el-table-column label="ID" prop="id" min-width="80" />
          <el-table-column label="项目领域" prop="projectScope" min-width="160" show-overflow-tooltip />
          <el-table-column label="买方单价" prop="buyerUnitPrice" min-width="120" />
          <el-table-column label="卖方单价" prop="sellerUnitPrice" min-width="120" />
          <el-table-column label="买方量" prop="buyerTradeQuantity" min-width="120" />
          <el-table-column label="卖方量" prop="sellerTradeQuantity" min-width="120" />
          <el-table-column label="创建时间" prop="createdTime" min-width="160" show-overflow-tooltip />
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

    <el-dialog title="询报价详情" :visible.sync="detailVisible" width="700px">
      <pre style="white-space: pre-wrap">{{ detail | json }}</pre>
      <div slot="footer" class="dialog-footer">
        <el-button class="light-green-btn" @click="detailVisible = false">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { getCarbonTradePriceList } from '@/api/carbonAssetApi'
import request from '@/utils/request'

export default {
  name: 'offer',
  data() {
    return {
      projectScopeCode: '',
      list: [],
      total: 0,
      current: 1,
      pageSize: 10,
      pageCount: 1,

      detailVisible: false,
      detail: null,
    }
  },
  filters: {
    json(v) {
      try {
        return JSON.stringify(v, null, 2)
      } catch (e) {
        return String(v)
      }
    },
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
      if (this.projectScopeCode) {
        param.projectScopeCode = this.projectScopeCode
      }
      getCarbonTradePriceList(param).then((res) => {
        this.list = (res && res.records) || []
        this.total = parseInt((res && res.total) || 0)
        this.current = parseInt((res && res.current) || page)
        this.pageCount = Math.ceil(this.total / this.pageSize)
      })
    },
    openDetail(row) {
      if (!row || !row.id) return
      request({
        url: '/trade/carbonTradePrice/info/' + row.id,
        method: 'GET',
        headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
      }).then((res) => {
        this.detail = res
        this.detailVisible = true
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
