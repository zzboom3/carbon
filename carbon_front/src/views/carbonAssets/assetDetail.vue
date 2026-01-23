<template>
  <div class="root">
    <div class="divBox">
      <div class="content-container">
        <div class="cardBody">
          <span class="header">碳信用详情</span>
          <span class="asset-title">{{ assetDetail["projectName"] }}</span>
          <el-divider></el-divider>

          <img src="@/assets/icon/icon_plant.png" alt="" class="icon" />
          <span class="asset-little-title">基本信息</span>
          <div class="basic-divx">
            <div class="item-div">
              <span class="item-title">核证标准</span>
              <br />
              <span class="item-content">{{ assetDetail["certificationCriteria"] }}</span>
            </div>
            <div class="item-div">
              <span class="item-title">采用方法学</span>
              <br />
              <span class="item-content">{{ assetDetail["carbonMethodologyName"] }}</span>
            </div>
            <div class="item-div">
              <span class="item-title">类型</span>
              <br />
              <span class="item-content">{{ assetDetail["industryCodeName"] }}</span>
            </div>
            <div class="item-div">
              <span class="item-title">核证机构</span>
              <br />
              <span class="item-content">{{ assetDetail["certifiedAgencyName"] }}</span>
            </div>
            <div class="item-div">
              <span class="item-title">有效期</span>
              <br />
              <span class="item-content">{{ assetDetail["expiryDate"] }}</span>
            </div>
            <div class="item-div">
              <span class="item-title">签发日期</span>
              <br />
              <span class="item-content">{{ assetDetail["issuingDate"] }}</span>
            </div>
            <div class="item-div">
              <span class="item-title">签发机构</span>
              <br />
              <span class="item-content">{{ assetDetail["issuingAgency"] }}</span>
            </div>
            <div class="item-div">
              <span class="item-title">签发凭证</span>
              <br />
              <span
                class="item-content"
                :style="styles(assetDetail['issuingCertificatesFileName'])"
                @click="toIssueFile"
              >
                {{ assetDetail["issuingCertificatesFileName"] | file }}
              </span>
            </div>
            <div class="item-div">
              <span class="item-title">一级市场持有机构</span>
              <br />
              <span class="item-content">{{ assetDetail["tenantName"] }}</span>
            </div>
          </div>

          <br />
          <br />

          <img src="@/assets/icon/icon_plant.png" alt="" class="icon" />
          <span class="asset-little-title">项目信息</span>
          <span
            v-if="assetDetail['projectIntroduction'] != '--'"
            class="detail"
            @click="toDetail"
          >
            查看详情<i class="el-icon-d-arrow-right"></i>
          </span>
          <div class="basic-div intro">
            <p class="paragraph">
              {{ assetDetail["projectIntroduction"] }}
            </p>
          </div>

          <br />
          <br />

          <img src="@/assets/icon/icon_plant.png" alt="" class="icon" />
          <span class="asset-little-title">持仓信息</span>
          <span class="detail" @click="toDetail">
            查看详情<i class="el-icon-d-arrow-right"></i>
          </span>
          <div class="basic-div compact">
            <div class="item-div">
              <span class="item-title">资产状态</span>
              <br />
              <span class="item-content">{{ assetDetail["assetsStatusName"] }}</span>
            </div>
            <div class="item-div">
              <span class="item-title">持仓总量(tCO2e)</span>
              <br />
              <span class="item-content">{{ setNumber(assetDetail["total"]) }}</span>
            </div>
            <div class="item-div">
              <span class="item-title">资产估值(¥)</span>
              <br />
              <span class="item-content">{{ setNumber(assetDetail["valuation"]) }}</span>
            </div>
            <div class="item-div">
              <span class="item-title">可用数量(tCO2e)</span>
              <br />
              <span class="item-content">{{ setNumber(assetDetail["availableAmount"]) }}</span>
            </div>
            <div class="item-div">
              <span class="item-title">锁定数量(tCO2e)</span>
              <br />
              <span class="item-content">{{ setNumber(assetDetail["lockedAmount"]) }}</span>
            </div>
            <div class="item-div">
              <span class="item-title">冻结数量(tCO2e)</span>
              <br />
              <span class="item-content">{{ setNumber(assetDetail["frozenAmount"]) }}</span>
            </div>
          </div>

          <br />
          <br />

          <img src="@/assets/icon/icon_plant.png" alt="" class="icon" />
          <span class="asset-little-title">交易信息</span>
          <span class="detail" @click="toCarbonExchangeWebsite">
            查看详情<i class="el-icon-d-arrow-right"></i>
          </span>
          <div class="basic-div compact">
            <div class="item-div">
              <span class="item-title">交易状态</span>
              <br />
              <span class="item-content">{{ assetDetail["transactionStatusName"] }}</span>
            </div>
            <div class="item-div">
              <span class="item-title">交割场所</span>
              <br />
              <span class="item-content">{{ assetDetail["carbonExchangeName"] }}</span>
            </div>
            <div class="item-div">
              <span class="item-title">购入凭证</span>
              <br />
              <span class="item-content file-link" @click="toBuyFile">
                {{ assetDetail["buyCertificateFileName"] | file }}
              </span>
            </div>
            <div class="item-div">
              <span class="item-title">购入单价(¥)</span>
              <br />
              <span class="item-content">{{ assetDetail["buyUnitPrice"] }}</span>
            </div>
            <div class="item-div">
              <span class="item-title">购入总价(¥)</span>
              <br />
              <span class="item-content">{{ assetDetail["buyTotalPrice"] }}</span>
            </div>
            <div class="item-div">
              <span class="item-title">购入日期</span>
              <br />
              <span class="item-content">{{ assetDetail["buyDate"] }}</span>
            </div>
          </div>

          <br />
          <br />

          <div class="basic-div compact action-bar">
            <button class="light-green-btn action-btn" @click="insideTransaction">
              场内交易
            </button>
            <button
              class="light-green-btn action-btn action-btn-gap"
              @click="ousideTransaction"
            >
              场外上架
            </button>
            <button class="normal-white-btn action-btn action-btn-gap" @click="goback">
              返回
            </button>
          </div>

          <el-dialog :title="title" :visible.sync="dialogFormVisible" width="720px">
            <el-form label-position="left" label-width="130px" :model="form">
              <el-form-item label="出售数量(tCO2e)" prop="tradeQuantity">
                <span class="require">*</span>
                <el-input v-model="form.tradeQuantity" size="medium" style="width: 268px; top: -5px" />
              </el-form-item>
              <el-form-item label="出售单价(￥)" prop="negotiatedPrice">
                <el-input v-model="form.negotiatedPrice" size="medium" style="width: 268px; top: -5px" />
              </el-form-item>
              <el-form-item label="出售截止时间" prop="expirationDate">
                <el-date-picker type="date" placeholder="选择日期" size="medium" v-model="form.expirationDate" style="width: 268px; top: -5px" />
              </el-form-item>
              <el-form-item label="期望交割时间" prop="deliveryTime">
                <el-date-picker type="date" placeholder="选择日期" size="medium" v-model="form.deliveryTime" style="width: 268px; top: -5px" />
              </el-form-item>
              <el-form-item label="期望交割方式" prop="deliveryMethod">
                <el-select v-model="form.deliveryMethod" placeholder="协议转入、竞价交易、定价交易" size="medium" style="width: 536px; top: -5px">
                  <el-option v-for="(item, index) in tradeMethods" :key="index" :label="item.name" :value="item.value" />
                </el-select>
              </el-form-item>
              <el-form-item label="期望交割场所" prop="deliveryExchange">
                <el-select v-model="form.deliveryExchange" placeholder="全国碳排放权交易中心、北京环境交易所、上海环境能源交易所" size="medium" style="width: 536px; top: -5px">
                  <el-option v-for="(item, index) in exchangeList" :key="index" :label="item.name" :value="item.value" />
                </el-select>
              </el-form-item>
            </el-form>
            <div slot="footer" class="dialog-footer">
              <el-button type="primary" @click="submit">确 定</el-button>
            </div>
          </el-dialog>

          <el-dialog title="上架成功" :visible.sync="showQuotation" width="30%">
            <span>是否进入供需行情，参与碳交易？</span>
            <span slot="footer" class="dialog-footer">
              <el-button @click="showQuotation = false">取 消</el-button>
              <el-button @click="toQuotation" type="primary">确 定</el-button>
            </span>
          </el-dialog>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { carbonAssetDetail } from "@/api/carbonAssetApi";
import { openUrlInNewWindow } from "@/libs/OpenHelper";
import { getExchangeDict } from "@/config/dictHelper";
import { getDiliveryMethodeDict } from "@/config/dictHelper";
import * as credit from "@/api/carbonAssetApi";
import { setLargeNumber } from "@/libs/public";

export default {
  name: "assetDetail",
  data() {
    return {
      assetId: 0,
      assetDetail: {},
      dialogFormVisible: false,
      exchangeList: [],
      tradeMethods: [],
      showQuotation: false,
      sellCarbonTotal: 0,
      form: {
        id: 0,
        tradeQuantity: "",
        negotiatedPrice: "",
        expirationDate: "",
        deliveryTime: "",
        deliveryMethod: "",
        deliveryExchange: "",
        projectType: "",
        projectId: null,
        direction: "0280000002",
        status: "",
        assetType: "0140000001",
        institutionName: "",
      },
      rules: {
        tradeQuantity: [{ message: "请输入出售数量", trigger: "blur" }],
      },
      title: "",
    };
  },
  mounted() {
    this.loadDetail();
    this.exchangeList = getExchangeDict(this.$store);
    this.tradeMethods = getDiliveryMethodeDict(this.$store);
  },
  filters: {
    file(value) {
      if (value) {
        let values = value.split("/");
        return values[values.length - 1];
      }
      return "--";
    },
  },
  methods: {
    setNumber(str) {
      return setLargeNumber(str);
    },
    styles(str) {
      if (str) {
        return "cursor: pointer; color: #126fff";
      } else {
        return "cursor: default; color: #126fff";
      }
    },
    toDetail() {
      this.$router.push("/account/wallet");
    },
    toCarbonExchangeWebsite() {
      openUrlInNewWindow(this.assetDetail["carbonExchangeWebsite"]);
    },
    toQuotation() {
      this.$router.push("/trade/quotation");
    },
    toBuyFile() {
      if (this.assetDetail["buyCertificate"]) {
        openUrlInNewWindow(this.assetDetail["buyCertificate"]);
      }
    },
    toIssueFile() {
      if (this.assetDetail["issuingCertificates"]) {
        openUrlInNewWindow(this.assetDetail["issuingCertificates"]);
      }
    },
    loadDetail() {
      let id = this.$route.query;
      this.assetId = id.id;
      carbonAssetDetail(this.assetId).then(
        (res) => {
          this.assetDetail = (res && (res.id || res.carbonProjectId)) ? res : ((res && res.data) || {});

          this.assetDetail["issuingDate"] = this.assetDetail["issuingDate"]
            ? this.formatDate(this.assetDetail["issuingDate"])
            : "--";
          this.assetDetail["expiryDate"] = this.assetDetail["expiryDate"]
            ? this.formatDate(this.assetDetail["expiryDate"])
            : "--";
          this.assetDetail["buyDate"] = this.assetDetail["buyDate"]
            ? this.formatDate(this.assetDetail["buyDate"])
            : "--";

          for (var i in this.assetDetail) {
            this.assetDetail[i] = this.assetDetail[i] ? this.assetDetail[i] : "--";
            if (this.assetDetail[i] == " ") {
              this.assetDetail[i] = "--";
            }
          }
        },
        () => {
          this.$message.success("加载数据失败");
        }
      );
    },
    formatDate(date) {
      let datelist = date.split(" ");
      return datelist[0];
    },
    ousideTransaction() {
      this.dialogFormVisible = true;
      this.form.id = this.assetDetail["id"];
      this.form.projectId = this.assetDetail["carbonProjectId"];
      this.form.projectType = this.assetDetail["fieldChildCode"];
      this.form.assetType = this.assetDetail["transactionStatus"];
      this.form.institutionName = this.assetDetail["projectName"];
      this.title = this.assetDetail["projectName"];
      this.form.deliveryMethod = "0190000000";
      this.form.deliveryExchange = "0170000000";
    },
    goback() {
      this.$router.go(-1);
    },
    insideTransaction() {
      this.$router.push("/trade/account/exchange");
    },
    submit() {
      const data = {
        id: this.form.id,
        transactionStatus: "0160000001",
      };
      if (this.assetDetail.availableAmount < this.form.tradeQuantity) {
        this.$message.warning("出售数量不能大于可用量");
        return;
      }
      credit.addcarbonAssetMarket(this.form).then(
        () => {
          credit.changeCredit(data).then(
            () => {
              this.$message.success("操作成功");
              this.dialogFormVisible = false;
              this.showQuotation = true;
              this.loadDetail();
            },
            () => {}
          );
        },
        () => {
          this.$message.success("操作失败");
        }
      );
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

.content-container {
  display: flex;
  flex-direction: column;
  width: 100%;
}

.cardBody {
  margin: 30px 30px 30px 30px;
}

.header {
  width: 95px;
  height: 18px;
  font-weight: 500;
  color: #24a776;
}

.asset-title {
  width: 396px;
  height: 18px;
  font-weight: 500;
  color: #242b35;
  margin-left: 20px;
}

.el-divider {
  margin-top: 30px;
}

.icon {
  height: 16px;
  width: 22px;
}

.asset-little-title {
  width: 64px;
  height: 16px;
  font-weight: 500;
  color: #424c5c;
  margin-left: 10px;
}

@media screen and (min-width: 1360px) {
  .basic-divx {
    width: 95%;
    min-height: 200px;
    background: #f7f9fc;
    border-radius: 6px;
    margin-left: 30px;
    margin-top: 30px;
  }
}

@media screen and (max-width: 1360px) {
  .basic-divx {
    width: 95%;
    min-height: 300px;
    background: #f7f9fc;
    border-radius: 6px;
    margin-left: 30px;
    margin-top: 30px;
  }
}

.basic-div {
  width: 95%;
  min-height: 200px;
  background: #f7f9fc;
  border-radius: 6px;
  margin-left: 30px;
  margin-top: 30px;
}

.basic-div.intro {
  padding: 20px 16px 20px 16px;
}

.basic-div.compact {
  height: 89px;
}

.basic-div.action-bar {
  background-color: white;
}

.item-title {
  width: 56px;
  height: 14px;
  font-weight: 400;
  color: #5e6c84;
}

.item-content {
  width: 42px;
  height: 15px;
  font-weight: 400;
  color: #424c5c;
  position: relative;
  top: 15px;
}

.item-div {
  float: left;
  margin: 24px 20px 20px 16px;
  width: 150px;
  height: 40px;
}

.paragraph {
  width: 95%;
  height: 108px;
  font-weight: 400;
  color: #808ea5;
  text-align: justify;
}

.detail {
  width: 56px;
  height: 14px;
  font-weight: 400;
  color: #24a776;
  margin-left: 15px;
  cursor: pointer;
}

.require {
  color: red;
  position: absolute;
  left: -20px;
}

.file-link {
  cursor: pointer;
  color: #126fff;
}

.action-btn {
  width: 96px;
  float: right;
}

.action-btn-gap {
  margin-right: 20px;
}
</style>
