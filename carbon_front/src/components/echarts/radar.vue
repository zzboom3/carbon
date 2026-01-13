<template>
  <div class="radar-container">
    <div id="myChart" class="radar"></div>
  </div>
</template>
 
<script>
import echarts from 'echarts';
export default {
  name: 'Radar',
  data() {
    return {
      myChart: null
    }
  },
  props: {
    greenIndex: {
      type: [Object, Array],
      default: () => ({})
    }
  },
  created() {
    window.addEventListener('resize', this.handleResize)
  },
  mounted() {
    this.$nextTick(() => {
      this.drawLine()
    })
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.handleResize)
    if (this.myChart) {
      this.myChart.dispose()
    }
  },
  methods: {
    handleResize() {
      if (this.myChart) {
        this.myChart.resize()
      }
    },

    drawLine() {
      // 基于准备好的dom，初始化echarts实例
      if (!this.myChart) {
        this.myChart = echarts.init(document.getElementById('myChart'))
      }

      let dataMax = [
        { name: '碳减排指标', max: 100},
        { name: '碳中和指标', max: 100 },
        { name: '碳普惠指标', max: 100 },
      ]
      
      let option = {
        color: ['#4CAF50'],
        radar: {
          name: {
            show: true,
            color: '#1B5E20',
            fontSize: 12,
            fontWeight: 'bold'
          },
          axisLabel: {
            show: false
          },
          indicator: dataMax,
          splitArea: {
            areaStyle: {
              color: ['rgba(232, 245, 233, 0.1)', 'rgba(232, 245, 233, 0.2)', 'rgba(232, 245, 233, 0.4)', 'rgba(232, 245, 233, 0.6)', 'rgba(232, 245, 233, 0.8)'],
              shadowColor: 'rgba(0, 0, 0, 0.1)',
              shadowBlur: 10
            }
          },
          axisLine: {
            lineStyle: {
              color: 'rgba(46, 125, 50, 0.3)'
            }
          },
          splitLine: {
            lineStyle: {
              color: 'rgba(46, 125, 50, 0.3)'
            }
          }
        },
        series: [
          {
            type: 'radar',
            symbol: 'circle',
            symbolSize: 6,
            areaStyle: {
              color: 'rgba(76, 175, 80, 0.5)'
            },
            lineStyle: {
              color: '#2E7D32',
              width: 2
            },
            itemStyle: {
              color: '#2E7D32',
              borderColor: '#fff',
              borderWidth: 2
            },
            data: [
              {
                value: [
                  this.greenIndex.carbonEmissionRate || 65, 
                  this.greenIndex.environmentRate || 35, 
                  this.greenIndex.energyEfficiencyRate || 35
                ],
                name: '企业绿度'
              },
            ],
          },
        ],
      }
      // 绘制图表
      this.myChart.setOption(option)
    },
  },
  watch: {
    greenIndex: {
      handler() {
        this.drawLine()
      },
      deep: true
    }
  }
}
</script>
<style lang="scss" scoped>
  .radar-container {
    width: 100%;
    height: 100%;
    display: flex;
    justify-content: center;
    align-items: center;
  }
  .radar {
    width: 100%;
    height: 300px;
  }
</style>
