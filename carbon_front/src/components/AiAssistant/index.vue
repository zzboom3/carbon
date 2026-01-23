<template>
  <div class="ai-assistant">
    <div class="ai-fab" @click="toggleOpen">AI</div>
    <div v-if="open" class="ai-window" :style="windowStyle">
      <div class="ai-header" @mousedown="onDragStart">
        <div class="ai-header-left">
          <div class="ai-title">AI 助手</div>
          <div v-if="sending" class="ai-subtitle">正在思考…</div>
        </div>
        <div class="ai-header-actions">
          <button class="ai-icon-btn" type="button" @click.stop="toggleMinimize">
            {{ minimized ? '□' : '—' }}
          </button>
          <button class="ai-icon-btn" type="button" @click.stop="close">×</button>
        </div>
      </div>

      <div v-show="!minimized" ref="body" class="ai-body">
        <div v-for="(m, idx) in messages" :key="idx" class="ai-msg" :class="m.role">
          <div class="ai-bubble">
            <pre class="ai-text">{{ m.content }}</pre>
          </div>
        </div>
      </div>

      <div v-show="!minimized" class="ai-footer">
        <el-input
          v-model="input"
          type="textarea"
          :rows="2"
          resize="none"
          placeholder="输入问题，Enter 发送，Shift+Enter 换行"
          @keydown.native="onKeyDown"
        />
        <div class="ai-footer-actions">
          <el-button size="mini" class="normal-white-btn" @click="clearMessages" :disabled="sending">清空</el-button>
          <el-button size="mini" class="light-green-btn" :loading="sending" @click="send">发送</el-button>
        </div>
      </div>

      <div v-show="!minimized" class="ai-resize-handle" @mousedown.stop.prevent="onResizeStart" />
    </div>
  </div>
</template>

<script>
import { streamChat } from "@/api/ai";

export default {
  name: "AiAssistant",
  data() {
    return {
      open: false,
      minimized: false,
      sending: false,
      input: "",
      messages: [
        { role: "assistant", content: "你好，我是 AI 助手。你可以直接问我任何问题。" },
      ],
      x: null,
      y: null,
      w: 380,
      h: 520,
      drag: null,
      resize: null,
      abortController: null,
    };
  },
  computed: {
    windowStyle() {
      return {
        left: `${this.x}px`,
        top: `${this.y}px`,
        width: `${this.w}px`,
        height: this.minimized ? "52px" : `${this.h}px`,
      };
    },
  },
  mounted() {
    this.restoreState();
    this.ensurePosition();
    window.addEventListener("mousemove", this.onGlobalMove);
    window.addEventListener("mouseup", this.onGlobalUp);
  },
  beforeDestroy() {
    window.removeEventListener("mousemove", this.onGlobalMove);
    window.removeEventListener("mouseup", this.onGlobalUp);
    this.cancel();
  },
  methods: {
    toggleOpen() {
      this.open = !this.open;
      if (this.open) {
        this.$nextTick(() => this.scrollToBottom());
      }
      this.persistState();
    },
    close() {
      this.open = false;
      this.persistState();
    },
    toggleMinimize() {
      this.minimized = !this.minimized;
      this.ensurePosition();
      if (!this.minimized) {
        this.$nextTick(() => this.scrollToBottom());
      }
      this.persistState();
    },
    onKeyDown(e) {
      if (e.key === "Enter" && !e.shiftKey) {
        e.preventDefault();
        this.send();
      }
    },
    clearMessages() {
      this.messages = [{ role: "assistant", content: "对话已清空。" }];
      this.persistState();
      this.$nextTick(() => this.scrollToBottom());
    },
    async send() {
      const text = (this.input || "").trim();
      if (!text || this.sending) return;
      this.cancel();

      this.sending = true;
      this.input = "";

      this.messages.push({ role: "user", content: text });
      const assistantIndex = this.messages.length;
      this.messages.push({ role: "assistant", content: "" });
      this.$nextTick(() => this.scrollToBottom());

      try {
        this.abortController = new AbortController();
        const history = this.messages
          .filter((m, idx) => idx !== assistantIndex)
          .map((m) => ({ role: m.role, content: m.content }));

        await streamChat({
          store: this.$store,
          messages: history,
          signal: this.abortController.signal,
          onDelta: (delta) => {
            this.messages[assistantIndex].content += delta;
            this.$nextTick(() => this.scrollToBottom());
          },
        });
      } catch (e) {
        const msg = (e && e.message) ? e.message : "AI 调用失败";
        this.messages[assistantIndex].content = msg;
      } finally {
        this.sending = false;
        this.abortController = null;
        this.persistState();
      }
    },
    cancel() {
      if (this.abortController) {
        try {
          this.abortController.abort();
        } catch (e) {}
        this.abortController = null;
      }
    },
    scrollToBottom() {
      const el = this.$refs.body;
      if (!el) return;
      el.scrollTop = el.scrollHeight + 200;
    },
    onDragStart(e) {
      this.drag = {
        startX: e.clientX,
        startY: e.clientY,
        origX: this.x,
        origY: this.y,
      };
    },
    onResizeStart(e) {
      if (this.minimized) return;
      this.resize = {
        startX: e.clientX,
        startY: e.clientY,
        origW: this.w,
        origH: this.h,
      };
    },
    onGlobalMove(e) {
      if (this.drag) {
        const dx = e.clientX - this.drag.startX;
        const dy = e.clientY - this.drag.startY;
        this.x = this.drag.origX + dx;
        this.y = this.drag.origY + dy;
        this.ensurePosition();
      }
      if (this.resize) {
        const dx = e.clientX - this.resize.startX;
        const dy = e.clientY - this.resize.startY;
        this.w = Math.max(320, this.resize.origW + dx);
        this.h = Math.max(360, this.resize.origH + dy);
        this.ensurePosition();
      }
    },
    onGlobalUp() {
      if (this.drag || this.resize) {
        this.drag = null;
        this.resize = null;
        this.persistState();
      }
    },
    ensurePosition() {
      const margin = 12;
      const vw = window.innerWidth || 1200;
      const vh = window.innerHeight || 800;
      const currentH = this.minimized ? 52 : this.h;

      if (this.x == null || this.y == null) {
        this.x = vw - this.w - 18;
        this.y = vh - currentH - 18;
      }

      const maxX = Math.max(margin, vw - this.w - margin);
      const maxY = Math.max(margin, vh - currentH - margin);

      this.x = Math.min(Math.max(margin, this.x), maxX);
      this.y = Math.min(Math.max(margin, this.y), maxY);
      return { x: this.x, y: this.y };
    },
    persistState() {
      const state = {
        open: this.open,
        minimized: this.minimized,
        x: this.x,
        y: this.y,
        w: this.w,
        h: this.h,
        messages: this.messages.slice(-20),
      };
      try {
        localStorage.setItem("carbon_ai_widget_state", JSON.stringify(state));
      } catch (e) {}
    },
    restoreState() {
      try {
        const raw = localStorage.getItem("carbon_ai_widget_state");
        if (!raw) return;
        const s = JSON.parse(raw);
        this.open = !!s.open;
        this.minimized = !!s.minimized;
        if (typeof s.x === "number") this.x = s.x;
        if (typeof s.y === "number") this.y = s.y;
        if (typeof s.w === "number") this.w = s.w;
        if (typeof s.h === "number") this.h = s.h;
        if (Array.isArray(s.messages) && s.messages.length) {
          this.messages = s.messages;
        }
      } catch (e) {}
    },
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

.ai-window {
  position: fixed;
  border-radius: 10px;
  background: #fff;
  box-shadow: 0 12px 30px rgba(0, 0, 0, 0.16);
  overflow: hidden;
  z-index: 3001;
}

.ai-header {
  height: 52px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 12px;
  background: linear-gradient(135deg, #24a776, #2bbd85);
  color: #fff;
  cursor: move;
  user-select: none;
}

.ai-header-left {
  display: flex;
  align-items: baseline;
  gap: 10px;
}

.ai-title {
  font-size: 16px;
  font-weight: 700;
  color: #fff;
}

.ai-subtitle {
  font-size: 12px;
  opacity: 0.9;
}

.ai-header-actions {
  display: flex;
  align-items: center;
  gap: 6px;
}

.ai-icon-btn {
  width: 28px;
  height: 28px;
  border-radius: 6px;
  border: 1px solid rgba(255, 255, 255, 0.35);
  background: rgba(255, 255, 255, 0.18);
  color: #fff;
  cursor: pointer;
  font-size: 16px;
  line-height: 26px;
  padding: 0;
}

.ai-body {
  height: calc(100% - 52px - 112px);
  padding: 12px;
  overflow: auto;
  background: #f7f9fb;
}

.ai-msg {
  display: flex;
  margin-bottom: 10px;
}

.ai-msg.user {
  justify-content: flex-end;
}

.ai-bubble {
  max-width: 85%;
  border-radius: 10px;
  padding: 10px 12px;
  background: #ffffff;
  border: 1px solid rgba(31, 45, 61, 0.08);
}

.ai-msg.user .ai-bubble {
  background: rgba(36, 167, 118, 0.12);
  border-color: rgba(36, 167, 118, 0.2);
}

.ai-text {
  margin: 0;
  white-space: pre-wrap;
  word-break: break-word;
  font-family: inherit;
  font-size: 13px;
  color: #1f2d3d;
}

.ai-footer {
  height: 112px;
  padding: 10px 12px;
  background: #fff;
  border-top: 1px solid rgba(31, 45, 61, 0.08);
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.ai-footer-actions {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
}

.ai-resize-handle {
  position: absolute;
  width: 14px;
  height: 14px;
  right: 2px;
  bottom: 2px;
  cursor: nwse-resize;
  background: rgba(0, 0, 0, 0);
}
</style>
