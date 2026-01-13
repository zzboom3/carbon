<template>
  <div class="root">
    <div class="divBox">
      <div class="content-container">
        <div class="container">
          <div class="selectbox-root">
            <span class="selectbox-hint" style="min-width: 80px">核证标准</span>
            <div class="selectbox-deliver" />
            <el-cascader
              placeholder="全部"
              class="selectbox-input"
              @change="update"
              v-model="selectedCertification"
              :options="Certification"
              clearable
            />
          </div>
          <div style="margin-left: 16px" class="selectbox-root">
            <span class="selectbox-hint" style="min-width: 60px">领域</span>
            <div class="selectbox-deliver" />
            <el-cascader
              placeholder="全部"
              class="selectbox-input"
              v-model="selectedArea"
              @change="update"
              :options="ProjectAreaDict"
              clearable
            />
          </div>
          <div style="margin-left: 16px" class="selectbox-root">
            <span class="selectbox-hint" style="min-width: 60px">行业</span>
            <div class="selectbox-deliver" />
            <el-cascader
              placeholder="全部"
              @change="update"
              class="selectbox-input"
              v-model="selectedIndustry"
              :options="IndustryList"
              clearable
            />
          </div>
          <div style="margin-left: 16px" class="selectbox-root">
            <span class="selectbox-hint" style="min-width: 60px">状态</span>
            <div class="selectbox-deliver" />
            <el-cascader
              placeholder="全部"
              @change="update"
              class="selectbox-input"
              v-model="selectedStatus"
              :options="statusList"
              clearable
            />
          </div>
          <div style="margin-left: 16px" class="selectbox-root">
            <span class="selectbox-hint" style="min-width: 80px">立项日期</span>
            <div class="selectbox-deliver" />
            <el-date-picker
              v-model="selectStartDate"
              @change="update"
              type="datetime"
              placeholder="选择开始时间"
              align="right"
              size="medium"
            />
            <el-date-picker
              v-model="selectEndDate"
              @change="update"
              type="datetime"
              placeholder="选择结束时间"
              align="right"
              size="medium"
            />
          </div>
        </div>
        <div style="clear: both; height: 10px"></div>
        <div class="container" style="margin-top: 0">
          <div style="margin-right: 15px" class="selectbox-root">
            <span class="selectbox-hint" style="min-width: 120px">按项目搜索</span>
            <div class="selectbox-deliver" />
            <el-input
              class="selectbox-input"
              v-model="searchProjectName"
              clearable
              placeholder="输入名称"
              @clear="onClickSearch"
              @keyup.enter.native="onClickSearch"
            />
          </div>
          <button class="light-green-btn" @click="onClickSearch">查询</button>
          <div style="margin-right: 15px; margin-left: 15px" class="selectbox-root">
            <span class="selectbox-hint" style="min-width: 120px">按方法学搜索</span>
            <div class="selectbox-deliver" />
            <el-input
              class="selectbox-input"
              v-model="searchMethodName"
              placeholder="输入名称"
              @clear="onClickSearch"
              @keyup.enter.native="onClickSearch"
              clearable
            />
          </div>
          <button class="light-green-btn" @click="onClickSearch">查询</button>
        </div>
      </div>

      <div>
        <button class="normal-white-btn create-btn" @click="onclickAdd">
          +创建项目
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
          stripe
          :row-style="{ height: '64px' }"
          style="width: 100%"
          @cell-click="handle"
          :cell-style="cellStyle"
        >
          <el-table-column min-width="10"></el-table-column>
          <el-table-column label="序号" align="left" min-width="40">
            <template slot-scope="scope"><span>{{ getCurListNo(scope.$index) }}</span></template>
          </el-table-column>
          <el-table-column
            :show-overflow-tooltip="true"
            prop="projectName"
            label="项目名称"
            min-width="100"
            align="left"
          />
          <el-table-column align="left" prop="certificationCriteriaName" label="核证标准" min-width="80" />
          <el-table-column align="left" prop="fieldCodeName" label="领域" min-width="80" />
          <el-table-column align="left" prop="industryCodeName" label="行业" min-width="110" />
          <el-table-column align="left" prop="projectStatusName" label="状态" min-width="50" />
          <el-table-column align="left" prop="address" label="项目所在地" min-width="100" />
          <el-table-column align="left" prop="ownerName" label="业主名称" min-width="100" />
          <el-table-column align="left" prop="initiationDate" label="立项日期" min-width="80" />
          <el-table-column label="操作" min-width="150" align="center">
            <template slot-scope="scope">
              <a class="list-blue-text" @click="toDetail(scope.row.id)" style="margin-left: 10px">查看</a>
              <a
                :class="editStyleChange(scope.row.projectStatus)"
                @click="onEdit(scope.row.projectStatus, scope.row.id)"
                style="margin-left: 10px"
              >修改</a>
              <a
                :class="deleteStyleChange(scope.row.projectStatus)"
                @click="onClickDelete(scope.row.projectStatus, scope.row.id)"
                style="margin-left: 10px"
              >删除</a>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
    <div style="margin-top: 30px; margin-bottom: 10px" class="pageBox">
      <div style="flex-grow: 1" />
      <a style="margin: auto" class="pageBox-total-num">共{{ total }}条</a>
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
  </div>
</template>

<script>
import { getCarbonProjectPageList } from "@/api/carbonAssetApi";
import { deleteCarbonProject } from "@/api/carbonAssetApi";
import { getCertificationCriteriaDict } from "@/config/dictHelper";
import { getProjectAreaDict } from "@/config/dictHelper";
import { getBusinessDict } from "@/config/dictHelper";
import { getProjectStatusDict } from "@/config/dictHelper";
import { setListNo } from "@/libs/public";

export default {
  name: "projectDevHome",
  data() {
    return {
      indeterminateFlag: false,
      reRender: true,
      allchecked: false,
      searchProjectName: "",
      searchMethodName: "",
      selectedCertification: [],
      selectedArea: [],
      selectedIndustry: [],
      selectedStatus: [],
      selectStartDate: "",
      selectEndDate: "",
      list: [],
      total: 0,
      current: 1,
      pageCount: 1,
      pageSize: 10,
      Certification: [],
      IndustryList: [],
      ProjectAreaDict: [],
      statusList: [],
      size: "10",
    };
  },
  methods: {
    cellStyle({ column }) {
      if (column.label === "操作") {
        return "padding:0 0;";
      }
      return "cursor:pointer;";
    },
    editStyleChange(status) {
      if (status === "0100000001") {
        return "list-yello-text";
      }
      return "afterIssueEdit";
    },
    deleteStyleChange(status) {
      if (status === "0100000001") {
        return "list-red-text";
      }
      return "afterSubmitOffline";
    },
    buildQueryData(extra = {}) {
      return {
        asc: true,
        size: this.pageSize,
        current: this.current,
        name: this.searchProjectName,
        methodologyName: this.searchMethodName,
        certificationCriteria: (this.selectedCertification && this.selectedCertification[0]) || "",
        fieldCode: (this.selectedArea && this.selectedArea[0]) || "",
        industryCode: (this.selectedIndustry && this.selectedIndustry[0]) || "",
        projectStatus: (this.selectedStatus && this.selectedStatus[0]) || "",
        initiationDateStart: this.selectStartDate,
        initiationDateEnd: this.selectEndDate,
        ...extra,
      };
    },
    update() {
      this.current = 1;
      this.getList(1);
    },
    onEdit(status, id) {
      if (status === "0100000001") {
        this.$router.push({
          path: "/carbon/projectCreate/projectEdit",
          query: { id, page: this.current },
        });
      } else {
        this.$message.warning("当前项目已立项，不可修改！");
      }
    },
    onclickAdd() {
      this.$router.push({ path: "/carbon/projectCreate/projectAdd" });
    },
    onClickDelete(status, id) {
      if (status !== "0100000001") {
        this.$message.warning("此项目已立项，不可删除");
        return;
      }
      this.$confirm("项目删除不可复原，请谨慎操作").then(() => {
        deleteCarbonProject(id).then(
          () => {
            this.$message.success("删除成功");
            this.getList(this.current);
          },
          () => {
            this.$message.error("删除失败");
          }
        );
      });
    },
    onClickSearch() {
      this.current = 1;
      this.getList(1);
    },
    toDetail(id) {
      this.$router.push({ path: "/carbon/projectCreate/projectDetail", query: { id } });
    },
    handle(row, column) {
      if (column.label !== "操作") {
        this.toDetail(row.id);
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
    getCurListNo(index) {
      let curNo = parseInt(index) + 1;
      let size = this.size || "10";
      let page = this.current - 1;
      let no = setListNo(page, size, curNo);
      return no ? no : 1;
    },
    getList(page) {
      const data = this.buildQueryData({ current: page });
      getCarbonProjectPageList(data)
        .then((res) => {
          this.list = (res && res.data && res.data.records) || [];
          this.total = Number((res && res.data && res.data.total) || 0);
          this.current = Number((res && res.data && res.data.current) || page);
          this.pageCount = Math.ceil(parseInt(this.total) / this.pageSize);
          this.list.map((v) => {
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
    formatCertification(data) {
      data.map((v) => {
        const item = { value: "", label: "" };
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
        const item = { value: "", label: "" };
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
        const item = { value: "", label: "" };
        if (v.name == "全部") {
          item.label = v.name;
        } else {
          item.value = v.value;
          item.label = v.name;
        }
        this.ProjectAreaDict.push(item);
      });
    },
    formatStatus(data) {
      data.map((v) => {
        const item = { value: "", label: "" };
        if (v.name == "全部") {
          item.label = v.name;
        } else {
          item.value = v.value;
          item.label = v.name;
        }
        this.statusList.push(item);
      });
    },
  },
  mounted() {
    const page = this.$route.query && this.$route.query.page ? Number(this.$route.query.page) : 1;
    this.getList(page);
    this.formatCertification(getCertificationCriteriaDict(this.$store));
    this.formatIndustry(getBusinessDict(this.$store));
    this.formatArea(getProjectAreaDict(this.$store));
    this.formatStatus(getProjectStatusDict(this.$store));
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

.create-btn {
  margin-top: 0px;
  margin-bottom: 20px;
  width: 103px;
}

>>> .el-cascader .el-input .el-input__inner,
>>> .el-cascader .el-input.is-focus .el-input__inner {
  border-color: transparent;
}
</style>
