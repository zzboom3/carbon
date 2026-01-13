<template>
  <div class="user-info-container">
    <div class="logo-wrapper">
      <img class="home-logo" src="@/assets/imgs/icon_home_logo.png"/>
      <span class="system-name">CARBON SYSTEM</span>
    </div>
    <div class="welcome-text">
      欢迎回来，共建绿色未来
    </div>
  </div>
</template>

<script>
export default {
  name: 'UserInfo',
  data() {
    return {}
  }
}
</script>

<style lang="scss" scoped>
.user-info-container {
  background: rgba(255, 255, 255, 0.6);
  backdrop-filter: blur(10px);
  height: 60px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  border-radius: 12px;
  border: 1px solid rgba(232, 245, 233, 0.6);
  margin-bottom: 20px;
}

.logo-wrapper {
  display: flex;
  align-items: center;
  
  .home-logo {
    width: 32px;
    height: 32px;
    margin-right: 10px;
  }
  
  .system-name {
    font-size: 18px;
    font-weight: bold;
    color: #2E7D32;
    letter-spacing: 1px;
  }
}

.welcome-text {
  font-size: 14px;
  color: #546E7A;
  font-weight: 500;
}
</style>
