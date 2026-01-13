<template>
  <div class="news-container">
    <div class="section-header">
      <span class="nav-title">行业资讯</span>
      <div class="header-line"></div>
      <span class="more-link" @click="handleMore">更多 ></span>
    </div>

    <div class="table-wrapper">
      <el-table 
        height="400px" 
        :show-header="false" 
        :data="records" 
        style="width: 100%" 
        @row-click="handle" 
        :cell-style="cellStyle"
        class="nature-table">
        
        <el-table-column width="40px" align="center">
          <template slot-scope="scope">
            <div class="icon-cell">
              <img class="icon hot" v-if="Number(scope.row.browseNum) >= 10" src="@/assets/imgs/icon_new_hot.png" title="热门" />
              <img class="icon new" v-else src="@/assets/imgs/icon_new_jin.png" title="最新" />
            </div>
          </template>
        </el-table-column>
        
        <el-table-column :show-overflow-tooltip="true" min-width="70%" prop="title" label="文章名称">
          <template slot-scope="scope">
            <span class="news-title">{{ scope.row.title }}</span>
          </template>
        </el-table-column>
        
        <el-table-column width="160px" prop="updatedTime" label="日期" align="right">
          <template slot-scope="scope">
            <span class="news-date">{{ scope.row.updatedTime }}</span>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
import * as artical from '@/api/article'
import { openUrlInNewWindow } from '@/libs/OpenHelper'
import { cursor } from '@/libs/element-table'

export default {
  name: 'NewsContainer',
  data() {
    return {
      records: [],
    }
  },
  mounted() {
    this.getNewsList()
  },

  methods: {
    dataFormat(time){
      var datetime = new Date();
      datetime.setTime(parseInt(time));
      var year = datetime.getFullYear();
      var month = datetime.getMonth() + 1 < 10 ? "0" + (datetime.getMonth() + 1) : datetime.getMonth() + 1;
      var date = datetime.getDate() < 10 ? "0" + datetime.getDate() : datetime.getDate();
      var hour = datetime.getHours()< 10 ? "0" + datetime.getHours() : datetime.getHours();
      var minute = datetime.getMinutes()< 10 ? "0" + datetime.getMinutes() : datetime.getMinutes();
      var second = datetime.getSeconds()< 10 ? "0" + datetime.getSeconds() : datetime.getSeconds();
      return year + "-" + month + "-" + date+" "+hour+":"+minute+":"+second;
    },
    cellStyle(data) {
      return {
        ...cursor(data),
        background: 'transparent'
      }
    },
    handle(row) {
      if (!row || !row.url || row.url === '') {
        return
      }
      openUrlInNewWindow(row.url)
    },
    handleMore() {
      // TODO: 跳转到资讯列表页
    },
    getNewsList() {
      const data = {
        "asc": true,
        "categoryId": "0180000002", // 分类ID: 1.咨询 2.知识库 3.公告 4.常见问题
      }
      artical.getCarbonArticles(data).then(res => {
        this.records = res.records;
      }).catch(error => {
        console.error(error)
      })
    }
  }
};
</script>

<style lang="scss" scoped>
/* 绿色环保主题变量 */
$eco-green: #2E7D32;
$eco-light: #4CAF50;
$eco-bg: #E8F5E9;
$text-main: #1B5E20;
$text-sub: #546E7A;
$white: #ffffff;

.news-container {
  padding: 0 10px;
}

.section-header {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
  
  .nav-title {
    font-size: 18px;
    font-weight: 600;
    color: $text-main;
    margin-right: 15px;
  }
  
  .header-line {
    flex: 1;
    height: 1px;
    background: linear-gradient(90deg, rgba(46, 125, 50, 0.3), transparent);
    margin-right: 15px;
  }

  .more-link {
    font-size: 13px;
    color: $text-sub;
    cursor: pointer;
    transition: color 0.3s;
    
    &:hover {
      color: $eco-green;
    }
  }
}

.table-wrapper {
  background: rgba(255, 255, 255, 0.5);
  border-radius: 12px;
  padding: 10px;
}

.nature-table {
  background: transparent !important;
  
  ::v-deep tr {
    background: transparent !important;
    transition: all 0.3s;
    
    &:hover {
      background: rgba(232, 245, 233, 0.5) !important;
      
      .news-title {
        color: $eco-green;
      }
    }
    
    td {
      border-bottom: 1px dashed rgba(46, 125, 50, 0.1) !important;
    }
  }
  
  ::v-deep .el-table__body-wrapper {
    &::-webkit-scrollbar {
      width: 6px;
    }
    &::-webkit-scrollbar-thumb {
      background: rgba(46, 125, 50, 0.2);
      border-radius: 3px;
    }
    &::-webkit-scrollbar-track {
      background: transparent;
    }
  }
}

.icon-cell {
  display: flex;
  justify-content: center;
  align-items: center;
}

.icon {
  width: 16px;
  height: 16px;
  object-fit: contain;
}

.news-title {
  font-size: 14px;
  color: $text-main;
  transition: color 0.3s;
}

.news-date {
  font-size: 13px;
  color: $text-sub;
  font-family: Arial, sans-serif;
}
</style>
