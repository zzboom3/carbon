<template>
  <div class="ai-assistant">
    <div class="ai-fab" @click="visible = true">AI</div>
    <el-dialog
      title="AI 助手"
      :visible.sync="visible"
      width="520px"
      append-to-body
    >
      <div class="ai-content">
        <div class="ai-title">AI 助手组件已接入</div>
        <div class="ai-desc">当前项目未配置实际 AI 服务，可在此处扩展对话能力。</div>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="visible = false">知道了</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  name: "AiAssistant",
  data() {
    return {
      visible: false,
    };
  },
};
</script>

<style scoped>
.ai-assistant {
  position: fixed;
  right: 18px;
  bottom: 18px;
  z-index: 3000;
}

.ai-fab {
  width: 44px;
  height: 44px;
  border-radius: 22px;
  background: #24a776;
  color: #fff;
  font-weight: 700;
  display: flex;
  align-items: center;
  justify-content: center;
  cursor: pointer;
  user-select: none;
  box-shadow: 0 10px 24px rgba(36, 167, 118, 0.28);
}

.ai-content {
  line-height: 1.8;
}

.ai-title {
  font-size: 16px;
  font-weight: 700;
  color: #1f2d3d;
}

.ai-desc {
  margin-top: 6px;
  font-size: 13px;
  color: #606266;
}
</style>

