<template>
  <div class="manage">
    <!-- 弹窗 -->
    <el-dialog
      title="提示"
      :visible.sync="dialogVisible"
      width="50%"
      :before-close="handleClose">
      <!-- 用户表单信息 -->
      <el-form ref="form" :rules="rules" :inline="true" :model="addForm" label-width="80px">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="addForm.name" placeholder="请输入客户姓名"></el-input>
        </el-form-item>
        <el-form-item label="身份证号" prop="idCard">
          <el-input v-model="addForm.idCard" placeholder="请输入客户身份证号"></el-input>
        </el-form-item>
        <el-form-item label="单位" prop="companyName">
          <el-input v-model="addForm.companyName" placeholder="请输入客户单位"></el-input>
        </el-form-item>
        <el-form-item label="营业执照" prop="businessLicense">
          <el-input v-model="addForm.businessLicense" placeholder="请输入客户营业执照编号"></el-input>
        </el-form-item>
        <el-form-item label="意向分数" prop="intensionScore">
          <el-input v-model="addForm.intensionScore" placeholder="请输入客户意向分数"></el-input>
        </el-form-item>
      </el-form>

      <span slot="footer" class="dialog-footer">
          <el-button @click="resetForm">重 置</el-button>
          <el-button type="primary" @click="submit">确 定</el-button>
      </span>
    </el-dialog>

    <div class="manage-header">
      <!-- 添加按钮 -->
      <el-button type="primary" @click="dialogVisible = true; modal=true"> 添加客户</el-button>
      <!-- 搜索框 -->
      <el-form :model="searchForm" :inline="true">
        <el-form-item>
          <el-input placeholder="请输入客户名称" v-model="searchForm.name"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="success" @click="searchName">客户查询</el-button>
        </el-form-item>
      </el-form>
    </div>
    <!-- 客户表格 -->
    <div class="manage-table">
      <el-table :data="tableData">
        <el-table-column label="客户编号" prop="id"></el-table-column>
        <el-table-column label="姓名" prop="name"></el-table-column>
        <el-table-column label="身份证号" prop="idCard"></el-table-column>
        <el-table-column label="客户单位" prop="companyName"></el-table-column>
        <el-table-column label="营业执照编号" prop="businessLicense"></el-table-column>
        <el-table-column label="贷款意向分" prop="intensionScore"></el-table-column>
        <el-table-column label="创建时间" prop="createTime"></el-table-column>
        <el-table-column label="更新时间" prop="updateTime"></el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button type="warning" size="mini" @click="$event => handleEdit(scope.row)">编辑</el-button>
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

export default {
  data() {
    return {
      modal: true, // true按钮触发添加，false按钮触发更新
      dialogVisible: false,
      addForm: {
        id: "",
        name: "",
        idCard: "",
        companyName: "",
        businessLicense: "",
        intensionScore: "",
        createTime: new Date(),
        updateTime: new Date(),
      },
      // 校验规则
      rules: {
        id: [{required: true, message: "请输入客户编号"}],
        name: [{required: true, message: "请输入客户姓名"}],
        idCard: [{required: true, message: "请输入客户身份证号"}],
        companyName: [{required: true, message: "请输入客户单位"}],
        businessLicense: [{required: true, message: "请输入客户营业执照编号"}],
        intensionScore: [{required: true, message: "请输入客户意向分数"}],
        // createTime: [{required: true, message: "请输入客户身份证号"}],
        // updateTime: [{required: true, message: "请输入客户身份证号"}],
      },
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
     * 将新增的用户信息提交给data中的addForm，并上传后端
     * @returns {Promise<void>}
     */
    async submit() {
      await this.$refs.form.validate((valid) => {
        if (valid) {
          // 添加用户
          if (this.modal) {
            this.addCustomer()
          } else {
            this.updateCustomer()
          }
        }
      })
    },
    /**
     * 表单的值添加到数据库
     * @returns {Promise<void>}
     */
    async addCustomer() {
      let insertCustomer = this.addForm
      insertCustomer.salesmanId = this.$store.getters.id
      await this.insertCustomer(insertCustomer) // 传入数据库
      await this.getTableData() // 重新获取表格数据
      this.dialogVisible = false
      location.reload() // 刷新页面
    },
    /**
     * 将表单的值更新
     * @returns {Promise<void>}
     */
    async updateCustomer() {
      this.addForm.updateTime = new Date()
      await updateCustomerById(this.addForm.id, this.addForm)
      this.dialogVisible = false
      location.reload()
    },
    /**
     * 按客户name搜索
     * @returns {Promise<void>}
     */
    async searchName() {
      if (this.searchForm.name != "") {
        this.isSearch = true // 开启搜索状态，即分页看到的是name相同的
        const data = (await findCustomerByName({name: this.searchForm.name})).data
        for (var i = 0; i < data.length; i++) {
          data[i].createTime = data[i].createTime.slice(0, 10)
          data[i].updateTime = data[i].updateTime.slice(0, 10)
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
     * 对话框重置信息
     */
    resetForm() {
      this.$refs.form.resetFields()
    },
    /**
     * 对话框关闭处理
     */
    handleClose() {
      this.dialogVisible = false
      console.log("closed!")
    },
    /**
     * 删除改行customer
     * @param row
     */
    handleDelete(row) {
      this.$confirm("是否确定要删除该记录？", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        deleteCustomerByCustomerId(row.id)
        this.$message({
          type: 'success',
          message: '删除成功!'
        })
        this.getTableData()
        location.reload()
      }).catch(() => {
        this.$message({type: "info", message: "取消成功！"})
      })
    },
    /**
     * 编辑客户信息
     * @param row
     */
    async handleEdit(row) {
      this.dialogVisible = true
      this.modal = false
      this.addForm = JSON.parse(JSON.stringify(row))
    },
    /**
     * 获取表格数据，
     * @param limited 当页限制的客户数量
     * @returns {Promise<void>}
     */
    async getTableData() {
      const {data} = (await getCustomerInfoByUserId(this.$store.getters.id))

      for (var i = 0; i < data.length; i++) {
        data[i].createTime = data[i].createTime.slice(0, 10)
        data[i].updateTime = data[i].updateTime.slice(0, 10)
      }
      var pageList = data.slice((this.pageData.page - 1) * this.pageData.limit, this.pageData.page * this.pageData.limit)
      this.pageNum = data.length
      this.tableData = pageList
    },
    /**
     * 新增客户细腻些
     * @param customer  客户信息
     * @returns {Promise<void>}
     */
    async insertCustomer(customer) {
      await insertCustomer(customer)
    },
    // 选择页码的回调函数
    async handlePage(val) {
      this.pageData.page = val
      // 是否为搜索模式
      if (this.isSearch === false) {
        await this.getTableData()
        this.isSearch = false // 关闭搜索状态
      } else {
        const data = (await findCustomerByName({name: this.searchForm.name})).data
        var pageList = data.slice((this.pageData.page - 1) * this.pageData.limit, this.pageData.page * this.pageData.limit)
        for (var i = 0; i < data.length; i++) {
          data[i].createTime = data[i].createTime.slice(0, 10)
          data[i].updateTime = data[i].updateTime.slice(0, 10)
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
