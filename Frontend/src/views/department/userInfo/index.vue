<template>
  <div class="app-container">
    <div class="manage-table">
      <el-form
        ref="searchForm"
        label-width="80px"
        :inline="true"
        size="small">
        <el-form-item>
          <el-input v-model="userName" placeholder="请输入部门员工名称"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="search">查询</el-button>
          <el-button icon="el-icon-refresh-right" @click="resetForm">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="departmentInformation" style="margin-top: 20px;">
      <div class="manage-table">
        <el-table :data="tableData">
          <el-table-column label="部门编号" prop="departmentId"></el-table-column>
          <el-table-column label="员工编号" prop="id"></el-table-column>
          <el-table-column label="部门名称" prop="departmentName"></el-table-column>
          <el-table-column label="员工姓名" prop="userName"></el-table-column>
          <el-table-column label="员工职务" prop="userRole"></el-table-column>
          <el-table-column label="操作">
            <template slot-scope="scope">
              <el-button type="warning" size="mini">转移</el-button>
              <el-button type="danger" size="mini">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
    <!-- 分页 -->
    <div>
      <el-pagination layout="prev, pager, next" :total="pageNum" @current-change="handlePage">
      </el-pagination>
    </div>
  </div>

</template>

<script>
import {getDepartmentTableData} from "@/api/department";

export default {
  data() {
    return {
      isSearch: false, // 是否搜索
      userName: "", // 搜索人的姓名
      tableData: [],
      pageNum: 10, // 总共的客户数量
      pageData: {
        page: 1,
        limit: 10
      }
    }
  },
  methods: {
    // 表格数据
    async getTableData() {
      var tableData = (await getDepartmentTableData(this.$store.getters.department_id)).data
      // 分页处理
      var pageList = tableData.slice((this.pageData.page - 1) * this.pageData.limit, this.pageData.page * this.pageData.limit)
      this.pageNum = tableData.length
      this.tableData = pageList
    },
    // 搜索员工
    async search() {
      this.isSearch = true
      var searchData = (await getDepartmentTableData(this.$store.getters.department_id)).data

      if (this.userName !== "") {
        searchData = searchData.filter(data => data.userName == this.userName)
        var pageList = searchData.slice((this.pageData.page - 1) * this.pageData.limit, this.pageData.page * this.pageData.limit)
        this.pageNum = searchData.length
        this.tableData = pageList
      } else {
        this.isSearch = false
        await this.getTableData()
      }
      return searchData
    },
    // 选择页码的回调函数
    async handlePage(val) {
      this.pageData.page = val
      // 是否为搜索模式
      if (this.isSearch === false) {
        await this.getTableData()
        this.isSearch = false // 关闭搜索状态
      }
    },
    resetForm() {
      this.userName = ""
    }
  },
  mounted() {
    this.getTableData()
  }
}
</script>

<style lang="less" scope>
.departmentInformation {
  background: #fff;
  padding: 20px;
}
</style>
