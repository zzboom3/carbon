<template>
  <div :class="{ 'has-logo': showLogo }">
    <logo v-if="showLogo" :collapse="isCollapse" />
    <el-scrollbar wrap-class="scrollbar-wrapper">
      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        :background-color="variables.menuBg"
        :text-color="variables.menuText"
        :unique-opened="true"
        :active-text-color="variables.menuActiveText"
        :collapse-transition="false"
        mode="vertical"
      >
        <sidebar-item
          v-for="route in sidebarList"
          :key="route.id"
          :item="route"
          :base-path="route.menuUrl"
        />
      </el-menu>
    </el-scrollbar>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import Logo from "./Logo";
import SidebarItem from "./SidebarItem";
import variables from "@/styles/variables.scss";
import { getSliderMenus } from "@/api/configApi";
import { Message } from "element-ui";

export default {
  components: { SidebarItem, Logo },
  data() {
    return {
      sidebarList: [],
      menuCacheKey: "carbon_sidebar_menus",
    };
  },

  created() {
    this.loadSlidata();
  },

  computed: {
    ...mapGetters(["permission_routes", "sidebar"]),
    activeMenu() {
      const route = this.$route;
      const { meta, path } = route;
      // if set path, the sidebar will highlight the path you set
      if (meta.activeMenu) {
        return meta.activeMenu;
      }
      return path;
    },
    showLogo() {
      return this.$store.state.settings.sidebarLogo;
    },
    variables() {
      return variables;
    },
    isCollapse() {
      return !this.sidebar.opened;
    },
  },
  methods: {
    loadSlidata() {
      getSliderMenus()
        .then((res) => {
          const list = Array.isArray(res) ? res : [];
          this.sidebarList = list;
          if (list.length > 0) {
            window.localStorage.setItem(this.menuCacheKey, JSON.stringify(list));
          }
        })
        .catch(() => {
          const cached = window.localStorage.getItem(this.menuCacheKey);
          if (cached) {
            try {
              const list = JSON.parse(cached);
              if (Array.isArray(list) && list.length > 0) {
                this.sidebarList = list;
                Message.warning("菜单加载失败，已展示缓存菜单");
                return;
              }
            } catch (e) {}
          }
          this.sidebarList = [];
          Message.error("菜单加载失败，请重新登录或联系管理员");
        });
    },
  },
};
</script>
