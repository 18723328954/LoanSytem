import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'/'el-icon-x' the icon show in the sidebar
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },
  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [{
      path: 'dashboard',
      name: 'Dashboard',
      component: () => import('@/views/dashboard/index'),
      meta: {title: '首页', icon: 'el-icon-s-home'}
    }]
  },
]

export const salesmanRoutes = [
  {
    path: '/customer',
    component: Layout,
    redirect: '/customer/table',
    name: 'Customer',
    children: [
      {
        path: 'customer',
        name: 'customer',
        component: () => import('@/views/customer/index'),
        meta: {title: '客户管理', icon: 'el-icon-s-custom'}
      }
    ]
  },

  {
    path: '/contract',
    component: Layout,
    children: [
      {
        path: 'contract',
        name: 'Contract',
        component: () => import('@/views/contract/index'),
        meta: {title: '贷款管理', icon: 'el-icon-money'}
      }
    ]
  }
]

export const faultRoutes = [
  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  },
  // 404 page must be placed at the end !!!
  {path: '*', redirect: '/404', hidden: true}
]

export const adminRoutes = [
  {
    path: '/user',
    component: Layout,
    redirect: '/user/table',
    name: 'User',
    meta: {
      title: '员工管理',
      icon: 'el-icon-user-solid'
    },
    children: [
      {
        path: 'info',
        component: () => import('@/views/user/info/index'), // Parent router-view
        name: 'Basic',
        meta: {title: '员工列表'},
      },
      {
        path: 'user',
        component: () => import('@/views/user/add/index'),
        name: 'Performance',
        meta: {title: '添加员工'}
      }
    ]
  },
  {
    path: '/system',
    component: Layout,
    redirect: '/system/table',
    name: 'System',
    meta: {
      title: '系统管理',
      icon: 'el-icon-menu'
    },
    children: [
      {
        path: 'log',
        component: () => import('@/views/system/log/index.vue'), // Parent router-view
        name: 'Log',
        meta: {title: '日志管理'},
      },
    ]
  },
]

export const departmentRoutes = [
  {
    path: '/department',
    component: Layout,
    redirect: '/department',
    name: 'Department',
    meta: {
      title: '部门管理',
      icon: 'el-icon-price-tag'
    },
    children: [
      {
        path: 'info',
        component: () => import('@/views/department/userInfo/index.vue'), // Parent router-view
        name: 'Basic',
        meta: {title: '员工信息',icon:"el-icon-tickets"},
      },
      {
        path: 'data',
        component: () => import('@/views/department/userData/index.vue'),
        name: 'Data',
        meta: {title: '员工数据',icon:"el-icon-medal"}
      }
    ]
  }
]

export const militaryRoutes = [
  {
    path: '/military',
    component: Layout,
    redirect: '/military',
    name: 'Military',
    meta: {
      title: '战区管理',
      icon: 'el-icon-house'
    },
    children: [
      {
        path: 'info',
        component: () => import('@/views/militaryRegion/userInfo/index.vue'), // Parent router-view
        name: 'Basic',
        meta: {title: '员工信息',icon:"el-icon-tickets"},
      },
      {
        path: 'data',
        component: () => import('@/views/militaryRegion/userData/index.vue'),
        name: 'Data',
        meta: {title: '员工数据',icon:"el-icon-medal"}
      }
    ]
  }
]

export const economyRoutes = [
  {
    path: '/economy',
    component: Layout,
    redirect: '/economy',
    children: [
      {
        path: 'economy',
        name: 'Economy',
        component: () => import('@/views/economy/index'),
        meta: {title: '金融管理', icon: 'el-icon-notebook-2'}
      }
    ]
  }
]

export const financeRoutes = [
  {
    path: '/finance',
    component: Layout,
    redirect: '/finance',
    children: [
      {
        path: 'finance',
        name: 'Finance',
        component: () => import('@/views/finance/index'),
        meta: {title: '财务管理', icon: 'el-icon-s-finance'}
      }
    ]
  }
]



const createRouter = () => new Router({
  // mode:'hash',
  mode: 'history', // require service support
  scrollBehavior: () => ({y: 0}),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
