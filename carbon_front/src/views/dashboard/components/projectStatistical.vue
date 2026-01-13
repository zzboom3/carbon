<template>
  <div class="project-stat-container">
    <div class="section-header">
      <router-link class="nav-title" to="/carbon/projectDo">项目统计</router-link>
      <div class="header-line"></div>
    </div>
    
    <div class="card-body">
      <!-- 左侧统计图表区 -->
      <div class="stat-visual">
        <div class="visual-bg">
          <!-- 中心数据 -->
          <div class="center-data">
            <span class="value">{{ projectStat.reductionTotal }}</span>
            <span class="unit">tCO2e</span>
            <span class="label">预计减排量</span>
          </div>
          
          <!-- 环绕数据点 -->
          <div class="stat-point point-1">
            <span class="point-val">{{ projectStat.approvedCount }}</span>
            <span class="point-label">累计审定</span>
          </div>
          <div class="stat-point point-2">
            <span class="point-val">{{ projectStat.filingCount }}</span>
            <span class="point-label">累计备案</span>
          </div>
          <div class="stat-point point-3">
            <span class="point-val">{{ projectStat.singCount }}</span>
            <span class="point-label">累计签发</span>
          </div>
        </div>
      </div>

      <!-- 右侧列表区 -->
      <div class="table-wrapper">
        <el-table 
          :data="projectStat.projectList" 
          stripe 
          style="width: 100%" 
          max-height="368"
          class="nature-table">
          <el-table-column prop="projectName" label="项目名称" min-width="140" show-overflow-tooltip></el-table-column>
          <el-table-column prop="reduction" label="预计减排量(tCO2e)" min-width="140" align="center"></el-table-column>
          <el-table-column prop="carbonValuation" label="碳资产估值(¥)" min-width="120" align="center">
            <template slot-scope="scope">
              <span class="valuation">¥{{ scope.row.carbonValuation }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="developmentStateName" label="项目状态" width="100" align="center">
            <template slot-scope="scope">
              <span class="status-tag">{{ scope.row.developmentStateName }}</span>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "ProjectStatistical",
  props: {
    projectStat: {
      type: Object,
      default: () => ({
        reductionTotal: 0,
        approvedCount: 0,
        filingCount: 0,
        singCount: 0,
        projectList: []
      })
    },
  }
};
</script>

<style lang="scss" scoped>
/* 绿色环保主题变量 */
$eco-green: #2E7D32;
$eco-light: #4CAF50;
$eco-bg: #E8F5E9;
$text-main: #1B5E20;
$text-sub: #546E7A;
$white: #ffffff;

.project-stat-container {
  padding: 0 10px;
}

.section-header {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
  
  .nav-title {
    font-size: 18px;
    font-weight: 600;
    color: $text-main;
    margin-right: 15px;
    text-decoration: none;
    cursor: pointer;
    
    &:hover {
      color: $eco-green;
    }
  }
  
  .header-line {
    flex: 1;
    height: 1px;
    background: linear-gradient(90deg, rgba(46, 125, 50, 0.3), transparent);
  }
}

.card-body {
  display: flex;
  gap: 30px;
  background: rgba(255, 255, 255, 0.6);
  border-radius: 16px;
  padding: 20px;
  
  @media screen and (max-width: 900px) {
    flex-direction: column;
  }
}

/* 左侧可视化区域 */
.stat-visual {
  flex: 0 0 300px;
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
  
  .visual-bg {
    width: 260px;
    height: 260px;
    background: url("~@/assets/imgs/bg_project_stati.png") center/contain no-repeat;
    position: relative;
    display: flex;
    justify-content: center;
    align-items: center;
  }
  
  .center-data {
    text-align: center;
    display: flex;
    flex-direction: column;
    z-index: 2;
    
    .value {
      font-size: 28px;
      font-weight: bold;
      color: $white;
      text-shadow: 0 2px 4px rgba(0,0,0,0.2);
    }
    
    .unit {
      font-size: 12px;
      color: rgba(255,255,255,0.9);
      margin-bottom: 5px;
    }
    
    .label {
      font-size: 12px;
      color: rgba(255,255,255,0.8);
    }
  }
  
  .stat-point {
    position: absolute;
    display: flex;
    flex-direction: column;
    align-items: center;
    
    .point-val {
      font-weight: bold;
      color: $text-main;
      font-size: 16px;
    }
    
    .point-label {
      font-size: 12px;
      color: $text-sub;
    }
    
    &.point-1 { top: 10%; right: 10%; }
    &.point-2 { bottom: 20%; right: 5%; }
    &.point-3 { bottom: 20%; left: 5%; }
  }
}

/* 右侧表格区域 */
.table-wrapper {
  flex: 1;
  overflow: hidden;
  border-radius: 12px;
  background: rgba(255,255,255,0.5);
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
  
  .valuation {
    color: #F57C00;
    font-weight: 600;
  }
  
  .status-tag {
    background: #E8F5E9;
    color: $eco-green;
    padding: 2px 8px;
    border-radius: 4px;
    font-size: 12px;
  }
}
</style>
