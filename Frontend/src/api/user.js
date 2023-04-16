import http from '@/utils/request'

/**
 * 用户登录
 * @returns
 */
export async function login(data) {
  return await http.login("/api/user/login", data)
}

/**
 * 获取用户信息和权限信息
 * @returns
 */
export async function getInfo() {
  return await http.get("/api/sysUser/getInfo")
}

/**
 * 获取头像
 */
export async function getAvatar() {
  return await http.get('api/sysUser/getAvatar')
}

/**
 * 退出登录
 * @returns
 */
export async function logout(param) {
  return await http.post("/api/sysUser/logout", param);
}

export async function getUserById(id) {
  return await http.get("/api/user/getUserInfo/" + id.toString())
}

export async function getCurrentTime() {
  return await http.get("/api/user/getCurrentTime")
}

/**
 * 更新用户信息
 */
export async function updateUserById(id, data) {
  return await http.post("/api/user/updateUserById/" + id.toString(), data)
}

export async function getUserTableData() {
  return await http.get("/api/user/getUserTableData")
}

export async function deleteUserById(id) {
  return await http.delete("/api/user/deleteUser/" + id.toString())
}

export async function insertUser(params) {
  return await http.post("/api/user/insertUser", params)
}

export async function getUserByName(params) {
  return await http.get("/api/user/getUserByName", params)
}
