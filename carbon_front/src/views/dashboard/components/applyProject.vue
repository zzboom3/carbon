<template>
  <div class="apply-project-container">
    <div class="section-header">
      <span class="nav-title">项目开发进展</span>
      <div class="header-line"></div>
    </div>
    
    <div class="process-grid">
      <!-- 阶段一 -->
      <div class="process-card">
        <div class="card-header">
          <span class="step-num">01</span>
          <span class="step-title">项目立项</span>
          <div class="header-decoration"></div>
        </div>
        <div class="card-content">
          <div class="action-list">
            <div class="action-item">
              <span class="label">选择方法学</span>
              <router-link to="/carbon/methodology" class="link-btn">去选择</router-link>
            </div>
            <div class="action-item">
              <span class="label">创建项目</span>
              <router-link to="/carbon/projectCreate/projectAdd" class="link-btn">去创建</router-link>
            </div>
            <div class="action-item">
              <span class="label">上传业主资料</span>
              <router-link to="/carbon/ownerAdd" class="link-btn">去上传</router-link>
            </div>
            <div class="action-item">
              <span class="label">查看项目情况</span>
              <router-link to="/carbon/projectCreate" class="link-btn">去查看</router-link>
            </div>
          </div>
        </div>
      </div>

      <!-- 阶段二 -->
      <div class="process-card">
        <div class="card-header">
          <span class="step-num">02</span>
          <span class="step-title">项目监测</span>
          <div class="header-decoration"></div>
        </div>
        <div class="card-content">
          <div class="action-list">
            <div class="action-item">
              <span class="label">选择项目</span>
              <span class="status-text">--</span>
            </div>
            <div class="action-item">
              <span class="label">添加监测数据</span>
              <span class="status-text">--</span>
            </div>
            <div class="action-item">
              <span class="label">查看监测情况</span>
              <span class="status-text">--</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 阶段三 -->
      <div class="process-card">
        <div class="card-header">
          <span class="step-num">03</span>
          <span class="step-title">项目开发</span>
          <div class="header-decoration"></div>
        </div>
        <div class="card-content">
          <div class="action-list">
            <div class="action-item">
              <span class="label">选择项目</span>
              <router-link to="/carbon/projectCreate" class="link-btn">去选择</router-link>
            </div>
            <div class="action-item">
              <span class="label">编写项目设计文档</span>
              <router-link to="/carbon/projectDo" class="link-btn">去编写</router-link>
            </div>
            <div class="action-item">
              <span class="label">查看开发进展</span>
              <router-link to="/carbon/projectDo" class="link-btn">去查看</router-link>
            </div>
          </div>
        </div>
      </div>

      <!-- 阶段四 -->
      <div class="process-card">
        <div class="card-header">
          <span class="step-num">04</span>
          <span class="step-title">项目材料</span>
          <div class="header-decoration"></div>
        </div>
        <div class="card-content">
          <div class="action-list">
            <div class="action-item">
              <span class="label">项目审定报告</span>
              <router-link to="/carbon/projectFile" class="link-btn">去上传</router-link>
            </div>
            <div class="action-item">
              <span class="label">项目备案书</span>
              <router-link to="/carbon/projectFile" class="link-btn">去上传</router-link>
            </div>
            <div class="action-item">
              <span class="label">项目核证报告</span>
              <router-link to="/carbon/projectFile" class="link-btn">去上传</router-link>
            </div>
            <div class="action-item">
              <span class="label">碳减排量核证签发</span>
              <router-link to="/carbon/projectFile" class="link-btn">去上传</router-link>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "ApplyProject",
  data() {
    return {};
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

.apply-project-container {
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
  }
  
  .header-line {
    flex: 1;
    height: 1px;
    background: linear-gradient(90deg, rgba(46, 125, 50, 0.3), transparent);
  }
}

.process-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 20px;
}

.process-card {
  background: rgba(255, 255, 255, 0.6);
  border-radius: 12px;
  overflow: hidden;
  border: 1px solid rgba(232, 245, 233, 0.6);
  transition: all 0.3s;

  &:hover {
    transform: translateY(-5px);
    box-shadow: 0 8px 25px rgba(46, 125, 50, 0.15);
    background: $white;
    
    .card-header {
      background: linear-gradient(135deg, $eco-green, $eco-light);
    }
  }
}

.card-header {
  padding: 15px 20px;
  background: linear-gradient(135deg, $eco-light, $eco-green);
  color: $white;
  display: flex;
  align-items: center;
  position: relative;
  overflow: hidden;
  transition: all 0.3s;
  
  .step-num {
    font-size: 24px;
    font-weight: bold;
    opacity: 0.8;
    margin-right: 10px;
    font-family: 'Arial', sans-serif;
  }
  
  .step-title {
    font-size: 16px;
    font-weight: 600;
    z-index: 1;
  }
  
  .header-decoration {
    position: absolute;
    right: -10px;
    top: -10px;
    width: 60px;
    height: 60px;
    background: rgba(255,255,255,0.1);
    border-radius: 50%;
  }
}

.card-content {
  padding: 20px;
}

.action-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.action-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 13px;
  
  .label {
    color: $text-main;
  }
  
  .link-btn {
    color: $eco-light;
    text-decoration: none;
    font-weight: 500;
    padding: 4px 12px;
    border-radius: 15px;
    background: $eco-bg;
    transition: all 0.2s;
    
    &:hover {
      background: $eco-light;
      color: $white;
    }
  }
  
  .status-text {
    color: #909399;
  }
}
</style>
