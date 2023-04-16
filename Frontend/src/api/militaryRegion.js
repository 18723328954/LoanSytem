import http from "@/utils/request";

export async function getMilitaryRegionTableData(militaryRegionId) {
  return await http.get("/api/militaryRegion/getMilitaryTableData/" + militaryRegionId.toString(), militaryRegionId)
}

export async function getMilitaryRegionLineData(militaryRegionId) {
  return await http.get("/api/militaryRegion/getMilitaryLineData/" + militaryRegionId.toString())
}

export async function getMilitaryRegionCallBarData(militaryRegionId) {
  return await http.get("/api/militaryRegion/getMilitaryCallBarData/" + militaryRegionId.toString())
}

export async function getMilitaryRegionCustomerBarData(militaryRegionId) {
  return await http.get("/api/militaryRegion/getMilitaryCustomerBarData/" + militaryRegionId.toString())
}
