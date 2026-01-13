<template>
  <div class="divBox">
    <div class="root">
      <div id="your-mount-point"></div>
    </div>
  </div>
</template>

<script>
import "@/api/feishuSdk.js";

export default {
  name: "projectSchedule",
  data() {
    return {
      fileUrl: "https://zfx2bso66l.feishu.cn/sheets/shtcnoHokr9Y571RpgiWODMHaHd",
    };
  },
  methods: {
    sendCode() {
      window.location.href = this.fileUrl;
    },
  },
  mounted() {
    this.sendCode();
  },
};
</script>

<style scoped>
.divBox {
  margin: 20px;
}
.root {
  padding-left: 30px;
  padding-right: 30px;
  background: #ffffff;
  box-shadow: 0px 2px 8px 0px #eaf0f3;
  border-radius: 8px;
  overflow: auto;
}
</style>
