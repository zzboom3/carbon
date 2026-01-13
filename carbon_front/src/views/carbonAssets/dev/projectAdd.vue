<template>
  <div class="root">
    <div class="divBox">
      <div class="content-container">
        <div class="cardBody">
          <span class="header">创建项目</span>
          <el-divider></el-divider>

          <div class="outer20 flex-col">
            <span class="info5">1&nbsp;填写项目基本信息</span>
          </div>
          <div class="outer21 flex-col" @click="toChangePage">
            <span class="word9">2&nbsp;上传项目业主资料</span>
          </div>

          <div class="changeLine"></div>

          <button class="normal-white-btn pick-method-btn" @click="toMethodManager">
            选择方法学
          </button>
          <el-input
            v-model="form.name"
            size="medium"
            placeholder="左侧按钮选择方法学"
            class="pick-method-input"
            disabled
          />

          <div class="section-gap"></div>

          <img src="@/assets/icon/icon_plant.png" alt="" class="icon" />
          <span class="asset-little-title">项目信息</span>

          <div class="section-gap"></div>

          <div class="right">
            <el-row>
              <el-col :span="12">
                <div class="selectbox-root selectbox-root-pad-right">
                  <span class="selectbox-hint selectbox-hint-w80">
                    项目<span class="required-mark">*</span>
                  </span>
                  <div class="selectbox-deliver" />
                  <el-input
                    size="medium"
                    class="selectbox-input"
                    v-model="form.projectName"
                    placeholder="输入项目名称"
                    clearable
                  />
                </div>
              </el-col>
              <el-col :span="12">
                <div class="selectbox-root">
                  <span class="selectbox-hint selectbox-hint-w120">
                    业主名称<span class="required-mark">*</span>
                  </span>
                  <div class="selectbox-deliver" />
                  <el-autocomplete
                    v-model="form.ownerName"
                    size="medium"
                    class="autocomplete-wide"
                    :fetch-suggestions="querySearchAsync"
                    placeholder="请输入内容"
                    @select="handleSelect"
                  />
                </div>
              </el-col>
            </el-row>

            <div class="row-gap"></div>

            <el-row>
              <el-col :span="8">
                <div class="selectbox-root">
                  <span class="selectbox-hint selectbox-hint-w70">
                    国家<span class="required-mark">*</span>
                  </span>
                  <div class="selectbox-deliver" />
                  <el-cascader
                    placeholder="全部"
                    class="selectbox-input"
                    v-model="form.country"
                    :options="countryList"
                    @change="changeCountry"
                    clearable
                    size="medium"
                  />
                </div>
              </el-col>
              <el-col :span="8">
                <div class="selectbox-root selectbox-root-pad-left">
                  <span class="selectbox-hint selectbox-hint-w80">
                    省份<span class="required-mark">*</span>
                  </span>
                  <div class="selectbox-deliver" />
                  <el-cascader
                    placeholder="请选择"
                    class="selectbox-input"
                    v-model="form.province"
                    :options="provinceList"
                    @change="changeProvince"
                    clearable
                    size="medium"
                    @clear="clearCity"
                  />
                </div>
              </el-col>
              <el-col :span="8">
                <div class="selectbox-root selectbox-root-pad-left">
                  <span class="selectbox-hint selectbox-hint-w130">
                    市/县/郡<span class="required-mark">*</span>
                  </span>
                  <div class="selectbox-deliver" />
                  <el-cascader
                    placeholder="请选择"
                    class="selectbox-input city-cascader"
                    v-model="form.city"
                    :options="nowCityList"
                    @change="changeCity"
                    clearable
                    size="medium"
                  />
                </div>
              </el-col>
            </el-row>

            <div class="row-gap"></div>

            <el-row>
              <el-col :span="12">
                <div class="selectbox-root selectbox-root-pad-right">
                  <span class="selectbox-hint selectbox-hint-w100">
                    项目地点<span class="required-mark">*</span>
                  </span>
                  <div class="selectbox-deliver" />
                  <el-input
                    size="medium"
                    class="selectbox-input input-wide"
                    v-model="form.address"
                    placeholder="请输入项目地点"
                    clearable
                  />
                </div>
              </el-col>
              <el-col :span="12">
                <div class="selectbox-root">
                  <span class="selectbox-hint selectbox-hint-w200">
                    资产开发机构名称<span class="required-mark">*</span>
                  </span>
                  <div class="selectbox-deliver" />
                  <el-autocomplete
                    v-model="form.assetsDevelopAgency"
                    size="medium"
                    class="autocomplete-wide"
                    :fetch-suggestions="querySearchAsync"
                    placeholder="请输入内容"
                    @select="handleSelect"
                  />
                </div>
              </el-col>
            </el-row>

            <div class="row-gap"></div>

            <el-row>
              <el-col :span="12">
                <div class="selectbox-root">
                  <span class="selectbox-hint selectbox-hint-w200">
                    减排量<span class="required-mark">*</span>
                  </span>
                  <div class="selectbox-deliver" />
                  <el-input
                    size="medium"
                    class="selectbox-input"
                    v-model="form.estimatedReduction"
                    placeholder="输入减排量"
                    clearable
                  />
                </div>
              </el-col>
            </el-row>

            <div class="section-gap-sm"></div>

            <img src="@/assets/icon/icon_plant.png" alt="" class="icon icon-left" />
            <span class="asset-little-title title-left">项目介绍</span>

            <div class="section-gap-sm"></div>

            <el-col :span="24">
              <div class="basic-div intro-box">
                <textarea
                  v-model="form.projectIntroduction"
                  placeholder="输入项目介绍"
                  cols="30"
                  rows="10"
                  class="projectInfo"
                ></textarea>
              </div>
            </el-col>

            <div class="section-gap-sm"></div>

            <button class="light-green-btn action-btn" @click="toNext">下一步</button>
            <button class="light-green-btn action-btn action-btn-gap" @click="onSave">保存</button>
          </div>

          <el-dialog title="方法学列表" :visible.sync="dialogTableVisible" width="75%">
            <el-row>
              <el-col :span="8">
                <div class="selectbox-root">
                  <a class="selectbox-hint" style="width: 95px">核证标准</a>
                  <div class="selectbox-deliver" />
                  <el-cascader
                    style="width: 85%"
                    size="medium"
                    placeholder="全部"
                    class="selectbox-input"
                    v-model="selectedCertification"
                    :options="Certification"
                    clearable
                    @change="update"
                  />
                </div>
              </el-col>
              <el-col :span="8">
                <div class="selectbox-root selectbox-root-pad-left">
                  <a class="selectbox-hint" style="width: 60px">领域</a>
                  <div class="selectbox-deliver" />
                  <el-cascader
                    style="width: 80%"
                    placeholder="全部"
                    size="medium"
                    class="selectbox-input"
                    v-model="selectedAreaDict"
                    :options="projectAreaDict"
                    clearable
                    @change="update"
                  />
                </div>
              </el-col>
              <el-col :span="8">
                <div class="selectbox-root selectbox-root-pad-left">
                  <a class="selectbox-hint" style="width: 90px">行业</a>
                  <div class="selectbox-deliver" />
                  <el-cascader
                    style="width: 80%"
                    size="medium"
                    placeholder="全部"
                    class="selectbox-input"
                    v-model="selectedIndustry"
                    :options="IndustryList"
                    clearable
                    @change="update"
                  />
                </div>
              </el-col>
            </el-row>

            <el-row style="margin-top: 10px">
              <el-col :span="16">
                <div class="selectbox-root selectbox-root-pad-right">
                  <a class="selectbox-hint" style="width: 120px">方法学搜索</a>
                  <div class="selectbox-deliver" />
                  <el-input
                    class="selectbox-input"
                    @keyup.enter.native="searchEnter"
                    v-model="searchKeyWord"
                    style="width: 80%"
                    size="medium"
                    placeholder="输入名称、编号、关键词等"
                    clearable
                  />
                </div>
              </el-col>
              <el-col :span="8">
                <button class="light-green-btn dialog-search-btn" @click="onClickSearch">
                  查询
                </button>
              </el-col>
            </el-row>

            <el-table :data="methodList" style="width: 100%" stripe>
              <el-table-column min-width="10"></el-table-column>
              <el-table-column label="编号" align="left" prop="methodCode" min-width="80" />
              <el-table-column :show-overflow-tooltip="true" align="left" prop="name" label="名称" min-width="230" />
              <el-table-column align="left" prop="industryCodeName" label="行业" min-width="80" />
              <el-table-column align="left" prop="certificationCriteriaName" label="核证标准" min-width="80" />
              <el-table-column label="操作" min-width="150" align="center">
                <template slot-scope="scope">
                  <a class="list-blue-text" @click="pickMethod(scope.row)">选择</a>
                </template>
              </el-table-column>
            </el-table>

            <div style="margin-top: 30px; margin-bottom: 10px" class="pageBox">
              <div style="flex-grow: 1" />
              <a style="margin: auto" class="pageBox-total-num">共{{ total }}条</a>
              <el-pagination
                style="margin: auto"
                background
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
                :current-page="methodCurrent"
                :page-size="methodPageSize"
                :page-count="pageCount"
                layout="total, sizes, prev, pager, next, jumper"
                :total="total"
              />
            </div>
          </el-dialog>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { loadMethodList } from "@/api/carbonAssetApi";
import { getProvinceDict } from "@/config/dictHelper";
import { getCountryDict } from "@/config/dictHelper";
import { getCertificationCriteriaDict } from "@/config/dictHelper";
import { getBusinessDict } from "@/config/dictHelper";
import { getProjectAreaDict } from "@/config/dictHelper";
import { getMethodologyDict } from "@/config/dictHelper";
import { getCityDict } from "@/api/carbonAssetApi";

export default {
  name: "projectADD",
  data() {
    return {
      timeout: null,
      searchKeyWord: "",
      states: [],
      projectAreaDict: [],
      selectedCertification: [],
      selectedIndustry: [],
      selectedAreaDict: [],
      Certification: [],
      IndustryList: [],
      provinceList: [],
      cityList: {},
      nowCityList: [],
      dialogTableVisible: false,
      methodList: [],
      countryList: [],
      pageCount: 0,
      total: 0,
      methodPageSize: 10,
      methodCurrent: 1,
      fromPath: "",
      showCancel: true,
      form: {
        id: "",
        name: "",
        carbonMethodology: "",
        projectName: "",
        ownerName: "",
        country: "",
        province: "",
        city: "",
        address: "",
        assetsDevelopAgency: "",
        projectIntroduction: "",
        projectStatus: "",
        projectType: "",
        approvalDate: "",
        estimatedReduction: "",
      },
    };
  },
  beforeRouteEnter(to, from, next) {
    next((vm) => {
      vm.fromPath = from.path;
    });
  },
  methods: {
    loadAll() {
      return [
        { value: "北京我爱我家地产有限公司", label: "北京我爱我家地产有限公司" },
        { value: "上海我爱我家地产有限公司", label: "上海我爱我家地产有限公司" },
        { value: "河南我爱我家地产有限公司", label: "河南我爱我家地产有限公司" },
      ];
    },
    toMethodManager() {
      this.dialogTableVisible = true;
      this.getMethodList(1);
    },
    handleCurrentChange(val) {
      this.methodCurrent = val;
      this.getMethodList(val);
    },
    handleSizeChange(val) {
      this.methodPageSize = val;
      this.getMethodList(1);
    },
    update() {
      const data = {
        asc: true,
        size: this.methodPageSize,
        searchKey: this.searchKeyWord,
        fieldCode: (this.selectedAreaDict && this.selectedAreaDict[0]) || undefined,
        industryCode: (this.selectedIndustry && this.selectedIndustry[0]) || undefined,
        certificationCriteria:
          (this.selectedCertification && this.selectedCertification[0]) || undefined,
        current: this.methodCurrent,
        status: "0450000002",
      };
      loadMethodList(data)
        .then((res) => {
          this.methodList = (res && res.data && res.data.records) || [];
          this.total = Number((res && res.data && res.data.total) || 0);
          this.methodCurrent = Number((res && res.data && res.data.current) || this.methodCurrent);
          this.pageCount = Math.ceil(parseInt(this.total) / this.methodPageSize);
          this.methodList.map((v) => {
            for (var i in v) {
              v[i] = v[i] ? v[i] : "--";
              if (v[i] == " ") {
                v[i] = "--";
              }
            }
          });
        })
        .catch(() => {});
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
    handleSelect() {},
    pickMethod(row) {
      this.form.name = row.name;
      this.form.carbonMethodology = row.dictCode;
      this.dialogTableVisible = false;
    },
    onSave() {
      sessionStorage.setItem("projectAdd", JSON.stringify(this.form));
      if (sessionStorage.getItem("projectAdd")) {
        this.$message.success("保存成功");
      } else {
        this.$message.error("保存失败");
      }
    },
    onClickSearch() {
      this.methodCurrent = 1;
      this.getMethodList(1);
    },
    getMethodList(val) {
      const data = {
        asc: true,
        size: this.methodPageSize,
        current: val,
        searchKey: this.searchKeyWord,
        fieldCode: (this.selectedAreaDict && this.selectedAreaDict[0]) || undefined,
        industryCode: (this.selectedIndustry && this.selectedIndustry[0]) || undefined,
        certificationCriteria:
          (this.selectedCertification && this.selectedCertification[0]) || undefined,
        status: "0450000002",
      };
      loadMethodList(data)
        .then((res) => {
          this.methodList = (res && res.data && res.data.records) || [];
          this.total = Number((res && res.data && res.data.total) || 0);
          this.methodCurrent = Number((res && res.data && res.data.current) || val);
          this.pageCount = Math.ceil(parseInt(this.total) / this.methodPageSize);
          this.methodList.map((v) => {
            for (var i in v) {
              v[i] = v[i] ? v[i] : "--";
              if (v[i] == " ") {
                v[i] = "--";
              }
            }
          });
        })
        .catch(() => {});
    },
    formatProvince(data) {
      data.map((v) => {
        const item = { value: "", label: "" };
        if (v.label != "全部") {
          item.value = v.value;
          item.label = v.name;
        }
        this.provinceList.push(item);
      });
    },
    toNext() {
      if (
        !(
          this.form.country &&
          this.form.province &&
          this.form.projectName &&
          this.form.ownerName &&
          this.form.address &&
          this.form.assetsDevelopAgency
        )
      ) {
        this.$message.error("必填项不能为空！");
        return;
      }
      sessionStorage.setItem("projectAdd", JSON.stringify(this.form));
      this.$router.push({ path: "/carbon/projectCreate/ownerAdd" });
    },
    toChangePage() {
      sessionStorage.setItem("projectAdd", JSON.stringify(this.form));
      this.$router.push({ path: "/carbon/projectCreate/ownerAdd" });
    },
    clearCity() {
      this.nowCityList = [];
    },
    changeCountry() {
      if (Array.isArray(this.form.country)) {
        this.form.country = this.form.country[0];
      }
    },
    changeCity() {
      if (Array.isArray(this.form.city)) {
        this.form.city = this.form.city[0];
      }
    },
    changeProvince() {
      this.nowCityList.length = 0;
      if (this.form.province) {
        if (Array.isArray(this.form.province)) {
          this.form.province = this.form.province[0];
        }
        const provinceCode = String(this.form.province).slice(8, 10);
        const list = this.cityList ? this.cityList[provinceCode] : null;
        if (list) {
          for (var j in list) {
            this.nowCityList.push({ value: "012015" + j, label: list[j] });
          }
        }
      }
    },
    searchEnter() {
      this.onClickSearch();
    },
    formatCountry(data) {
      data.map((v) => {
        const item = { value: "", label: "" };
        if (v.label == "全部") {
          item.value = "";
          item.label = v.name;
        } else {
          item.value = v.value;
          item.label = v.name;
        }
        this.countryList.push(item);
      });
    },
    loadProject() {
      const form = sessionStorage.getItem("projectAdd");
      if (form) {
        const myForm = JSON.parse(form);
        this.form = myForm;
        if (this.form.province) {
          this.changeProvince();
        }
      }
      const data = this.$route.query || {};
      const method = getMethodologyDict(this.$store) || [];
      if (data.code) {
        this.form.carbonMethodology = data.code;
      }
      method.map((v) => {
        if (v.value == this.form.carbonMethodology) {
          this.form.name = v.name;
        }
      });
    },
    loadCityDict() {
      getCityDict().then((res) => {
        this.cityList = (res && res.data) || {};
        this.loadProject();
      });
    },
    formatStatus(data) {
      data.map((v) => {
        const item = { label: "" };
        if (v.name == "全部") {
          item.label = v.name;
        } else {
          item.value = v.value;
          item.label = v.name;
        }
        this.Certification.push(item);
      });
    },
    formatIndustry(data) {
      data.map((v) => {
        const item = { label: "" };
        if (v.name == "全部") {
          item.label = v.name;
        } else {
          item.value = v.value;
          item.label = v.name;
        }
        this.IndustryList.push(item);
      });
    },
    formatArea(data) {
      data.map((v) => {
        const item = { label: "" };
        if (v.name == "全部") {
          item.label = v.name;
        } else {
          item.value = v.value;
          item.label = v.name;
        }
        this.projectAreaDict.push(item);
      });
    },
  },
  mounted() {
    const data = this.$route.query || {};
    if (data) {
      this.form.name = data.methodName || this.form.name;
      this.form.carbonMethodology = data.code || this.form.carbonMethodology;
    }
    this.formatProvince(getProvinceDict(this.$store));
    this.formatCountry(getCountryDict(this.$store));
    this.formatStatus(getCertificationCriteriaDict(this.$store));
    this.formatIndustry(getBusinessDict(this.$store));
    this.formatArea(getProjectAreaDict(this.$store));
    this.loadCityDict();
    this.form.country = (this.countryList[0] && this.countryList[0].value) || this.form.country;
    this.states = this.loadAll();
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
}

.content-container {
  display: flex;
  flex-direction: column;
  width: 100%;
}

.cardBody {
  margin: 30px;
}

.header {
  overflow-wrap: break-word;
  color: rgba(36, 167, 118, 1);
}

.el-divider {
  margin-top: 20px;
}

.outer20 {
  background-color: rgba(38, 181, 129, 1);
  height: 38px;
  width: 194px;
  cursor: pointer;
  float: left;
  margin-top: 20px;
  .info5 {
    width: 123px;
    height: 14px;
    overflow-wrap: break-word;
    color: rgba(255, 255, 255, 1);
    text-align: center;
    white-space: nowrap;
    margin: 20px 0 0 35px;
    position: relative;
    top: 12px;
  }
}

.outer21 {
  background-color: rgba(38, 181, 129, 0.5);
  height: 38px;
  width: 194px;
  float: left;
  cursor: pointer;
  margin-left: 20px;
  margin-top: 20px;
  .word9 {
    width: 126px;
    height: 14px;
    overflow-wrap: break-word;
    color: rgba(255, 255, 255, 1);
    text-align: center;
    white-space: nowrap;
    margin: 20px 0 0 33px;
    position: relative;
    top: 12px;
  }
}

.changeLine {
  clear: both;
  height: 40px;
}

.pick-method-btn {
  margin-top: 0px;
  margin-bottom: 20px;
  width: 119px;
  height: 36px;
}

.pick-method-input {
  width: 350px;
  margin-left: 16px;
}

.section-gap {
  clear: both;
  height: 20px;
}

.section-gap-sm {
  clear: both;
  height: 20px;
}

.row-gap {
  clear: both;
  height: 40px;
}

.icon {
  height: 16px;
  width: 22px;
}

.icon-left {
  position: relative;
  right: 30px;
}

.asset-little-title {
  width: 64px;
  height: 16px;
  font-weight: 500;
  color: #424c5c;
  margin-left: 10px;
}

.title-left {
  position: relative;
  right: 30px;
}

.right {
  width: 100%;
  margin-left: 30px;
}

.required-mark {
  color: red;
}

.selectbox-root-pad-left {
  margin-left: 17px;
  padding: 0;
}

.selectbox-root-pad-right {
  margin-right: 15px;
  padding: 0;
}

.selectbox-hint-w70 {
  width: 70px;
}

.selectbox-hint-w80 {
  width: 80px;
}

.selectbox-hint-w100 {
  width: 100px;
}

.selectbox-hint-w120 {
  width: 120px;
}

.selectbox-hint-w130 {
  width: 130px;
}

.selectbox-hint-w200 {
  width: 200px;
}

.autocomplete-wide {
  width: 85%;
}

.input-wide {
  width: 85%;
}

.basic-div {
  height: 189px;
  padding: 20px;
  border-radius: 6px;
  border: 1px solid #e3e7eb;
}

.intro-box {
  height: 189px;
}

.projectInfo {
  height: 100%;
  width: 100%;
  border-radius: 6px;
  border: 0px solid;
  font-weight: 400;
  text-align: justify;
}

.action-btn {
  width: 96px;
  float: right;
}

.action-btn-gap {
  margin-right: 20px;
}

.dialog-search-btn {
  position: relative;
  top: 3px;
}
</style>
