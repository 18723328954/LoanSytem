import http from "@/utils/request";

// ------------------------------------------------
//                    product
// ------------------------------------------------
/**
 * 获取销售代表的金融产品销售数量
 */
export async function getProductByUserId(id) {
  return await http.get("/api/product/getProductsByUserId/" + id.toString())
}

/**
 * 获取销售代表本周或本月的销售数量
 */
export async function getWeekOrMonthProductByUserId(id, params) {
  return await http.get("/api/product/getWeekOrMonthProductByUserId/" + id.toString(), params)
}

/**
 * 获取销售代表的所有销售量
 */
export async function getTotalProductByUserId(id) {
  return await http.get("/api/product/getTotalProductByUserId/" + id.toString())
}

/**
 * 获取销售代表X月的产品销量
 */
export async function getXMonthProductByUserId(id, params) {
  return await http.get("/api/product/getProductBasedThisMonthByUserId/" + id.toString(), params)
}

/**
 * 获取当前和前monthSteps内的时间
 */
export async function getLocalDateList(params) {
  return await http.get("/api/product/getLocalDateList", params)
}
