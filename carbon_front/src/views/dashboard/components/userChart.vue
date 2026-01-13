<template>
  <div class="user-chart-container">
    <el-row :gutter="20">
      <el-col :xl="16" :lg="16" :md="24" :sm="24" :xs="24" class="mb-20">
        <el-card :bordered="false" class="glass-card">
          <div slot="header" class="card-header">
            <div class="header-title">
              <div class="icon-wrapper blue">
                <i class="el-icon-s-operation"></i>
              </div>
              <span class="title-text">运营数据</span>
            </div>
          </div>
          <div class="card-body">
            <operation-data ref="operationData"/>
          </div>
        </el-card>
      </el-col>
      
      <el-col :xl="8" :lg="8" :md="24" :sm="24" :xs="24" class="mb-20">
        <el-card :bordered="false" class="glass-card">
          <div slot="header" class="card-header">
            <div class="header-title">
              <div class="icon-wrapper green">
                <i class="el-icon-picture-outline-round"></i>
              </div>
              <span class="title-text">企业绿度</span>
            </div>
          </div>
          <div class="card-body">
            <green-degree ref="greenDegree"/>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import operationData from './operationData.vue'
import greenDegree from './greenDegree.vue'

export default {
  name: 'UserChart',
  components: { operationData, greenDegree },
  data() {
    return {}
  },
  methods: {
    handleResize() {
      // Pass resize event to children if needed
    }
  }
}
</script>

<style lang="scss" scoped>
$eco-green: #2E7D32;
$eco-light: #4CAF50;
$eco-bg: #E8F5E9;
$text-main: #1B5E20;

.user-chart-container {
  width: 100%;
}

.mb-20 {
  margin-bottom: 20px;
}

.glass-card {
  background: rgba(255, 255, 255, 0.6);
  border-radius: 12px;
  border: 1px solid rgba(232, 245, 233, 0.6);
  height: 100%;
  
  ::v-deep .el-card__header {
    border-bottom: 1px solid rgba(46, 125, 50, 0.1);
    padding: 15px 20px;
  }
  
  ::v-deep .el-card__body {
    padding: 20px;
  }
}

.card-header {
  display: flex;
  align-items: center;
}

.header-title {
  display: flex;
  align-items: center;
  
  .icon-wrapper {
    width: 32px;
    height: 32px;
    border-radius: 8px;
    display: flex;
    align-items: center;
    justify-content: center;
    margin-right: 10px;
    font-size: 16px;
    
    &.blue {
      background: #E3F2FD;
      color: #1976D2;
    }
    
    &.green {
      background: #E8F5E9;
      color: #2E7D32;
    }
  }
  
  .title-text {
    font-size: 16px;
    font-weight: 600;
    color: $text-main;
  }
}

.card-body {
  min-height: 380px;
}
</style>
