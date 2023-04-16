<template>
  <div class="app-container">
    <div style="display: flex;justify-content: space-between; margin-bottom: 50px;">
      <div>
        <el-input v-model="searchUsername" style="width: 200px;margin-right: 20px" prefix-icon="el-icon-search" placeholder="请输入员工的姓名."></el-input>
        <el-input v-model="searchUserRole" style="width: 200px;margin-right: 20px" prefix-icon="el-icon-search" placeholder="请输入员工的职称"></el-input>
        <el-button type="primary" icon="el-icon-search" @click="search">搜索</el-button>
        <el-button type="warning" icon="el-icon-refresh" @click="reset">重置</el-button>
      </div>
    </div>
    <!-- 员工表格 -->
    <div class="manage-table">
      <el-table :data="tableData">
        <el-table-column label="员工编号" prop="id"></el-table-column>
        <el-table-column label="员工姓名" prop="username"></el-table-column>
        <el-table-column label="员工职称" prop="userRole"></el-table-column>
        <el-table-column label="员工性别" prop="sex"></el-table-column>
        <el-table-column label="员工年龄" prop="age"></el-table-column>
        <el-table-column label="员工部门" prop="departmentName"></el-table-column>
        <el-table-column label="员工战区" prop="militaryRegionName"></el-table-column>
        <el-table-column label="员工薪资" prop="salary"></el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button type="warning" size="mini">编辑</el-button>
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
import {deleteUserById, getUserTableData} from "@/api/user";
import {deleteCustomerByCustomerId, findCustomerByName} from "@/api/customer";
import {getDepartmentTableData} from "@/api/department";

export default {
  data() {
    return {
      isSearch: false, // 是否搜索
      searchUsername: "",
      searchUserRole: "",
      pageNum: 10,
      searchData: [],
      tableData: [
        {id: 1, username: "游平杰", userRole: "销售代表",sex:" 男",age:19,departmentName: "销售部", militaryRegionName: "战区1",salary:2000}
      ],
      pageData: {
        page: 1,
        limit: 10
      },
    }
  },
  methods: {
    // 重制搜索框
    reset() {
      this.searchUserRole = ""
      this.searchUsername = ""
    },
    async getTableData() {
      const tableData = (await getUserTableData()).data
      this.tableData = tableData
      console.log(tableData)
      // 分页处理
      var pageList = tableData.slice((this.pageData.page - 1) * this.pageData.limit, this.pageData.page * this.pageData.limit)
      this.pageNum = tableData.length
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

      }
    },
    // 搜索员工
    async search() {
      this.isSearch = true
      var searchData = (await getUserTableData()).data

      if (this.searchUsername !== "") {
        searchData = searchData.filter(data => data.username == this.searchUsername)
        var pageList = searchData.slice((this.pageData.page - 1) * this.pageData.limit, this.pageData.page * this.pageData.limit)
        this.pageNum = searchData.length
        this.tableData = pageList
      }
      if (this.searchUserRole !== "") {
        searchData = searchData.filter(data => data.userRole == this.searchUserRole)
        var pageList = searchData.slice((this.pageData.page - 1) * this.pageData.limit, this.pageData.page * this.pageData.limit)
        this.pageNum = searchData.length
        this.tableData = pageList
      }

      if (this.searchUsername === "" && this.searchUserRole === "") {
        this.isSearch = false
        await this.getTableData()
      }
      this.searchData = searchData
    },
    // 删除行
    handleDelete(row) {
      this.$confirm("是否确定要删除该记录？", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        deleteUserById(row.id)
        this.$message({
          type: 'success',
          message: '删除成功!'
        })
        this.getTableData()
        location.reload()
      }).catch(() => {
        this.$message({type: "info", message: "取消成功！"})
      })
    }
  },
  mounted() {
    this.getTableData()
  }
}
</script>
