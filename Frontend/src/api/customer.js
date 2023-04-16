import http from "@/utils/request";

/**
 * 返回销售达标当前月份的客户数，用于柱状图
 * @param id  销售代表id
 * @param params  距离本月的月份数
 * @returns {Promise<AxiosResponse<any>>}
 */
export async function getBarBasedThisMonthByUserId(id, params) {
  return await http.get("/api/customer/getBarBasedThisMonthByUserId/" + id.toString(), params)
}

export async function getCustomerInfoByUserId(id, params) {
  return await http.get("/api/customer/getCustomerBySalesmanId/" + id.toString(), params)
}

export async function insertCustomer(params) {
  return await http.post("/api/customer/insertCustomer", params)
}

export async function deleteCustomer(cutsomerId) {
  return await http.delete("/api/customer/deleteCustomer/" + cutsomerId.toString())
}

export async function updateCustomerById(customerId,params){
  return await http.post("/api/customer/updateCustomerById/"+customerId.toString(),params)
}

export async function findCustomerByName(params) {
  return await http.get("/api/customer/getCustomerByName", params)
}

export async function deleteCustomerByCustomerId(id){
  return await http.delete("/api/customer/deleteCustomerByCustomerId/"+id.toString())
}
