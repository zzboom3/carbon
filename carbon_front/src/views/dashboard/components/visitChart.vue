<template>
  <div class="visit-chart-container">
    <el-card :bordered="false" class="dashboard-console-visit glass-card">
      <div slot="header">
        <div class="header-content">
          <div class="title-section">
            <div class="icon-wrapper">
              <i class="el-icon-s-data"></i>
            </div>
            <span class="title-text">订单</span>
          </div>
          <div class="checkTime">
            <el-radio-group v-model="visitDate" class="nature-radio-group">
              <el-radio-button label="last30" @change.native="handleChangeVisitType">30天</el-radio-button>
              <el-radio-button label="week" @change.native="handleChangeWeek">周</el-radio-button>
              <el-radio-button label="month" @change.native="handleChangeMonth">月</el-radio-button>
              <el-radio-button label="year" @change.native="handleChangeYear">年</el-radio-button>
            </el-radio-group>
          </div>
        </div>
      </div>
      <h4 class="chart-title">订单量趋势</h4>
      <echarts-from ref="visitChart" :yAxisData="yAxisData" :seriesData="series" :xAxis="xAxis" v-if="info" :legendData="legendData"></echarts-from>
    </el-card>
  </div>
</template>

<script>
import { chartOrder30Api, chartOrderWeekApi, chartOrderMonthApi, chartOrderYearApi } from '@/api/dashboard';
import echartsFrom from '@/components/echarts/index';

export default {
  name: 'VisitChart',
  components: { echartsFrom },
  data() {
    return {
      infoList: null,
      visitDate: 'last30',
      series: [],
      xAxis: [],
      info: {},
      legendData: [],
      yAxisData: []
    }
  },
  mounted() {
    this.initYAxis();
    this.handleChangeVisitType();
  },
  methods: {
    initYAxis() {
      this.yAxisData = [
        {
          type: 'value',
          name: '金额',
          axisLine: { show: false },
          axisTick: { show: false },
          axisLabel: { textStyle: { color: '#546E7A' } },
          splitLine: { show: true, lineStyle: { type: 'dashed', color: 'rgba(46, 125, 50, 0.1)' } }
        },
        {
          type: 'value',
          name: '数量',
          axisLine: { show: false },
          axisTick: { show: false },
          axisLabel: { textStyle: { color: '#546E7A' } },
          splitLine: { show: false }
        }
      ]
    },
    // Common color gradients
    getBarColor() {
      return {
        x: 0, y: 0, x2: 0, y2: 1,
        colorStops: [
          { offset: 0, color: '#66BB6A' }, // Light Green
          { offset: 0.5, color: '#43A047' },
          { offset: 1, color: '#2E7D32' }  // Dark Green
        ]
      }
    },
    getLineColor() {
      return {
        x: 0, y: 0, x2: 0, y2: 1,
        colorStops: [
          { offset: 0, color: '#26C6DA' }, // Cyan
          { offset: 0.5, color: '#00ACC1' },
          { offset: 1, color: '#00838F' }
        ]
      }
    },

    handleChangeVisitType() {
      this.xAxis = []
      this.legendData = []
      chartOrder30Api().then(async res => {
        this.info = res
        let pices = []
        let qualitys = []
        for (let key in res.price) {
          pices.push(Number(res.price[key]))
          this.xAxis.push(key)
        }
        for (let key in res.quality) {
          qualitys.push(Number(res.quality[key]))
        }
        this.legendData = ['订单金额', '订单数']
        this.series = [{
          name: "订单金额",
          type: "bar",
          barMaxWidth: 20,
          itemStyle: {
            normal: {
              color: this.getBarColor(),
              barBorderRadius: [4, 4, 0, 0]
            }
          },
          data: pices
        },
        {
          name: "订单数",
          type: "line",
          smooth: true,
          itemStyle: {
            normal: {
              color: '#00ACC1',
              lineStyle: { width: 3 }
            }
          },
          yAxisIndex: 1,
          data: qualitys
        }]
      })
    },
    handleChangeWeek() {
      this.fetchCompareData(chartOrderWeekApi, ["上周金额", "本周金额", "上周订单数", "本周订单数"])
    },
    handleChangeMonth() {
      this.fetchCompareData(chartOrderMonthApi, ["上月金额", "本月金额", "上月订单数", "本月订单数"])
    },
    handleChangeYear() {
      this.fetchCompareData(chartOrderYearApi, ["去年金额", "今年金额", "去年订单数", "今年订单数"])
    },
    
    fetchCompareData(apiFunc, legends) {
      this.xAxis = []
      this.legendData = legends
      apiFunc().then(async res => {
        this.info = res
        let prePrice = [], price = [], qualitys = [], preQuality = []
        
        for (let key in res.prePrice) {
          prePrice.push(Number(res.prePrice[key]))
          this.xAxis.push(key)
        }
        for (let key in res.price) price.push(Number(res.price[key]))
        for (let key in res.preQuality) preQuality.push(Number(res.preQuality[key]))
        for (let key in res.quality) qualitys.push(Number(res.quality[key]))

        this.series = [
          {
            name: legends[0],
            type: "bar",
            barMaxWidth: 15,
            itemStyle: {
              normal: {
                color: 'rgba(102, 187, 106, 0.5)', // Light green transparent
                barBorderRadius: [4, 4, 0, 0]
              }
            },
            data: prePrice
          },
          {
            name: legends[1],
            type: "bar",
            barMaxWidth: 15,
            itemStyle: {
              normal: {
                color: this.getBarColor(),
                barBorderRadius: [4, 4, 0, 0]
              }
            },
            data: price
          },
          {
            name: legends[2],
            type: "line",
            smooth: true,
            itemStyle: { normal: { color: '#B2EBF2', lineStyle: { type: 'dashed' } } },
            yAxisIndex: 1,
            data: preQuality
          },
          {
            name: legends[3],
            type: "line",
            smooth: true,
            itemStyle: { normal: { color: '#00ACC1', lineStyle: { width: 3 } } },
            yAxisIndex: 1,
            data: qualitys
          }
        ]
      })
    },
    handleResize() {
      if (this.infoList) this.$refs.visitChart.handleResize();
    }
  }
}
</script>

<style lang="scss" scoped>
$eco-green: #2E7D32;
$eco-light: #4CAF50;
$eco-bg: #E8F5E9;
$text-main: #1B5E20;
$text-sub: #546E7A;

.visit-chart-container {
  width: 100%;
}

.glass-card {
  background: rgba(255, 255, 255, 0.6);
  border-radius: 12px;
  border: 1px solid rgba(232, 245, 233, 0.6);
  
  ::v-deep .el-card__header {
    border-bottom: 1px solid rgba(46, 125, 50, 0.1);
    padding: 15px 20px;
  }
  
  ::v-deep .el-card__body {
    padding: 20px;
  }
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.title-section {
  display: flex;
  align-items: center;
  
  .icon-wrapper {
    width: 32px;
    height: 32px;
    background: $eco-bg;
    border-radius: 8px;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-right: 10px;
    color: $eco-green;
  }
  
  .title-text {
    font-size: 16px;
    font-weight: 600;
    color: $text-main;
  }
}

.chart-title {
  margin: 0 0 20px 0;
  font-size: 14px;
  color: $text-sub;
  font-weight: normal;
}

.nature-radio-group {
  ::v-deep .el-radio-button__inner {
    background: transparent;
    border: 1px solid rgba(46, 125, 50, 0.2);
    color: $text-sub;
    padding: 8px 16px;
    font-size: 12px;
    box-shadow: none !important;
  }
  
  ::v-deep .el-radio-button:first-child .el-radio-button__inner {
    border-radius: 16px 0 0 16px;
  }
  
  ::v-deep .el-radio-button:last-child .el-radio-button__inner {
    border-radius: 0 16px 16px 0;
  }
  
  ::v-deep .el-radio-button__orig-radio:checked + .el-radio-button__inner {
    background-color: $eco-green;
    border-color: $eco-green;
    color: #fff;
    box-shadow: none;
  }
}
</style>
