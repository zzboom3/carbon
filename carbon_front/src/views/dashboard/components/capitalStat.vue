<template>
  <div class="capital-stat-grid">
    <!-- 供应量卡片 -->
    <div class="stat-card">
      <div class="card-header">
        <div class="header-content">
          <span class="title">供应量</span>
          <span class="subtitle">上月({{ monthSupply.statDate }})</span>
        </div>
      </div>
      <div class="card-body">
        <div class="main-stat">
          <div class="stat-left">
            <div class="icon-wrapper supply">
              <img class="flag-icon" src="@/assets/imgs/icon_capital_one.png" />
            </div>
            <div class="stat-value-group">
              <span class="sum-amount">{{ monthSupply.carbonSupply }}</span>
              <span class="unit">{{ assetsIncomeUnit }}</span>
            </div>
          </div>
          
          <div class="divider-vertical"></div>
          
          <div class="stat-right">
            <div class="rate-item">
              <span class="label">碳信用：</span>
              <span class="value">{{ monthSupply.carbonCredit }} {{ carbonCreditUnit }}</span>
            </div>
            <div class="rate-item">
              <span class="label">碳配额：</span>
              <span class="value">{{ monthSupply.carbonQuota }} {{ carbonQuotaUnit }}</span>
            </div>
            <div class="rate-item">
              <span class="label">绿证：</span>
              <span class="value">{{ monthSupply.greenScore }} {{ greenCertUnit }}</span>
            </div>
          </div>
        </div>

        <div class="card-footer">
          <div class="footer-item">
            <span class="footer-label">总供应量：</span>
            <span class="footer-value">{{ monthSupply.carbonSupplyTotal }} {{ assetsIncomeUnit }}</span>
          </div>
          <div class="footer-item">
            <span class="footer-label">碳资产估值：</span>
            <span class="footer-value highlight">¥ {{ monthSupply.carbonValuation }}</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 开发量卡片 -->
    <div class="stat-card">
      <div class="card-header">
        <div class="header-content">
          <span class="title">开发量</span>
          <span class="subtitle">上月({{ monthDevelopment.statDate }})</span>
        </div>
      </div>
      <div class="card-body">
        <div class="main-stat">
          <div class="stat-left">
            <div class="icon-wrapper develop">
              <img class="flag-icon" src="@/assets/imgs/icon_capital_two.png" />
            </div>
            <div class="stat-value-group">
              <span class="sum-amount">{{ monthDevelopment.carbonSupply }}</span>
              <span class="unit">{{ assetsIncomeUnit }}</span>
            </div>
          </div>
          
          <div class="divider-vertical"></div>
          
          <div class="stat-right">
            <div class="rate-item">
              <span class="label">碳信用：</span>
              <span class="value">{{ monthDevelopment.carbonCredit }} {{ carbonCreditUnit }}</span>
            </div>
            <div class="rate-item">
              <span class="label">碳配额：</span>
              <span class="value">{{ monthDevelopment.carbonQuota }} {{ carbonQuotaUnit }}</span>
            </div>
            <div class="rate-item">
              <span class="label">绿证：</span>
              <span class="value">{{ monthDevelopment.greenScore }} {{ greenCertUnit }}</span>
            </div>
          </div>
        </div>

        <div class="card-footer">
          <div class="footer-item">
            <span class="footer-label">总供应量：</span>
            <span class="footer-value">{{ monthDevelopment.carbonSupplyTotal }} {{ assetsIncomeUnit }}</span>
          </div>
          <div class="footer-item">
            <span class="footer-label">碳资产估值：</span>
            <span class="footer-value highlight">¥ {{ monthDevelopment.carbonValuation }}</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 销售量卡片 -->
    <div class="stat-card">
      <div class="card-header">
        <div class="header-content">
          <span class="title">销售量</span>
          <span class="subtitle">上月({{ monthSales.statDate }})</span>
        </div>
      </div>
      <div class="card-body">
        <div class="main-stat">
          <div class="stat-left">
            <div class="icon-wrapper sales">
              <img class="flag-icon" src="@/assets/imgs/icon_capital_three.png" />
            </div>
            <div class="stat-value-group">
              <span class="sum-amount">{{ monthSales.carbonSupply }}</span>
              <span class="unit">{{ assetsIncomeUnit }}</span>
            </div>
          </div>
          
          <div class="divider-vertical"></div>
          
          <div class="stat-right">
            <div class="rate-item">
              <span class="label">碳信用：</span>
              <span class="value">{{ monthSales.carbonCredit }} {{ carbonCreditUnit }}</span>
            </div>
            <div class="rate-item">
              <span class="label">碳配额：</span>
              <span class="value">{{ monthSales.carbonQuota }} {{ carbonQuotaUnit }}</span>
            </div>
            <div class="rate-item">
              <span class="label">绿证：</span>
              <span class="value">{{ monthSales.greenScore }} {{ greenCertUnit }}</span>
            </div>
          </div>
        </div>

        <div class="card-footer">
          <div class="footer-item">
            <span class="footer-label">总供应量：</span>
            <span class="footer-value">{{ monthSales.carbonSupplyTotal }} {{ assetsIncomeUnit }}</span>
          </div>
          <div class="footer-item">
            <span class="footer-label">碳资产估值：</span>
            <span class="footer-value highlight">¥ {{ monthSales.carbonValuation }}</span>
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
    };
  },
  props: {
    monthSupply: {
      type: Object,
      default: () => ({})
    },
    monthDevelopment: {
      type: Object,
      default: () => ({})
    },
    monthSales: {
      type: Object,
      default: () => ({})
    },
  }
};
</script>

<style lang="scss" scoped>
$eco-green: #2E7D32;
$eco-light: #4CAF50;
$eco-bg: #E8F5E9;
$text-main: #1B5E20;
$text-sub: #546E7A;

.capital-stat-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(320px, 1fr));
  gap: 20px;
}

.stat-card {
  background: rgba(255, 255, 255, 0.9);
  border-radius: 12px;
  overflow: hidden;
  transition: all 0.3s;
  border: 1px solid rgba(232, 245, 233, 0.5);

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(46, 125, 50, 0.1);
  }

  .card-header {
    padding: 15px 20px;
    border-bottom: 1px solid $eco-bg;

    .header-content {
      display: flex;
      align-items: baseline;
      gap: 10px;

      .title {
        font-size: 16px;
        font-weight: 600;
        color: $text-main;
      }

      .subtitle {
        font-size: 12px;
        color: $text-sub;
      }
    }
  }

  .card-body {
    padding: 20px;

    .main-stat {
      display: flex;
      align-items: center;
      margin-bottom: 20px;
    }

    .stat-left {
      flex: 1;
      display: flex;
      flex-direction: column;
      align-items: center;
      gap: 10px;

      .icon-wrapper {
        width: 48px;
        height: 48px;
        border-radius: 12px;
        display: flex;
        align-items: center;
        justify-content: center;
        
        &.supply { background: #E3F2FD; }
        &.develop { background: #E8F5E9; }
        &.sales { background: #FFF3E0; }

        .flag-icon {
          width: 24px;
          height: 24px;
        }
      }

      .stat-value-group {
        text-align: center;
        
        .sum-amount {
          font-size: 24px;
          font-weight: bold;
          color: $eco-green;
        }

        .unit {
          font-size: 12px;
          color: $text-sub;
          margin-left: 4px;
        }
      }
    }

    .divider-vertical {
      width: 1px;
      height: 60px;
      background: #E0E0E0;
      margin: 0 20px;
    }

    .stat-right {
      flex: 1.2;
      display: flex;
      flex-direction: column;
      gap: 8px;

      .rate-item {
        display: flex;
        justify-content: space-between;
        font-size: 13px;
        
        .label {
          color: $text-sub;
        }
        
        .value {
          font-weight: 500;
          color: $text-main;
        }
      }
    }

    .card-footer {
      border-top: 1px dashed #E0E0E0;
      padding-top: 15px;
      display: flex;
      justify-content: space-between;
      font-size: 12px;

      .footer-item {
        display: flex;
        flex-direction: column;
        gap: 4px;

        .footer-label {
          color: $text-sub;
        }

        .footer-value {
          font-weight: 600;
          color: $text-main;

          &.highlight {
            color: $eco-green;
          }
        }
      }
    }
  }
}
</style>
