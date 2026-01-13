<template>
  <div class="question-list-container">
    <el-table 
      height="176px" 
      :show-header="false" 
      :data="records" 
      style="width: 100%" 
      @row-click="handle" 
      :cell-style="cellStyle"
      class="nature-table">
      
      <el-table-column min-width="100%" :show-overflow-tooltip="true" prop="title" label="标题">
        <template slot-scope="scope">
          <div class="question-item">
            <span class="q-mark">Q</span>
            <span class="question-title">{{ scope.row.title }}</span>
          </div>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import * as artical from '@/api/article'
import { openUrlInNewWindow } from '@/libs/OpenHelper'
import { cursor } from '@/libs/element-table'

export default {
  name: 'QuestionList',
  data() {
    return {
      records: [],
    }
  },
  mounted() {
    this.getNewsList()
  },

  methods: {
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
    getNewsList() {
      const data = {
        "asc": true,
        "categoryId": "0180000004", // 分类ID: 1.咨询 2.知识库 3.公告 4.常见问题 
      }
      artical.getCarbonArticles(data).then(res => {
        this.records = res.records
      }).catch(errror => {
        console.error(errror)
      })
    }
  }
};
</script>

<style lang="scss" scoped>
/* 绿色环保主题变量 */
$eco-green: #2E7D32;
$text-main: #1B5E20;

.question-list-container {
  width: 100%;
}

.nature-table {
  background: transparent !important;
  
  ::v-deep tr {
    background: transparent !important;
    transition: all 0.3s;
    
    &:hover {
      background: rgba(232, 245, 233, 0.5) !important;
      
      .question-title {
        color: $eco-green;
      }
      
      .q-mark {
        background: $eco-green;
        color: #ffffff;
      }
    }
    
    td {
      border-bottom: 1px dashed rgba(46, 125, 50, 0.1) !important;
      padding: 8px 0;
    }
  }

  ::v-deep .el-table__body-wrapper {
    &::-webkit-scrollbar {
      width: 4px;
    }
    &::-webkit-scrollbar-thumb {
      background: rgba(46, 125, 50, 0.2);
      border-radius: 2px;
    }
    &::-webkit-scrollbar-track {
      background: transparent;
    }
  }
}

.question-item {
  display: flex;
  align-items: center;
  
  .q-mark {
    width: 20px;
    height: 20px;
    background: #E8F5E9;
    color: $eco-green;
    border-radius: 4px;
    margin-right: 10px;
    display: flex;
    align-items: center;
    justify-content: center;
    font-size: 12px;
    font-weight: bold;
    transition: all 0.3s;
  }
  
  .question-title {
    font-size: 13px;
    color: $text-main;
    transition: color 0.3s;
  }
}
</style>
