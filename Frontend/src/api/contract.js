import http from "@/utils/request";

export async function getTableData(id) {
  return await http.get("/api/contract/getContractTableData/" + id.toString())
}

export async function insertOrUpdateContract(params) {
  return await http.post("/api/contract/insertOrUpdateContract", params)
}

export async function getLastContract() {
  return await http.get("/api/contract/getLastContract")
}

export async function deleteRow(contractNo) {
  return await http.delete("/api/contract/deleteRow/" + contractNo.toString())
}

export async function getContractNoByCustomerId(customerId) {
  return await http.get("/api/salesmanRepresentativeCustomer/getContractNoByCustomerId/" + customerId.toString())
}

// 用于金融专员等处理
export async function getEconomyTableData() {
  return await http.get("/api/contract/getEconomyTableData")
}

export async function getSalesmanIdByContractNo(contractNo){
  return await http.get("/api/contract/getSalesmanIdByContractNo/"+contractNo.toString())
}
