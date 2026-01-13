<template>
  <el-dialog
    top="15px"
    :title="title"
    :visible.sync="show"
    width="720px"
    :before-close="clickClose"
  >
    <el-form
      label-position="left"
      label-width="130px"
      :model="subForm"
      :rules="formRules"
      ref="orderForm"
    >
      <el-form-item label="供应方" prop="institutionName">
        <el-input
          disabled
          v-model="subForm.institutionName"
          size="medium"
          class="contentItem"
        />
      </el-form-item>

      <el-form-item prop="assetTypeName">
        <label slot="label">资产类型<span style="color: red">*</span></label>
        <el-cascader
          class="selectbox-root"
          style="width: 100%; top: -5px; background-color: #f5f7fa"
          v-model="subForm.assetType"
          :options="assetTypeList"
          disabled
        />
      </el-form-item>

      <el-form-item prop="projectScopeCode">
        <label slot="label">项目领域<span style="color: red">*</span></label>
        <el-cascader
          class="selectbox-root"
          style="width: 100%; top: -5px; background-color: #f5f7fa"
          v-model="subForm.projectScopeCode"
          :options="projectAreaList"
          disabled
        />
      </el-form-item>

      <el-form-item prop="projectTypeName">
        <label slot="label">项目类型<span style="color: red">*</span></label>
        <el-cascader
          class="selectbox-root"
          style="width: 100%; top: -5px; background-color: #f5f7fa"
          v-model="subForm.projectType"
          :options="projectTypeList"
          disabled
        />
      </el-form-item>

      <el-form-item prop="tradeQuantity">
        <label slot="label">采购数量(tCO2e)<span style="color: red">*</span></label>
        <el-input v-model="subForm.tradeQuantity" size="medium" class="contentItem" />
      </el-form-item>

      <el-form-item prop="assetUnitPrice">
        <label slot="label">询价(¥)<span style="color: red">*</span></label>
        <el-input
          v-model="subForm.assetUnitPrice"
          size="medium"
          class="contentItem"
          type="number"
        />
      </el-form-item>

      <el-form-item label="期望交割日期" prop="deliveryTime">
        <el-date-picker
          class="contentItem"
          v-model="selectDate"
          type="datetime"
          placeholder="选择日期时间"
          align="right"
          :picker-options="pickerOptions"
          size="medium"
          value-format="yyyy-MM-dd HH:mm:ss"
        />
      </el-form-item>

      <el-form-item label="期望交割方式" prop="deliveryMethodName">
        <el-cascader
          class="selectbox-root"
          style="width: 100%; top: -5px"
          placeholder="全部"
          v-model="subForm.deliveryMethod"
          :options="deliverMethodList"
          clearable
        />
      </el-form-item>

      <el-form-item label="期望交割场所" prop="deliveryExchangeName">
        <el-cascader
          class="selectbox-root"
          style="width: 100%; top: -5px"
          placeholder="全部"
          v-model="subForm.deliveryExchange"
          :options="exchangeList"
          clearable
        />
      </el-form-item>
    </el-form>

    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="clickSubmit" class="light-green-btn">
        {{ btnText }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import { startTrading } from "@/api/carbonAssetApi";
import {
  getDiliveryMethodeDict,
  getExchangeDict,
  getAssetTypeDict,
  getProjectTypeDict,
  getProjectAreaDict,
} from "@/config/dictHelper";
import { getAccoutEnterPriseInfo } from "@/api/tenant";

export default {
  name: "sellOrder",
  props: {
    form: { type: Object, default: () => ({}) },
    title: { type: String, default: "" },
    dialogFormVisible: { type: Boolean, default: false },
  },
  data() {
    return {
      btnText: "询价",
      show: false,
      subForm: {
        id: "",
        institutionName: "",
        deliveryExchange: [],
        deliveryMethod: [],
        assetUnitPrice: "",
        assetType: [],
        projectType: [],
        projectScopeCode: [],
        tradeQuantity: "",
      },
      selectDate: "",
      deliverMethodList: [],
      exchangeList: [],
      assetTypeList: [],
      projectTypeList: [],
      projectAreaList: [],
      pickerOptions: {
        shortcuts: [
          { text: "今天", onClick: (picker) => picker.$emit("pick", new Date()) },
          {
            text: "昨天",
            onClick: (picker) => {
              const date = new Date();
              date.setTime(date.getTime() - 3600 * 1000 * 24);
              picker.$emit("pick", date);
            },
          },
          {
            text: "一周前",
            onClick: (picker) => {
              const date = new Date();
              date.setTime(date.getTime() - 3600 * 1000 * 24 * 7);
              picker.$emit("pick", date);
            },
          },
        ],
      },
      formRules: {
        assetUnitPrice: [
          { pattern: /^[0-9]+(\.[0-9]{1,4})?$/, message: "只能输入数字" },
          { required: true, message: "请输入询价价格", trigger: "blur" },
        ],
        tradeQuantity: [
          { pattern: /^[0-9]*$/, message: "只能输入数字" },
          { required: true, message: "请输入采购数量", trigger: "blur" },
        ],
      },
    };
  },
  watch: {
    form() {
      this.initParams();
    },
    dialogFormVisible() {
      this.show = this.dialogFormVisible;
    },
  },
  mounted() {
    this.initParams();
    this.show = this.dialogFormVisible;
  },
  methods: {
    checkParam(value) {
      return !value || value === "" || value === "--" || value === "/";
    },
    normalizeCascaderValue(value, defaultValue) {
      if (Array.isArray(value)) return value;
      if (this.checkParam(value)) return defaultValue ? [defaultValue] : [];
      return [value];
    },
    initParams() {
      const base = {
        id: "",
        institutionName: "",
        deliveryExchange: [],
        deliveryMethod: [],
        assetUnitPrice: "",
        assetType: [],
        projectType: [],
        projectScopeCode: [],
        tradeQuantity: "",
      };

      this.subForm = { ...base, ...(this.form || {}) };

      this.subForm.assetType = this.normalizeCascaderValue(this.subForm.assetType, "0140000000");
      this.subForm.projectType = this.normalizeCascaderValue(this.subForm.projectType, "0040000000");
      this.subForm.projectScopeCode = this.normalizeCascaderValue(this.subForm.projectScopeCode);
      this.subForm.deliveryMethod = this.normalizeCascaderValue(this.subForm.deliveryMethod, "全部");
      this.subForm.deliveryExchange = this.normalizeCascaderValue(this.subForm.deliveryExchange, "全部");
      this.subForm.assetUnitPrice = this.checkParam(this.subForm.assetUnitPrice)
        ? "0"
        : String(this.subForm.assetUnitPrice);

      if (this.subForm.deliveryTime && typeof this.subForm.deliveryTime === "string") {
        this.subForm.deliveryTime = this.subForm.deliveryTime.split(" ")[0];
      }

      if (this.form && this.form.publisherId) {
        getAccoutEnterPriseInfo(this.form.publisherId).then((res) => {
          this.subForm.institutionName = (res && res.tenantName) || this.subForm.institutionName;
        });
      }

      const deliverData = getDiliveryMethodeDict(this.$store) || [];
      this.deliverMethodList = deliverData.map((v) => ({ value: v.value, label: v.name }));

      const exchangeData = getExchangeDict(this.$store) || [];
      this.exchangeList = exchangeData.map((v) => ({ value: v.value, label: v.name }));

      const assetData = getAssetTypeDict(this.$store) || [];
      this.assetTypeList = assetData.map((v) => ({ value: v.value, label: v.name }));

      const projectData = getProjectTypeDict(this.$store) || [];
      this.projectTypeList = projectData.map((v) => ({ value: v.value, label: v.name }));

      const areaData = getProjectAreaDict(this.$store) || [];
      this.projectAreaList = areaData.map((v) => ({ value: v.value, label: v.name }));
    },
    clickClose() {
      this.$emit("changeDialogFormVisible", false);
      this.show = false;
    },
    clickSubmit() {
      if (Number(this.subForm.tradeQuantity) <= 0) {
        this.$message("请输入正确的询价数量");
        return;
      }
      if (Number(this.subForm.assetUnitPrice) <= 0) {
        this.$message("请输入正确的询价价格");
        return;
      }

      this.clickClose();

      const data = {
        assetUnitPrice: this.subForm.assetUnitPrice,
        projectScopeCode: this.subForm.projectScopeCode[0] || "",
        assetType: this.subForm.assetType[0] === "0140000000" ? "" : this.subForm.assetType[0],
        projectType: this.subForm.projectType[0] === "0040000000" ? "" : this.subForm.projectType[0],
        deliveryExchange: this.subForm.deliveryExchange[0] === "全部" ? "" : this.subForm.deliveryExchange[0],
        deliveryMethod: this.subForm.deliveryMethod[0] === "全部" ? "" : this.subForm.deliveryMethod[0],
        deliveryTime: this.selectDate || "",
        tradeQuantity: this.subForm.tradeQuantity,
        tradeQuoteId: this.subForm.id,
      };

      startTrading(data)
        .then(() => {
          this.$emit("successSubmit", false);
        })
        .catch((error) => {
          this.$message((error && error.msg) || "提交失败");
        });
    },
  },
};
</script>

<style lang="scss" scoped>
.contentItem {
  width: 100%;
  top: -5px;
}

>>> .el-form-item.is-required:not(.is-no-asterisk) > .el-form-item__label:before,
.el-form-item.is-required:not(.is-no-asterisk) .el-form-item__label-wrap > .el-form-item__label:before {
  content: "";
  color: transparent !important;
  margin-right: 0px;
}

>>> .el-form-item__error {
  color: #ff4949;
  padding-top: 4px;
  position: absolute;
  top: 100%;
  left: 0px;
}
</style>
