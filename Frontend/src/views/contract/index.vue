<template>
  <div class="app-container">
    <el-dialog
      title="添加后不可更改，请谨慎输入！"
      :visible.sync="dialogVisible"
      width="50%"
      :before-close="handleClose">
      <!-- 用户表单信息 -->
      <el-form ref="form" :rules="rules" :inline="true" :model="addForm" label-width="80px">
        <el-form-item label="客户姓名" prop="name">
          <el-input v-model="addForm.name" placeholder="请输入客户姓名"></el-input>
        </el-form-item>
        <el-form-item label="贷款期限" prop="loanTerm">
          <el-input v-model="addForm.loanTerm" placeholder="请输入客户贷款期限"></el-input>
        </el-form-item>
        <el-form-item label="服务费" prop="serviceFee">
          <el-input v-model="addForm.serviceFee" placeholder="请输入客户服务费"></el-input>
        </el-form-item>
        <el-form-item label="签约金额" prop="contractAmount">
          <el-input v-model="addForm.contractAmount" placeholder="请输入客户贷款金额"></el-input>
        </el-form-item>
      </el-form>

      <span slot="footer" class="dialog-footer">
          <el-button @click="resetDialogForm">重 置</el-button>
          <el-button type="primary" @click="submit">确 定</el-button>
      </span>
    </el-dialog>


    <div class="contractInformation">
      <!-- 搜索框 -->
      <el-form ref="searchForm"
               :model="searchForm"
               label-width="130px"
               @keyup.enter.native="handleSearch">
        <el-row :gutter="24">
          <el-col :span="8">
            <el-form-item label="合同编号" prop="contractNo">
              <el-input v-model.trim="searchForm.contractNo"
                        placeholder="请输入合同编号"
                        maxlength="15"
                        clearable/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="客户姓名" prop="customerName">
              <el-input v-model.trim="searchForm.customerName"
                        placeholder="请输入客户姓名"
                        maxlength="15"
                        clearable/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="审批状态" prop="approveStatus">
              <el-select v-model="searchForm.approveStatus" placeholder="请选择审批状态">
                <el-option v-for="item in options" :key="item.value" v-html:label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="8">
            <el-form-item label="创建时间">
              <el-date-picker v-model="searchForm.createTime"
                              type="date"
                              placeholder="请选择日期"
                              style="width: 100%;">
              </el-date-picker>
            </el-form-item>
          </el-col>

          <el-col :span="16" class="text-right">
            <el-button type="success" @click="search">查询</el-button>
            <el-button type="warning" @click="resetForm">重置</el-button>
            <el-button type="primary" @click="add">新增</el-button>
          </el-col>
        </el-row>

      </el-form>
    </div>

    <div class="contractInformation" style="margin-top: 20px;">
      <div class="manage-table">
        <el-table :data="tableData">
          <el-table-column label="合同编号" prop="contractNo"></el-table-column>
          <el-table-column label="客户编号" prop="customerId"></el-table-column>
          <el-table-column label="客户姓名" prop="customerName"></el-table-column>
          <el-table-column label="贷款期限" prop="loanTerm"></el-table-column>
          <el-table-column label="服务费 " prop="serviceFee"></el-table-column>
          <el-table-column label="放款金额" prop="loanAmount"></el-table-column>
          <el-table-column label="签约金额" prop="contractAmount"></el-table-column>
          <el-table-column label="审批状态" prop="approveStatusTag"></el-table-column>
          <el-table-column label="创建时间" prop="createTime"></el-table-column>
          <el-table-column label="更新时间" prop="updateTime"></el-table-column>
          <el-table-column label="操作">
            <template slot-scope="scope">
              <!--              <el-button type="warning" size="mini" @click="$event=>editContract(scope.row)">编辑</el-button>-->
              <el-button type="danger" size="mini" @click="$event=>deleteContract(scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>

    <div>
      <el-pagination layout="prev, pager, next" :total="pageNum" :page-size="pageData.limit" @current-change="handlePage">
      </el-pagination>
    </div>
  </div>
</template>

<script>
import {deleteRow, getLastContract, getTableData, insertOrUpdateContract} from "@/api/contract";
import {findCustomerByName, insertCustomer, updateCustomerById} from "@/api/customer";
import th from "element-ui/src/locale/lang/th";
import {insertOrUpdateSalesmanRepresentativeCustomer} from "@/api/salesmanRepresentativeCustomer";
import {insertOrUpdatePerformance} from "@/api/performance";

export default {
  data() {
    return {
      modal: false, // true按钮触发添加，false按钮触发编辑
      dialogVisible: false, // 显示新增框
      addForm: {
        name: "",
        loanTerm: "",
        serviceFee: "",
        contractAmount: ""
      },
      // 校验规则
      rules: {
        name: [{required: true, message: "请输入客户姓名"}],
        loanTerm: [{required: true, message: "请输入客户贷款期限"}],
        serviceFee: [{required: true, message: "请输入客户服务费"}],
        contractAmount: [{required: true, message: "请输入客户贷款金额"}],
      },
      searchForm: {
        contractNo: "",
        customerName: "",
        approveStatus: "",
        createTime: ""
      },
      options: [
        {value: 0, label: "<span style='color:orange'>待审<span>"},
        {value: 1, label: "<span style='color:green'>通过<span>"},
        {value: 2, label: "<span style='color:red'>拒绝<span>"}
      ],
      indexToTag: [
        "<span style='color:orange'>待审<span>",
        "<span style='color:green'>通过<span>",
        "<span style='color:red'>拒绝<span>"
      ]
      ,
      tableData: [{
        contractNo: 1,
        customerId: 1,
        customerName: "ypj",
        loanTerm: "5年",
        serviceFee: 500.0,
        loanAmount: 50000.0,
        contractAmount: 50000.0,
        approveStatus: 0,
        approveStatusTag: <el-tag type="success">通过</el-tag>,
        createTime: new Date().toString().slice(0, 10),
        updateTime: Date().toString().slice(0, 10)
      }], // 当页显示的数据
      pageNum: 1, // 总共的客户数量
      pageData: {
        page: 1,
        limit: 7
      },
      isSearch: false, // 是否处于搜索状态
    }
  },
  methods: {
    // 提交确认框
    async submit() {
      await this.$refs.form.validate((valid) => {
        if (valid) {
          // 添加合同
          if (this.modal)
            // console.log(this.addForm)
            this.addContract()
          else
            this.editContract()
        }
      })
    },
    // 新增
    add() {
      this.modal = true
      this.dialogVisible = true
    },
    // 编辑合同
    async editContract(row) {

    },
    // 新建合同
    async addContract() {
      var customerId
      var contractNo
      var approveStatus = 0

      // 更新客户表
      const customerInfo = (await findCustomerByName({name: this.addForm.name})).data // 获取客户信息，判断是否存在

      if (customerInfo.length == 0) {
        // 没有客户信息，则新建，并且获取客户id
        await insertCustomer({name: this.addForm.name, salesmanId: this.$store.getters.id})
        customerId = (await findCustomerByName({name: this.addForm.name})).data[0].id
      } else {
        // 有客户信息
        customerId = customerInfo[0].id // 获取客户信息
        await updateCustomerById(customerId, {name: this.addForm.name, salesmanId: this.$store.getters.id})
      }
      // 更新合同表
      const contract = {contractAmount: this.addForm.contractAmount, serviceFee: this.addForm.serviceFee, loanTerm: this.addForm.loanTerm, approveStatus: approveStatus, approveId: this.$store.getters.id}
      await insertOrUpdateContract(contract)
      const lastContract = (await getLastContract()).data
      contractNo = lastContract.contractNo

      // 更新映射表
      const salesmanRepresentativeCustomer = {salesmanId: this.$store.getters.id, customerId: customerId, contractNo: contractNo}
      await insertOrUpdateSalesmanRepresentativeCustomer(salesmanRepresentativeCustomer)

      // 更新销售代表业绩表
      var performance = {salesmanId: this.$store.getters.id, serviceFee:this.addForm.serviceFee}
      if (approveStatus === 1) {
        performance.contractAmount = this.addForm.contractAmount
      }
      await insertOrUpdatePerformance(performance)

      this.dialogVisible = false
      location.reload()
    },
    // 删除行
    async deleteContract(row) {
      // 成功状态下不可删除
      if (row.approveStatus != 1) {
        await deleteRow(row.contractNo)
        location.reload()
      }
    },
    resetForm() {
      this.$refs.searchForm.resetFields()
      this.searchForm.createTime = ""
    },
    resetDialogForm() {
      this.$refs.form.resetFields()
    }
    ,
    async getContractTableData() {
      var tableData = (await getTableData(this.$store.getters.id)).data;

      for (var i = 0; i < tableData.length; i++) {
        tableData[i].createTime = tableData[i].createTime.slice(0, 10)
        tableData[i].updateTime = tableData[i].updateTime.slice(0, 10)
        if (tableData[i].approveStatus === 0) {
          tableData[i].approveStatusTag = <el-tag type="warning">待审</el-tag>
        } else if (tableData[i].approveStatus == 1) {
          tableData[i].approveStatusTag = <el-tag type="success">通过</el-tag>
        } else {
          tableData[i].approveStatusTag = <el-tag type="danger">拒绝</el-tag>
        }
      }

      var pageList = tableData.slice((this.pageData.page - 1) * this.pageData.limit, this.pageData.page * this.pageData.limit)
      this.pageNum = tableData.length
      this.tableData = pageList
      console.log(pageList)
    },
    // 选择页码的回调函数
    async handlePage(val) {
      this.pageData.page = val

      // 是否为搜索模式
      if (this.isSearch === false) {
        await this.getContractTableData()
        this.isSearch = false // 关闭搜索状态
      } else {
        const data = this.search()
        var pageList = data.slice((this.pageData.page - 1) * this.pageData.limit, this.pageData.page * this.pageData.limit)
        for (var i = 0; i < data.length; i++) {
          data[i].createTime = data[i].createTime.slice(0, 10)
          data[i].updateTime = data[i].updateTime.slice(0, 10)
        }
        this.pageNum = data.length
        this.tableData = pageList
      }
    },
    // 搜索合同
    async search() {
      this.isSearch = true
      var searchData = (await getTableData(this.$store.getters.id)).data

      for (var i = 0; i < searchData.length; i++) {
        searchData[i].createTime = searchData[i].createTime.slice(0, 10)
        searchData[i].updateTime = searchData[i].updateTime.slice(0, 10)
        if (searchData[i].approveStatus === 0) {
          searchData[i].approveStatusTag = <el-tag type="warning">待审</el-tag>
        } else if (searchData[i].approveStatus == 1) {
          searchData[i].approveStatusTag = <el-tag type="success">通过</el-tag>
        } else {
          searchData[i].approveStatusTag = <el-tag type="danger">拒绝</el-tag>
        }
      }

      if (this.searchForm.contractNo != "" || this.searchForm.customerName != "" || this.searchForm.approveStatus !== "" || this.searchForm.createTime != "") {
        // 查询非空的条件
        if (this.searchForm.contractNo != "") {
          searchData = searchData.filter(data => data.contractNo == this.searchForm.contractNo)
        }
        if (this.searchForm.customerName != "") {
          searchData = searchData.filter(data => data.customerName == this.searchForm.customerName)
        }

        if (this.searchForm.approveStatus !== "") {
          searchData = searchData.filter(data => data.approveStatus == this.searchForm.approveStatus)
        }
        if (this.searchForm.createTime != "") {
          var date = new Date(this.searchForm.createTime)
          // 获取年、月、日
          const year = date.getFullYear();
          const month = (date.getMonth() + 1).toString().padStart(2, '0');
          const day = date.getDate().toString().padStart(2, '0');

          // 拼接日期字符串
          const time = `${year}-${month}-${day}`;
          searchData = searchData.filter(data => data.createTime == time)
        }

        var pageList = searchData.slice((this.pageData.page - 1) * this.pageData.limit, this.pageData.page * this.pageData.limit)
        this.pageNum = searchData.length
        this.tableData = pageList
      } else {
        this.isSearch = false
        await this.getContractTableData()
      }
      return searchData
    },
  },
  mounted() {
    this.getContractTableData()
  }
}

</script>

<style lang="less" scope>
.contractInformation {
  background: #fff;
  padding: 20px;
}

.text-right {
  text-align: right;
}

.el-select {
  width: 100%;
}
</style>
