<template>
  <div class="root">
    <div class="divBox">
      <div class="content-container">
        <div class="container">
          <div style="width: 230px" class="selectbox-root">
            <span class="selectbox-hint">文档类型</span>
            <div class="selectbox-deliver" />
            <el-cascader
              style="width: 100px"
              placeholder="全部"
              class="selectbox-input"
              v-model="selectedType"
              :options="DocumentTypeDict"
              clearable
              @clear="update"
              @change="update"
            />
          </div>
          <div style="width: 380px; margin-left: 16px" class="selectbox-root">
            <span class="selectbox-hint" style="width: 100px">完成日期</span>
            <div class="selectbox-deliver" />
            <el-date-picker
              v-model="selectedDate"
              type="date"
              placeholder="选择开始时间"
              align="right"
              size="medium"
              @change="update"
            />
            <el-date-picker
              v-model="selectedEndDate"
              type="date"
              placeholder="选择结束时间"
              align="right"
              size="medium"
              @change="update"
            />
          </div>
          <div style="margin-left: 16px; margin-right: 15px; width: 350px" class="selectbox-root">
            <span class="selectbox-hint" style="width: 120px">按名称搜索</span>
            <div class="selectbox-deliver" />
            <el-input
              v-model="searchKeyWord"
              placeholder="输入名称"
              clearable
              size="medium"
              @clear="onClickSearch"
              @keyup.enter.native="onClickSearch"
            />
          </div>
          <button class="light-green-btn" @click="onClickSearch">查询</button>
        </div>

        <button class="normal-white-btn upload-btn" @click="onUpload">
          +上传文档
        </button>
      </div>

      <div>
        <el-table
          :header-cell-style="{
            background: '#F2F5F7',
            border: '0px solid #DDDDDD',
            color: '#242B35',
            height: '64px',
          }"
          :show-header="true"
          :data="list"
          stripe
          :row-style="{ height: '64px' }"
          style="width: 100%"
          :cell-style="cellStyle"
          @cell-click="handle"
        >
          <el-table-column min-width="10"></el-table-column>
          <el-table-column label="序号" align="left" min-width="40">
            <template slot-scope="scope"><span>{{ getCurListNo(scope.$index) }}</span></template>
          </el-table-column>
          <el-table-column align="left" prop="projectName" label="所属项目" min-width="100" />
          <el-table-column align="left" prop="title" label="文档名称" min-width="120" />
          <el-table-column align="left" prop="typeName" label="文档类型" min-width="100" />
          <el-table-column align="left" prop="completionDate" label="完成日期" min-width="100" />
          <el-table-column align="left" prop="updatedTime" label="更新日期" min-width="100" />
          <el-table-column label="操作" min-width="150" align="left">
            <template slot-scope="scope">
              <a class="list-blue-text" @click="onEdit(scope.row)">查看</a>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <div style="margin-top: 30px; margin-bottom: 10px; margin-right: 20px" class="pageBox">
        <div style="flex-grow: 1" />
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

      <el-dialog title="上传文档" :visible.sync="dialogFormVisible" width="600px">
        <el-form>
          <el-form-item>
            <span class="label">所属项目<span style="color: red">*</span></span>
            <el-input
              size="medium"
              placeholder="右侧按钮选择项目"
              v-model="projectName"
              disabled
              style="width: 330px"
            />
            <el-button type="primary" style="width: 100px; margin-left: 15px" @click="pickProject">
              选择项目
            </el-button>
          </el-form-item>
          <el-form-item>
            <span class="label">文档类型<span style="color: red">*</span></span>
            <el-select v-model="form.type" style="width: 330px">
              <el-option v-for="item in DocumentTypeDict" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <span class="label">项目文档<span style="color: red">*</span></span>
            <el-upload
              class="upload-demo"
              ref="upload"
              :action="upLoadParam.url"
              :file-list="fileList"
              :auto-upload="true"
              :limit="1"
              :on-success="uploadSuccess"
              :on-preview="handleFile"
              :headers="{ token: upLoadParam.token }"
              :on-change="handleUploadChange"
            >
              <el-button type="primary">上传</el-button>
            </el-upload>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer" style="text-align: center">
          <el-button @click="dialogFormVisible = false">取 消</el-button>
          <el-button type="primary" @click="fileSubmit">确 定</el-button>
        </div>
      </el-dialog>

      <el-dialog title="项目列表" :visible.sync="dialogTableVisible" width="800px">
        <el-table :data="projectList" style="width: 100%" stripe>
          <el-table-column min-width="10"></el-table-column>
          <el-table-column label="编号" align="left" prop="id" min-width="80"></el-table-column>
          <el-table-column
            :show-overflow-tooltip="true"
            align="left"
            prop="projectName"
            label="名称"
            min-width="180"
          />
          <el-table-column align="left" prop="industryCodeName" label="行业" min-width="120" />
          <el-table-column align="left" prop="certificationCriteriaName" label="核证标准" min-width="80" />
          <el-table-column align="left" prop="projectStatusName" label="状态" min-width="60" />
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
            @size-change="handleProjectSizeChange"
            @current-change="handleProjectCurrentChange"
            :current-page="projectCurrent"
            :page-size="projectPageSize"
            :page-count="projectPageCount"
            layout="total, sizes, prev, pager, next, jumper"
            :total="projectTotal"
          />
        </div>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import { loadcarbonProjectDoc, getCarbonProjectPageList, addCarbonProjectFile } from "@/api/carbonAssetApi";
import { getFeiShuUpLoadProjectParams } from "@/api/tenant";
import { setListNo } from "@/libs/public";
import { openUrlInNewWindow } from "@/libs/OpenHelper";
import { getDocumentTypeDict } from "@/config/dictHelper";

export default {
  name: "projectFile",
  data() {
    return {
      projectPageSize: 10,
      projectCurrent: 1,
      projectPageCount: null,
      projectTotal: 0,
      dialogTableVisible: false,
      fileList: [],
      upLoadParam: { url: "", token: "" },
      searchKeyWord: "",
      selectedDate: "",
      selectedEndDate: "",
      DocumentTypeDict: [],
      selectedType: [],
      list: [],
      projectList: [],
      projectName: "",
      form: {
        carbonProjectId: null,
        title: "",
        type: "",
        url: "",
      },
      dialogFormVisible: false,
      total: 0,
      current: 1,
      pageCount: 1,
      pageSize: 10,
      size: "10",
      carbonProjectId: "",
    };
  },
  methods: {
    cellStyle({ column }) {
      if (column.label !== "操作") {
        return "cursor:pointer;";
      }
    },
    handleFile(file) {
      const fileUrl = (file && file.response && file.response.msg) || file.url;
      if (fileUrl) {
        openUrlInNewWindow(fileUrl);
      }
    },
    uploadSuccess(res, file) {
      if (res && res.code == 200) {
        this.form.url = res.msg;
        this.form.title = file.name;
      } else {
        this.$message.warning("上传失败，请重试");
      }
    },
    handleUploadChange(file, fileList) {
      this.fileList = fileList;
    },
    handleProjectSizeChange(value) {
      this.projectPageSize = value;
      this.getProjectList(this.projectCurrent);
    },
    handleProjectCurrentChange(value) {
      this.projectCurrent = value;
      this.getProjectList(value);
    },
    pickProject() {
      this.dialogTableVisible = true;
      this.getProjectList(1);
    },
    fileSubmit() {
      if (this.form.title && this.form.carbonProjectId && this.form.type && this.form.url) {
        addCarbonProjectFile(this.form)
          .then(() => {
            this.$message.success("上传成功");
            this.dialogFormVisible = false;
            this.getList(1);
          })
          .catch((err) => {
            this.$message.error(err);
          });
      } else {
        this.$message.warning("必填项不能为空");
      }
    },
    pickProjectDone(row) {
      this.form.carbonProjectId = row.id;
      this.projectName = row.projectName;
      this.dialogTableVisible = false;
    },
    update() {
      this.current = 1;
      const data = {
        asc: true,
        type: (this.selectedType && this.selectedType[0]) || undefined,
        completionDateStart: this.selectedDate,
        completionDateEnd: this.selectedEndDate,
        searchKey: this.searchKeyWord,
        current: this.current,
        size: this.pageSize,
      };
      loadcarbonProjectDoc(data).then((res) => {
        this.list = (res && res.data && res.data.records) || [];
        this.total = Number((res && res.data && res.data.total) || 0);
        this.current = Number((res && res.data && res.data.current) || 1);
        this.pageCount = Math.ceil(parseInt(this.total) / this.pageSize);
        this.list.map((v) => {
          v.type = String(v.type);
          v.typeName = this.docTypeLabel(v.type);
          for (var i in v) {
            v[i] = v[i] ? v[i] : "--";
            if (v[i] == " ") {
              v[i] = "--";
            }
          }
        });
      });
    },
    onUpload() {
      this.dialogFormVisible = true;
      this.fileList = [];
      this.form = { carbonProjectId: null, title: "", type: "", url: "" };
      this.projectName = "";
    },
    getCurListNo(index) {
      let curNo = parseInt(index) + 1;
      let size = this.size || "10";
      let page = this.current - 1;
      let no = setListNo(page, size, curNo);
      return no ? no : 1;
    },
    onClickSearch() {
      this.current = 1;
      this.getList(1);
    },
    onEdit(row) {
      if (row.url && row.url != "--") {
        openUrlInNewWindow(row.url);
      } else {
        this.$message.warning("没有对应的url");
      }
    },
    handle(row, column) {
      if (column.label != "操作") {
        this.onEdit(row);
      }
    },
    handleSizeChange(val) {
      this.pageSize = val;
      this.getList(1);
    },
    handleCurrentChange(val) {
      this.current = val;
      this.getList(val);
    },
    getList(val) {
      this.current = val;
      const data = {
        asc: true,
        size: this.pageSize,
        current: val,
      };
      if (this.carbonProjectId) {
        data["projectId"] = this.carbonProjectId;
      }
      loadcarbonProjectDoc(data)
        .then((res) => {
          this.list = (res && res.data && res.data.records) || [];
          this.total = Number((res && res.data && res.data.total) || 0);
          this.current = Number((res && res.data && res.data.current) || val);
          this.pageCount = Math.ceil(parseInt(this.total) / this.pageSize);
          this.list.map((v) => {
            v.type = String(v.type);
            v.typeName = this.docTypeLabel(v.type);
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
    docTypeLabel(value) {
      const found = (this.DocumentTypeDict || []).find((d) => String(d.value) === String(value));
      return (found && found.label) || "--";
    },
    getProjectList(current) {
      this.projectCurrent = current;
      const data = {
        current: this.projectCurrent,
        size: this.projectPageSize,
        async: true,
      };
      getCarbonProjectPageList(data).then((res) => {
        this.projectList = (res && res.data && res.data.records) || [];
        this.projectTotal = Number((res && res.data && res.data.total) || 0);
        this.projectCurrent = Number((res && res.data && res.data.current) || current);
        this.projectPageCount = Math.ceil(parseInt(this.projectTotal) / this.projectPageSize);
      });
    },
  },
  mounted() {
    const data = this.$route.query;
    if (data && data.id) {
      this.carbonProjectId = parseInt(data.id);
    }
    this.getList(1);
    this.getProjectList(1);
    this.upLoadParam = getFeiShuUpLoadProjectParams();
    this.DocumentTypeDict = getDocumentTypeDict(this.$store);
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

.upload-btn {
  margin-top: 0px;
  margin-bottom: 20px;
  width: 103px;
}

>>> .el-cascader .el-input .el-input__inner,
>>> .el-cascader .el-input.is-focus .el-input__inner {
  border-color: transparent;
}

.label {
  font-weight: 700;
  width: 80px;
  display: inline-block;
}
</style>
