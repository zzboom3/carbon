<template>
  <div class="root">
    <div class="divBox">
      <div class="content-container">
        <div class="myassets-div">
          <img class="icon" src="@/assets/icon/icon_my_assets.png" />
          <span style="margin-left: 8px" class="list-blue-text text-left">我的碳配额资产</span>
        </div>

        <div class="myassets-container">
          <span class="assets-hint">持仓总量</span>
          <span class="assets-text">{{ setNumber(topData.total) }}(tCO2e)</span>
          <div class="assets-line" />
          <span class="assets-hint">可用数量</span>
          <span class="assets-text">{{ setNumber(topData.availableAmount) }}(tCO2e)</span>
          <div class="assets-line" />
          <span class="assets-hint">锁定数量</span>
          <span class="assets-text">{{ setNumber(topData.lockedAmount) }}(tCO2e)</span>
          <div class="assets-line" />
          <span class="assets-hint">冻结数量</span>
          <span class="assets-text">{{ setNumber(topData.frozenAmount) }}(tCO2e)</span>
          <div class="empty-holder" />

          <button style="width: 68px; margin-left: 16px" class="normal-blue-btn" @click="onClickUpload">上传</button>
          <button style="width: 68px; margin-left: 16px" class="normal-white-btn vertical-center" @click="onClickBuy">
            我想买
          </button>
        </div>

        <div class="container">
          <div style="width: 270px; margin-right: 16px" class="selectbox-root">
            <a class="selectbox-hint cursor-mi">资产状态</a>
            <div class="selectbox-deliver" />
            <el-cascader
              style="width: 120px"
              placeholder="全部"
              class="selectbox-input"
              v-model="seletedAssetStatus"
              :options="assetStatusList"
              clearable
              @change="update"
            />
          </div>
          <div style="margin-right: 16px; padding-right: 0px" class="selectbox-root">
            <a class="selectbox-hint cursor-mi" style="width: 100px">有效日期</a>
            <div class="selectbox-deliver" />
            <el-date-picker
              v-model="selectDate"
              type="datetime"
              placeholder="选择开始时间"
              align="right"
              :picker-options="pickerOptions"
              @change="update"
              size="medium"
              value-format="yyyy-MM-dd HH:mm:ss"
            />
            <el-date-picker
              v-model="selectEndDate"
              type="datetime"
              placeholder="选择结束时间"
              align="right"
              :picker-options="pickerOptions"
              @change="update"
              size="medium"
              value-format="yyyy-MM-dd HH:mm:ss"
            />
          </div>

          <div style="flex-grow: 1; margin-left: 16px; margin-right: 16px" class="selectbox-root">
            <a class="selectbox-hint cursor-mi" style="min-width: 100px">关键词搜索</a>
            <div class="selectbox-deliver" />
            <el-input
              v-model="searchKeyWord"
              placeholder="请输入名称"
              clearable
              size="medium"
              @clear="onclickSearch"
              @keyup.enter.native="onclickSearch"
            />
          </div>
          <button style="margin: auto" class="light-green-btn" @click="onclickSearch">查询</button>
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
            stripe
            @cell-click="handle"
            :row-style="{ height: '64px' }"
            :cell-style="cellStyle"
            style="width: 100%"
          >
            <el-table-column min-width="10" />
            <el-table-column label="序号" align="left" min-width="40">
              <template slot-scope="scope">
                <span>{{ getCurListNo(scope.$index) }}</span>
              </template>
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              prop="agencyName"
              align="left"
              label="一级市场持有机构"
              min-width="120"
            />
            <el-table-column align="left" prop="total" label="持仓量(tCO2e)" min-width="90">
              <template slot-scope="scope">
                <span>{{ setNumber(scope.row.total) }}</span>
              </template>
            </el-table-column>
            <el-table-column align="left" prop="valuation" label="资产估值(¥)" min-width="90">
              <template slot-scope="scope">
                <span>{{ setNumber(scope.row.valuation) }}</span>
              </template>
            </el-table-column>
            <el-table-column align="left" prop="assetsStatusName" label="资产状态" min-width="60" />
            <el-table-column align="left" prop="expiryDate" label="有效日期" min-width="60" />
            <el-table-column label="操作" min-width="150" align="center">
              <template slot-scope="scope">
                <a class="list-blue-text" @click.stop="toDetail(scope.row.id)">查看</a>
                <a style="margin-left: 10px" class="list-blue-text" @click.stop="OutsizeTransaction(scope.row)">场外报价</a>
                <a style="margin-left: 10px" class="list-green-text" @click.stop="insideTransaction">场内交易</a>
              </template>
            </el-table-column>
          </el-table>
        </div>
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

      <el-dialog :title="title" :visible.sync="dialogFormVisible" width="720px">
        <el-form label-position="left" label-width="130px" :model="form">
          <el-form-item label="出售数量(tCO2e)" prop="tradeQuantity">
            <span class="require">*</span>
            <el-input v-model="form.tradeQuantity" size="medium" style="width: 268px; top: -5px" />
          </el-form-item>
          <el-form-item label="出售单价(￥)" prop="negotiatedPrice">
            <el-input v-model="form.assetUnitPrice" size="medium" style="width: 268px; top: -5px" />
          </el-form-item>
          <el-form-item label="出售截止时间" prop="expirationDate">
            <el-date-picker
              type="date"
              placeholder="选择日期"
              size="medium"
              v-model="form.expirationDate"
              value-format="yyyy-MM-dd HH:mm:ss"
              style="width: 268px; top: -5px"
            />
          </el-form-item>
          <el-form-item label="期望交割时间" prop="deliveryTime">
            <el-date-picker
              type="date"
              placeholder="选择日期"
              size="medium"
              v-model="form.deliveryTime"
              value-format="yyyy-MM-dd HH:mm:ss"
              style="width: 268px; top: -5px"
            />
          </el-form-item>
          <el-form-item label="期望交割方式" prop="deliveryMethod">
            <el-select
              v-model="form.deliveryMethod"
              placeholder="协议转入、竞价交易、定价交易"
              size="medium"
              style="width: 536px; top: -5px"
            >
              <el-option v-for="(item, index) in tradeMethods" :key="index" :label="item.name" :value="item.value" />
            </el-select>
          </el-form-item>
          <el-form-item label="期望交割场所" prop="deliveryExchange">
            <el-select
              v-model="form.deliveryExchange"
              placeholder="全国碳排放权交易中心、北京环境交易所、上海环境能源交易所"
              size="medium"
              style="width: 536px; top: -5px"
            >
              <el-option v-for="(item, index) in exchangeList" :key="index" :label="item.name" :value="item.value" />
            </el-select>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button type="primary" @click="submit('form')" class="light-green-btn">确 定</el-button>
        </div>
      </el-dialog>

      <el-dialog title="上架成功" :visible.sync="showQuotation" width="30%">
        <span>您的采购单已提交，可在供需行情中查看。确定为您跳转供需行情沟通</span>
        <span slot="footer" class="dialog-footer">
          <el-button @click="showQuotation = false">取 消</el-button>
          <el-button @click="toQuotation" type="primary">确 定</el-button>
        </span>
      </el-dialog>

      <BuyAssets :dialogFormVisible="buyAssetsDlg" @changeBuyAssetsDialogFormVisible="changeDialogFormVisible" />

      <carbonUploadVue
        :dialogFormVisible="carbonUploadDlg"
        :selData="list"
        :isCredit="false"
        title="碳配额项目上传"
        @changeVisible="changeCarbonVisible"
        @submit="submited"
      />
    </div>
  </div>
</template>

<script>
import { getExchangeDict, getDiliveryMethodeDict, getAssetStatusDict } from "@/config/dictHelper";
import carbonUploadVue from "./carbonUpload.vue";
import { setListNo, setLargeNumber } from "@/libs/public";
import * as quota from "@/api/carbonAssetApi";
import BuyAssets from "@/views/carbonTrade/quotation/buyAssets.vue";

export default {
  name: "quota",
  components: { BuyAssets, carbonUploadVue },
  data() {
    return {
      pickerOptions: {
        disabledDate(time) {
          return time.getTime() < new Date().setTime(new Date().getTime() - 3600 * 1000 * 24);
        },
      },
      exchangeList: [],
      tradeMethods: [],
      searchKeyWord: "",
      assetStatusList: [],
      assetStatusMap: {},
      dialogFormVisible: false,
      buyAssetsDlg: false,
      list: [],
      total: 0,
      current: 1,
      carbonUploadDlg: false,
      pageCount: 1,
      showQuotation: false,
      sellCarbonTotal: 0,
      loadingTop: false,
      topData: {
        total: 0,
        availableAmount: 0,
        lockedAmount: 0,
        frozenAmount: 0,
      },
      form: {
        id: 0,
        tradeQuantity: "",
        assetUnitPrice: "",
        expirationDate: "",
        deliveryTime: "",
        deliveryMethod: "",
        deliveryExchange: "",
        projectType: "",
        tradeRole: "0270000002",
        status: "",
        projectId: null,
        assetType: "0140000002",
        institutionName: "",
      },
      title: "",
      pageSize: 10,
      seletedAssetStatus: [],
      selectDate: "",
      selectEndDate: "",
    };
  },
  mounted() {
    this.getList(1);
    this.getTopData();
    this.exchangeList = getExchangeDict(this.$store);
    this.tradeMethods = getDiliveryMethodeDict(this.$store);
    this.formatAssetStatus(getAssetStatusDict(this.$store));
  },
  activated() {
    this.getTopData();
    this.getList(this.current || 1);
  },
  methods: {
    setNumber(str) {
      return setLargeNumber(str);
    },
    showTip() {
      this.showQuotation = true;
    },
    toQuotation() {
      this.$router.push("/trade/quotation");
    },
    changeCarbonVisible(res) {
      this.carbonUploadDlg = res;
    },
    changeDialogFormVisible(res) {
      this.buyAssetsDlg = res;
    },
    cellStyle({ column }) {
      if (column.label !== "操作") {
        return "cursor:pointer;";
      }
      return "";
    },
    normalizeRow(row) {
      const v = { ...(row || {}) };
      v.assetsStatusName = this.assetStatusMap[v.assetsStatus] || v.assetsStatusName;
      if (v.expiryDate) {
        const time = String(v.expiryDate).split(" ");
        v.expiryDate = time[0];
      }
      Object.keys(v).forEach((k) => {
        v[k] = v[k] ? v[k] : "--";
        if (v[k] === " ") v[k] = "--";
      });
      return v;
    },
    submited(res) {
      if (res) {
        this.getList(1);
        this.getTopData();
      }
    },
    formatAssetStatus(data) {
      (data || []).forEach((v) => {
        const item = { label: "" };
        if (v.name === "全部") {
          item.label = v.name;
        } else {
          item.value = v.value;
          item.label = v.name;
          this.assetStatusMap[v.value] = v.name;
        }
        this.assetStatusList.push(item);
      });
    },
    onClickBuy() {
      this.buyAssetsDlg = true;
    },
    toDetail(id) {
      this.$router.push({ path: "/assets/quota/quotaDetail", query: { id } });
    },
    buildQuery(page) {
      const data = {
        asc: true,
        current: page,
        size: this.pageSize,
        sortField: "",
      };
      if (this.seletedAssetStatus && this.seletedAssetStatus[0]) {
        data.assetsStatus = this.seletedAssetStatus[0];
      }
      if (this.selectDate) data.expiryDateStart = this.selectDate;
      if (this.selectEndDate) data.expiryDateEnd = this.selectEndDate;
      if (this.searchKeyWord) data.agencyName = this.searchKeyWord;
      return data;
    },
    onclickSearch() {
      this.current = 1;
      this.getList(1);
    },
    OutsizeTransaction(row) {
      this.dialogFormVisible = true;
      this.title = "场外报价：" + row.agencyName;
      this.form.id = row.id;
      this.form.projectId = row.carbonProjectId;
      this.form.assetType = row.transactionStatus;
      this.form.institutionName = row.agencyName;
      this.sellCarbonTotal = row.availableAmount;
    },
    handleSizeChange(val) {
      this.pageSize = val;
      this.getList(1);
    },
    handleCurrentChange(val) {
      this.current = val;
      this.getList(val);
    },
    handle(row, column) {
      if (column.label !== "操作") {
        this.toDetail(row.id);
      }
    },
    getList(page) {
      const data = this.buildQuery(page);
      quota
        .loadCarbonQuotaPageList(data)
        .then((res) => {
          const records = (res && res.data && res.data.records) || [];
          this.list = records.map(this.normalizeRow);
          this.total = Number((res && res.data && res.data.total) || 0);
          this.current = Number((res && res.data && res.data.current) || page);
          this.pageCount = Math.ceil(parseInt(this.total, 10) / this.pageSize) || 1;
        })
        .catch(() => {});
    },
    update() {
      this.getList(1);
    },
    submit() {
      const data = {
        id: this.form.id,
        transactionStatus: "0160000001",
      };
      if (Number(this.form.tradeQuantity) > Number(this.sellCarbonTotal)) {
        this.$message.warning("出售数量不能大于可用量");
        return;
      }

      if (this.form.tradeQuantity) {
        this.form.assetType = "0140000002";
        quota.addcarbonAssetMarket(this.form).then(
          () => {
            quota.changeQuota(data).then(
              () => {
                this.$message.success("操作成功");
                this.showTip();
                this.dialogFormVisible = false;
                this.getList(1);
                this.getTopData();
              },
              () => {}
            );
          },
          () => {
            this.$message.warning("操作失败");
          }
        );
      } else {
        this.$message.warning("必填项不能为空");
      }
    },
    onClickUpload() {
      this.carbonUploadDlg = true;
    },
    getTopData() {
      if (this.loadingTop) return;
      this.loadingTop = true;
      quota
        .getQuotaData()
        .then((res) => {
          this.topData = (res && res.data) || this.topData;
        })
        .finally(() => {
          this.loadingTop = false;
        });
    },
    insideTransaction() {
      this.$router.push("/trade/account/exchange");
    },
    getCurListNo(index) {
      const curNo = parseInt(index, 10) + 1;
      const page = this.current - 1;
      const no = setListNo(page, this.pageSize, curNo);
      return no ? no : 1;
    },
    onClickExport() {
      this.$message.success("功能即将推出，敬请期待！");
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

::v-deep .el-date-picker.has-sidebar.has-time {
  background: #0a5857d6;
  color: #fff;
  border: 1px solid #22f4d6;
}

::v-deep .el-date-picker__header-label {
  color: #ffffff;
}

.myassets-div {
  width: 184px;
  display: flex;
  flex-direction: row;
}

.icon {
  width: 24px;
  height: 24px;
}

.text-left {
  margin: auto;
  font-weight: 500;
  color: #24a776;
}

.myassets-container {
  display: flex;
  flex-direction: row;
  margin-top: 15px;
  margin-bottom: 20px;
  padding-left: 10px;
  padding-right: 10px;
  height: 54px;
  background: #e3f2ec;
  border-radius: 6px;
}

.assets-hint {
  margin-top: auto;
  margin-bottom: auto;
  font-weight: 400;
  color: #424c5c;
}

.assets-text {
  margin-top: auto;
  margin-bottom: auto;
  margin-left: 6px;
  font-weight: 500;
  color: #24a776;
}

.assets-line {
  margin: auto;
  margin-left: 10px;
  margin-right: 10px;
  width: 1px;
  height: 18px;
  border: 1px solid rgba(38, 181, 129, 0.5);
}

.empty-holder {
  flex-grow: 1;
}

.vertical-center {
  margin-top: auto;
  margin-bottom: auto;
}

.cursor-mi {
  cursor: default;
}

.require {
  color: red;
  position: absolute;
  left: -20px;
}
</style>
