<template>
  <div class="root">
    <div class="divBox">
      <div class="content-container">
        <div class="cardBody">
          <span class="header">项目详情</span><span class="asset-title">{{ assetDetail["projectName"] }}</span>
          <el-divider></el-divider>
          <img src="@/assets/icon/icon_plant.png" alt="" class="icon" />
          <span class="asset-little-title">基本信息</span>
          <div class="basic-div">
            <div class="item-div">
              <span class="item-title">国家</span> <br /><span class="item-content">{{ assetDetail["countryName"]
              }}</span>
            </div>
            <div class="item-div">
              <span class="item-title">省份</span> <br /><span class="item-content">{{ assetDetail["provinceName"]
              }}</span>
            </div>
            <div class="item-div">
              <span class="item-title">市/县/郡</span> <br /><span class="item-content">{{ assetDetail["cityName"]
              }}</span>
            </div>
            <div class="item-div">
              <span class="item-title">项目地点</span> <br /><span class="item-content">{{ assetDetail["address"] }}</span>
            </div>
            <div class="item-div">
              <span class="item-title">立项日期</span> <br /><span class="item-content">{{ assetDetail["initiationDate"] |
              formatDate }}</span>
            </div>
            <div class="item-div">
              <span class="item-title">采用方法学</span> <br /><span class="item-content">{{
              assetDetail["carbonMethodologyName"] }}</span>
            </div>
            <div class="item-div">
              <span class="item-title">开发机构</span> <br /><span class="item-content">{{
              assetDetail["assetsDevelopAgency"] }}</span>
            </div>
            <div class="item-div">
              <span class="item-title">开发负责人</span>
              <br /><span class="item-content">{{
              assetDetail["principalName"]
              }}</span>
            </div>
            <div class="item-div">
              <span class="item-title">开发负责人电话</span> <br /><span class="item-content">{{ assetDetail["principalPhone"]
              }}</span>
            </div>
          </div>
          <br />
          <br />
          <img src="@/assets/icon/icon_plant.png" alt="" class="icon" />
          <span class="asset-little-title">项目信息</span><span class="detail" @click="toDetail" v-if="url">查看详情<i
              class="el-icon-d-arrow-right"></i></span>
          <div class="basic-div" style="padding: 20px 16px 20px 16px; height: auto">
            <p class="paragraph">
              {{ assetDetail["projectIntroduction"] }}
            </p>
          </div>
          <br />
          <br />
          <img src="@/assets/icon/icon_plant.png" alt="" class="icon" />
          <span class="asset-little-title">业主信息</span><span class="detail" @click="toDetail" v-if="url">查看详情<i
              class="el-icon-d-arrow-right"></i></span>
          <div class="basic-div" style="height: 174px">
            <div class="item-div">
              <span class="item-title">业主名称</span> <br /><span class="item-content">{{ assetDetail["ownerName"] 
              }}</span>
            </div>
            <div class="item-div">
              <span class="item-title">业主法人姓名</span> <br /><span class="item-content">{{ assetDetail["legalPersonName"]
              }}</span>
            </div>
            <div class="item-div">
              <span class="item-title">法人联系方式</span> <br /><span class="item-content">{{ assetDetail["legalPersonPhone"]
              }}</span>
            </div>
            <div class="item-div">
              <span class="item-title">业主营业执照</span>
              <br /><span class="item-content" style="color: #126fff; cursor: pointer"
                @click="toFile(ownerRunCard.url)">{{ownerRunCard.fileName || "--"}}</span>
            </div>
          </div>
          <br />
          <br />
          <img src="@/assets/icon/icon_plant.png" alt="" class="icon" />
          <span class="asset-little-title">材料信息</span>
          <div class="fileDiv">
            <div v-for="(v, i) in assetDetail['annexList']" :key="i">
              <div class="item-div" v-if="v.fileDictValue != '02300000013'">
                <span class="item-title">{{ v.fileDictValueName }}</span><br /><span class="item-content file-link"
                @click="toFile(v.url)">{{ v.fileName }}</span>
              </div>
            </div>
            <div class="item-div" v-if="!assetDetail['annexList']">
              <span class="item-title">--</span>
              <br /><span class="item-content"></span>
            </div>
          </div>
          <br />
          <div class="basic-div-bottom">
            <button style="width: 96px" class="normal-white-btn" @click="goback">
              返回
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { readCarbonProject } from "@/api/carbonAssetApi";
import { openUrlInNewWindow } from "@/libs/OpenHelper";
export default {
  name: "projectDetail",
  data() {
    return {
      assetId: 0,
      assetDetail: {},
      url: "",
      ownerRunCard: {
        fileName: "",
        fileDictValueName: "",
        fileDictValue: "",
        url: "",
      }, //业主营业执照
      annexList: [],
    };
  },
  filters: {
    formatDate(val) {
      if (!val || val === "--") {
        return "--";
      }
      var day = String(val).split(" ");
      return day[0] || "--";
    },
  },
  mounted() {
    this.loadDetail();
  },
  methods: {
    toDetail() {
      if (this.url) {
        openUrlInNewWindow(this.url);
      }
    },
    toFile(url) {
      if (url) {
        openUrlInNewWindow(url);
      }
    },
    loadDetail() {
      let id = this.$route.query;
      this.assetId = id.id;
      readCarbonProject(this.assetId).then(
        (res) => {
          this.assetDetail = (res && res.data) || {};
          this.url = this.assetDetail.designDocument || "";
          const annexList = Array.isArray(this.assetDetail.annexList) ? this.assetDetail.annexList : [];
          if (annexList.length) {
            this.annexList = annexList;
            this.annexList.map((v) => {
              if (v.fileDictValue == "02300000013") {
                v.fileDictValueName = "法人营业执照";
                this.ownerRunCard = v;
              }
            });
          } else {
            this.annexList = [
              {
                fileName: "--",
                fileDictValueName: "--",
                fileDictValue: "--",
                url: "",
              },
            ];
          }
          for (var i in this.assetDetail) {
            this.assetDetail[i] = this.assetDetail[i]
              ? this.assetDetail[i]
              : "--";
            if (this.assetDetail[i] == " ") {
              this.assetDetail[i] = "--";
            }
          }
        },
        (err) => {
          this.$message.success("加载数据失败");
        }
      );
    },
    goback() {
      this.$router.go(-1);
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
}

.container {
  margin: 10px 0px 20px 0px;
  display: flex;
  flex-direction: row;
}

.cardBody {
  margin: 30px 30px 30px 30px;
}

.header {
  width: 95px;
  height: 18px;
  font-size: 18px;
  font-family: PingFangSC-Medium, PingFang SC;
  font-weight: 500;
  color: #24a776;
  line-height: 18px;
}

.asset-title {
  width: 396px;
  height: 18px;
  font-size: 18px;
  font-family: PingFangSC-Medium, PingFang SC;
  font-weight: 500;
  color: #242b35;
  line-height: 18px;
  margin-left: 20px;
}

.el-divider {
  margin-top: 30px;
}

.icon {
  height: 16px;
  width: 22px;
}

.asset-little-title {
  width: 64px;
  height: 16px;
  font-size: 16px;
  font-family: PingFangSC-Medium, PingFang SC;
  font-weight: 500;
  color: #424c5c;
  line-height: 16px;
  margin-left: 10px;
}

.basic-div {
  width: 95%;
  height: 261px;
  background: #f7f9fc;
  border-radius: 6px;
  margin-left: 30px;
  margin-top: 30px;
}

.basic-div-bottom {
  width: 95%;
  height: 174px;
  text-align: right;
  margin-left: 30px;
  margin-top: 15px;
}

.item-title {
  width: 56px;
  height: 14px;
  font-size: 14px;
  font-family: PingFangSC-Regular, PingFang SC;
  font-weight: 400;
  color: #5e6c84;
  line-height: 14px;
}

.item-content {
  width: 42px;
  height: 15px;
  font-size: 15px;
  font-family: PingFangSC-Regular, PingFang SC;
  font-weight: 400;
  line-height: 15px;
  position: relative;
  top: 15px;
}

.item-div {
  float: left;
  margin: 24px 10px 20px 10px;
  width: 200px;
  height: 40px;
  // overflow: hidden;
}

.accessory {
  width: 28px;
  height: 14px;
  font-size: 14px;
  font-family: PingFangSC-Regular, PingFang SC;
  font-weight: 400;
  color: #126fff;
  line-height: 14px;
  margin-left: 10px;
  cursor: pointer;
}

.paragraph {
  width: 95%;
  height: 108px;
  font-size: 14px;
  font-family: PingFangSC-Regular, PingFang SC;
  font-weight: 400;
  color: #808ea5;
  line-height: 21px;
  text-align: justify;
}

.detail {
  width: 56px;
  height: 14px;
  font-size: 14px;
  font-family: PingFangSC-Regular, PingFang SC;
  font-weight: 400;
  color: #24a776;
  line-height: 14px;
  margin-left: 15px;
  cursor: pointer;
}

.fileDiv {
  width: 95%;
  min-height: 261px;
  background: #f7f9fc;
  border-radius: 6px;
  margin-left: 30px;
  margin-top: 30px;
}

.file-link {
  color: #126fff;
  cursor: pointer;
}
</style>
