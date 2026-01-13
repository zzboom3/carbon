<template>
  <div class="sidebar-logo-container" :class="{'collapse':collapse}">
    <transition name="sidebarLogoFade">
      <router-link v-if="collapse" key="collapse" class="sidebar-logo-link" to="/">
        <span class="sidebar-logo-text-small">C</span>
      </router-link>
      <router-link v-else key="expand" class="sidebar-logo-link" to="/">
        <div class="logo-wrapper">
          <span class="sidebar-logo-text">CARBON SYSTEM</span>
        </div>
      </router-link>
    </transition>
  </div>
</template>

<script>
  export default {
    name: 'SidebarLogo',
    props: {
      collapse: {
        type: Boolean,
        required: true
      }
    },
    data() {
      return {
        title: 'Carbon System'
      }
    }
  }
</script>

<style lang="scss" scoped>
  .sidebarLogoFade-enter-active {
    transition: opacity 1.5s;
  }

  .sidebarLogoFade-enter,
  .sidebarLogoFade-leave-to {
    opacity: 0;
  }

  .sidebar-logo-container {
    position: relative;
    width: 100%;
    height: 64px;
    line-height: 64px;
    background: #ffffff;
    text-align: center;
    overflow: hidden;
    box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);

    & .sidebar-logo-link {
      height: 100%;
      width: 100%;
      display: flex;
      justify-content: center;
      align-items: center;
      text-decoration: none;

      .logo-wrapper {
        display: flex;
        align-items: center;
        justify-content: center;
      }

      .sidebar-logo-text {
        color: #2E7D32;
        font-weight: bold;
        font-size: 20px;
        font-family: Avenir, Helvetica Neue, Arial, Helvetica, sans-serif;
        letter-spacing: 1px;
      }
      
      .sidebar-logo-text-small {
        color: #2E7D32;
        font-weight: bold;
        font-size: 24px;
        font-family: Avenir, Helvetica Neue, Arial, Helvetica, sans-serif;
      }
    }

    &.collapse {
      .sidebar-logo {
        margin-right: 0px;
      }
    }
  }
</style>
