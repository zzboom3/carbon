<template>
  <div>
    <el-dialog
      :title="title"
      :visible.sync="show"
      :before-close="clickClose"
      width="720px"
    >
      <el-form v-if="isCredit" :model="form" :inline="true">
        <el-form-item>
          <span class="label">项目名称<span style="color: red">*</span></span>
          <el-input v-model="form.projectName" disabled size="medium" style="width: 420px" />
        </el-form-item>
        <el-button
          @click="pickProject"
          style="width: 100px; text-align: center"
          type="primary"
        >
          选择项目
        </el-button>

        <el-form-item>
          <span class="label">采用方法学</span>
          <el-input
            v-model="form.carbonMethodologyName"
            size="medium"
            style="width: 180px"
            disabled
          />
        </el-form-item>
        <el-form-item style="margin-left: 40px">
          <span class="label">类型</span>
          <el-input v-model="form.projectScopeType" size="medium" style="width: 180px" disabled />
        </el-form-item>

        <el-form-item>
          <span class="label">持仓总量(tCO2e)<span style="color: red">*</span></span>
          <el-input v-model="form.total" autocomplete="off" size="medium" style="width: 180px" />
        </el-form-item>
        <el-form-item style="margin-left: 40px">
          <span class="label">交易单价(￥)</span>
          <el-input v-model="form.buyUnitPrice" autocomplete="off" size="medium" style="width: 180px" />
        </el-form-item>
        <el-form-item>
          <span class="label">交易总价(￥)</span>
          <el-input v-model="form.buyTotalPrice" autocomplete="off" size="medium" style="width: 180px" />
        </el-form-item>
        <el-form-item style="margin-left: 40px">
          <span class="label">核证机构</span>
          <el-input v-model="form.projectVerifierCodeName" style="width: 180px" size="medium" disabled />
        </el-form-item>

        <el-form-item>
          <span class="label">签发日期</span>
          <el-date-picker type="date" v-model="form.issuingDate" autocomplete="off" size="medium" style="width: 180px" disabled />
        </el-form-item>
        <el-form-item>
          <span class="label" style="margin-left: 40px">交易日期</span>
          <el-date-picker type="date" v-model="form.buyDate" autocomplete="off" size="medium" style="width: 180px" />
        </el-form-item>
        <el-form-item>
          <span class="label">交易所</span>
          <el-select v-model="form.carbonExchangeId" placeholder="请选择" style="width: 180px" size="medium">
            <el-option v-for="(item, index) in exchangeList" :key="index" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>

        <br />

        <el-form-item>
          <span class="label" style="width: 305px">持仓凭证<span style="color: red">*</span></span>
          <el-upload
            class="upload-demo"
            ref="upload1"
            :action="upLoadParam.url"
            :file-list="issueFileList"
            :limit="2"
            :auto-upload="true"
            :on-success="creditSuccess1"
            style="width: 180px"
            :headers="{ token: upLoadParam.token }"
            :on-change="handleIssueChange"
            :on-preview="creditHandleFile"
          >
            <el-button type="primary">上传</el-button>
          </el-upload>
        </el-form-item>
        <el-form-item style="margin-left: 40px">
          <span class="label">交易凭证</span>
          <el-upload
            class="upload-demo"
            ref="upload2"
            :action="upLoadParam.url"
            :file-list="tranFileList"
            :auto-upload="false"
            :limit="2"
            style="width: 180px"
            :on-success="creditSuccess2"
            :headers="{ token: upLoadParam.token }"
            :on-change="handleTranChange"
            :on-preview="creditHandleFile"
          >
            <el-button type="primary">上传</el-button>
          </el-upload>
        </el-form-item>
      </el-form>

      <el-form v-if="!isCredit" :model="quotaForm" :inline="true">
        <el-form-item>
          <span class="label" style="width: 163px">名称(一级持有机构)<span style="color: red">*</span></span>
          <el-autocomplete
            v-model="quotaForm.agencyName"
            size="medium"
            style="width: 494px"
            :fetch-suggestions="querySearchAsync"
            placeholder="请输入内容"
            @select="handleSelect"
          />
        </el-form-item>

        <el-form-item>
          <span class="label">持仓总量(tCO2e)<span style="color: red">*</span></span>
          <el-input v-model="quotaForm.total" size="medium" style="width: 180px" type="number" @input="setFormValuation" />
        </el-form-item>
        <el-form-item style="margin-left: 40px">
          <span class="label">购入单价(￥)</span>
          <el-input v-model="quotaForm.buyUnitPrice" size="medium" style="width: 180px" />
        </el-form-item>
        <el-form-item>
          <span class="label">购入总价(￥)</span>
          <el-input v-model="quotaForm.buyTotalPrice" autocomplete="off" size="medium" style="width: 180px" />
        </el-form-item>
        <el-form-item style="margin-left: 40px">
          <span class="label">购入日期</span>
          <el-date-picker type="date" v-model="quotaForm.buyDate" autocomplete="off" size="medium" style="width: 180px" />
        </el-form-item>
        <el-form-item>
          <span class="label">交易所</span>
          <el-select v-model="quotaForm.carbonExchangeId" placeholder="请选择" style="width: 180px" size="medium">
            <el-option v-for="(item, index) in exchangeList" :key="index" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
        <el-form-item style="margin-left: 40px">
          <span class="label">可用数量(tCO2e)<span style="color: red">*</span></span>
          <el-input v-model="quotaForm.availableAmount" autocomplete="off" size="medium" style="width: 180px" />
        </el-form-item>
        <el-form-item>
          <span class="label">锁定数量(tCO2e)<span style="color: red">*</span></span>
          <el-input v-model="quotaForm.lockedAmount" autocomplete="off" size="medium" style="width: 180px" />
        </el-form-item>
        <el-form-item style="margin-left: 40px">
          <span class="label">冻结数量(tCO2e)<span style="color: red">*</span></span>
          <el-input v-model="quotaForm.frozenAmount" autocomplete="off" size="medium" style="width: 180px" />
        </el-form-item>
        <el-form-item>
          <span class="label">资产估值(￥)</span>
          <el-input v-model="quotaForm.valuation" autocomplete="off" size="medium" style="width: 180px" disabled />
        </el-form-item>
        <el-form-item style="margin-left: 40px">
          <span class="label">有效期</span>
          <el-date-picker type="date" v-model="quotaForm.expiryDate" autocomplete="off" size="medium" style="width: 180px" />
        </el-form-item>
        <el-form-item>
          <span class="label">签发日期<span style="color: red">*</span></span>
          <el-date-picker type="date" v-model="quotaForm.issuingDate" autocomplete="off" size="medium" style="width: 180px" />
        </el-form-item>
        <el-form-item style="margin-left: 40px">
          <span class="label">签发机构<span style="color: red">*</span></span>
          <el-select v-model="quotaForm.issuingAgency" placeholder="请选择" style="width: 180px" size="medium">
            <el-option v-for="item in issueInstitution" :key="item.value" :label="item.name" :value="item.value" />
          </el-select>
        </el-form-item>

        <br />

        <el-form-item>
          <span class="label">持仓凭证<span style="color: red">*</span></span>
          <el-upload
            class="upload-demo"
            ref="upload3"
            :action="upLoadParam.url"
            :file-list="buyFileList"
            :auto-upload="true"
            :limit="2"
            :on-success="quotaSuccess"
            style="width: 180px"
            :headers="{ token: upLoadParam.token }"
            :on-change="handleBuyChange"
            :on-preview="creditHandleFile"
          >
            <el-button type="primary">上传</el-button>
          </el-upload>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="clickClose">取 消</el-button>
        <el-button type="primary" @click="clickSave">保存</el-button>
        <el-button type="primary" @click="submit" style="margin-right: 25px">提交</el-button>
      </div>
    </el-dialog>

    <el-dialog title="项目列表" :visible.sync="dialogTableVisible" width="800px">
      <el-input
        v-model="searchProjectKeyword"
        placeholder="输入项目名称"
        clearable
        size="medium"
        style="width: 60%"
        @clear="search"
      />
      <button style="margin-left: 15px" class="light-green-btn" @click="search">
        查询
      </button>
      <el-table :data="projectList" style="width: 100%" stripe>
        <el-table-column min-width="10" />
        <el-table-column label="序号" align="left" prop="order" min-width="80" />
        <el-table-column :show-overflow-tooltip="true" align="left" prop="projectName" label="名称" min-width="180" />
        <el-table-column align="left" prop="projectScope" label="领域" min-width="120" />
        <el-table-column align="left" prop="certifiedStandard" label="核证标准" min-width="80" />
        <el-table-column label="操作" min-width="150" align="center">
          <template slot-scope="scope">
            <a class="list-blue-text" @click="pickProjectDone(scope.row)">选择</a>
          </template>
        </el-table-column>
      </el-table>
      <div style="margin-top: 30px; margin-bottom: 10px" class="pageBox">
        <el-pagination
          style="margin: auto"
          background
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="current"
          :page-size="pageSize"
          :page-count="pageCount"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total"
        />
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getCarbonMetaregistryPageList,
  addCarbonCredit,
  addCarbonQuota,
  changeCredit,
  changeQuota,
  loadCarbonExchangeList,
} from "@/api/carbonAssetApi";
import { openUrlInNewWindow } from "@/libs/OpenHelper";
import { getCertificationInstitutionDict, getIssueInstitution } from "@/config/dictHelper";
import { getFeiShuUpLoadProjectParams } from "@/api/tenant";

export default {
  name: "carbonUpload",
  props: {
    dialogFormVisible: {
      type: Boolean,
      default: false,
    },
    title: {
      type: String,
      default: "",
    },
    isCredit: {
      type: Boolean,
      default: true,
    },
    row: {
      type: Object,
      default: () => ({}),
    },
    isEdit: {
      type: Boolean,
      default: false,
    },
    selData: {
      type: Array,
      default: () => [],
    },
  },
  data() {
    return {
      timeout: null,
      states: null,
      quotaIsSuccess: true,
      searchProjectKeyword: "",
      form: {
        industryCodeName: "",
        carbonMethodologyName: "",
        issuingCertificates: "",
        issuingCertificatesFileName: "",
        certifiedAgency: "",
        certifiedAgencyName: "",
        assetsStatus: "0130000004",
        projectName: "",
        issuingDate: "",
        total: null,
        buyCertificate: null,
        buyCertificateFileName: "",
        carbonExchangeId: null,
        buyUnitPrice: null,
        carbonProjectId: null,
        buyTotalPrice: null,
        buyDate: null,
        projectScopeType: "",
        projectVerifierCodeName: null,
      },
      issueInstitution: [],
      quotaForm: {
        agencyName: "",
        agencyCode: "",
        total: null,
        buyUnitPrice: null,
        buyTotalPrice: null,
        buyDate: null,
        carbonExchangeId: null,
        availableAmount: null,
        lockedAmount: null,
        frozenAmount: null,
        valuation: null,
        assetsStatus: "0130000004",
        expiryDate: null,
        buyCertificateFileName: "",
        issuingDate: null,
        issuingAgency: null,
        buyCertificate: null,
      },
      dialogTableVisible: false,
      pageSize: 10,
      optionsStandard: [],
      current: 1,
      total: 0,
      creditCreditialUrl: "",
      creditTradeCreditialUrl: "",
      quotaCreditialUrl: "",
      pageCount: 0,
      havePickProject: false,
      fileList: [],
      exchangeList: [],
      issueFileList: [],
      tranFileList: [],
      buyFileList: [],
      issueUrl: "",
      tranUrl: "",
      show: false,
      projectList: [],
      upLoadParam: {
        url: null,
        token: null,
      },
      buyUrl: "",
    };
  },
  methods: {
    setFormValuation(val) {
      const num = Number(val);
      this.quotaForm.valuation = Number.isFinite(num) ? num * 50 : 0;
    },
    clickClose(done) {
      this.$emit("changeVisible", false);
      this.show = false;
      if (typeof done === "function") {
        done();
      }
    },
    setSelData(data) {
      const list = data || [];
      const arr = [];
      for (let i = 0; i < list.length; i++) {
        if (list[i] && list[i].value != "0620000000") {
          arr.push({
            label: list[i].value,
            value: list[i].name,
          });
        }
      }
      return arr;
    },
    loadAll() {
      return [
        { value: "北京我爱我家地产有限公司", label: "北京我爱我家地产有限公司" },
        { value: "上海我爱我家地产有限公司", label: "上海我爱我家地产有限公司" },
        { value: "河南我爱我家地产有限公司", label: "河南我爱我家地产有限公司" },
      ];
    },
    querySearchAsync(queryString, cb) {
      const restaurants = this.states || [];
      const results = queryString
        ? restaurants.filter(this.createStateFilter(queryString))
        : restaurants;
      clearTimeout(this.timeout);
      this.timeout = setTimeout(() => {
        cb(results);
      }, 1000 * Math.random());
    },
    createStateFilter(queryString) {
      return (state) => {
        return state.value.toLowerCase().indexOf(queryString.toLowerCase()) === 0;
      };
    },
    handleIssueChange(file, fileList) {
      if (fileList && fileList.length >= 2) {
        fileList.shift();
      }
      const reader = new FileReader();
      this.issueFileList = fileList;
      reader.readAsDataURL(file.raw);
      reader.onload = () => {
        this.issueUrl = reader.result;
      };
    },
    handleTranChange(file, fileList) {
      if (fileList && fileList.length >= 2) {
        fileList.shift();
      }
      const reader = new FileReader();
      this.tranFileList = fileList;
      reader.readAsDataURL(file.raw);
      reader.onload = () => {
        this.tranUrl = reader.result;
      };
    },
    handleBuyChange(file, fileList) {
      this.quotaIsSuccess = false;
      if (fileList && fileList.length >= 2) {
        fileList.shift();
      }
      const reader = new FileReader();
      this.buyFileList = fileList;
      reader.readAsDataURL(file.raw);
      reader.onload = () => {
        this.buyUrl = reader.result;
      };
    },
    clickSave() {
      if (this.isCredit) {
        sessionStorage.setItem("carbonUpload", JSON.stringify(this.form));
        this.$message.success("保存成功");
      } else {
        sessionStorage.setItem("carbonQuotaUpload", JSON.stringify(this.quotaForm));
        this.$message.success("保存成功");
      }
    },
    pickProjectDone(row) {
      this.form.projectName = row.projectName;
      this.form.carbonMethodologyName = row.methodologyName;
      this.form.projectScopeType = row.projectScope;
      this.form.carbonProjectId = row.id;
      this.form.issuingDate = row.issueTime;
      this.form.certifiedAgency = row.projectVerifierCode;
      this.form.certifiedAgencyName = row.projectVerifier;
      this.dialogTableVisible = false;
      this.havePickProject = true;
      this.form.projectVerifierCodeName = row.projectVerifierCodeName;
    },
    handleCurrentChange(val) {
      this.current = val;
      this.getProjectList(val);
    },
    submit() {
      if (this.isCredit) {
        this.submitCredit();
      } else {
        this.submitQuota();
      }
    },
    search() {
      const data = {
        current: this.current,
        size: this.pageSize,
        async: true,
        projectName: this.searchProjectKeyword,
        projectStatusCode: "0100000013",
      };
      getCarbonMetaregistryPageList(data).then((res) => {
        this.projectList = res.data.records;
        this.total = Number(res.data.total);
        this.current = Number(res.data.current);
        this.pageCount = Math.ceil(parseInt(res.data.total) / this.pageSize);
        this.projectList.map((v, i) => {
          v.order = i + 1;
          for (var k in v) {
            if (v[k] == "") {
              v[k] = "--";
            }
          }
        });
      });
    },
    creditHandleFile(file) {
      if (file.response) {
        openUrlInNewWindow(file.response.msg);
      }
      if (file.url) {
        openUrlInNewWindow(file.url);
      }
    },
    handleSelect(item) {
      this.quotaForm.agencyCode = item.label;
    },
    submitCredit() {
      for (let i in this.form) {
        if (this.form[i] == "--") {
          this.form[i] = "";
        }
      }
      if (!this.isEdit) {
        if (this.form.projectName && this.form.total && this.issueFileList[0]) {
          this.form.assetsStatus = "0130000004";
          if (this.tranFileList.length > 0) {
            this.$refs.upload2.submit();
          } else {
            addCarbonCredit(this.form)
              .then(() => {
                this.$message.success("提交成功");
                this.$emit("changeVisible", false);
                this.show = false;
                this.$emit("submit", true);
              })
              .catch((err) => {
                this.$message.error(err);
              });
          }
        } else {
          this.$message.warning("必填项不能为空");
        }
      } else {
        var data = JSON.parse(JSON.stringify(this.form));
        for (var j in data) {
          if (data[j] == "--") {
            data[j] = "";
          }
        }
        changeCredit(data)
          .then(() => {
            this.$message.success("修改成功");
            this.$emit("changeVisible", false);
            this.show = false;
            this.$emit("submit", true);
          })
          .catch((err) => {
            this.$message.error(err);
          });
      }
    },
    creditSuccess1(res, file) {
      this.form.issuingCertificates = res.msg;
      this.form.issuingCertificatesFileName = file.name;
      this.creditCreditialUrl = res.msg;
      this.$message.success("上传成功");
    },
    creditSuccess2(res, file) {
      this.form.buyCertificate = res.msg;
      this.form.buyCertificateFileName = file.name;
      addCarbonCredit(this.form)
        .then(() => {
          this.$message.success("提交成功");
          this.$emit("changeVisible", false);
          this.show = false;
          this.$emit("submit", true);
        })
        .catch((err) => {
          this.$message.error(err);
        });
    },
    quotaSuccess(res, file) {
      this.quotaForm.buyCertificate = res.msg;
      this.quotaForm.buyCertificateFileName = file.name;
      this.$message.success("上传成功");
    },
    submitQuota() {
      if (!this.quotaForm.buyCertificate) {
        this.$message.warning("请先上传文件，或等待文件上传成功再提交");
        return;
      }
      if (!this.isEdit) {
        if (
          this.quotaForm.agencyName &&
          this.quotaForm.total &&
          this.quotaForm.availableAmount &&
          this.quotaForm.lockedAmount &&
          this.quotaForm.frozenAmount &&
          this.quotaForm.issuingDate &&
          this.quotaForm.issuingAgency &&
          this.buyFileList[0]
        ) {
          if (
            Number(this.quotaForm.total) !=
            Number(this.quotaForm.availableAmount) +
              Number(this.quotaForm.lockedAmount) +
              Number(this.quotaForm.frozenAmount)
          ) {
            this.$message.warning("可用数量、锁定数量、冻结数量总和必须等于持仓总量");
            return;
          }
          this.quotaForm.assetsStatus = "0130000004";
          addCarbonQuota(this.quotaForm)
            .then(() => {
              if (this.buyFileList.length > 0) {
                this.$message.success("提交成功");
                this.$emit("changeVisible", false);
                this.show = false;
                this.$emit("submit", true);
              }
            })
            .catch((err) => {
              this.$message.error(err);
            });
        } else {
          this.$message.warning("必填项不能为空");
          return;
        }
      } else {
        var data2 = JSON.parse(JSON.stringify(this.quotaForm));
        for (var m in data2) {
          if (data2[m] == "--") {
            data2[m] = "";
          }
        }
        changeQuota(data2)
          .then(() => {
            if (this.buyFileList.length > 0) {
              this.$message.success("提交成功");
              this.$emit("changeVisible", false);
              this.show = false;
              this.$emit("submit", true);
            }
          })
          .catch((err) => {
            this.$message.error(err);
          });
      }
    },
    getProjectList(current) {
      this.current = current;
      const data = {
        current: this.current,
        size: this.pageSize,
        async: true,
        projectName: this.searchProjectKeyword,
        projectStatusCode: "0100000013",
      };
      getCarbonMetaregistryPageList(data).then((res) => {
        this.projectList = res.data.records;
        this.total = Number(res.data.total);
        this.current = Number(res.data.current);
        this.pageCount = Math.ceil(parseInt(res.data.total) / this.pageSize);
        this.projectList.map((v, i) => {
          v.order = i + 1;
          for (var k in v) {
            if (v[k] == "" || !v[k]) {
              v[k] = "--";
            }
          }
        });
      });
    },
    handleSizeChange(val) {
      this.pageSize = val;
      this.getProjectList(1);
    },
    pickProject() {
      this.dialogTableVisible = true;
      this.getProjectList(1);
    },
  },
  watch: {
    dialogFormVisible() {
      this.show = this.dialogFormVisible;
    },
    row() {
      this.form = this.row;
      if (this.form.issuingCertificates) {
        this.issueFileList = [
          {
            name: this.form.issuingCertificatesFileName,
            url: this.form.issuingCertificates,
          },
        ];
      }
      if (this.form.buyCertificate) {
        this.tranFileList = [
          {
            name: this.form.buyCertificateFileName,
            url: this.form.buyCertificate,
          },
        ];
      }
    },
  },
  mounted() {
    this.upLoadParam = getFeiShuUpLoadProjectParams();
    this.optionsStandard = getCertificationInstitutionDict(this.$store);
    const form = sessionStorage.getItem("carbonUpload");
    const quotaForm = sessionStorage.getItem("carbonQuotaUpload");
    loadCarbonExchangeList({ asc: true }).then((res) => {
      this.exchangeList = res.data.records;
    });
    this.states = this.loadAll();
    this.issueInstitution = getIssueInstitution(this.$store);
    if (form) {
      this.form = JSON.parse(form);
    }
    if (quotaForm) {
      this.quotaForm = JSON.parse(quotaForm);
    }
  },
};
</script>

<style lang="scss" scoped>
.required-text {
  color: red;
  position: absolute;
  left: -60px;
  top: 3px;
}

.label {
  font-weight: 700;
  width: 120px;
  display: inline-block;
}
</style>
