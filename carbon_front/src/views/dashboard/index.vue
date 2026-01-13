<template>
  <div class="dashboard-container">
    <!-- 自然动态背景 -->
    <div class="nature-bg">
      <div class="gradient-layer"></div>
      <div class="sun-glow"></div>
      <div class="leaf-particles">
        <span v-for="n in 15" :key="`leaf-${n}`" :style="getParticleStyle(n)"></span>
      </div>
    </div>

    <div class="dashboard-content">
      <!--头部-->
      <div class="glass-card mb-20">
        <company-package 
          ref="companyPackage" 
          :accountVo="accountVo" 
          :assetsIncome="assetsIncome" 
          :fundIncome="fundIncome" 
          :carbonCredit="carbonCredit" 
          :carbonQuota="carbonQuota" 
          :greenScore="greenScore" 
        />
      </div>

      <div class="glass-card mb-20">
        <capital-stat 
          ref="capitalStat" 
          :monthSupply="monthSupply" 
          :monthDevelopment="monthDevelopment" 
          :monthSales="monthSales" 
        />
      </div>

      <!--小方块-->
      <div class="grid-section mb-20">
        <div class="glass-card">
          <apply-project />
        </div>
        
        <div class="glass-card" v-if="(projectStat.projectList && projectStat.projectList.length > 0)">
           <!-- <project-stat ref="projectStat" :projectStat="projectStat" /> -->
           <projectStatistical :projectStat="projectStat" />
        </div>
      </div>

      <div class="glass-card mb-20">
        <trade-info :quotation="quotation" />
      </div>

      <div class="glass-card mb-20">
        <newsContaiert ref="newsList" />
      </div>

      <div class="glass-card">
        <commom-usage-grid />
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import applyProject from "./components/applyProject";
import companyPackage from "./components/companyPackage.vue";
import capitalStat from "./components/capitalStat.vue";
import newsContaiert from "./components/newsContaiert.vue";
import tradeInfo from "./components/tradeInfo.vue";
import commomUsageGrid from "./components/commomUsageGrid.vue";
import { getHomePanelData } from "@/api/homeApi.js";
import { getToken } from "@/utils/auth";
import { getAllDiction } from "@/config/dictHelper";
import projectStatistical from "./components/projectStatistical.vue";
import { setLargeNumber } from "@/libs/public"

export default {
  name: "Dashboard",
  components: {
    applyProject,
    tradeInfo,
    companyPackage,
    capitalStat,
    newsContaiert,
    commomUsageGrid,
    projectStatistical,
  },
  data() {
    return {
      currentRole: "adminDashboard",
      accountVo: {},
      assetsIncome: { statDate: '--', totalIncome: 0, monthOnMonthRatio: 0, yearOnYearRatio: 0 },
      carbonCredit: 0,
      carbonQuota: 0,
      fundIncome: { statDate: '--', monthIncome: 0, totalIncome: 0, monthOnMonthRatio: 0, yearOnYearRatio: 0 },
      greenScore: 0,
      monthDevelopment: {},
      monthSales: {},
      monthSupply: {},
      projectStat: {},
      quotation: {},
    }
  },
  methods: {
    getParticleStyle(n) {
      return {
        left: Math.random() * 100 + '%',
        top: Math.random() * 100 + '%',
        animationDelay: Math.random() * 10 + 's',
        opacity: Math.random() * 0.4 + 0.1,
        transform: `scale(${Math.random() * 0.5 + 0.5})`
      }
    },
    setDataNumber(obj){
      for(let i in obj) {
          if(typeof(obj[i]) == "number"){
          obj[i] = setLargeNumber(obj[i]);
        }
      }
      return obj;
    },
    getNewsList(){},
    getAllDictionary() {
      getAllDiction(this.$store)
    },
    getLastMonth(date) {
      var dateArr = date.split("-");
      var m = parseInt(dateArr[1])
      if (m == 1) {
        return 12 + '月'
      } else {
        return (m - 1) + '月'
      }
    },
    getHomePanelData() {
      let token = getToken()
      getHomePanelData(token).then(res => {
        this.accountVo = res.accountVo
        if(!this.accountVo.avatar || this.accountVo.avatar == "" || (this.accountVo.avatar).match(/^[ ]*$/)){
          this.accountVo.avatar = '/static/img/icon_accoun_logo.e4db51e4.jpg';
        }else{
          window.localStorage.setItem("avatar",this.accountVo.avatar);
        }
        this.assetsIncome = res.assetsIncome
        for (let i in this.assetsIncome) {
          if(typeof(this.assetsIncome[i]) == "number"){
            this.assetsIncome[i] = setLargeNumber(this.assetsIncome[i]);
          }
        }
        this.carbonCredit = setLargeNumber(res.carbonCredit)
        this.carbonQuota = setLargeNumber(res.carbonQuota)
        this.fundIncome = res.fundIncome
        this.greenScore = res.greenScore
        this.monthDevelopment = this.setDataNumber(res.monthDevelopment)
        this.monthSales = this.setDataNumber(res.monthSales)
        this.monthSupply = this.setDataNumber(res.monthSupply)
        this.projectStat = this.setDataNumber(res.projectStat)
        for (let i = 0; i < (this.projectStat.projectList).length; i++) {
          this.projectStat.projectList[i].reduction = setLargeNumber(this.projectStat.projectList[i].reduction)
          this.projectStat.projectList[i].carbonValuation = setLargeNumber(this.projectStat.projectList[i].carbonValuation)
        }
        this.quotation = this.setDataNumber(res.quotation)
        this.quotation.projects = this.setDataNumber(res.quotation.projects)
        this.carbonCredit = setLargeNumber(res.carbonCredit)
        this.carbonQuota = setLargeNumber(res.carbonQuota)
        this.greenScore = setLargeNumber(res.greenScore)
        this.assetsIncome.statDate = this.getLastMonth(this.assetsIncome.statDate)
        this.fundIncome.statDate = this.getLastMonth(this.fundIncome.statDate)
        this.monthSupply.statDate = this.getLastMonth(this.monthSupply.statDate)
        this.monthDevelopment.statDate = this.getLastMonth(this.monthDevelopment.statDate)
        this.monthSales.statDate = this.getLastMonth(this.monthSales.statDate)
      }
      ).catch((res) => {
      })
    },
  },
  mounted() {
    this.getAllDictionary()
    this.getHomePanelData()
    this.getNewsList()
  },
  computed: {
    ...mapGetters([
      'roles'
    ])
  },
  created() {
    if (!this.roles.includes('admin')) {
      this.currentRole = 'editorDashboard'
    }
  }
}
</script>

<style lang="scss" scoped>
/* 绿色环保主题变量 */
$eco-green: #2E7D32;
$eco-light: #4CAF50;
$eco-pale: #E8F5E9;
$text-dark: #1B5E20;
$text-grey: #546E7A;
$white: #ffffff;

.dashboard-container {
  min-height: 100vh;
  position: relative;
  background-color: #f1f8e9;
  font-family: 'PingFang SC', 'Helvetica Neue', Arial, sans-serif;
}

/* 自然动态背景 */
.nature-bg {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 0;
  overflow: hidden;
  
  .gradient-layer {
    width: 100%;
    height: 100%;
    background: linear-gradient(135deg, #e8f5e9 0%, #c8e6c9 100%);
  }
  
  .sun-glow {
    position: absolute;
    top: -20%;
    left: -10%;
    width: 800px;
    height: 800px;
    background: radial-gradient(circle, rgba(255, 235, 59, 0.2) 0%, transparent 70%);
    filter: blur(50px);
    animation: sunMove 20s ease-in-out infinite alternate;
  }
  
  .leaf-particles {
    span {
      position: absolute;
      width: 10px;
      height: 10px;
      background: rgba(76, 175, 80, 0.2);
      border-radius: 50% 0 50% 0;
      animation: floatLeaf 15s linear infinite;
    }
  }
}

@keyframes sunMove {
  0% { transform: translate(0, 0); }
  100% { transform: translate(50px, 50px); }
}

@keyframes floatLeaf {
  0% { transform: translateY(100vh) rotate(0deg); opacity: 0; }
  20% { opacity: 0.6; }
  80% { opacity: 0.6; }
  100% { transform: translateY(-100px) rotate(360deg); opacity: 0; }
}

/* 内容区域 */
.dashboard-content {
  position: relative;
  z-index: 1;
  padding: 20px;
}

/* 磨砂玻璃卡片 */
.glass-card {
  background: rgba(255, 255, 255, 0.6);
  backdrop-filter: blur(10px);
  border-radius: 16px;
  border: 1px solid rgba(255, 255, 255, 0.6);
  box-shadow: 0 4px 20px rgba(46, 125, 50, 0.05);
  padding: 20px;
  transition: all 0.3s;

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 8px 30px rgba(46, 125, 50, 0.1);
    background: rgba(255, 255, 255, 0.8);
  }
}

.mb-20 {
  margin-bottom: 20px;
}

.grid-section {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(400px, 1fr));
  gap: 20px;
}

/* 适配移动端 */
@media screen and (max-width: 768px) {
  .grid-section {
    grid-template-columns: 1fr;
  }
}
</style>
