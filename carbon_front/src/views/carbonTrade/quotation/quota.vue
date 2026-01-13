<template>
  <div class="root">
    <div class="divBox">
      <el-tabs v-model="activeTab">
        <el-tab-pane label="采购行情" name="buy">
          <buy-quote />
        </el-tab-pane>
        <el-tab-pane label="供应行情" name="sell">
          <sell-quote />
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script>
import BuyQuote from "./buy/buyQuote.vue";
import SellQuote from "./sell/sellQuote.vue";

export default {
  name: "quotationQuota",
  components: { BuyQuote, SellQuote },
  data() {
    return {
      activeTab: "buy",
    };
  },
};
</script>

<style lang="scss" scoped>
.root {
  background: #f2f5f7;
}

.divBox {
  margin: 20px;
  background: #ffffff;
  box-shadow: 0px 2px 8px 0px #eaf0f3;
  border-radius: 8px;
  padding: 10px 20px 20px 20px;
}
</style>
