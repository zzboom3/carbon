<template>
  <div class="trade-info-container">
    <div class="section-header">
      <div class="left-section">
        <span class="nav-title">碳交易行情</span>
        <div class="header-line"></div>
        <el-tabs v-model="activeName" @tab-click="handleClick" class="nature-tabs">
          <el-tab-pane label="VCS" name="VCS"></el-tab-pane>
        </el-tabs>
      </div>
      <router-link to="/trade/quotation" class="more-link">查看更多 ></router-link>
    </div>

    <div class="card-body">
      <!-- 签发量图表 -->
      <div class="chart-wrapper">
        <div class="chart-header">
          <span class="chart-title">{{ activeName }} 签发量</span>
          <span class="chart-total">{{ quotation.ccerCount }}</span>
          <span class="chart-unit">tCO2e</span>
        </div>
        <div id="xunjian_echart" class="chart-instance"></div>
        <div class="chart-legend">
          <div class="legend-item">
            <span class="dot green"></span>
            <span class="label">已核销</span>
            <span class="value">{{ percentWrittenOffCount }}%</span>
          </div>
          <div class="legend-item">
            <span class="dot blue"></span>
            <span class="label">存量</span>
            <span class="value">{{ percentStockCount }}%</span>
          </div>
        </div>
      </div>

      <!-- 项目量图表 -->
      <div class="chart-wrapper">
        <div class="chart-header">
          <span class="chart-title">{{ activeName }} 项目量</span>
          <span class="chart-total">{{ quotation.ccerProjectCount }}</span>
          <span class="chart-unit">个</span>
        </div>
        <div id="lvxin_echart" class="chart-instance"></div>
        <div class="chart-legend">
          <div class="legend-item">
            <span class="dot green"></span>
            <span class="label">已审定</span>
            <span class="value">{{ percentApprovedCount }}%</span>
          </div>
          <div class="legend-item">
            <span class="dot blue"></span>
            <span class="label">已备案</span>
            <span class="value">{{ percentFilingCount }}%</span>
          </div>
          <div class="legend-item">
            <span class="dot cyan"></span>
            <span class="label">已签发</span>
            <span class="value">{{ percentSingCount }}%</span>
          </div>
        </div>
      </div>

      <!-- 列表数据 -->
      <div class="table-wrapper">
        <el-table 
          :data="quotation.projects" 
          stripe 
          style="width: 100%" 
          max-height="370"
          class="nature-table">
          <el-table-column prop="type" label="项目类型" min-width="120" align="center"></el-table-column>
          <el-table-column prop="singCount" label="签发量(tCO2e)" min-width="120" align="center"></el-table-column>
          <el-table-column prop="stockCount" label="存量(tCO2e)" min-width="120" align="center"></el-table-column>
          <el-table-column prop="singCount" label="项目数量(个)" min-width="100" align="center"></el-table-column>
        </el-table>
      </div>
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts';
import { getHomePanelData } from '@/api/homeApi.js'
import { getToken } from '@/utils/auth'

export default {
  name: 'TradeInfo',
  data() {
    return {
      ccerCount: 0,
      stockCount: 0,
      writtenOffCount: 0,
      percentStockCount: 0,
      percentWrittenOffCount: 0,
      ccerProjectCount: 0,
      singCount: 0,
      filingCount: 0,
      approvedCount: 0,
      percentSingCount: 0,
      percentFilingCount: 0,
      percentApprovedCount: 0,
      quotation: {},
      activeName: "VCS",
      xunjianChart: null,
      lvxinChart: null,
      quotationRefreshTimer: null
    }
  },
  mounted() {
    this.cacultePercent("VCS")
    window.addEventListener('resize', this.resizeCharts)
    this.quotationRefreshTimer = setInterval(() => {
      this.cacultePercent(this.activeName)
    }, 60000)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.resizeCharts)
    if (this.xunjianChart) this.xunjianChart.dispose()
    if (this.lvxinChart) this.lvxinChart.dispose()
    if (this.quotationRefreshTimer) {
      clearInterval(this.quotationRefreshTimer)
      this.quotationRefreshTimer = null
    }
  },
  methods: {
    resizeCharts() {
      if (this.xunjianChart) this.xunjianChart.resize()
      if (this.lvxinChart) this.lvxinChart.resize()
    },
    handleClick(tab) {
      this.cacultePercent(this.activeName)
    },
    cacultePercent(str) {
      let token = getToken()
      getHomePanelData(token).then(res => {
        let index = 0;
        for (let i = 0; i < res.quotation.length; i++) {
          if (res.quotation[i].type == str) {
            index = i;
          }
        }

        let quotation = res.quotation[index]
        this.quotation = quotation
        this.ccerCount = quotation.ccerCount
        this.stockCount = quotation.stockCount
        this.writtenOffCount = quotation.writtenOffCount

        this.ccerProjectCount = quotation.ccerProjectCount
        this.singCount = quotation.singCount
        this.filingCount = quotation.filingCount
        this.approvedCount = quotation.approvedCount

        const safePercent = (num, den) => {
          if (!den || den === 0) return '0.00'
          return parseFloat(num / den * 100).toFixed(2)
        }
        this.percentStockCount = safePercent(this.stockCount, this.ccerCount)
        this.percentWrittenOffCount = safePercent(this.writtenOffCount, this.ccerCount)
        this.percentSingCount = safePercent(this.singCount, this.ccerProjectCount)
        this.percentFilingCount = safePercent(this.filingCount, this.ccerProjectCount)
        this.percentApprovedCount = safePercent(this.approvedCount, this.ccerProjectCount)
        
        this.$nextTick(() => {
          this.drawRing()
        })
      }).catch((res) => {
        console.error(res)
      })
    },
    drawRing() {
      const chartColor = ["#2E7D32", "#4CAF50", "#81C784", "#A5D6A7"];
      
      // 初始化图表1
      if (!this.xunjianChart) {
        this.xunjianChart = echarts.init(document.getElementById('xunjian_echart'));
      }
      
      // 初始化图表2
      if (!this.lvxinChart) {
        this.lvxinChart = echarts.init(document.getElementById('lvxin_echart'));
      }

      const commonOption = {
        color: chartColor,
        tooltip: { trigger: 'item' },
        title: {
          show: true,
          left: 'center',
          top: 'center',
          textStyle: {
            fontSize: '14',
            fontWeight: 'bold',
            color: '#1B5E20'
          },
        },
        series: [{
          type: 'pie',
          radius: ['50%', '70%'],
          avoidLabelOverlap: false,
          label: { show: false },
          labelLine: { show: false },
          itemStyle: {
            borderRadius: 5,
            borderColor: '#fff',
            borderWidth: 2
          }
        }]
      };

      // 设置数据
      const option1 = JSON.parse(JSON.stringify(commonOption));
      option1.title.text = this.ccerCount;
      option1.series[0].data = [
        { value: this.stockCount, name: '存量' },
        { value: this.writtenOffCount, name: '已核销' },
      ];

      const option2 = JSON.parse(JSON.stringify(commonOption));
      option2.title.text = this.ccerProjectCount;
      option2.series[0].data = [
        { value: this.approvedCount, name: '已审定' },
        { value: this.filingCount, name: '已备案' },
        { value: this.singCount, name: '已签发' },
      ];

      this.xunjianChart.setOption(option1);
      this.lvxinChart.setOption(option2);
    }
  }
}
</script>

<style lang="scss" scoped>
/* 绿色环保主题变量 */
$eco-green: #2E7D32;
$eco-light: #4CAF50;
$eco-bg: #E8F5E9;
$text-main: #1B5E20;
$text-sub: #546E7A;
$white: #ffffff;

.trade-info-container {
  padding: 0 10px;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  
  .left-section {
    display: flex;
    align-items: center;
    flex: 1;
  }
  
  .nav-title {
    font-size: 18px;
    font-weight: 600;
    color: $text-main;
    margin-right: 15px;
  }
  
  .header-line {
    width: 40px;
    height: 1px;
    background: linear-gradient(90deg, rgba(46, 125, 50, 0.3), transparent);
    margin-right: 20px;
  }

  .more-link {
    font-size: 13px;
    color: $text-sub;
    text-decoration: none;
    transition: color 0.3s;
    
    &:hover {
      color: $eco-green;
    }
  }
}

.nature-tabs {
  ::v-deep .el-tabs__header {
    margin: 0;
  }
  
  ::v-deep .el-tabs__item {
    font-size: 14px;
    color: $text-sub;
    height: 30px;
    line-height: 30px;
    
    &.is-active, &:hover {
      color: $eco-green;
    }
  }
  
  ::v-deep .el-tabs__active-bar {
    background-color: $eco-green;
  }
  
  ::v-deep .el-tabs__nav-wrap::after {
    display: none;
  }
}

.card-body {
  display: flex;
  gap: 20px;
  background: rgba(255, 255, 255, 0.6);
  border-radius: 16px;
  padding: 20px;
  flex-wrap: wrap;
}

.chart-wrapper {
  flex: 0 0 220px;
  display: flex;
  flex-direction: column;
  align-items: center;
  background: rgba(255, 255, 255, 0.5);
  border-radius: 12px;
  padding: 15px;
  
  .chart-header {
    text-align: center;
    margin-bottom: 10px;
    
    .chart-title {
      font-size: 14px;
      color: $text-sub;
      display: block;
    }
    
    .chart-total {
      font-size: 20px;
      font-weight: bold;
      color: $text-main;
      margin: 5px 0;
      display: block;
    }
    
    .chart-unit {
      font-size: 12px;
      color: $text-sub;
    }
  }
  
  .chart-instance {
    width: 160px;
    height: 160px;
  }
  
  .chart-legend {
    margin-top: 15px;
    width: 100%;
    
    .legend-item {
      display: flex;
      align-items: center;
      margin-bottom: 8px;
      font-size: 12px;
      
      .dot {
        width: 8px;
        height: 8px;
        border-radius: 50%;
        margin-right: 8px;
        
        &.green { background: #4CAF50; }
        &.blue { background: #2E7D32; }
        &.cyan { background: #81C784; }
      }
      
      .label {
        color: $text-sub;
        flex: 1;
      }
      
      .value {
        color: $text-main;
        font-weight: 600;
      }
    }
  }
}

.table-wrapper {
  flex: 1;
  min-width: 300px;
  border-radius: 12px;
  overflow: hidden;
  background: rgba(255, 255, 255, 0.5);
}

.nature-table {
  background: transparent !important;
  
  ::v-deep th {
    background: rgba(46, 125, 50, 0.05) !important;
    color: $text-sub;
    font-weight: 500;
    border-bottom: 1px solid rgba(46, 125, 50, 0.1);
  }
  
  ::v-deep tr {
    background: transparent !important;
    
    &.el-table__row--striped td {
      background: rgba(232, 245, 233, 0.3) !important;
    }
    
    &:hover td {
      background: rgba(232, 245, 233, 0.6) !important;
    }
    
    td {
      border-bottom: 1px dashed rgba(46, 125, 50, 0.1) !important;
      color: $text-main;
    }
  }
}
</style>
