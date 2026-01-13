<template>
  <div class="green-degree-container">
    <div class="chart-wrapper">
      <radar :greenIndex="greenIndex"/>
    </div>
  </div>
</template>

<script>
import radar from '@/components/echarts/radar.vue';
import { loadGreenIndex } from '@/api/GreenApi'

export default {
  name: 'GreenDegree',
  components: { radar },
  data() {
    return {
      greenIndex: {}
    }
  },
  methods: {
    getGreenIndex() {
      loadGreenIndex().then(res => {
        this.greenIndex = {
          carbonEmissionRate: res.carbonEmissionRate,
          energyEfficiencyRate: res.energyEfficiencyRate,
          environmentRate: res.environmentRate
        }
      }).catch(e => {
        console.error(e)
      })
    },
  },
  mounted() {
    this.getGreenIndex()
  }
}
</script>

<style lang="scss" scoped>
.green-degree-container {
  width: 100%;
  height: 100%;
  padding: 10px;
}
.chart-wrapper {
  background: rgba(255, 255, 255, 0.5);
  border-radius: 12px;
  padding: 10px;
}
</style>
