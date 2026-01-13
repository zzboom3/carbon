<template>
  <div class="operation-data-container">
    <div class="stat-cards">
      <div class="stat-card">
        <div class="card-content">
          <span class="card-title">碳减排量</span>
          <span class="card-value highlight-red">{{ carbonReduce }}</span>
        </div>
      </div>
      <div class="stat-card">
        <div class="card-content">
          <span class="card-title">资源减排核证</span>
          <span class="card-value highlight-blue">{{ carbonCertificationCount }}</span>
        </div>
      </div>
      <div class="stat-card">
        <div class="card-content">
          <span class="card-title">碳减排方法学</span>
          <span class="card-value highlight-green">{{ carbonMethodologyCount }}</span>
        </div>
      </div>
      <div class="stat-card">
        <div class="card-content">
          <span class="card-title">碳排放权交易所</span>
          <span class="card-value highlight-orange">{{ carbonExchangeCount }}</span>
        </div>
      </div>
    </div>

    <div class="table-wrapper">
      <el-table
        :data="carbonAssetsList"
        height="250"
        style="width: 100%"
        class="nature-table">
        <el-table-column prop="amount" label="中和资产(CNC)" min-width="100" align="center"></el-table-column>
        <el-table-column prop="typeName" label="资源类型" min-width="100" align="center"></el-table-column>
        <el-table-column prop="uom" label="资产UOM" min-width="80" align="center"></el-table-column>
        <el-table-column prop="carbonProjectName" label="资产开发项目" min-width="120" show-overflow-tooltip></el-table-column>
        <el-table-column prop="carbonMethodologyName" label="资产开发方法学" min-width="120" show-overflow-tooltip></el-table-column>
        <el-table-column prop="sourceChannelName" label="资产来源" min-width="100" show-overflow-tooltip></el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
import { getOperationData } from '@/api/operationApi';

export default {
  name: 'OperationData',
  data() {
    return {
      carbonReduce: 0,
      carbonCertificationCount: 0,
      carbonMethodologyCount: 0,
      carbonExchangeCount: 0,
      carbonAssetsList: [],
    }
  },
  methods: {
    loadData() {
      getOperationData().then(res => {
        this.carbonReduce = res.carbonReduce
        this.carbonCertificationCount = res.carbonCertificationCount
        this.carbonMethodologyCount = res.carbonMethodologyCount
        this.carbonExchangeCount = res.carbonExchangeCount
        this.carbonAssetsList = res.carbonAssetsList
      })
    }
  },
  mounted() {
    this.loadData()
  }
}
</script>

<style lang="scss" scoped>
/* 绿色环保主题变量 */
$eco-green: #2E7D32;
$eco-light: #4CAF50;
$text-main: #1B5E20;
$text-sub: #546E7A;
$white: #ffffff;

.operation-data-container {
  width: 100%;
}

.stat-cards {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 15px;
  margin-bottom: 20px;
  
  @media screen and (max-width: 768px) {
    grid-template-columns: repeat(2, 1fr);
  }
}

.stat-card {
  background: rgba(255, 255, 255, 0.6);
  border-radius: 12px;
  padding: 15px;
  border: 1px solid rgba(232, 245, 233, 0.6);
  transition: all 0.3s;
  
  &:hover {
    transform: translateY(-2px);
    background: rgba(255, 255, 255, 0.9);
    box-shadow: 0 4px 12px rgba(46, 125, 50, 0.1);
  }
  
  .card-content {
    display: flex;
    flex-direction: column;
    align-items: center;
    text-align: center;
    
    .card-title {
      font-size: 13px;
      color: $text-sub;
      margin-bottom: 8px;
    }
    
    .card-value {
      font-size: 20px;
      font-weight: bold;
      font-family: Arial, sans-serif;
      
      &.highlight-red { color: #E53935; }
      &.highlight-blue { color: #1E88E5; }
      &.highlight-green { color: #43A047; }
      &.highlight-orange { color: #FB8C00; }
    }
  }
}

.table-wrapper {
  background: rgba(255, 255, 255, 0.5);
  border-radius: 12px;
  overflow: hidden;
  border: 1px solid rgba(232, 245, 233, 0.6);
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
    
    &:hover td {
      background: rgba(232, 245, 233, 0.6) !important;
    }
    
    td {
      border-bottom: 1px dashed rgba(46, 125, 50, 0.1) !important;
      color: $text-main;
      font-size: 13px;
    }
  }
  
  ::v-deep .el-table__body-wrapper {
    &::-webkit-scrollbar {
      width: 6px;
      height: 6px;
    }
    &::-webkit-scrollbar-thumb {
      background: rgba(46, 125, 50, 0.2);
      border-radius: 3px;
    }
    &::-webkit-scrollbar-track {
      background: transparent;
    }
  }
}
</style>
