<template>
  <div class="manage">
    <div class="manage-header">
      <!-- 搜索框 -->
      <el-form :model="searchForm" :inline="true">
        <el-form-item>
          <el-input placeholder="请输入员工名称" v-model="searchForm.name"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="success" @click="searchName">日志查询</el-button>
        </el-form-item>
      </el-form>
    </div>
    <!-- 客户表格 -->
    <div class="manage-table">
      <el-table :data="tableData">
        <el-table-column label="员工编号" prop="userId"></el-table-column>
        <el-table-column label="员工姓名" prop="username"></el-table-column>
        <el-table-column label="员工操作" prop="operation"></el-table-column>
        <el-table-column label="操作时间" prop="time"></el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button type="danger" size="mini" @click="$event => handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <!-- 分页 -->
    <div>
      <el-pagination layout="prev, pager, next" :total="pageNum" @current-change="handlePage">
      </el-pagination>
    </div>
  </div>
</template>

<script>
import {deleteCustomer, deleteCustomerByCustomerId, findCustomerByName, getCustomerInfoByUserId, insertCustomer, updateCustomerById} from "@/api/customer";
import {error} from "autoprefixer/lib/utils";
import da from "element-ui/src/locale/lang/da";
import fa from "element-ui/src/locale/lang/fa";
import {deleteRow, getContractNoByCustomerId} from "@/api/contract";
import {getAllLog, getLogByName} from "@/api/log";

export default {
  data() {
    return {
      modal: true, // true按钮触发添加，false按钮触发更新
      // 搜索框
      searchForm: {name: ""},
      tableData: [{name: "ypj", age: 22}, {name: "zcl", age: 20}], // 当页显示的数据
      pageNum: 10, // 总共的客户数量
      pageData: {
        page: 1,
        limit: 10
      },
      isSearch: false, // 是否处于搜索状态
    }
  },
  inject: [
    'reload'
  ],
  methods: {
    /**
     * 按客户name搜索
     * @returns {Promise<void>}
     */
    async searchName() {
      if (this.searchForm.name != "") {
        this.isSearch = true // 开启搜索状态，即分页看到的是name相同的
        const data = (await getLogByName({name: this.searchForm.name})).data
        for (var i = 0; i < data.length; i++) {
          data[i].time = data[i].time.slice(0, 10)
        }
        var pageList = data.slice((this.pageData.page - 1) * this.pageData.limit, this.pageData.page * this.pageData.limit)
        this.pageNum = data.length
        this.tableData = pageList
      } else {
        this.isSearch = false
        await this.getTableData()
      }
    },
    /**
     * 获取表格数据，
     * @param limited 当页限制的客户数量
     * @returns {Promise<void>}
     */
    async getTableData() {
      const {data} = (await getAllLog())

      for (var i = 0; i < data.length; i++) {
        data[i].time = data[i].time.slice(0, 10)
      }
      var pageList = data.slice((this.pageData.page - 1) * this.pageData.limit, this.pageData.page * this.pageData.limit)
      this.pageNum = data.length
      this.tableData = pageList
    },

    // 选择页码的回调函数
    async handlePage(val) {
      this.pageData.page = val
      // 是否为搜索模式
      if (this.isSearch === false) {
        await this.getTableData()
        this.isSearch = false // 关闭搜索状态
      } else {
        const data = (await getLogByName({name: this.searchForm.name})).data
        var pageList = data.slice((this.pageData.page - 1) * this.pageData.limit, this.pageData.page * this.pageData.limit)
        for (var i = 0; i < data.length; i++) {
          data[i].time = data[i].time.slice(0,10)
        }
        this.pageNum = data.length
        this.tableData = pageList
      }
    },
  },
  mounted() {
    this.getTableData()
  }
}
</script>

<style>
.manage-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1% 1%;
}

.manage-table {
  padding: 1% 1%;
}
</style>
