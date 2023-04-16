import {getToken} from "@/utils/auth";
import {getPerformance} from "@/api/salesmanRepresentativeCustomer";
import router from "@/router";
import store from "@/store";
import {error} from "autoprefixer/lib/utils";
import da from "element-ui/src/locale/lang/da";
import {int} from "mockjs/src/mock/random/basic";

const getDefaultState = () => {
  return {
    contract_customers: 0, // 签约客户数
    contract_amount: 0, // 签约金额
    loan_amount: 0, // 放款金额
    call_times: 0, // 拨打电话次数
    valid_calls: 0, // 有效拨打电话数
    intention_customers: 0 // 有意向客户数
  }
}

const state = getDefaultState()

const mutations = {
  RESET_STATE: (state) => {
    Object.assign(state, getDefaultState())
  },
  SET_CONTRACT_CUSTOMERS: (state, contract_customers) => {
    state.contract_customers = contract_customers
  },
  SET_CONTRACT_AMOUNT: (state, contract_amount) => {
    state.contract_amount = contract_amount
  },
  SET_LOAN_AMOUNT: (state, loan_amount) => {
    state.loan_amount = loan_amount
  },
  SET_CALL_TIMES: (state, call_times) => {
    state.call_times = call_times
  },
  SET_VALID_CALLS: (state, valid_calls) => {
    state.valid_calls = valid_calls
  },
  SET_INTENTION_CUSTOMERS: (state, intention_customers) => {
    state.intention_customers = intention_customers
  },
}

const actions = {
  // 获取根据id获取销售业绩
  getPerformance({commit}, id) {
    return new Promise((resolve, reject) => {
      getPerformance(id).then(response => {
        const {data} = response
        const {contractCustomers, contractAmount, loanAmount, callTimes, validCalls, intentionCustomers} = data
        commit("SET_CONTRACT_AMOUNT", contractAmount)
        commit("SET_CONTRACT_CUSTOMERS", contractCustomers)
        commit("SET_LOAN_AMOUNT", loanAmount)
        commit("SET_CALL_TIMES", callTimes)
        commit("SET_VALID_CALLS", validCalls)
        commit("SET_INTENTION_CUSTOMERS", intentionCustomers)
        resolve(data)
      }).catch(error => {
        reject(error)
      })
    })
  }
}

export default {
  namespaced: true,
  state,
  mutations,
  actions
}
