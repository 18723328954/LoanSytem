import http from "@/utils/request";

export async function getDepartmentTableData(departmentId) {
  return await http.get("/api/department/getDepartmentTableData/" + departmentId.toString(), departmentId)
}

export async function getDepartmentLineData(departmentId) {
  return await http.get("/api/department/getDepartmentLineData/" + departmentId.toString())
}

export async function getDepartmentCallBarData(departmentId) {
  return await http.get("/api/department/getDepartmentCallBarData/" + departmentId.toString())
}

export async function getDepartmentCustomerBarData(departmentId) {
  return await http.get("/api/department/getDepartmentCustomerBarData/" + departmentId.toString())
}
