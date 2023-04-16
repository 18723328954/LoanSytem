<template>
  <div class="dashboard-container">
    <!-- 四个统计 -->
    <div class="firstPage">
      <div class="card_wrap">
        <div class="one_card">
          <span>总合同数</span>
          <span>15</span>
        </div>
        <div class="second_card">
          <span>待审批数</span>
          <span>20</span>
        </div>
        <div class="third_card">
          <span>审批通过数</span>
          <span>30</span>
        </div>
        <div class="fourth_card">
          <span>审批未通过数</span>
          <span>40</span>
        </div>
      </div>
    </div>
    <!-- 大的折线图 -->
    <el-row>
      <el-card style="margin-left: 50px; margin-right: 50px;">
        <div ref="line" style="height: 280px; width:100%;"></div>
      </el-card>
    </el-row>
    <!-- 柱状图 -->
    <el-row>
      <el-col :span="12">
        <el-card style="margin-left: 50px; margin-right: 50px; margin-top: 20px;">
          <div ref="callBar" style="height: 280px; width:100%;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card style="margin-left: 50px; margin-right: 50px; margin-top: 20px;">
          <div ref="customerBar" style="height: 280px; width:100%;"></div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import * as echarts from "echarts";
import {getDepartmentCallBarData, getDepartmentCustomerBarData, getDepartmentLineData} from "@/api/department";
import {getMilitaryRegionCallBarData, getMilitaryRegionCustomerBarData, getMilitaryRegionLineData} from "@/api/militaryRegion";

export default {
  data() {
    return {
      lineData: [],
      callBarData: [],
      customerData: [],
    }
  },
  methods: {
    async getLineData() {
      let resData = {}
      var data = (await getMilitaryRegionLineData(this.$store.getters.military_region_id)).data

      var lineLabel = []
      var lineData = []

      for (var i = 0; i < data.length; i++) {
        lineLabel.push(data[i].name)
        lineData.push({签约金额: data[i].contractAmount, 贷款金额: data[i].loanAmount})
      }

      resData.label = lineLabel
      resData.data = lineData
      return resData
    },
    // 电话柱状图
    async getCallBarData() {
      let data = (await getMilitaryRegionCallBarData(this.$store.getters.military_region_id)).data

      return data
    },
    // 意向客户柱状图
    async getCustomerData() {
      let data = (await getMilitaryRegionCustomerBarData(this.$store.getters.military_region_id)).data

      return data
    }
  },
  mounted() {
    // 折线图数据
    this.getLineData().then(response => {
      const {label, data} = response
      this.lineData.label = label
      this.lineData.data = data

      const lineChart = echarts.init(this.$refs.line)
      // 组装数据
      var lineOtion = {// 设置标题
        title: {
          text: '销售代表业绩',
          left: 'center'
        },
        xAxis:{
          axisLabel:{
              rotate: 60 // 设置旋转角度为45度
          }
        }
      }
      // 处理xAxis

      const xAxis = Object.keys(this.lineData.data[0])

      const xLabel = this.lineData.label
      const xAxisData = {
        data: xAxis,
        // 垂直，水平
        orient: 'vertical',
        left: 'right',
        // 图例文字颜色
        textStyle: {
          color: "#333",
        }
      }
      const xLabelData = {
        data: xLabel
      }

      lineOtion.xAxis = xLabelData
      lineOtion.yAxis = {}
      lineOtion.tooltip = {
        trigger: 'axis', //坐标轴触发，主要在柱状图，折线图等会使用类目轴的图表中使用
        axisPointer: {// 坐标轴指示器，坐标轴触发有效
          type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
        }
      }
      lineOtion.legend = xAxisData
      lineOtion.series = []
      xAxis.forEach(key => {
        lineOtion.series.push({
          name: key,
          data: this.lineData.data.map(item => item[key]),
          type: 'line',
          itemStyle: {normal: {label: {show: true}}}
        })
      })

      // 根据配置显示图表
      lineChart.setOption(lineOtion)
    })
    // 电话客户柱状图
    this.getCallBarData().then(response => {
      this.callBarData = response

      // 柱状图
      const callBarChart = echarts.init(this.$refs.callBar)
      const callBarOption = {
        // 设置标题
        title: {
          text: '员工电话情况',
          left: 'center'
        },
        legend: {
          // 垂直，水平
          orient: 'vertical',
          left: 'left',
          // 图例文字颜色
          textStyle: {
            color: "#333",
          },
        },
        grid: {
          left: "20%",
        },
        // 提示框
        tooltip: {
          trigger: "axis",
        },
        xAxis: {
          type: "category", // 类目轴
          data: this.callBarData.map(item => item.name),
          axisLine: {
            lineStyle: {
              color: "#17b3a3",
            },
          },
          axisLabel: {
            rotate: 60, // 设置旋转角度为45度
            interval: 0,
            color: "#333",
          },
        },
        yAxis: [
          {
            type: "value",
            axisLine: {
              lineStyle: {
                color: "#17b3a3",
              },
            },
          },
        ],
        color: ["#2ec7c9", "#b6a2de"],
        series: [
          {
            name: "总电话数",
            data: this.callBarData.map(item => item.callTimes),
            type: "bar"
          },
          {
            name: "有效电话数",
            data: this.callBarData.map(item => item.validCalls),
            type: "bar"
          }
        ],
      }
      callBarChart.setOption(callBarOption)
    })
    // 客户柱状图
    this.getCustomerData().then(response => {
      this.customerData = response
      // 柱状图
      const customerBarChart = echarts.init(this.$refs.customerBar)
      const customerBarOption = {
        // 设置标题
        title: {
          text: '员工电话情况',
          left: 'center'
        },
        legend: {
          // 垂直，水平
          orient: 'vertical',
          left: 'left',
          // 图例文字颜色
          textStyle: {
            color: "#333",
          },
        },
        grid: {
          left: "20%",
        },
        // 提示框
        tooltip: {
          trigger: "axis",
        },
        xAxis: {
          type: "category", // 类目轴
          data: this.customerData.map(item => item.name),
          axisLine: {
            lineStyle: {
              color: "#17b3a3",
            },
          },
          axisLabel: {
            rotate: 60, // 设置旋转角度为45度
            interval: 0,
            color: "#333",
          },
        },
        yAxis: [
          {
            type: "value",
            axisLine: {
              lineStyle: {
                color: "#17b3a3",
              },
            },
          },
        ],
        color: ["#2ec7c9", "#b6a2de"],
        series: [
          {
            name: "采访客户",
            data: this.customerData.map(item => item.interviewCustomers),
            type: "bar"
          },
          {
            name: "意向客户",
            data: this.customerData.map(item => item.intentionCustomers),
            type: "bar"
          }
        ],
      }
      customerBarChart.setOption(customerBarOption)
    })
  },
}
</script>

<style scoped lang="less">
.firstPage {
  background-color: #fff;
  padding: 20px;
  min-height: 100%;
  box-sizing: border-box;

  .card_wrap {
    display: flex;
    justify-content: space-around;
    width: 100%;
    height: 20%;

    div {
      width: 23%;
      padding: 20px;
      box-sizing: border-box;
      background-color: rgb(166, 179, 179);
      border-radius: 10px;
      font-size: 30px;
      color: #fff;
      font-weight: 900;
      display: flex;
      align-items: center;
      justify-content: center;
      flex-direction: column;
    }

    .one_card {
      background-color: rgb(212, 224, 96);
    }

    .second_card {
      background-color: rgb(126, 224, 96);
    }

    .third_card {
      background-color: rgb(96, 183, 224);
    }

    .fourth_card {
      background-color: rgb(96, 119, 224);
    }
  }
}
</style>
