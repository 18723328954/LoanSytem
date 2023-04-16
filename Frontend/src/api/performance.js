import http from "@/utils/request";

export async function insertOrUpdatePerformance(params) {
  return await http.post("/api/salesmanPerformance/insertOrUpdatePerformance", params)
}

export async function exist(salesmanId){
  return await http.get("/api/salesmanPerformance/exist/"+salesmanId.toString())
}
