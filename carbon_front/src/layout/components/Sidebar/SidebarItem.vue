<template>
  <div v-if="!item.hidden">
    <template v-if="hasOneShowingChild(item.children, item)">
      <app-link v-if="onlyOneChild" :to="resolvePath(onlyOneChild.menuUrl)">
        <el-menu-item
          :index="resolvePath(onlyOneChild.menuUrl)"
          class="submenu-title-noDropdown"
        >
          <item
            :icon="onlyOneChild.menuIcon || item.menuIcon"
            :title="onlyOneChild.menuName || item.menuName"
          />
        </el-menu-item>
      </app-link>
    </template>
    <el-submenu
      v-else
      ref="subMenu"
      :index="resolvePath(item.menuUrl)"
      popper-append-to-body
    >
      <template slot="title">
        <item v-if="item" :icon="item.menuIcon" :title="item.menuName" />
      </template>
      <sidebar-item
        v-for="childs in item.children"
        :key="childs.id"
        :is-nest="true"
        :item="childs"
        :base-path="childs.menuUrl"
      />
    </el-submenu>
  </div>
</template>

<script>
import path from "path";
import { isExternal } from "@/utils/validate";
import Item from "./Item";
import AppLink from "./Link";
import FixiOSBug from "./FixiOSBug";

export default {
  name: "SidebarItem",
  components: { Item, AppLink },
  mixins: [FixiOSBug],
  props: {
    // route object
    item: {
      type: Object,
      required: true,
    },
    isNest: {
      type: Boolean,
      default: false,
    },
    basePath: {
      type: String,
      default: "",
    },
  },
  mounted() {},
  data() {
    // To fix https://github.com/PanJiaChen/vue-admin-template/issues/237
    // TODO: refactor with render function
    this.onlyOneChild = null;
    return {};
  },
  computed: {
    getIcon(s) {
      return "@/assets/menu" + s + ".png";
    },
  },
  methods: {
    hasOneShowingChild(child = [], parent) {
      const children = Array.isArray(child) ? child.filter((c) => !c.hidden) : [];

      if (!children.length && parent) {
        this.onlyOneChild = { ...parent, path: "", noShowingChildren: true };
        return true;
      }

      if (children.length === 1) {
        this.onlyOneChild = children[0];
        return true;
      }

      return false;
    },
    resolvePath(routePath) {
      if (isExternal(routePath)) {
        return routePath;
      }
      if (isExternal(this.basePath)) {
        return this.basePath;
      }
      return path.resolve(this.basePath, routePath);
    },
  },
};
</script>
<style lang="less">
</style>
