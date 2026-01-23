<template>
  <div class="root">
    <div class="divBox">
      <div class="content-container">
        <div class="container">
          <el-row style="flex-grow: 1">
            <el-col :span="6">
              <div class="selectbox-root">
                <span class="selectbox-hint" style="min-width: 80px">核证标准</span>
                <div class="selectbox-deliver" />
                <el-cascader
                  style="width: 120px"
                  placeholder="全部"
                  class="selectbox-input"
                  v-model="selectedCertification"
                  :options="Certification"
                  clearable
                  @change="onClickSearch"
                />
              </div>
            </el-col>
            <el-col :span="6">
              <div style="margin-left: 16px" class="selectbox-root">
                <span class="selectbox-hint" style="min-width: 60px">领域</span>
                <div class="selectbox-deliver" />
                <el-cascader
                  placeholder="全部"
                  class="selectbox-input"
                  v-model="selectedField"
                  :options="fieldCodeList"
                  clearable
                  @change="onClickSearch"
                />
              </div>
            </el-col>
            <el-col :span="6">
              <div style="margin-left: 16px" class="selectbox-root">
                <span class="selectbox-hint" style="min-width: 60px">行业</span>
                <div class="selectbox-deliver" />
                <el-cascader
                  placeholder="全部"
                  class="selectbox-input"
                  v-model="selectedIndustry"
                  :options="IndustryList"
                  clearable
                  @change="onClickSearch"
                />
              </div>
            </el-col>
            <el-col :span="6">
              <div style="margin-left: 16px" class="selectbox-root">
                <span class="selectbox-hint" style="min-width: 60px">状态</span>
                <div class="selectbox-deliver" />
                <el-cascader
                  placeholder="全部"
                  class="selectbox-input"
                  v-model="selectedStatus"
                  :options="optionsOnlines"
                  clearable
                  @change="onClickSearch"
                />
              </div>
            </el-col>
          </el-row>
        </div>

        <div style="display: flex">
          <div style="margin: 10px 10px 10px 0px; min-width: 600px" class="selectbox-root">
            <span class="selectbox-hint" style="min-width: 100px">方法学搜索</span>
            <div class="selectbox-deliver" />
            <el-input
              v-model="searchKeyWord"
              placeholder=""
              clearable
              size="medium"
              @keyup.enter.native="onClickSearch"
              @clear="onClickSearch"
            />
          </div>
          <button class="light-green-btn" @click="onClickSearch">查询</button>
        </div>

        <div>
          <button
            style="margin-top: 0px; margin-bottom: 20px; width: 103px"
            class="normal-white-btn"
            @click="onclickAdd"
          >
            +添加方法学
          </button>
          <el-table
            :header-cell-style="{
              background: '#F2F5F7',
              border: '0px solid #DDDDDD',
              color: '#242B35',
              height: '64px',
            }"
            :show-header="true"
            :data="list"
            :row-style="{ height: '64px' }"
            :cell-style="cellStyle"
            stripe
            style="width: 100%; cursor: default"
          >
            <el-table-column min-width="20" />
            <el-table-column label="编号" align="left" prop="methodCode" min-width="60" />
            <el-table-column :show-overflow-tooltip="true" prop="name" label="名称" align="left" min-width="230" />
            <el-table-column :show-overflow-tooltip="true" prop="fieldCodeName" label="领域" align="left" min-width="140" />
            <el-table-column align="left" prop="industryCodeName" label="行业" min-width="80" />
            <el-table-column align="left" prop="certificationCriteriaName" label="核证标准" min-width="80" />
            <el-table-column align="left" prop="statusName" label="状态" min-width="60" />
            <el-table-column label="操作" min-width="150" align="center">
              <template slot-scope="scope">
                <a class="list-blue-text" @click="goMethod(scope.row.sourceFileUrl)">查看</a>
                <a class="list-green-text" style="margin-left: 10px" @click="onEdit(scope.row)">编辑</a>
                <a
                  style="margin-left: 10px"
                  :class="publishStyleChange(scope.row.statusCode)"
                  @click="onClickPublish(scope.row.id)"
                >
                  发布
                </a>
                <a
                  style="margin-left: 10px"
                  :class="offlineStyleChange(scope.row.statusCode)"
                  @click="onClickOffline(scope.row.id)"
                >
                  下线
                </a>
                <a style="margin-left: 10px" class="afterSubmitOffline" @click="onClickDelete(scope.row.id)">删除</a>
              </template>
            </el-table-column>
          </el-table>
        </div>

        <div style="margin-top: 30px; margin-bottom: 10px" class="pageBox">
          <div style="flex-grow: 1" />
          <span style="margin: auto" class="pageBox-total-num">共{{ total }}条</span>
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

        <el-dialog :title="dialogText" :visible.sync="addMethodFormVisible" width="40%">
          <el-row>
            <el-col :span="5"><span class="table-text">方法学编号:<span style="color: red">*</span></span></el-col>
            <el-col :span="19">
              <el-input v-model="methodForm.methodCode" placeholder="请输入方法学编号" autocomplete="off" size="medium" />
            </el-col>
            <div style="clear: both; height: 20px" />
            <el-col :span="5"><span class="table-text">方法学字典码:<span style="color: red">*</span></span></el-col>
            <el-col :span="19">
              <el-input v-model="methodForm.dictCode" placeholder="请输入方法学字典码" autocomplete="off" size="medium" />
            </el-col>
            <div style="clear: both; height: 20px" />
            <el-col :span="5"><span class="table-text">方法学名称:<span style="color: red">*</span></span></el-col>
            <el-col :span="19">
              <el-input v-model="methodForm.name" placeholder="请输入方法学名称" autocomplete="off" size="medium" />
            </el-col>
            <div style="clear: both; height: 20px" />
            <el-col :span="5"><span class="table-text">领域:<span style="color: red">*</span></span></el-col>
            <el-col :span="19">
              <el-select v-model="methodForm.fieldCode" placeholder="请选择领域" size="medium" style="width: 100%">
                <el-option v-for="(item, index) in fieldCodeList" :key="index" :label="item.label" :value="item.value" />
              </el-select>
            </el-col>
            <div style="clear: both; height: 20px" />
            <el-col :span="5"><span class="table-text">行业:</span></el-col>
            <el-col :span="19">
              <el-select v-model="methodForm.industryCode" placeholder="请选择行业" size="medium" style="width: 100%">
                <el-option v-for="(item, index) in IndustryList" :key="index" :label="item.label" :value="item.value" />
              </el-select>
            </el-col>
            <div style="clear: both; height: 20px" />
            <el-col :span="5"><span class="table-text">类型:</span></el-col>
            <el-col :span="19">
              <el-select v-model="methodForm.fieldChildType" placeholder="请选择类型" size="medium" style="width: 100%">
                <el-option v-for="(item, index) in fieldChildCodeList" :key="index" :label="item.label" :value="item.value" />
              </el-select>
            </el-col>
            <div style="clear: both; height: 20px" />
            <el-col :span="5"><span class="table-text">核证标准:<span style="color: red">*</span></span></el-col>
            <el-col :span="19">
              <el-select v-model="methodForm.certificationCriteria" placeholder="请选择核证标准" size="medium" style="width: 100%">
                <el-option v-for="(item, index) in Certification" :key="index" :label="item.label" :value="item.value" />
              </el-select>
            </el-col>
            <div style="clear: both; height: 20px" />
            <el-col :span="5"><span class="table-text">pdf文档url:<span style="color: red">*</span></span></el-col>
            <el-col :span="19">
              <el-input v-model="methodForm.sourceFileUrl" placeholder="请输入pdf文档url" autocomplete="off" size="medium" />
            </el-col>
            <div style="clear: both; height: 20px" />
            <el-col :span="5"><span class="table-text">word文档url:</span></el-col>
            <el-col :span="19">
              <el-input v-model="methodForm.wordUrl" placeholder="请输入word文档url" autocomplete="off" size="medium" />
            </el-col>
          </el-row>
          <div slot="footer" class="dialog-footer" align="right">
            <el-button @click="onCancel">取 消</el-button>
            <el-button @click="saveMothod" type="primary">保存</el-button>
          </div>
        </el-dialog>
      </div>
    </div>
  </div>
</template>
<script>
import {
  loadMethodList,
  addCarbonMethodology,
  updateCarbonMethodology,
  synContentCarbonMethodology,
  deleteMethod,
  editMethod,
} from "@/api/carbonAssetApi";
import { openUrlInNewWindow } from "@/libs/OpenHelper";
import {
  getCertificationCriteriaDict,
  getBusinessDict,
  getMethodStatusDict,
  getProjectAreaDict,
  getProjectTypeDict,
} from "@/config/dictHelper";
import { cursor } from "@/libs/element-table";

export default {
  name: "methodManager",
  data() {
    return {
      searchKeyWord: "",
      selectedCertification: [],
      selectedIndustry: [],
      selectedStatus: [],
      selectedField: [],

      fieldCodeList: [],
      IndustryList: [],
      Certification: [],
      optionsOnlines: [],
      fieldChildCodeList: [],

      list: [],
      total: 0,
      current: 1,
      pageCount: 1,
      pageSize: 10,

      addMethodFormVisible: false,
      dialogText: "添加方法学",
      methodForm: {
        id: "",
        dictCode: "",
        fieldCode: "",
        certificationCriteria: "",
        industryCode: "",
        methodCode: "",
        name: "",
        pdfUrl: "",
        wordUrl: "",
        fieldChildType: "",
        sourceFileUrl: "",
      },
    };
  },
  mounted() {
    this.formatIndustry(getBusinessDict(this.$store));
    this.formatMethodStatus(getMethodStatusDict(this.$store));
    this.formatFieldCode(getProjectAreaDict(this.$store));
    this.Certification = this.formatStatus(getCertificationCriteriaDict(this.$store));
    this.fieldChildCodeList = this.formatStatus(getProjectTypeDict(this.$store));
    this.getList(1);
  },
  methods: {
    cellStyle(data) {
      return cursor(data);
    },
    publishStyleChange(status) {
      if (status === "0450000002") {
        return "afterSubmitPublish";
      }
      return "list-blue-text";
    },
    offlineStyleChange(status) {
      if (status === "0450000002") {
        return "list-red-text";
      }
      return "afterSubmitOffline";
    },
    onclickAdd() {
      this.dialogText = "添加方法学";
      this.methodForm = {
        id: "",
        dictCode: "",
        fieldCode: "",
        certificationCriteria: "",
        industryCode: "",
        methodCode: "",
        name: "",
        pdfUrl: "",
        wordUrl: "",
        fieldChildType: "",
        sourceFileUrl: "",
      };
      this.addMethodFormVisible = true;
    },
    onCancel() {
      this.addMethodFormVisible = false;
    },
    syncContentCarbonMethodology(data) {
      const datas = {
        methodCode: data.methodCode,
        wordUrl: data.wordUrl,
      };
      synContentCarbonMethodology(datas).then(() => {
        this.getList(1);
        this.$message.success("操作成功");
      });
    },
    onClickPublish(id) {
      const data = { id, statusCode: "0450000002" };
      editMethod(data)
        .then(() => {
          this.$message.success("发布成功");
          this.getList(this.current);
        })
        .catch(() => {
          this.$message.error("发布失败");
        });
    },
    onClickOffline(id) {
      const data = { id, statusCode: "0450000003" };
      editMethod(data)
        .then(() => {
          this.$message.success("下架成功");
          this.getList(this.current);
        })
        .catch(() => {
          this.$message.error("下架失败");
        });
    },
    onClickDelete(id) {
      this.$confirm("删除内容不可复原，请谨慎操作").then(() => {
        deleteMethod(id)
          .then(() => {
            this.$message.success("删除成功");
            this.getList(this.current);
          })
          .catch(() => {
            this.$message.error("删除失败");
          });
      });
    },
    buildQueryData() {
      const data = {
        asc: true,
        size: this.pageSize,
        current: this.current,
        statusCode: (this.selectedStatus && this.selectedStatus[0]) || undefined,
        industryCode: (this.selectedIndustry && this.selectedIndustry[0]) || undefined,
        fieldCode: (this.selectedField && this.selectedField[0]) || undefined,
        certificationCriteria: (this.selectedCertification && this.selectedCertification[0]) || undefined,
      };
      if (this.searchKeyWord) {
        data.searchKey = this.searchKeyWord;
      }
      return data;
    },
    onClickSearch() {
      this.current = 1;
      this.getList(1);
    },
    onEdit(row) {
      this.dialogText = "修改方法学";
      this.addMethodFormVisible = true;
      this.methodForm = { ...(row || {}) };
    },
    saveMothod() {
      if (!this.methodForm.methodCode) return this.$message("请输入方法学编码!");
      if (!this.methodForm.name) return this.$message("请输入方法学名称!");
      if (!this.methodForm.fieldCode) return this.$message("请选择方法学领域!");
      if (!this.methodForm.certificationCriteria) return this.$message("请选择方法学核证标准!");
      if (!this.methodForm.sourceFileUrl) return this.$message("请输入方法学飞书pdf路径!");

      const data = { ...this.methodForm };
      data.pdfUrl = data.sourceFileUrl;

      if (data.id) {
        Object.keys(data).forEach((k) => {
          if (data[k] === "--") data[k] = "";
        });
        data.createdTime = null;
        updateCarbonMethodology(data)
          .then(() => {
            this.$message.success("操作成功！");
            this.addMethodFormVisible = false;
            this.getList(this.current);
            if (this.methodForm.wordUrl) {
              this.syncContentCarbonMethodology(this.methodForm);
            }
          })
          .catch((err) => {
            this.$message.error((err && err.msg) || "操作失败");
          });
      } else {
        delete data.id;
        addCarbonMethodology(data)
          .then(() => {
            this.$message.success("添加成功！");
            this.addMethodFormVisible = false;
            this.getList(1);
          })
          .catch((err) => {
            this.$message.error((err && err.msg) || "添加失败");
          });
      }
    },
    goMethod(url) {
      openUrlInNewWindow(url);
    },
    handleSizeChange(val) {
      this.pageSize = val;
      this.getList(1);
    },
    handleCurrentChange(val) {
      this.current = val;
      this.getList(val);
    },
    statusName(status) {
      if (status === "0450000002") return "已发布";
      if (status === "0450000001") return "未发布";
      if (status === "0450000003") return "已下线";
      return "--";
    },
    getList(page) {
      this.current = page;
      const data = this.buildQueryData();
      loadMethodList(data)
        .then((res) => {
          const rows = (res && res.records) || [];
          this.list = rows.map((v) => {
            const row = { ...(v || {}) };
            row.statusName = this.statusName(row.statusCode);
            Object.keys(row).forEach((k) => {
              if (!row[k] || row[k] === " ") row[k] = "--";
            });
            return row;
          });
          this.total = parseInt((res && res.total) || 0, 10);
          this.current = Number((res && res.current) || page);
          this.pageCount = Math.ceil(this.total / this.pageSize) || 1;
        })
        .catch(() => {});
    },
    formatStatus(data) {
      const list = [];
      (data || []).forEach((v) => {
        const item = { label: "" };
        if (v.name === "全部") {
          item.label = v.name;
        } else {
          item.value = v.value;
          item.label = v.name;
        }
        list.push(item);
      });
      return list;
    },
    formatMethodStatus(data) {
      (data || []).forEach((v) => {
        const item = { label: "" };
        if (v.name === "全部") {
          item.label = v.name;
        } else {
          item.value = v.value;
          item.label = v.name;
        }
        this.optionsOnlines.push(item);
      });
    },
    formatFieldCode(data) {
      (data || []).forEach((v) => {
        const item = { label: "" };
        if (v.name === "全部") {
          item.label = v.name;
        } else {
          item.value = v.value;
          item.label = v.name;
        }
        this.fieldCodeList.push(item);
      });
    },
    formatIndustry(data) {
      (data || []).forEach((v) => {
        const item = { label: "" };
        if (v.name === "全部") {
          item.label = v.name;
        } else {
          item.value = v.value;
          item.label = v.name;
        }
        this.IndustryList.push(item);
      });
    },
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
  padding: 0 20px 20px 20px;
}

.container {
  margin: 20px 0px 20px 0px;
  display: flex;
  flex-direction: row;
}

.content-container {
  display: flex;
  flex-direction: column;
  width: 100%;
}

::v-deep .el-cascader .el-input .el-input__inner,
::v-deep .el-cascader .el-input.is-focus .el-input__inner {
  border-color: transparent;
}

::v-deep .el-dialog__header {
  background: #fcfcfc;
}

::v-deep .el-dialog__title {
  color: #000 !important;
}

.require {
  color: red;
  font-size: 16px;
  position: relative;
  right: 20px;
}

.table-text {
  position: relative;
  top: 10px;
  margin-left: 10px;
}
</style>
