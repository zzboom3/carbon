<template>
  <div class="root">
    <div class="search-container">
      <div style="width: 280px; margin-right: 16px" class="selectbox-root">
        <a class="selectbox-hint cursor-mi" style="min-width: 70px">资产类型</a>
        <div class="selectbox-deliver" />
        <el-cascader
          style="width: 120px"
          class="selectbox-input"
          v-model="seletedAssetStatus"
          :options="assetStatusList"
          @change="onClickSearch"
          clearable
        />
      </div>
      <div style="flex-grow: 1; margin-right: 16px" class="selectbox-root">
        <a class="selectbox-hint cursor-mi" style="min-width: 70px">项目领域</a>
        <div class="selectbox-deliver" />
        <el-cascader
          style="width: 120px"
          placeholder="全部"
          class="selectbox-input"
          v-model="seletedField"
          @change="onClickSearch"
          :options="projectField"
          clearable
        />
      </div>
      <div style="flex-grow: 1; margin-right: 16px" class="selectbox-root">
        <a class="selectbox-hint cursor-mi" style="min-width: 70px">项目类型</a>
        <div class="selectbox-deliver" />
        <el-cascader
          style="width: 120px"
          placeholder="全部"
          class="selectbox-input"
          v-model="seletedProject"
          @change="onClickSearch"
          :options="projectList"
          clearable
          :disabled="isProjectTypeDisable"
        />
      </div>
      <div style="width: 430px" class="selectbox-root">
        <a class="selectbox-hint cursor-mi" style="min-width: 100px">采购截止日期</a>
        <div class="selectbox-deliver" />
        <el-date-picker
          type="date"
          style="width: 210px"
          v-model="selectDate"
          placeholder="选择开始日期"
          align="right"
          @change="onClickSearch"
          :picker-options="pickerOptions"
          size="medium"
          value-format="yyyy-MM-dd"
        />
        <el-date-picker
          v-model="selectEndDate"
          type="date"
          placeholder="选择结束日期"
          align="right"
          size="medium"
          :picker-options="pickerOptions"
          @change="onClickSearch"
          value-format="yyyy-MM-dd"
        />
      </div>
    </div>

    <div class="search-container" style="margin-top: 0">
      <div style="width: 600px; margin-right: 16px" class="selectbox-root">
        <el-input
          style="width: 600px"
          class="selectbox-input"
          v-model="searchKeyWord"
          @keyup.enter.native="onClickSearch"
          clearable
          @clear="onClickSearch"
        />
      </div>
      <button class="light-green-btn" @click="onClickSearch">搜索</button>
    </div>

    <div class="content-container">
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
        style="width: 100%"
      >
        <el-table-column min-width="3%" />
        <el-table-column label="序号" align="left" min-width="10%">
          <template slot-scope="scope">
            <span>{{ getCurListNo(scope.$index) }}</span>
          </template>
        </el-table-column>
        <el-table-column align="left" prop="assetTypeName" label="资产类型" min-width="15%" />
        <el-table-column align="left" prop="projectTypeName" label="项目类型" min-width="15%" />
        <el-table-column
          :show-overflow-tooltip="true"
          prop="publisherName"
          label="机构名称"
          align="left"
          min-width="30%"
        />
        <el-table-column align="left" prop="tradeQuantity" label="采购量(tCO2e)" min-width="15%" />
        <el-table-column align="left" prop="assetUnitPrice" label="询价(元/tCO2e)" min-width="15%" />
        <el-table-column align="left" prop="expirationDate" label="采购截止日期" min-width="10%" />
        <el-table-column label="操作" align="center" min-width="15%">
          <template slot-scope="scope">
            <a style="margin-left: 10px" class="list-blue-text" @click.stop="clickDetail(scope.row)"
              >查看</a
            >
            <a style="margin-left: 10px" class="list-green-text" @click.stop="clickSell(scope.row)"
              >{{ btnText }}</a
            >
          </template>
        </el-table-column>
      </el-table>
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
        :page-count="pageCount"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      />
    </div>

    <buy-order
      :form="orderForm"
      :title="title"
      :dialogFormVisible="orderDialogFormVisible"
      @changeDialogFormVisible="changeOrderDialogFormVisible"
      @successSubmit="showOrderResult"
    />
    <buy-detail
      :form="form"
      :title="title"
      :dialogFormVisible="detailDialogFormVisible"
      @changeDialogFormVisible="changeDetailDialogFormVisible"
      @successSubmit="closeDetail"
    />
    <order-result
      :title="tipTitle"
      :content="tipConetent"
      :bottomTxt="tipBottomTxt"
      :img="tipImg"
      :dialogFormVisible="comformDialogFormVisible"
      @changeComfromDialogVisible="listencomformDialogFormVisible"
    />
  </div>
</template>

<script>
import { getCarbonTradeQuoteList } from "@/api/carbonAssetApi";
import { setListNo, isProjectTypeDisable } from "@/libs/public";
import { getAssetTypeDict, getProjectTypeDict, getProjectAreaDict } from "@/config/dictHelper";
import BuyOrder from "./buyOrder.vue";
import BuyDetail from "./buyDetail.vue";
import OrderResult from "../orderResult.vue";

export default {
  name: "buyQuote",
  components: { BuyOrder, BuyDetail, OrderResult },
  data() {
    return {
      list: [],
      total: 0,
      current: 1,
      pageCount: 1,
      pageSize: 10,

      projectField: [],
      assetStatusList: [],
      projectList: [],

      seletedAssetStatus: [],
      seletedField: [],
      seletedProject: [],
      selectDate: "",
      selectEndDate: "",
      searchKeyWord: "",
      isSearch: false,
      isProjectTypeDisable: false,

      title: "",
      btnText: "报价",
      form: null,
      orderForm: null,
      orderDialogFormVisible: false,
      detailDialogFormVisible: false,
      comformDialogFormVisible: false,

      tipTitle: "报价提示",
      tipConetent: "您的报价已提交，可在询报价管理中查看，确定为您转跳询报价管理",
      tipBottomTxt: "如需帮助，可添加交易专员企业微信，“为您做交易引导服务",
      tipImg: "@/assets/imgs/head.gif",

      pickerOptions: {
        shortcuts: [
          { text: "今天", onClick: (picker) => picker.$emit("pick", new Date()) },
          {
            text: "昨天",
            onClick: (picker) => {
              const date = new Date();
              date.setTime(date.getTime() - 3600 * 1000 * 24);
              picker.$emit("pick", date);
            },
          },
          {
            text: "一周前",
            onClick: (picker) => {
              const date = new Date();
              date.setTime(date.getTime() - 3600 * 1000 * 24 * 7);
              picker.$emit("pick", date);
            },
          },
        ],
      },
    };
  },
  mounted() {
    this.formatFieldDict(getProjectAreaDict(this.$store));
    this.formatAssetDict(getAssetTypeDict(this.$store));
    this.formatProjectTypeDict(getProjectTypeDict(this.$store));
    this.getList(1);
  },
  methods: {
    getCurListNo(index) {
      const curNo = parseInt(index, 10) + 1;
      const page = this.current - 1;
      return setListNo(page, this.pageSize, curNo);
    },
    buildQuery(page) {
      const assetType = (this.seletedAssetStatus && this.seletedAssetStatus[0]) || "";
      const projectScopeCode = (this.seletedField && this.seletedField[0]) || "";
      const projectType = (this.seletedProject && this.seletedProject[0]) || "";

      return {
        asc: true,
        current: page,
        size: this.pageSize,
        assetType,
        projectScopeCode,
        projectType,
        expirationDateStart: this.selectDate,
        expirationDateEnd: this.selectEndDate,
        tradeRole: "0270000001",
        keyword: this.searchKeyWord || undefined,
      };
    },
    fetchList(page) {
      const data = this.buildQuery(page);
      getCarbonTradeQuoteList(data)
        .then((res) => {
          const records = (res && res.data && res.data.records) || [];
          this.list = records.map((v) => {
            const row = { ...v };
            Object.keys(row).forEach((k) => {
              if (!row[k] || row[k] === " ") row[k] = "--";
            });
            return row;
          });
          this.total = Number(res.data.total || 0);
          this.current = Number(res.data.current || page);
          this.pageCount = Math.ceil(this.total / this.pageSize) || 1;
        })
        .catch(() => {});
    },
    onClickSearch() {
      this.isSearch = !!this.searchKeyWord;
      this.fetchList(1);
    },
    getList(page) {
      this.isSearch = false;
      this.fetchList(page);
    },
    handleSizeChange(val) {
      this.pageSize = val;
      this.fetchList(1);
    },
    handleCurrentChange(val) {
      this.current = val;
      this.fetchList(val);
    },
    clickDetail(row) {
      this.title = "采购单";
      this.form = { ...row };
      this.detailDialogFormVisible = true;
      this.orderDialogFormVisible = false;
      this.comformDialogFormVisible = false;
    },
    clickSell(row) {
      this.title = "报价单";
      this.orderForm = { ...row };
      this.detailDialogFormVisible = false;
      this.orderDialogFormVisible = true;
      this.comformDialogFormVisible = false;
    },
    changeOrderDialogFormVisible(res) {
      this.orderDialogFormVisible = res;
    },
    changeDetailDialogFormVisible(res) {
      this.detailDialogFormVisible = res;
    },
    showOrderResult() {
      this.detailDialogFormVisible = false;
      this.orderDialogFormVisible = false;
      this.comformDialogFormVisible = true;
    },
    closeDetail(res) {
      this.detailDialogFormVisible = res;
    },
    listencomformDialogFormVisible(res) {
      this.comformDialogFormVisible = res;
      if (!res) {
        this.$router.push("/trade/offer");
      }
    },
    formatFieldDict(data) {
      const list = data || [];
      this.projectField = list.map((v) => ({ value: v.value, label: v.name }));
    },
    formatAssetDict(data) {
      const list = data || [];
      this.assetStatusList = list.map((v) => ({ value: v.value, label: v.name }));
      if (this.assetStatusList[0]) this.assetStatusList[0].value = "";
    },
    formatProjectTypeDict(data) {
      const list = data || [];
      this.projectList = list.map((v) => ({ value: v.value, label: v.name }));
    },
    changeAssetType() {
      this.isProjectTypeDisable = isProjectTypeDisable(
        (this.seletedAssetStatus && this.seletedAssetStatus[0]) || ""
      );
      this.onClickSearch();
    },
  },
};
</script>

<style lang="scss" scoped>
.root {
  display: flex;
  flex-direction: column;
  background: white;
}

.search-container {
  display: flex;
  flex-direction: row;
  width: 100%;
  margin-top: 10px;
  margin-bottom: 20px;
}

.content-container {
  flex-grow: 1;
  display: flex;
  flex-direction: column;
  width: 100%;
}

>>> .el-cascader .el-input .el-input__inner,
>>> .el-cascader .el-input.is-focus .el-input__inner {
  border-color: transparent;
}

.cursor-mi {
  cursor: default;
}
</style>
