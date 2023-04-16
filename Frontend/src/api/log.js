import http from "@/utils/request";

export async function getAllLog(){
  return await http.get("/api/log/getAllLog")
}

export async function getLogByName(name){
  return await http.get("/api/log/getLogByName",name)
}

export async function insertLog(params){
  return await http.post("/api/log/insertLog",params)
}
