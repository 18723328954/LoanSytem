import router, {asyncRoutes, constantRoutes, faultRoutes} from './router'
import store from './store'
import {Message} from 'element-ui'
import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import {getToken} from '@/utils/auth' // get token from cookie
import getPageTitle from '@/utils/get-page-title'
import {getCurrentTime} from "@/api/user";
import da from "element-ui/src/locale/lang/da";
import {data} from "autoprefixer";
import {date} from "mockjs/src/mock/random/date";
import {insertLog} from "@/api/log";

NProgress.configure({showSpinner: false}) // NProgress Configuration

const whiteList = ['/login'] // no redirect whitelist

router.beforeEach(async (to, from, next) => {
  // start progress bar
  NProgress.start()

  // set page title
  document.title = getPageTitle(to.meta.title)

  // determine whether the user has logged in
  const hasToken = getToken()

  // 已经有token了
  if (hasToken) {
    if (to.path === '/login') {
      next({path: '/'})
      NProgress.done()
    } else {
      const hasGetUserInfo = store.getters.name
      const hasRole = store.getters.role !== ''
      if (hasRole) {
        next()
      } else {
        try {
          const {roleId, username} = await store.dispatch('user/getInfo') // 从后端获取用户权限id

          const params = {role_id: roleId, username: username}
          const accessRoute = await store.dispatch('user/generateRoute', params) // 根据roleId生成动态路由
          // const accessRoute = await store.dispatch('user/generateRoute',roleId,username) // 根据roleId生成动态路由

          constantRoutes.push(...accessRoute) // 动态添加路由
          router.addRoutes(accessRoute)

          // constantRoutes.push(...faultRoutes)
          // router.addRoutes(faultRoutes)

          next({...to, replace: true})// bug
        } catch (error) {
          // remove token and go to login page to re-login
          await store.dispatch('user/resetToken')
          Message.error(error || 'Has Error')
          next(`/login?redirect=${to.path}`)
          NProgress.done()
        }
      }
    }
  } else {
    /* has no token*/

    if (whiteList.indexOf(to.path) !== -1) {
      // in the free login whitelist, go directly
      next()
    } else {
      // other pages that do not have permission to access are redirected to the login page.
      next(`/login?redirect=${to.path}`)
      NProgress.done()
    }
  }
})

router.afterEach(() => {
  // finish progress bar
  NProgress.done()
})
