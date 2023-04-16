<template>
  <div class="navbar">
    <hamburger :is-active="sidebar.opened" class="hamburger-container" @toggleClick="toggleSideBar"/>

    <breadcrumb class="breadcrumb-container"/>

    <div class="right-menu">
      <el-dropdown class="avatar-container" trigger="click">
        <div class="avatar-wrapper">
          <img :src="require('../../assets/avatar_images/'+avatar.replace('../../assets/avatar_images/',''))" class="user-avatar" alt="">
          <i class="el-icon-caret-bottom"/>
        </div>
        <el-dropdown-menu slot="dropdown" class="user-dropdown">
          <router-link to="/">
            <el-dropdown-item>
              首页
            </el-dropdown-item>
          </router-link>
          <!--          <router-link to="/profile/index">-->
          <router-link to="/">
            <el-dropdown-item @click.native="test">
              个人中心
            </el-dropdown-item>
          </router-link>
          <el-dropdown-item divided @click.native="logout">
            <span style="display:block;">退出系统</span>
          </el-dropdown-item>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
  </div>
</template>

<script>
import {mapGetters} from 'vuex'
import Breadcrumb from '@/components/Breadcrumb'
import Hamburger from '@/components/Hamburger'
import {getCurrentTime, logout, updateUserById} from "@/api/user";
import {getToken, removeToken, clearStorage} from "@/utils/auth"
import {getUserById} from "@/api/user";
import da from "element-ui/src/locale/lang/da";
import {data} from "autoprefixer";
import {getLocalDateList, getProductByUserId, getTotalProductByUserId, getWeekOrMonthByUserId, getXMonthProductByUserId} from "@/api/product";
import {findCustomerByName, getBarBasedThisMonthByUserId, getCustomerInfoByUserId, insertCustomer, updateCustomerById} from "@/api/customer";
import {getEconomyTableData, getTableData, insertOrUpdateContract} from "@/api/contract";
import {insertOrUpdatePerformance} from "@/api/performance";
import {insertOrUpdateSalesmanRepresentativeCustomer} from "@/api/salesmanRepresentativeCustomer";

export default {
  components: {
    Breadcrumb,
    Hamburger
  },
  computed: {
    ...mapGetters([
      'sidebar',
      'avatar',
      'contract_customers',
      'contract_amount',
      'loan_amount',
      'call_times',
      'valid_calls',
      'intention_customers'
    ])
  },
  methods: {
    // 测试方法
    async test() {
      await getEconomyTableData()
    },
    toggleSideBar() {
      this.$store.dispatch('app/toggleSideBar')
    },
    async logout() {
      this.$confirm("确定退出系统？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(async () => {
        // 动态获取当前时间，来更新user中的updateTime
        // let currentDate
        // await getCurrentTime().then(response=>{
        //   const {data} = response
        //   currentDate = data
        // }).catch(error=>{
        //   console.log(error)
        // })

        const currentDate = new Date()
        console.log(currentDate)
        const data = {updateTime: currentDate}
        console.log(data)
        await updateUserById(this.$store.state.user.id, data)

        // 退出界面
        let params = {token: getToken()} // 请求参数
        let res = await logout(params) // 发送退出请求

        // 判断是否成功
        if (res.success) {
          // 清空token
          removeToken()
          clearStorage()

          // 跳转页面
          //this.$router.push(`/login?redirect=${this.$route.fullPath}`)
          window.location.href = "/login"
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.navbar {
  height: 50px;
  overflow: hidden;
  position: relative;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, .08);

  .hamburger-container {
    line-height: 46px;
    height: 100%;
    float: left;
    cursor: pointer;
    transition: background .3s;
    -webkit-tap-highlight-color: transparent;

    &:hover {
      background: rgba(0, 0, 0, .025)
    }
  }

  .breadcrumb-container {
    float: left;
  }

  .right-menu {
    float: right;
    height: 100%;
    line-height: 50px;

    &:focus {
      outline: none;
    }

    .right-menu-item {
      display: inline-block;
      padding: 0 8px;
      height: 100%;
      font-size: 18px;
      color: #5a5e66;
      vertical-align: text-bottom;

      &.hover-effect {
        cursor: pointer;
        transition: background .3s;

        &:hover {
          background: rgba(0, 0, 0, .025)
        }
      }
    }

    .avatar-container {
      margin-right: 30px;

      .avatar-wrapper {
        margin-top: 5px;
        position: relative;

        .user-avatar {
          cursor: pointer;
          width: 40px;
          height: 40px;
          border-radius: 10px;
        }

        .el-icon-caret-bottom {
          cursor: pointer;
          position: absolute;
          right: -20px;
          top: 25px;
          font-size: 12px;
        }
      }
    }
  }
}
</style>
