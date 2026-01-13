<template>
  <div class="common-usage-container">
    <div class="section-header">
      <span class="nav-title">常用功能</span>
      <div class="header-line"></div>
    </div>
    
    <div class="usage-grid">
      <router-link :to="{ path: '/carbon/projectCreate' }" class="grid-card">
        <div class="card-content">
          <div class="icon-wrapper add">
            <img class="my-icon" src="@/assets/imgs/icon_usage_add.png" />
          </div>
          <div class="text-content">
            <h3 class="my-text">项目立项</h3>
            <p class="hint-text">查看项目立项，提供碳信用项目立项报表模板</p>
          </div>
          <div class="hover-arrow">
            <i class="el-icon-right"></i>
          </div>
        </div>
      </router-link>

      <router-link :to="{ path: '/carbon/methodology' }" class="grid-card">
        <div class="card-content">
          <div class="icon-wrapper project">
            <img class="my-icon" src="@/assets/imgs/icon_usage_project.png" />
          </div>
          <div class="text-content">
            <h3 class="my-text">项目方法学</h3>
            <p class="hint-text">涵盖国内外各类碳信用开发方法学，最全方法</p>
          </div>
          <div class="hover-arrow">
            <i class="el-icon-right"></i>
          </div>
        </div>
      </router-link>
     
      <router-link :to="{ path: '/trade/quotation' }" class="grid-card">
        <div class="card-content">
          <div class="icon-wrapper quote">
            <img class="my-icon" src="@/assets/imgs/icon_usage_quote.png" />
          </div>
          <div class="text-content">
            <h3 class="my-text">交易行情</h3>
            <p class="hint-text">国内碳交易行情纵览，随时掌握交易动态</p>
          </div>
          <div class="hover-arrow">
            <i class="el-icon-right"></i>
          </div>
        </div>
      </router-link>

      <router-link :to="{ path: '/trade/account/exchange' }" class="grid-card">
        <div class="card-content">
          <div class="icon-wrapper open">
            <img class="my-icon" src="@/assets/imgs/icon_usage_open.png" />
          </div>
          <div class="text-content">
            <h3 class="my-text">交易所开户</h3>
            <p class="hint-text">国内外权威碳交易所列目，支持各大交易所开户</p>
          </div>
          <div class="hover-arrow">
            <i class="el-icon-right"></i>
          </div>
        </div>
      </router-link>
    </div>
  </div>
</template>

<script>
export default {
  name: 'CommonUsageGrid',
  data() {
    return {
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

.common-usage-container {
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

.usage-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 20px;
}

.grid-card {
  display: block;
  text-decoration: none;
  background: rgba(255, 255, 255, 0.8);
  border-radius: 16px;
  overflow: hidden;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  border: 1px solid rgba(232, 245, 233, 0.6);
  position: relative;

  &:hover {
    transform: translateY(-5px);
    box-shadow: 0 8px 25px rgba(46, 125, 50, 0.15);
    background: $white;
    border-color: $eco-light;

    .icon-wrapper {
      transform: scale(1.1);
    }

    .hover-arrow {
      opacity: 1;
      transform: translateX(0);
    }
  }
}

.card-content {
  padding: 25px;
  display: flex;
  align-items: flex-start;
  position: relative;
  height: 100%;
}

.icon-wrapper {
  width: 50px;
  height: 50px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 15px;
  flex-shrink: 0;
  transition: transform 0.3s;

  &.add { background: #E8F5E9; }
  &.project { background: #E3F2FD; }
  &.quote { background: #FFF3E0; }
  &.open { background: #F3E5F5; }

  .my-icon {
    width: 28px;
    height: 28px;
  }
}

.text-content {
  flex: 1;
  padding-right: 20px;

  .my-text {
    font-size: 16px;
    font-weight: 600;
    color: $text-main;
    margin: 0 0 8px 0;
  }

  .hint-text {
    font-size: 13px;
    color: $text-sub;
    line-height: 1.5;
    margin: 0;
    display: -webkit-box;
    -webkit-line-clamp: 2;
    -webkit-box-orient: vertical;
    overflow: hidden;
  }
}

.hover-arrow {
  position: absolute;
  top: 25px;
  right: 20px;
  color: $eco-green;
  font-size: 20px;
  opacity: 0;
  transform: translateX(-10px);
  transition: all 0.3s;
}
</style>
