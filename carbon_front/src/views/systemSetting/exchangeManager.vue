<template>
  <div class="root">
    <div class="divBox">
      <div class="content-container">
        <div class="container">
          <div style="width: 390px" class="selectbox-root">
            <span class="selectbox-hint" style="width: 60px">搜索</span>
            <div class="selectbox-deliver" />
            <el-input
              v-model="searchKeyword"
              placeholder="名称、编号、关键字等"
              clearable
              size="medium"
              @keyup.enter.native="onClickSearch"
              @clear="onClickSearch"
            />
          </div>
          <button style="margin-left: 16px" class="light-green-btn" @click="onClickSearch">查询</button>
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
            stripe
            style="width: 100%"
          >
            <el-table-column min-width="10" />
            <el-table-column label="序号" align="left" min-width="60">
              <template slot-scope="scope">
                <span>{{ getCurListNo(scope.$index) }}</span>
              </template>
            </el-table-column>
            <el-table-column :show-overflow-tooltip="true" prop="name" label="交易所名称" align="left" width="150" />
            <el-table-column align="center" prop="city" label="城市" width="100" />
            <el-table-column align="left" prop="businessScope" label="业务范围" width="600" />
            <el-table-column label="操作" align="center" min-width="170">
              <template slot-scope="scope">
                <a style="margin-left: 10px" class="list-blue-text" @click="toDetail(scope.row)">查看</a>
                <a style="margin-left: 10px" @click="toExchange(scope.row.website, 0)" class="list-green-text">开户</a>
                <a style="margin-left: 10px" class="list-green-text" @click="toExchange(scope.row.website, 1)">交易</a>
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

      <el-dialog title="提示" :visible.sync="dialogVisible" width="560px">
        <span class="tips">{{ dialogVisibleText }}</span>
        <el-button
          type="primary"
          @click="openAccount"
          class="light-green-btn"
          style="float: right; margin-right: 30px"
        >
          确 定
        </el-button>
        <div style="clear: both"></div>
        <div class="dialog-footer">
          <span class="footer-tip">如需帮助，可添加交易专员企业微信，为您做开户引导服务</span>
          <img :src="qrUrl" alt="" class="img" />
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import { loadCarbonExchangeList } from "@/api/carbonAssetApi";
import { setListNo } from "@/libs/public";
import { openUrlInNewWindow } from "@/libs/OpenHelper";
import { getWeChatQrUrl } from "@/utils/wechatQr";

export default {
  name: "exchangeManager",
  data() {
    return {
      searchKeyword: "",
      list: [],
      total: 0,
      current: 1,
      dialogVisible: false,
      dialogVisibleText: "是否确认开户？",
      pageCount: 1,
      pageSize: 10,
      exchangesUrl: "https://carbonmsger.feishu.cn/drive/folder/fldcn66yo6D4OoXwZqEMHL6OQSg?from=space_persnoal_filelist",
      openUrl: "",
      qrUrl: getWeChatQrUrl(),
    };
  },
  mounted() {
    this.getList(1);
  },
  methods: {
    normalizeRow(row) {
      const v = { ...(row || {}) };
      v.name = v.name ? v.name : "--";
      v.businessScope = v.businessScope ? v.businessScope : "--";
      v.city = v.city ? v.city : "--";
      Object.keys(v).forEach((k) => {
        v[k] = v[k] ? v[k] : "--";
        if (v[k] === " ") v[k] = "--";
      });
      return v;
    },
    onClickAdd() {
      openUrlInNewWindow(this.exchangesUrl);
    },
    onClickSearch() {
      this.current = 1;
      this.getList(1);
    },
    toExchange(url, type) {
      this.dialogVisibleText = type ? "是否确认交易？" : "是否确认开户？";
      this.dialogVisible = true;
      this.openUrl = url;
    },
    toDetail(row) {
      if (row && row.detailUrl) {
        openUrlInNewWindow(row.detailUrl);
      } else {
        this.$message.warning("没有对应的url");
      }
    },
    openAccount() {
      this.dialogVisible = false;
      if (this.openUrl) {
        openUrlInNewWindow(this.openUrl);
      } else {
        this.$message.warning("没有对应的url");
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
        status: 1,
        searchKey: this.searchKeyword,
      };
      loadCarbonExchangeList(data)
        .then((res) => {
          const records = (res && res.data && res.data.records) || [];
          this.list = records.map(this.normalizeRow);
          this.total = Number((res && res.data && res.data.total) || 0);
          this.current = Number((res && res.data && res.data.current) || page);
          this.pageCount = Math.ceil(parseInt(this.total, 10) / this.pageSize) || 1;
        })
        .catch(() => {});
    },
    getCurListNo(index) {
      const curNo = parseInt(index, 10) + 1;
      const page = this.current - 1;
      const no = setListNo(page, this.pageSize, curNo);
      return no ? no : 1;
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

::v-deep .el-cascader .el-input .el-input__inner,
::v-deep .el-cascader .el-input.is-focus .el-input__inner {
  border-color: transparent;
}

.tips {
  width: 112px;
  height: 16px;
  font-weight: 400;
  color: #424c5c;
}

.dialog-footer {
  width: 560px;
  height: 110px;
  background: rgba(38, 181, 129, 0.1);
  border-radius: 0px 0px 8px 8px;
  margin-top: 20px;
  position: relative;
  left: -20px;
  bottom: -20px;
}

.footer-tip {
  width: 416px;
  height: 16px;
  font-weight: 400;
  color: #24a776;
  margin-left: 20px;
  position: relative;
}

.img {
  width: 60px;
  height: 60px;
  position: relative;
  top: 25px;
  left: 30px;
}
</style>
