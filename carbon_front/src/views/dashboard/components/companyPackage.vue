<template>
  <div class="company-package-container">
    <!-- 资产与资金收入卡片 -->
    <div class="stat-card assets-card">
      <div class="card-header">
        <div class="header-item">
          <span class="title">资产收入</span>
          <span class="subtitle">上月({{ assetsIncome.statDate }})</span>
        </div>
        <div class="header-divider"></div>
        <div class="header-item">
          <span class="title">资金收入</span>
          <span class="subtitle">上月({{ fundIncome.statDate }})</span>
        </div>
      </div>
      
      <div class="card-body">
        <div class="income-stats">
          <!-- 资产收入部分 -->
          <div class="income-section">
            <div class="main-value">
              <span class="amount">{{ assetsIncome.totalIncome }}</span>
              <span class="unit">{{ assetsIncomeUnit }}</span>
            </div>
            <div class="rate-group">
              <div class="rate-item">
                <span class="label">环比</span>
                <div class="value-wrapper">
                  <span :class="getRateClass(assetsIncome.monthOnMonthRatio)">
                    {{ assetsIncome.monthOnMonthRatio }}%
                  </span>
                  <i :class="getRateIcon(assetsIncome.monthOnMonthRatio)"></i>
                </div>
              </div>
              <div class="rate-item">
                <span class="label">同比</span>
                <div class="value-wrapper">
                  <span :class="getRateClass(assetsIncome.yearOnYearRatio)">
                    {{ assetsIncome.yearOnYearRatio }}%
                  </span>
                  <i :class="getRateIcon(assetsIncome.yearOnYearRatio)"></i>
                </div>
              </div>
              <div class="rate-item">
                <span class="label">总收入</span>
                <span class="value">¥ {{ assetsIncome.totalIncome }}</span>
              </div>
            </div>
          </div>

          <div class="section-divider"></div>

          <!-- 资金收入部分 -->
          <div class="income-section">
            <div class="main-value">
              <span class="amount">{{ fundIncome.monthIncome }}</span>
              <span class="unit">¥ {{ capitalIncomeUnit }}</span>
            </div>
            <div class="rate-group">
              <div class="rate-item">
                <span class="label">环比</span>
                <div class="value-wrapper">
                  <span :class="getRateClass(fundIncome.monthOnMonthRatio)">
                    {{ fundIncome.monthOnMonthRatio }}%
                  </span>
                  <i :class="getRateIcon(fundIncome.monthOnMonthRatio)"></i>
                </div>
              </div>
              <div class="rate-item">
                <span class="label">同比</span>
                <div class="value-wrapper">
                  <span :class="getRateClass(fundIncome.yearOnYearRatio)">
                    {{ fundIncome.yearOnYearRatio }}%
                  </span>
                  <i :class="getRateIcon(fundIncome.yearOnYearRatio)"></i>
                </div>
              </div>
              <div class="rate-item">
                <span class="label">总收入</span>
                <span class="value">¥ {{ fundIncome.totalIncome }}</span>
              </div>
            </div>
          </div>
        </div>

        <div class="card-footer">
          <div class="footer-item">
            <span class="label">碳信用：</span>
            <span class="value">{{ carbonCredit }} {{ carbonCreditUnit }}</span>
          </div>
          <div class="footer-item">
            <span class="label">碳配额：</span>
            <span class="value">{{ carbonQuota }} {{ carbonQuotaUnit }}</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 账户信息卡片 -->
    <div class="stat-card account-card" @click="goUserInfo">
      <div class="card-header">
        <span class="title">账户信息</span>
        <i class="el-icon-arrow-right"></i>
      </div>
      
      <div class="card-body">
        <div class="account-info">
          <div class="info-top">
            <img class="avatar" :src="accountVo.avatar" />
            <div class="user-details">
              <div class="name-row">
                <span class="name">{{ accountVo.accountName }}</span>
                
                <!-- 角色标签 -->
                <span v-if="accountVo.roleNames" class="tag role-tag">
                  <i class="el-icon-user-solid"></i>
                  {{accountVo.roleNames[0]}}
                </span>

                <!-- 版本标签 -->
                <span v-if="accountVo.productVersion" class="tag version-tag" :class="getVersionClass(accountVo.productVersion)">
                  <i class="el-icon-medal"></i>
                  {{ accountVo.productVersionName }}
                </span>
              </div>
              <div class="company-name">{{ accountVo.enterpriseName }}</div>
            </div>
          </div>
          
          <div class="account-decoration">
            <img src="@/assets/imgs/icon_account_flag.png" />
          </div>
        </div>

        <div class="card-footer">
          <div class="footer-item">
            <span class="label">碳中和比例：</span>
            <span class="value highlight">{{ accountVo.carbonNeutralRatio || '--'}}</span>
          </div>
          <div class="footer-item">
            <span class="label">ESG评分：</span>
            <span class="value highlight">{{ accountVo.esgScore || '--' }}</span>
          </div>
          <div class="footer-item">
            <span class="label">有效期：</span>
            <span class="value" :class="{ 'expired': accountVo.expired }">
              {{ (accountVo.expired ? '已过期' : accountVo.accountValidity) | formatData }}
            </span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { greenUnit, creditUnit, qoutaUnit, cncUnit } from "@/config/ComConfig";

export default {
  name: "companyPackage",
  data() {
    return {
      lastMonth: "2月",
      assetsIncomeUnit: cncUnit,
      carbonCreditUnit: creditUnit,
      carbonQuotaUnit: qoutaUnit,
      greenCertUnit: greenUnit,
      capitalIncomeUnit: "",
    };
  },
  props: {
    accountVo: { type: Object, default: () => ({}) },
    assetsIncome: { type: Object, default: () => ({}) },
    fundIncome: { type: Object, default: () => ({}) },
    carbonCredit: { type: [Number, String], default: 0 },
    carbonQuota: { type: [Number, String], default: 0 },
    greenScore: { type: [Number, String], default: 0 },
  },
  methods: {
    goUserInfo() {
      this.$router.push({ path: "/account/info" });
    },
    getRateClass(ratio) {
      return Number(ratio) >= 0 ? 'positive' : 'negative';
    },
    getRateIcon(ratio) {
      return Number(ratio) >= 0 ? 'el-icon-top' : 'el-icon-bottom';
    },
    getVersionClass(version) {
      const map = {
        '0400000001': 'trial',
        '0400000002': 'basic',
        '0400000003': 'standard',
        '0400000004': 'pro'
      };
      return map[version] || 'basic';
    }
  },
  filters: {
    formatData(value) {
      if (!value) return '--';
      return value.split(' ')[0];
    }
  }
};
</script>

<style lang="scss" scoped>
$eco-green: #2E7D32;
$eco-light: #4CAF50;
$eco-bg: #E8F5E9;
$text-main: #1B5E20;
$text-sub: #546E7A;
$white: #ffffff;
$danger: #e53935;
$success: #43a047;

.company-package-container {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 20px;
  
  @media screen and (max-width: 992px) {
    grid-template-columns: 1fr;
  }
}

.stat-card {
  background: rgba(255, 255, 255, 0.9);
  border-radius: 16px;
  border: 1px solid rgba(232, 245, 233, 0.6);
  overflow: hidden;
  transition: all 0.3s;

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 8px 25px rgba(46, 125, 50, 0.1);
  }

  .card-header {
    padding: 15px 25px;
    border-bottom: 1px solid $eco-bg;
    display: flex;
    align-items: center;
    
    .title {
      font-size: 16px;
      font-weight: 600;
      color: $text-main;
      margin-right: 10px;
    }

    .subtitle {
      font-size: 12px;
      color: $text-sub;
    }

    .header-divider {
      width: 1px;
      height: 20px;
      background: #E0E0E0;
      margin: 0 20px;
    }
  }

  .card-body {
    padding: 20px 25px;
  }

  .card-footer {
    margin-top: 20px;
    padding-top: 15px;
    border-top: 1px dashed #E0E0E0;
    display: flex;
    gap: 30px;
    font-size: 13px;

    .footer-item {
      display: flex;
      align-items: center;
      
      .label { color: $text-sub; }
      .value { 
        font-weight: 600; 
        color: $text-main;
        margin-left: 5px;
        
        &.highlight { color: $eco-green; }
        &.expired { color: $text-sub; }
      }
    }
  }
}

/* 收入卡片样式 */
.assets-card {
  .income-stats {
    display: flex;
    align-items: flex-start;
  }

  .income-section {
    flex: 1;

    .main-value {
      margin-bottom: 15px;
      .amount {
        font-size: 28px;
        font-weight: bold;
        color: $eco-green;
        margin-right: 5px;
      }
      .unit {
        font-size: 13px;
        color: $text-sub;
      }
    }

    .rate-group {
      display: flex;
      gap: 20px;

      .rate-item {
        display: flex;
        flex-direction: column;
        gap: 5px;

        .label {
          font-size: 12px;
          color: $text-sub;
        }

        .value-wrapper {
          display: flex;
          align-items: center;
          gap: 3px;
          font-weight: 600;

          .positive { color: $danger; }
          .negative { color: $success; }
          i { font-size: 12px; }
        }
        
        .value {
          font-size: 13px;
          font-weight: 600;
          color: $text-main;
        }
      }
    }
  }

  .section-divider {
    width: 1px;
    height: 80px;
    background: #E0E0E0;
    margin: 0 30px;
  }
}

/* 账户卡片样式 */
.account-card {
  cursor: pointer;

  .card-header {
    justify-content: space-between;
    i { color: $text-sub; }
  }

  .account-info {
    position: relative;
    
    .info-top {
      display: flex;
      align-items: center;
      margin-bottom: 15px;
    }

    .avatar {
      width: 50px;
      height: 50px;
      border-radius: 50%;
      border: 2px solid $eco-bg;
      margin-right: 15px;
    }

    .user-details {
      .name-row {
        display: flex;
        align-items: center;
        gap: 10px;
        margin-bottom: 5px;

        .name {
          font-size: 16px;
          font-weight: 600;
          color: $text-main;
        }
      }

      .company-name {
        font-size: 13px;
        color: $text-sub;
      }
    }

    .tag {
      padding: 2px 8px;
      border-radius: 4px;
      font-size: 11px;
      display: flex;
      align-items: center;
      gap: 3px;

      &.role-tag {
        background: #FFF3E0;
        color: #F57C00;
      }

      &.version-tag {
        background: #E3F2FD;
        color: #1976D2;
        
        &.trial { background: #E1F5FE; color: #0288D1; }
        &.basic { background: #F3E5F5; color: #7B1FA2; }
        &.pro { background: #E8F5E9; color: #2E7D32; }
      }
    }

    .account-decoration {
      position: absolute;
      top: -10px;
      right: 0;
      opacity: 0.8;
      img { height: 60px; }
    }
  }
}
</style>
