<template>
  <div class="root">
    <div class="divBox">
      <div class="content-container">
        <div class="container">
          <div style="width: 390px" class="selectbox-root">
            <a class="selectbox-hint cursor-mi">搜索</a>
            <div class="selectbox-deliver" />
            <el-input
              class="selectbox-input"
              v-model="searchKeyword"
              placeholder="输入账户名或交易所名称"
              clearable
              size="medium"
              @keyup.enter.native="onClickSearch"
              @clear="onClickSearch"
            />
          </div>
          <button style="margin-left: 16px" class="light-green-btn" @click="onClickSearch">查询</button>
          <button style="margin-left: 16px" class="light-blue-btn" @click="onClickOpenAccount">+开户</button>
          <button style="margin-left: 16px; width: 120px" class="normal-white-btn" @click="onClickBindAccount">绑定交易账号</button>
        </div>

        <div>
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
            style="width: 100%"
            @row-click="handle"
          >
            <el-table-column min-width="10" />
            <el-table-column label="序号" align="left" min-width="60">
              <template slot-scope="scope">
                <span>{{ getCurListNo(scope.$index) }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="accountName" label="账号名" min-width="100" />
            <el-table-column prop="carbonExchangeName" label="交易所" min-width="120" />
            <el-table-column style="padding-right: 5px" prop="createdTime" label="开户时间" min-width="80" />
            <el-table-column prop="businessScope" label="业务范围" min-width="300" />
            <el-table-column label="操作" align="center" min-width="50">
              <template slot-scope="scope">
                <a class="list-green-text" @click.stop="readAccout(scope.row)">查看</a>
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
    </div>
  </div>
</template>
<script>
import { getTradeAccountList } from "@/api/tradeApi";
import { openUrlInNewWindow } from "@/libs/OpenHelper";
import { cursor } from "@/libs/element-table";
import { setListNo } from "@/libs/public";

export default {
  name: "tradeAccountManager",
  data() {
    return {
      searchKeyword: "",
      list: [],
      total: 0,
      current: 1,
      pageCount: 1,
      pageSize: 10,
    };
  },
  mounted() {
    this.getList(1);
  },
  methods: {
    cellStyle(data) {
      return cursor(data);
    },
    normalizeRow(row) {
      const v = { ...(row || {}) };
      Object.keys(v).forEach((k) => {
        v[k] = v[k] ? v[k] : "--";
        if (v[k] === " ") v[k] = "--";
      });
      return v;
    },
    onClickOpenAccount() {
      this.$router.push({ path: "/trade/account/exchange" });
    },
    onClickBindAccount() {
      this.$router.push({ path: "/account/info/", query: { path: "second" } });
    },
    getCurListNo(index) {
      const curNo = parseInt(index, 10) + 1;
      const page = this.current - 1;
      const no = setListNo(page, this.pageSize, curNo);
      return no ? no : 1;
    },
    onClickSearch() {
      this.current = 1;
      this.getList(1);
    },
    handle(row) {
      if (row && row.url && row.url !== "--") {
        openUrlInNewWindow(row.url);
      }
    },
    handleSizeChange(val) {
      this.pageSize = val;
      this.getList(1);
    },
    handleCurrentChange(val) {
      this.current = val;
      this.getList(val);
    },
    getList(page) {
      const data = {
        asc: true,
        current: page,
        size: this.pageSize,
        sortField: "",
        accountStatus: "",
        keyWords: this.searchKeyword,
      };
      getTradeAccountList(data)
        .then((res) => {
          const records = (res && res.data && res.data.records) || [];
          this.list = records.map(this.normalizeRow);
          this.total = Number((res && res.data && res.data.total) || 0);
          this.current = Number((res && res.data && res.data.current) || page);
          this.pageCount = Math.ceil(parseInt(this.total, 10) / this.pageSize) || 1;
        })
        .catch(() => {});
    },
    readAccout(row) {
      if (row && row.url && row.url !== "--") {
        openUrlInNewWindow(row.url);
      } else {
        this.$message.warning("没有对应的url");
      }
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
  margin: 10px 0px 20px 0px;
  display: flex;
  flex-direction: row;
}

.content-container {
  display: flex;
  flex-direction: column;
  width: 100%;
  padding: 0 20px 20px 20px;
}

.cursor-mi {
  cursor: default;
}
</style>
