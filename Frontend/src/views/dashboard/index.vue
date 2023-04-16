<template>
  <div class="dashboard-container">
    <el-row>
      <!--左侧部分-->
      <!--用户身份-->
      <el-col :span="8" style="padding-right: 10px">
        <el-card class="box-card">
          <div class="user">
            <img :src="require('../../assets/avatar_images/'+avatar.replace('../../assets/avatar_images/',''))" alt="">
            <div class="user-info">
              <p class="name">{{ name }}</p>
              <p class="user_role">{{ user_role }}</p>
            </div>
          </div>
          <div class="login-info">
            <p>上次登录时间：<span>{{ update_time.toString().slice(0, 10) }}</span></p>
            <p>上次登录地点：<span>重庆</span></p>
          </div>
        </el-card>
        <!--表单-->
        <el-card class="table-card">
          <h1 style=" font-size: 20px; text-align: center ; ">金融产品业绩表</h1>
          <el-table
            :data="tableData"
            style="width: 100%">
            <el-table-column v-for="(name,data) in tableLabel" :prop="data" :label="name" :key="name"/>
          </el-table>
        </el-card>
      </el-col>
      <!--右侧部分-->
      <el-col :span="16">
        <div class="num">
          <el-card v-for="item in countData" :key="item.name" :body-style="{display: 'flex' ,padding: 0}">
            <i class="icon" :class="`el-icon-${item.icon}`" :style="{background: item.color}"></i>
            <div class="detail">
              <p class="price">{{ item.value }}</p>
              <p class="desc">{{ item.name }}</p>
            </div>
          </el-card>
        </div>
        <div>
          <el-card style="height: 280px">
            <div ref="echarts1" style="height: 280px"></div>
          </el-card>
        </div>

        <div class="graph">
          <el-card>
            <!--柱状图-->
            <div ref="bar" style="height: 260px"></div>
          </el-card>
          <el-card>
            <!--饼状图-->
            <div ref="pie" style="height: 240px"></div>
          </el-card>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import {mapGetters} from 'vuex'
import * as echarts from "echarts"
import {getPerformance} from "@/api/salesmanRepresentativeCustomer";
import da from "element-ui/src/locale/lang/da";
import {error} from "autoprefixer/lib/utils";
import {getWeekOrMonthProductByUserId, getTotalProductByUserId, getLocalDateList, getXMonthProductByUserId} from "@/api/product";
import {date} from "mockjs/src/mock/random/date";
import {getBarBasedThisMonthByUserId} from "@/api/customer";

export default {
  data() {
    return {
      countData: [],
      tableData: [],
      LineData: {date: [], data: []}, // 更新echarts时在调用函数里更新，保证正常加载
      barData: {}, // 柱状图数据
      pieData: [], // 饼状图数据
      tableLabel: {
        name: "金融产品",
        weekBuy: "本周购买",
        monthBuy: "本月购买",
        totalBuy: "总共购买"
      },
    }
  },
  mounted() {
    // 业绩
    this.getCountData().then(response => {
      this.countData = response
    }).catch(error => {
      console.log(error)
    })
    // 周月销量
    this.getTableData().then(response => {
      this.tableData = response
    }).catch(error => {
      console.log(error)
    })
    // 折线图
    this.getLineData().then(response => {
      const {date, data} = response
      this.LineData.date = date.reverse()
      this.LineData.data = data.reverse()
      const lineChart = echarts.init(this.$refs.echarts1)
      // 组装数据
      var lineOtion = {// 设置标题
        title: {
          text: '金融产品业绩',
          left: 'center'
        }
      }
      // 处理xAxis

      const xAxis = Object.keys(this.LineData.data[0])

      const xLabel = this.LineData.date
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
          data: this.LineData.data.map(item => item[key]),
          type: 'line',
          itemStyle: {normal: {label: {show: true}}}
        })
      })
      // 根据配置显示图表
      lineChart.setOption(lineOtion)
    }).catch(error => {
      console.log(error)
    })

    // 更新柱状图数据
    this.getBarData().then(response => {
      this.barData = response.reverse()
      // 柱状图
      const barChart = echarts.init(this.$refs.bar)
      const barOption = {
        // 设置标题
        title: {
          text: '客户分布',
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
          data: this.barData.map(item => item.date),
          axisLine: {
            lineStyle: {
              color: "#17b3a3",
            },
          },
          axisLabel: {
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
            name: "全部客户",
            data: this.barData.map(item => item.cutsomer_num),
            type: "bar"
          },
          {
            name: "活跃客户",
            data: this.barData.map(item => item.activeCustomerNum),
            type: "bar"
          }
        ],
      }
      barChart.setOption(barOption)
    })

    // 更新饼状图数据
    this.getPieData().then(response => {
      this.pieData = response

      // 饼状图
      const pieChart = echarts.init(this.$refs.pie)
      const pieOption = {
        // 设置标题
        title: {
          text: '金融产品销量分布',
          left: 'center'
        },
        // 设置左侧对应模块标题
        legend: {
          // 垂直，水平
          orient: 'vertical',
          left: 'left'
        },
        tooltip: {
          trigger: "item",
        },
        color: [
          "#0f78f4",
          "#dd536b",
          "#9462e5",
          "#a6a6a6",
          "#e1bb22",
          "#39c362",
          "#3ed1cf",
        ],
        series: [{
          data: this.pieData,
          type: 'pie',
          radius: ['30%', '80%'],
          avoidLabelOverlap: false,
          label: {
            normal: {
              show: true,
              position: "inside",
              formatter: `{d}%`,
            },
          },
          labelLine: {
            normal: {
              show: false,
              length: 0
            }
          }
        }],
      }
      pieChart.setOption(pieOption)
    })
  },

  methods: {
    async getCountData() {
      try {
        await this.$store.dispatch("salesmanRepresentativeCustomer/getPerformance", this.$store.getters.id)
      } catch (error) {
        console.error(error)
      }

      return [{name: "签约客户数", value: this.$store.getters.contract_customers, icon: "success", color: "#2ec7c9",},
        {name: "签约金额", value: this.$store.getters.contract_amount, icon: "star-on", color: "#ffb980",},
        {name: "放款金额", value: this.$store.getters.loan_amount, icon: "s-goods", color: "#5ab1ef",},
        {name: "拨打电话数", value: this.$store.getters.call_times, icon: "success", color: "#2ec7c9",},
        {name: "有效电话数", value: this.$store.getters.valid_calls, icon: "star-on", color: "#ffb980",},
        {name: "意向客户数", value: this.$store.getters.intention_customers, icon: "s-goods", color: "#5ab1ef"}]
    },
    async getTableData() {
      let productList = []
      const weekData = (await getWeekOrMonthProductByUserId(this.$store.getters.id, {isWeek: true})).data
      const monthData = (await getWeekOrMonthProductByUserId(this.$store.getters.id, {isWeek: false})).data
      const totalData = (await getTotalProductByUserId(this.$store.getters.id)).data

      // 封装成一个对象数组
      for (var idx in totalData) {
        if (idx >= monthData.length) {
          productList.push({name: "金融产品" + totalData[idx].productId, weekBuy: 0, monthBuy: 0, totalBuy: totalData[idx].productNum})
        }
        if (idx >= weekData.length) {
          productList.push({name: "金融产品" + totalData[idx].productId, weekBuy: 0, monthBuy: monthData[idx].productNum, totalBuy: totalData[idx].productNum})
        } else {
          productList.push({name: "金融产品" + weekData[idx].productId, weekBuy: weekData[idx].productNum, monthBuy: monthData[idx].productNum, totalBuy: totalData[idx].productNum})
        }
      }

      return productList
    },
    // 折线图数据
    async getLineData() {
      let resData = {}
      const monthSteps = 8
      const time = (await getLocalDateList({monthSteps: monthSteps})).data
      var lineDate = []
      let lineData = []
      // 获得当月到month-monthSteps内的数据
      for (var i = 0; i < monthSteps; i++) {
        const {data} = await getXMonthProductByUserId(this.$store.getters.id, {monthSteps: i}) // 获取该月的product数据

        lineDate.push(time[i].toString().slice(0, 7))
        lineData.push({
          金融产品1: data[0].productNum, 金融产品2: data[1].productNum, 金融产品3: data[2].productNum,
          金融产品4: data[3].productNum, 金融产品5: data[4].productNum, 金融产品6: data[5].productNum
        })
      }
      resData.date = lineDate
      resData.data = lineData

      return resData
    },
    // 获取柱状图数据
    async getBarData() {
      let barData = []

      const monthSteps = 8
      const timeList = (await getLocalDateList({monthSteps: monthSteps})).data // 获取距离当前monthSteps月内的月份字符串数组

      for (var i = 0; i < monthSteps; i++) {
        // 距离本月第i个月的客户数据
        const params = {monthStep: i}
        const customerData = (await getBarBasedThisMonthByUserId(this.$store.getters.id, params)).data
        barData.push({date: timeList[i].slice(5, 7), cutsomer_num: customerData.customerNum, activeCustomerNum: customerData.activeCustomerNum})
      }

      return barData
    },
    // 获取饼状图数据
    async getPieData() {
      let pieData = []
      const {data} = (await getTotalProductByUserId(this.$store.getters.id))

      // 遍历product，添加到pieData
      for (var i = 0; i < data.length; i++) {
        pieData.push({name: "金融产品" + data[i].productId.toString(), value: data[i].productNum})
      }

      return pieData
    },
    getData() {
      return {}
    }
  },

  name: 'Dashboard',

  computed: {
    ...mapGetters([
      'name',
      'user_role',
      'avatar',
      'update_time',
    ])
  }
}
</script>

<!--<style lang="scss" scoped>-->
<!--.dashboard {-->
<!--  &-container {-->
<!--    margin: 30px;-->
<!--  }-->
<!--  &-text {-->
<!--    font-size: 30px;-->
<!--    line-height: 46px;-->
<!--  }-->
<!--}-->
<!--</style>-->

<style lang="less" scoped>
.user {
  padding-bottom: 20px;
  margin-bottom: 20px;
  border-bottom: 1px solid #ccc;
  display: flex;
  align-items: center;

  img {
    margin-right: 40px;
    width: 150px;
    height: 150px;
    border-radius: 50%;
  }

  .user-info {
    .name {
      font-size: 32px;
      margin-bottom: 10px;
    }

    .user_role {
      color: #999999;
    }
  }
}

.login-info {
  p {
    line-height: 28px;
    font-size: 14px;
    color: #999999;

    span {
      color: #666666;
      margin-left: 60px;
    }
  }
}

.table-card {
  margin-top: 20px;
  height: 460px;
}

.num {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;

  .icon {
    width: 80px;
    height: 80px;
    font-size: 30px;
    text-align: center;
    line-height: 80px;
    color: #fff;
  }

  .detail {
    display: flex;
    flex-direction: column;
    margin-left: 15px;
    justify-content: center;

    .price {
      font-size: 30px;
      margin-bottom: 10px;
      line-height: 30px;
      height: 30px;
    }

    .desc {
      font-size: 14px;
      color: #999;
      text-align: center;
    }
  }

  .el-card {
    width: 32%;
    margin-bottom: 20px;
  }
}

.graph {
  display: flex;
  margin-top: 20px;
  justify-content: space-between;
  height: 260px;

  .el-card {
    width: 48%;
  }
}
</style>
