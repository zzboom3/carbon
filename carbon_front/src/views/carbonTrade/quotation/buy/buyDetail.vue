<template>
  <el-dialog
    top="15px"
    :title="title"
    :visible.sync="show"
    width="720px"
    :before-close="clickClose"
  >
    <el-form label-position="left" label-width="130px">
      <el-form-item label="采购方">
        <el-input
          disabled
          v-model="subForm.institutionName"
          size="medium"
          class="contentItem"
        />
      </el-form-item>
      <el-form-item label="资产类型">
        <el-input
          disabled
          v-model="subForm.assetTypeName"
          size="medium"
          class="contentItem"
        />
      </el-form-item>
      <el-form-item label="项目类型">
        <el-input
          disabled
          v-model="subForm.projectTypeName"
          size="medium"
          class="contentItem"
        />
      </el-form-item>
      <el-form-item label="采购数量(tCO2e)">
        <el-input
          disabled
          v-model="subForm.tradeQuantity"
          size="medium"
          class="contentItem"
        />
      </el-form-item>
      <el-form-item label="询价(¥)">
        <el-input
          disabled
          v-model="subForm.assetUnitPrice"
          size="medium"
          class="contentItem"
        />
      </el-form-item>
      <el-form-item label="期望交割日期">
        <el-input
          disabled
          v-model="subForm.deliveryTime"
          size="medium"
          class="contentItem"
        />
      </el-form-item>
      <el-form-item label="期望交割方式">
        <el-input
          disabled
          v-model="subForm.deliveryMethodName"
          size="medium"
          class="contentItem"
        />
      </el-form-item>
      <el-form-item label="期望交割场所">
        <el-input
          disabled
          v-model="subForm.deliveryExchangeName"
          size="medium"
          class="contentItem"
        />
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="clickClose" class="light-green-btn">
        {{ btnText }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getAccoutEnterPriseInfo } from "@/api/tenant";

export default {
  name: "buyDetail",
  props: {
    form: { type: Object, default: () => ({}) },
    title: { type: String, default: "" },
    dialogFormVisible: { type: Boolean, default: false },
  },
  data() {
    return {
      btnText: "确定",
      show: false,
      subForm: {},
    };
  },
  watch: {
    form() {
      this.subForm = { ...this.form };
      this.initParams();
    },
    dialogFormVisible() {
      this.show = this.dialogFormVisible;
    },
  },
  mounted() {
    this.subForm = { ...this.form };
    this.show = this.dialogFormVisible;
    this.initParams();
  },
  methods: {
    clickClose() {
      this.$emit("changeDialogFormVisible", false);
      this.show = false;
    },
    checkParam(value) {
      return !value || value === "" || value === "--" || value === "/";
    },
    initParams() {
      this.subForm.assetUnitPrice = this.checkParam(this.subForm.assetUnitPrice)
        ? "0"
        : this.subForm.assetUnitPrice;

      if (this.subForm.deliveryTime && typeof this.subForm.deliveryTime === "string") {
        this.subForm.deliveryTime = this.subForm.deliveryTime.split(" ")[0];
      }

      if (this.form && this.form.publisherId) {
        getAccoutEnterPriseInfo(this.form.publisherId).then((res) => {
          this.subForm.institutionName =
            (res && res.tenantName) || this.subForm.institutionName;
        });
      }
    },
  },
};
</script>

<style scoped>
.contentItem {
  width: 100%;
  top: -5px;
}
</style>
