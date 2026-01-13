<template>
  <div class="root">
    <div class="header">
      <span class="baseInfo">基本信息</span>
      <span
        v-if="AccoutBaseInfo.accountType == '0380000002'"
        class="icon-qy-status"
      >
        <img class="icon-ec-status-logo" src="@/assets/icon/icon-qy-time.png" />
        <span class="qy-text">{{ AccoutBaseInfo.accountTypeName }}</span>
      </span>
      <span
        v-else-if="AccoutBaseInfo.accountType == '0380000001'"
        class="icon-qy-status"
      >
        <img class="icon-ec-status-logo" src="@/assets/icon/on-trial-icon.png" />
        <span class="qy-text">{{ AccoutBaseInfo.accountTypeName }}</span>
      </span>
      <div class="empty-div" />
    </div>

    <div class="second-line-div">
      <div class="single-item">
        <span class="secon-line-hint">企业名称</span>
        <input class="input-css input-grow" v-model="baseInfo.tenantName" disabled />
      </div>

      <div class="single-item">
        <span class="secon-line-hint">企业电话</span>
        <input class="input-css input-grow" v-model="baseInfo.telephone" disabled />
      </div>

      <div class="single-item">
        <span class="secon-line-hint">企业传真</span>
        <input class="input-css input-grow" v-model="baseInfo.faxNumber" disabled />
      </div>

      <div class="single-item">
        <span class="secon-line-hint">所属行业</span>
        <input class="input-css input-grow" v-model="baseInfo.industry" disabled />
      </div>

      <div class="single-item">
        <span class="secon-line-hint">开户日期</span>
        <input class="input-css input-grow" v-model="baseInfo.createdTime" disabled />
      </div>

      <div class="single-item">
        <span class="secon-line-hint">租户有效期</span>
        <input class="input-css input-grow" v-model="baseInfo.validityDate" disabled />
      </div>
    </div>

    <div class="single-item address-row">
      <span class="secon-line-hint">企业地址</span>
      <input class="input-css input-grow address-input" v-model="baseInfo.address" disabled />
    </div>

    <div class="deliver" />

    <div class="header section-header">
      <span class="baseInfo">企业介绍</span>
      <div class="empty-div" />
    </div>
    <span class="companyInfo">{{ baseInfo.introduction }}</span>
  </div>
</template>

<script>
import { getAccoutEnterPriseInfo, getAccoutBaseInfo } from "@/api/tenant";
import Cookies from "js-cookie";

export default {
  name: "index",
  data() {
    return {
      baseInfo: {
        tenantName: "--",
        telephone: "--",
        faxNumber: "--",
        industry: "--",
        contactsEmail: "--",
        eroTime: "--",
        validityDate: "--",
        createdTime: "--",
        address: "--",
        introduction: "暂无该企业介绍信息",
      },
      AccoutBaseInfo: {
        accountTypeName: "",
        accountType: "",
      },
    };
  },
  methods: {
    getAccoutEnterPriseInfo(tenantId) {
      getAccoutEnterPriseInfo(tenantId)
        .then((res) => {
          this.baseInfo = res;
        })
        .catch(() => {});
    },
    getUserBaseInfo() {
      const info = JSON.parse(Cookies.get("JavaInfo"));
      const accountId = info.accountId;
      getAccoutBaseInfo(accountId).then((res) => {
        this.AccoutBaseInfo.accountType = res.accountType || "0380000001";
        this.AccoutBaseInfo.accountTypeName = res.accountTypeName || "试用账户";
        this.getAccoutEnterPriseInfo(res.tenantId);
      });
    },
  },
  mounted() {
    this.getUserBaseInfo();
  },
};
</script>
<style scoped lang="scss">
.root {
  padding-left: 30px;
  padding-right: 30px;
  background: #ffffff;
  box-shadow: 0px 2px 8px 0px #eaf0f3;
  border-radius: 8px;
  margin: 28px;
  overflow: auto;
}

.header {
  display: flex;
  flex-direction: row;
  padding-top: 40px;
}

.section-header {
  padding-top: 0;
  margin-bottom: 20px;
}

.baseInfo {
  text-align: center;
  margin: auto;
  font-size: 18px;
  font-family: PingFangSC-Medium, PingFang SC;
  font-weight: 500;
  color: #242b35;
}

.empty-div {
  flex-grow: 1;
}

.second-line-div {
  display: grid;
  grid: auto / auto auto auto;
  grid-gap: 20px;
}

.single-item {
  margin-top: 20px;
  display: flex;
  flex-direction: row;
}

.address-row {
  margin-top: 40px;
  width: 66%;
}

.secon-line-hint {
  text-align: center;
  align-self: center;
  width: 96px;
  font-size: 16px;
  margin-right: 30px;
  font-family: PingFangSC-Regular, PingFang SC;
  font-weight: 400;
  color: #3a4063;
  white-space: nowrap;
}

.input-grow {
  flex-grow: 1;
}

.address-input {
  margin-right: 10px;
}

.deliver {
  margin-top: 40px;
  height: 1px;
  border: 1px dashed #dadee5;
}

.companyInfo {
  font-size: 16px;
  font-family: PingFangSC-Regular, PingFang SC;
  font-weight: 400;
  color: #5e6c84;
  line-height: 24px;
}

.qy-text {
  font-size: 14px;
  font-family: PingFangSC-Regular, PingFang SC;
  font-weight: 400;
  color: #fd7e14;
  vertical-align: middle;
}

.icon-ec-status-logo {
  width: 14px;
  height: 14px;
  vertical-align: middle;
}

.icon-qy-status {
  height: 22px;
  margin-left: 16px;
  background-image: url("~@/assets/icon/bg-icon-01.png");
  background-repeat: no-repeat;
  background-size: 100% 100%;
  background-position: center;
  padding: 4px 10px;
}
</style>
