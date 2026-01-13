<template>
  <div class="root">
    <div class="divBox">
      <div class="content-container">
        <div class="container">
          <div style="width: 210px" class="selectbox-root">
            <a class="selectbox-hint cursor-mi" style="width: 80px">核证标准</a>
            <div class="selectbox-deliver" />
            <el-cascader
              style="width: 120px"
              placeholder="全部"
              class="selectbox-input"
              v-model="seletedStandard"
              :options="optionsStandard"
              clearable
              @change="update"
            />
          </div>
          <div style="width: 210px; margin-left: 16px" class="selectbox-root">
            <a class="selectbox-hint cursor-mi" style="width: 60px">领域</a>
            <div class="selectbox-deliver" />
            <el-cascader
              style="width: 120px"
              placeholder="全部"
              class="selectbox-input"
              v-model="seletedIndustry"
              :options="optionsIndustory"
              clearable
              @change="update"
            />
          </div>
          <div style="width: 210px; margin-left: 16px" class="selectbox-root">
            <a class="selectbox-hint cursor-mi" style="width: 60px">状态</a>
            <div class="selectbox-deliver" />
            <el-cascader
              style="width: 120px"
              placeholder="全部"
              class="selectbox-input"
              v-model="seletedStatus"
              :options="optionsOnlines"
              clearable
              @change="update"
            />
          </div>
          <div style="width: 210px; margin-left: 16px" class="selectbox-root">
            <a class="selectbox-hint cursor-mi" style="width: 80px">资产状态</a>
            <div class="selectbox-deliver" />
            <el-cascader
              style="width: 120px"
              placeholder="全部"
              class="selectbox-input"
              v-model="seletedAssetsStatus"
              :options="optionsAssetStatus"
              clearable
              @change="update"
            />
          </div>
        </div>

        <div class="container" style="margin-top: 0">
          <div style="width: 500px" class="selectbox-root">
            <a class="selectbox-hint cursor-mi" style="width: 80px">签发日期</a>
            <div class="selectbox-deliver" />
            <el-date-picker
              style="width: 200px"
              v-model="selectDate"
              type="date"
              placeholder="开始日期"
              align="right"
              size="medium"
              value-format="yyyy-MM-dd"
              @change="update"
            />
            <el-date-picker
              style="width: 200px"
              v-model="selectEndDate"
              type="date"
              placeholder="结束日期"
              align="right"
              size="medium"
              value-format="yyyy-MM-dd"
              @change="update"
            />
          </div>
          <div style="flex-grow: 1; margin-left: 16px" class="selectbox-root">
            <a class="selectbox-hint cursor-mi" style="width: 120px">按项目搜索</a>
            <div class="selectbox-deliver" />
            <el-input
              class="selectbox-input"
              v-model="searchProjectKeyword"
              placeholder="输入名称"
              clearable
              size="medium"
              @keyup.enter.native="update"
              @clear="update"
            />
          </div>
        </div>

        <div class="container" style="margin-top: 0">
          <div style="flex-grow: 1; margin-right: 16px" class="selectbox-root">
            <a class="selectbox-hint cursor-mi" style="width: 120px">按方法学搜索</a>
            <div class="selectbox-deliver" />
            <el-input
              class="selectbox-input"
              v-model="searchMethodKeyword"
              placeholder="输入名称"
              clearable
              size="medium"
              @keyup.enter.native="update"
              @clear="update"
            />
          </div>
          <button class="light-green-btn" @click="update">查询</button>
          <button style="margin-left: 16px" class="light-blue-btn" @click="insideTransaction">
            场内交易
          </button>
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
            <el-table-column :show-overflow-tooltip="true" prop="projectName" label="项目名称" min-width="200" />
            <el-table-column :show-overflow-tooltip="true" prop="carbonMethodologyName" label="方法学" min-width="160" />
            <el-table-column prop="industryCodeName" label="类型" min-width="120" />
            <el-table-column prop="certificationCriteriaName" label="核证标准" min-width="120" />
            <el-table-column prop="transactionStatusName" label="交易状态" min-width="120" />
            <el-table-column prop="assetsStatusName" label="资产状态" min-width="120" />
            <el-table-column prop="issuingDate" label="签发日期" min-width="120" />
            <el-table-column label="操作" min-width="120" align="center" fixed="right">
              <template slot-scope="scope">
                <a class="list-blue-text" @click.stop="toDetail(scope.row)">查看</a>
              </template>
            </el-table-column>
          </el-table>
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
  </div>
</template>
<script>
import { loadCarbonCreditPageList } from "@/api/carbonAssetApi";
import {
  getExchangeDict,
  getDiliveryMethodeDict,
  getCertificationCriteriaDict,
  getProjectAreaDict,
  getAssetTradeStatusDict,
  getAssetStatusDict,
} from "@/config/dictHelper";
import { setListNo } from "@/libs/public";

export default {
  name: "carbonCredit",
  data() {
    return {
      list: [],
      total: 0,
      current: 1,
      pageCount: 1,
      pageSize: 10,

      exchangeList: [],
      tradeMethods: [],

      optionsStandard: [],
      optionsIndustory: [],
      optionsOnlines: [],
      optionsAssetStatus: [],

      seletedStandard: [],
      seletedIndustry: [],
      seletedStatus: [],
      seletedAssetsStatus: [],

      selectDate: "",
      selectEndDate: "",
      searchProjectKeyword: "",
      searchMethodKeyword: "",
    };
  },
  mounted() {
    this.getList(1);
    this.exchangeList = getExchangeDict(this.$store);
    this.tradeMethods = getDiliveryMethodeDict(this.$store);
    this.formatCertification(getCertificationCriteriaDict(this.$store));
    this.formatIndustory(getProjectAreaDict(this.$store));
    this.formatStatus(getAssetTradeStatusDict(this.$store));
    this.formatAssetStatus(getAssetStatusDict(this.$store));
  },
  methods: {
    getCurListNo(index) {
      const curNo = parseInt(index, 10) + 1;
      const page = this.current - 1;
      return setListNo(page, this.pageSize, curNo);
    },
    normalizeRow(row) {
      const v = { ...(row || {}) };

      if (v.issuingDate) {
        const time = String(v.issuingDate).split(" ");
        v.issuingDate = time[0];
      }

      if (!v.carbonMethodologyName || v.carbonMethodologyName === " ") {
        v.carbonMethodologyName = v.methodName || "--";
      }

      Object.keys(v).forEach((k) => {
        v[k] = v[k] ? v[k] : "--";
        if (v[k] === " ") v[k] = "--";
      });
      return v;
    },
    toDetail(row) {
      if (!row || !row.id || row.id === "--") return;
      this.$router.push({ path: "/assets/credit/assetDetail", query: { id: row.id } });
    },
    insideTransaction() {
      this.$router.push("/trade/account/exchange");
    },
    buildQuery(page) {
      const data = {
        asc: true,
        current: page,
        size: this.pageSize,
      };

      const certificationCriteria = this.seletedStandard && this.seletedStandard[0];
      const transactionStatus = this.seletedStatus && this.seletedStatus[0];
      const assetsStatus = this.seletedAssetsStatus && this.seletedAssetsStatus[0];
      const projectScopeCode = this.seletedIndustry && this.seletedIndustry[0];

      if (certificationCriteria) data.certificationCriteria = certificationCriteria;
      if (transactionStatus) data.transactionStatus = transactionStatus;
      if (assetsStatus) data.assetsStatus = assetsStatus;
      if (projectScopeCode) data.projectScopeCode = projectScopeCode;
      if (this.selectDate) data.issuingDateStart = this.selectDate;
      if (this.selectEndDate) data.issuingDateEnd = this.selectEndDate;
      if (this.searchProjectKeyword) data.projectName = this.searchProjectKeyword;
      if (this.searchMethodKeyword) data.methodName = this.searchMethodKeyword;

      return data;
    },
    update() {
      this.getList(1);
    },
    getList(page) {
      const data = this.buildQuery(page);
      loadCarbonCreditPageList(data)
        .then((res) => {
          const records = (res && res.data && res.data.records) || [];
          this.list = records.map(this.normalizeRow);
          this.total = Number(res.data.total || 0);
          this.current = Number(res.data.current || page);
          this.pageCount = Math.ceil(parseInt(this.total, 10) / this.pageSize) || 1;
        })
        .catch(() => {});
    },
    handleSizeChange(val) {
      this.pageSize = val;
      this.getList(1);
    },
    handleCurrentChange(val) {
      this.current = val;
      this.getList(val);
    },
    formatCertification(data) {
      (data || []).forEach((v) => {
        const item = { label: "" };
        if (v.name === "全部") {
          item.label = v.name;
        } else {
          item.value = v.value;
          item.label = v.name;
        }
        this.optionsStandard.push(item);
      });
    },
    formatIndustory(data) {
      (data || []).forEach((v) => {
        const item = { label: "" };
        if (v.name === "全部") {
          item.label = v.name;
        } else {
          item.value = v.value;
          item.label = v.name;
        }
        this.optionsIndustory.push(item);
      });
    },
    formatStatus(data) {
      (data || []).forEach((v) => {
        const item = { label: "" };
        if (v.name === "全部") {
          item.label = v.name;
        } else {
          item.value = v.value;
          item.label = v.name;
        }
        this.optionsOnlines.push(item);
      });
    },
    formatAssetStatus(data) {
      (data || []).forEach((v) => {
        const item = { label: "" };
        if (v.name === "全部") {
          item.label = v.name;
        } else {
          item.value = v.value;
          item.label = v.name;
        }
        this.optionsAssetStatus.push(item);
      });
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
  margin: 20px 0px 20px 0px;
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

.cursor-mi {
  cursor: default;
}
</style>
