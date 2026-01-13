<template>
  <div class="root">
    <div class="divBox">
      <div class="content-container">
        <div class="container">
          <el-row>
            <el-col :span="6">
              <div class="selectbox-root">
                <span class="selectbox-hint" style="min-width: 80px"
                  >核证标准</span
                >
                <div class="selectbox-deliver" />
                <el-cascader
                  style="width: 120px"
                  placeholder="全部"
                  class="selectbox-input"
                  v-model="selectedCertification"
                  :options="Certification"
                  clearable
                  @change="update"
                >
                </el-cascader>
              </div>
            </el-col>
            <el-col :span="5">
              <div style="margin-left: 16px" class="selectbox-root">
                <span class="selectbox-hint" style="min-width: 60px">领域</span>
                <div class="selectbox-deliver" />
                <el-cascader
                  style="width: 120px"
                  placeholder="全部"
                  class="selectbox-input"
                  v-model="selectedAreaDict"
                  :options="projectAreaDict"
                  clearable
                  @change="update"
                >
                </el-cascader>
              </div>
            </el-col>
            <el-col :span="5">
              <div style="margin-left: 16px" class="selectbox-root">
                <span class="selectbox-hint" style="min-width: 60px">行业</span>
                <div class="selectbox-deliver" />
                <el-cascader
                  style="width: 120px"
                  placeholder="全部"
                  class="selectbox-input"
                  v-model="selectedIndustry"
                  :options="IndustryList"
                  clearable
                  @change="update"
                >
                </el-cascader>
              </div>
            </el-col>
            <el-col :span="8">
              <div
                style="margin-left: 10px; margin-right: 10px"
                class="selectbox-root"
              >
                <span class="selectbox-hint" style="min-width: 100px"
                  >方法学搜索</span
                >
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
            </el-col>
          </el-row>

          <button class="light-green-btn" @click="onClickSearch">查询</button>
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
            @cell-click="handle"
            :cell-style="cellStyle"
            :row-style="{ height: '64px' }"
            style="width: 100%"
          >
            <!-- <el-table-column min-width="40" label="" align="center" width="40">
              <template slot="header" slot-scope="{ column }">
                <el-checkbox
                  v-model="column.checked"
                  :indeterminate="indeterminateFlag"
                  :checked="allchecked"
                  label=""
                  @change="updateAllSelected"
                ></el-checkbox>
              </template>
              <template slot-scope="scope">
                <el-checkbox
                  @change="signCheckChange"
                  v-model="scope.row.checked"
                ></el-checkbox>
              </template>
            </el-table-column> -->
            <el-table-column min-width="10"></el-table-column>
            <el-table-column
              label="编号"
              align="left"
              prop="methodCode"
              min-width="80"
            >
            </el-table-column>
            <el-table-column
              :show-overflow-tooltip="true"
              align="left"
              prop="name"
              label="名称"
              min-width="250"
            />
            <el-table-column
              align="left"
              prop="fieldCodeName"
              label="领域"
              min-width="80"
            />
            <el-table-column
              align="left"
              prop="industryCodeName"
              label="行业"
              min-width="140"
            />
            <el-table-column
              align="left"
              prop="certificationCriteriaName"
              label="核证标准"
              min-width="60"
            />
            <el-table-column label="操作" min-width="150" align="center">
              <template slot-scope="scope">
                <a
                  class="list-blue-text"
                  @click="onEdit(scope.row.sourceFileUrl)"
                  >查看</a
                >
                <a
                  style="margin-left: 10px"
                  class="list-green-text"
                  @click="onclickAdd(scope.row.dictCode, scope.row.name)"
                  >创建项目</a
                >
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
        >
        </el-pagination>
      </div>
    </div>
  </div>
</template>
<script>
import { getEscarbonMethodologyByKeyword } from "@/api/carbonAssetApi";
import { openUrlInNewWindow } from "@/libs/OpenHelper";
import { getCertificationCriteriaDict } from "@/config/dictHelper";
import { getBusinessDict } from "@/config/dictHelper";
import { getProjectAreaDict } from "@/config/dictHelper";
export default {
  name: "method",
  data() {
    return {
      searchKeyWord: "",
      projectAreaDict: [], //项目领域字典
      selectedCertification: [], //被选中的标准
      selectedIndustry: [], //被选中的行业
      selectedAreaDict: [], //被选中的领域
      list: [],
      total: 0,
      current: 1,
      pageCount: 1,
      pageSize: 10,
      Certification: [], //核证标准字典
      IndustryList: [], //行业字典
    };
  },
  methods: {
    cellStyle({ row, column, rowIndex, columnIndex }) {
      if (column.label == "编号") {
        return "margin-left:0px;padding:0 0;";
      } else {
        return "cursor:pointer;";
      }
    },
    onclickAdd(dictCode, name) {
      // debugger
      this.$router.push({
        path: "/carbon/projectCreate/projectAdd",
        query: { code: dictCode, methodName: name },
      });
    },
    onClickSearch() {
      this.current = 1;
      const data = {
        asc: true,
        size: this.pageSize,
        fieldCode: (this.selectedAreaDict && this.selectedAreaDict[0]) || undefined,
        industryCode: (this.selectedIndustry && this.selectedIndustry[0]) || undefined,
        certificationCriteria: (this.selectedCertification && this.selectedCertification[0]) || undefined,
        status: "0450000002",
        current: this.current,
      };
      if(this.searchKeyWord){
        data["searchKey"] = this.searchKeyWord;
      }
      getEscarbonMethodologyByKeyword(data)
        .then((res) => {
          const payload = res && res.data ? res.data : {};
          this.list = payload.data || [];
          this.total = Number(payload.total || 0);
          this.current = Number(payload.current || this.current);
          this.pageCount = Math.ceil(parseInt(this.total) / this.pageSize);
          this.list.map((v) => {
            //遍历表格数据
            v.checked = false;
            v.statusName = this.statusName(v.status);
            for (var i in v) {
              v[i] = v[i] ? v[i] : "--";
            }
          });
        })
        .catch((errror) => {});
    },
    onEdit(url) {
      if (url != "--") {
        openUrlInNewWindow(url);
      } else {
        this.$message.warning("没有对应的url");
      }
    },
    handle(row, column, cell, event) {
      if (column.label != "操作") {
        if (row.sourceFileUrl != "--") {
          openUrlInNewWindow(row.sourceFileUrl);
        } else {
          this.$message.warning("没有对应的url");
        }
      }
      // openUrlInNewWindow(row.sourceFileUrl)
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
      if (status == 1) {
        return "启用";
      } else {
        return "禁用";
      }
    },
    getList(page) {
      this.current = page;
      const data = {
        asc: true,
        size: this.pageSize,
        fieldCode: (this.selectedAreaDict && this.selectedAreaDict[0]) || undefined,
        industryCode: (this.selectedIndustry && this.selectedIndustry[0]) || undefined,
        certificationCriteria: (this.selectedCertification && this.selectedCertification[0]) || undefined,
        current: page,
        status: "0450000002",
      };
      if(this.searchKeyWord){
        data["searchKey"] = this.searchKeyWord;
      }
      getEscarbonMethodologyByKeyword(data)
        .then((res) => {
          const payload = res && res.data ? res.data : {};
          this.list = payload.data || [];
          this.total = Number(payload.total || 0);
          this.current = Number(payload.current || page);
          this.pageCount = Math.ceil(parseInt(this.total) / this.pageSize);
          this.list.map((v) => {
            //遍历表格数据
            v.checked = false;
            v.statusName = this.statusName(v.status);
            for (var i in v) {
              v[i] = v[i] ? v[i] : "--";
            }
          });
        })
        .catch((errror) => {});
    },
    update() {
      const data = {
        asc: true,
        size: this.pageSize,
        fieldCode: (this.selectedAreaDict && this.selectedAreaDict[0]) || undefined,
        industryCode: (this.selectedIndustry && this.selectedIndustry[0]) || undefined,
        certificationCriteria: (this.selectedCertification && this.selectedCertification[0]) || undefined,
        current: this.current,
        status: "0450000002",
      };
      if(this.searchKeyWord){
        data["searchKey"] = this.searchKeyWord;
      }
      getEscarbonMethodologyByKeyword(data)
        .then((res) => {
          const payload = res && res.data ? res.data : {};
          this.list = payload.data || [];
          this.total = Number(payload.total || 0);
          this.current = Number(payload.current || this.current);
          this.pageCount = Math.ceil(parseInt(this.total) / this.pageSize);
          this.list.map((v) => {
            v.checked = false;
            v.statusName = this.statusName(v.status);
            for (var i in v) {
              v[i] = v[i] ? v[i] : "--";
            }
          });
        })
        .catch((errror) => {});
    },
    // 格式化标准字典
    formatStatus(data) {
      data.map((v) => {
        let CertificationItem = {
          label: "",
        };
        if (v.name == "全部") {
          CertificationItem.label = v.name;
        } else {
          CertificationItem.value = v.value;
          CertificationItem.label = v.name;
        }
        this.Certification.push(CertificationItem);
      });
    },
    // 格式化行业字典
    formatIndustry(data) {
      data.map((v) => {
        let CertificationItem = {
          label: "",
        };
        if (v.name == "全部") {
          CertificationItem.label = v.name;
        } else {
          CertificationItem.value = v.value;
          CertificationItem.label = v.name;
        }
        this.IndustryList.push(CertificationItem);
      });
    },
    // 格式化领域字典
    formatArea(data) {
      data.map((v) => {
        let CertificationItem = {
          label: "",
        };
        if (v.name == "全部") {
          CertificationItem.label = v.name;
        } else {
          CertificationItem.value = v.value;
          CertificationItem.label = v.name;
        }
        this.projectAreaDict.push(CertificationItem);
      });
    },
  },
  // checkbox end
  created() {
    // this.handleChangeVisitType();
  },
  mounted() {
    this.getList(1);
    this.formatStatus(getCertificationCriteriaDict(this.$store));
    this.formatIndustry(getBusinessDict(this.$store));
    this.formatArea(getProjectAreaDict(this.$store));
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

>>> .el-cascader .el-input .el-input__inner,
>>> .el-cascader .el-input.is-focus .el-input__inner {
  border-color: transparent;
}

.acea-row {
  ::v-deep.el-avatar--small {
    width: 22px;
    height: 22px;
    line-height: 22px;
  }
}

.checkTime {
  ::v-deep.el-radio__input {
    display: none;
  }
}

.ivu-pl-8 {
  margin-left: 8px;
  font-size: 14px;
}

.dashboard-console-visit {
  ::v-deep.el-card__header {
    padding: 14px 20px !important;
  }

  ul {
    li {
      list-style-type: none;
      margin-top: 12px;
    }
  }
}

.ivu-mb {
  margin-bottom: 10px;
}

.newsImg {
  width: 30px;
  height: 30px;
  border-radius: 4px;
}
</style>
