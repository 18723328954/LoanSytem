<template>
  <div class="app-container">
    <el-dialog
      title="添加后不可更改，请谨慎输入！"
      :visible.sync="dialogVisible"
      width="50%"
      :before-close="handleClose">
      <!-- 用户表单信息 -->
      <el-form ref="form" :rules="rule" :inline="true" :model="loanForm" label-width="120px">
        <el-form-item label="本次放款金额" prop="loanAmount">
          <el-input v-model="loanForm.loanAmount" placeholder="请输入客户贷款金额"></el-input>
        </el-form-item>
      </el-form>

      <span slot="footer" class="dialog-footer">
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
            <el-form-item label="销售姓名" prop="salesmanName">
              <el-input v-model.trim="searchForm.salesmanName"
                        placeholder="请输入销售姓名"
                        maxlength="15"
                        clearable/>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">

          <el-col :span="8">
            <el-form-item label="起始时间">
              <el-date-picker ref="startPikcer"
                              v-model="searchForm.startTime"
                              type="date"
                              placeholder="请选择日期"
                              style="width: 100%;">
              </el-date-picker>
            </el-form-item>
          </el-col>

          <el-col :span="8">
            <el-form-item label="最晚时间">
              <el-date-picker ref="endPikcer"
                              v-model="searchForm.endTime"
                              type="date"
                              placeholder="请选择日期"
                              style="width: 100%;">
              </el-date-picker>
            </el-form-item>
          </el-col>

          <el-col :span="8" class="text-right">
            <el-button type="success" @click="search">查询</el-button>
            <el-button type="warning" @click="resetForm">重置</el-button>
          </el-col>
        </el-row>

      </el-form>
    </div>

    <div class="contractInformation" style="margin-top: 20px;">
      <div class="manage-table">
        <el-table :data="tableData">
          <el-table-column label="合同编号" prop="contractNo"></el-table-column>
          <el-table-column label="客户姓名" prop="customerName"></el-table-column>
          <el-table-column label="销售姓名" prop="salesmanName"></el-table-column>
          <el-table-column label="贷款期限" prop="loanTerm"></el-table-column>
          <el-table-column label="放款金额" prop="loanAmount"></el-table-column>
          <el-table-column label="签约金额" prop="contractAmount"></el-table-column>
          <el-table-column label="创建时间" prop="createTime"></el-table-column>
          <el-table-column label="更新时间" prop="updateTime"></el-table-column>
          <el-table-column label="操作">
            <template slot-scope="scope" class="button-container">
              <el-button type="success" size="mini" @click="$event=>loan(scope.row)">发放贷款</el-button>
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
import {deleteRow, getEconomyTableData, getLastContract, getSalesmanIdByContractNo, getTableData, insertOrUpdateContract} from "@/api/contract";
import {findCustomerByName, insertCustomer, updateCustomerById} from "@/api/customer";
import th from "element-ui/src/locale/lang/th";
import {insertOrUpdateSalesmanRepresentativeCustomer} from "@/api/salesmanRepresentativeCustomer";
import {insertOrUpdatePerformance} from "@/api/performance";

export default {
  data() {
    return {
      dialogVisible: false,
      isSearch: false,
      contractNo:"",
      loanForm: {
        loanAmount: "0"
      },
      rule: {
        loanAmount: [{required: true, message: "请输入本次放款金额"}]
      },
      searchForm: {
        contractNo: "",
        customerName: "",
        salesmanName: "",
        approveStatus: "",
        startTime: "",
        endTime: ""
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
        customerName: "ypj",
        salesmanName: "youpingjie",
        loanTerm: "5年",
        contractAmount: 50000.0,
        approveStatus: 0,
        approveStatusTag: <el-tag type="success">通过</el-tag>,
        createTime: new Date().toString().slice(0, 10),
        updateTime: Date().toString().slice(0, 10)
      }], // 当页显示的数据
      pageNum: 1, // 总共的客户数量
      pageData: {
        page: 1,
        limit: 6
      },
    }
  },
  methods: {
    async submit() {
      // 更新合同表
      const contract = {contractNo:this.contractNo,loanAmount:this.loanForm.loanAmount}
      await insertOrUpdateContract(contract)
      // 更新销售代表业绩
      var salesmanId = (await getSalesmanIdByContractNo(this.contractNo)).data
      const performance = {salesmanId: salesmanId, loanAmount: this.loanForm.loanAmount}
      await insertOrUpdatePerformance(performance)

      this.dialogVisible = false
    },
    resetForm() {
      this.$refs.searchForm.resetFields()
      this.searchForm.startTime = ""
      this.searchForm.endTime = ""
    },

    async getEconomyTableData() {
      var tableData = (await getEconomyTableData()).data
      // 修改审批状态和日期
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
    },
    // 选择页码的回调函数
    async handlePage(val) {
      this.pageData.page = val

      // 是否为搜索模式
      if (this.isSearch === false) {
        await this.getEconomyTableData()
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
      var searchData = (await getEconomyTableData()).data

      // 改变显示方式
      for (var i = 0; i < searchData.length; i++) {
        searchData[i].createTime = searchData[i].createTime.slice(0, 10)
        searchData[i].updateTime = searchData[i].updateTime.slice(0, 10)
      }

      if (this.searchForm.contractNo != "" || this.searchForm.customerName != "" || this.searchForm.salesmanName != "" || this.searchForm.approveStatus !== "" || this.searchForm.startTime != "" || this.searchForm.endTime != "") {
        // 查询非空的条件
        if (this.searchForm.contractNo != "") {
          searchData = searchData.filter(data => data.contractNo == this.searchForm.contractNo)
        }
        if (this.searchForm.customerName != "") {
          searchData = searchData.filter(data => data.customerName == this.searchForm.customerName)
        }
        if (this.searchForm.salesmanName != "") {
          searchData = searchData.filter(data => data.salesmanName == this.searchForm.salesmanName)
        }

        if (this.searchForm.startTime != "") {
          var date = new Date(this.searchForm.startTime)
          var dateTime = date.getTime()

          searchData = searchData.filter(data => new Date(data.createTime).getTime() > dateTime)
        }

        if (this.searchForm.endTime != "") {
          var date = new Date(this.searchForm.endTime)
          var dateTime = date.getTime()

          searchData = searchData.filter(data => new Date(data.createTime).getTime() < dateTime)
        }

        var pageList = searchData.slice((this.pageData.page - 1) * this.pageData.limit, this.pageData.page * this.pageData.limit)
        this.pageNum = searchData.length
        this.tableData = pageList
      }
      // 啥都没有，直接获取全部数据
      else {
        this.isSearch = false
        await this.getEconomyTableData()
      }
      return searchData
    },
    async loan(row) {
      this.dialogVisible = true
      this.contractNo = row.contractNo

      // var contractParams = {contractNo:row.contractNo,approveStatus: 1}
      // await insertOrUpdateContract(contractParams)
      // var salesmanId = (await getSalesmanIdByContractNo(row.contractNo)).data
      // var performanceParams = {salesmanId:salesmanId,contractNo:row.contractNo,contractAmount:row.contractAmount}
      // await insertOrUpdatePerformance(performanceParams)
      // location.reload()
    },
    refuse(row) {
      var contractParams = {contractNo: row.contractNo, approveStatus: 2}
      insertOrUpdateContract(contractParams)
      location.reload()
    }
  },
  mounted() {
    this.getEconomyTableData()
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
