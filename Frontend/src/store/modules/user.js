import {login, logout, getInfo, getCurrentTime} from '@/api/user'
import {getToken, setToken, removeToken} from '@/utils/auth'
import {adminRoutes, departmentRoutes, economyRoutes, financeRoutes, managerRoutes, militaryRoutes, resetRouter, salesmanRoutes} from '@/router'
import {asyncRoutes, constantRoutes} from "@/router";
import Layout from "@/layout/index.vue";
import da from "element-ui/src/locale/lang/da";
import ro from "element-ui/src/locale/lang/ro";
import {exist, insertOrUpdatePerformance} from "@/api/performance";
import {getSalesmanIdByContractNo} from "@/api/contract";

const getDefaultState = () => {
  return {
    token: getToken(),
    id: '',
    name: '',
    avatar: '',
    role: '',
    user_role: '',
    department_id: '',
    military_region_id: '',
    route: '',
    update_time: '',
    first_login: false,
  }
}

const state = getDefaultState()

const mutations = {
  RESET_STATE: (state) => {
    Object.assign(state, getDefaultState())
  },
  SET_TOKEN: (state, token) => {
    state.token = token
  },
  SET_NAME: (state, name) => {
    state.name = name
  },
  SET_AVATAR: (state, avatar) => {
    state.avatar = avatar
  },
  SET_ROLEID: (state, roleId) => {
    state.role = roleId
  },
  SET_USER_ROLE: (state, user_role) => {
    state.user_role = user_role
  },
  SET_ROUTE: (state, route) => {
    state.route = route
  },
  SET_ID: (state, id) => {
    state.id = id
  },
  SET_UPDATE_TIME: (state, update_time) => {
    state.update_time = update_time
  },
  SET_DEPARTMENT_ID: (state, department_id) => {
    state.department_id = department_id
  },
  SET_MILITARY_REGION_ID: (state, military_region_id) => {
    state.military_region_id = military_region_id
  },
  SET_FIRST_LOGIN:(state,first_login) => {
    state.first_login = first_login
  }
}

const actions = {
  // 用户登录方法
  async login({commit}, userInfo) {
    const {username, password} = userInfo // 从loginForm中解构用户名和密码
    // 对异步操作进行处理
    return new Promise((resolve, reject) => {
      // 调用axios中的login方法，即发起post请求
      login({username: username.trim(), password: password}).then(response => {
        const {token} = response // JWT token
        commit('SET_TOKEN', token) // 更新vuex中的token状态
        setToken(token) // 更新cookie中token
        resolve() // 放行
      }).catch(error => {
        reject(error) // 拦截
      })
    })
  },

  firstLogin({commit,state}) {
    return new Promise((resolve,reject) => {
        commit('SET_FIRST_LOGIN',true)
        resolve()
    })
  },

  // 查看用户信息
  getInfo({commit, state}) {
    return new Promise((resolve, reject) => {
      // 调用axios中的getInfo方法，即发起get请求
      getInfo(state.token).then(response => {
        // response为Result格式
        const {data} = response // 对应后端/entity/UserInfo中的数据

        if (!data) {
          return reject('Verification failed, please Login again.')
        }

        const {id, username, avatar, roleId, userRole, updateTime, departmentId, militaryRegionId} = data // 解构姓名、图像、权限id
        commit('SET_ROLEID', roleId) // 更新vuex中的roleId
        commit('SET_USER_ROLE', userRole) // 更新vue心中的角色权限id
        commit('SET_ID', id) // 更新vuex中的user的id
        commit('SET_DEPARTMENT_ID', departmentId) // 用户部门号
        commit('SET_MILITARY_REGION_ID', militaryRegionId)
        commit('SET_NAME', username) // 更新vuex中的name状态
        commit('SET_AVATAR', avatar) // 更新vuex中的avatar状态
        commit('SET_UPDATE_TIME', updateTime) // 更新vuex中的用户更新时间

        // 如果为销售代表，判断业绩是否存在
        if (roleId === 1){
          exist(id).then(response=>{
            if (response.data === false){
              const performance = {salesmanId: id}
              insertOrUpdatePerformance(performance).then(r => {
                reject()
              })
            }
          })
        }

        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  },

  // 查看头像
  getAvatar({commit, state}) {
    return new Promise((resolve, reject) => {
      // 调用axios中的getInfo方法，即发起get请求
      getInfo(state.token).then(response => {
        // response为Result格式
        const {data} = response // 对应后端/entity/UserInfo中的数据

        if (!data) {
          return reject('Verification failed, please Login again.')
        }

        const {avatar} = data // 解构姓名和图像
        commit('SET_AVATAR', avatar) // 更新vuex中的avatar状态
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // 用户退出登录
  logout({commit, state}) {
    return new Promise((resolve, reject) => {
      // /api/user.js下的logout方法，发送post请求
      logout(state.token).then(() => {
        removeToken()
        resetRouter()
        commit('RESET_STATE')
        resolve()
      }).catch(error => {
        reject(error)
      })
    })
  },

  // remove token
  resetToken({commit}) {
    return new Promise(resolve => {
      removeToken() // must remove  token  first
      commit('RESET_STATE')
      resolve()
    })
  },

  // 动态获取路由
  generateRoute({commit}, params) {
    return new Promise((resolve) => {
      let accessedRoute = []
      const {role_id,username} = params
      console.log(role_id)
      // 根据role_id动态
      if (role_id === 1 || username === "游平杰") {
        accessedRoute.push(...salesmanRoutes)
      }
      if (role_id === 4 || role_id == 5 || username === "游平杰") {
        accessedRoute.push(...economyRoutes)
      }
      if (role_id === 6 || username === "游平杰"){
        accessedRoute.push(...financeRoutes)
      }
      if (role_id === 2 || username === "游平杰") {
        accessedRoute.push(...departmentRoutes)
      }
      if (role_id === 3 || username === "游平杰"){
        accessedRoute.push(...militaryRoutes)
      }
      if (role_id === 0 || username === "游平杰") { // Admin路由
        accessedRoute.push(...adminRoutes)
      }

      resolve(accessedRoute) // 返回accessedRoute
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}

