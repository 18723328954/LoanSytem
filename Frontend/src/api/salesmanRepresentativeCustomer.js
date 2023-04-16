import http from '@/utils/request'

/**
 * 通过用户id查询销售业绩
 * @returns
 */
export async function getPerformance(id){
  return await http.get("/api/salesmanPerformance/getSalemanPerformanceBySalesmanId/"+id.toString())
}

export async function insertOrUpdateSalesmanRepresentativeCustomer(params){
  return await http.post("/api/salesmanRepresentativeCustomer/insertOrUpdateSalesmanRepresentativeCustomer",params)
}
