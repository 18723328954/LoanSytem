const getters = {
  sidebar: state => state.app.sidebar,
  device: state => state.app.device,
  // 用户类
  token: state => state.user.token,
  avatar: state => state.user.avatar,
  name: state => state.user.name,
  role: state => state.user.role,
  id: state => state.user.id,
  department_id: state => state.user.department_id,
  military_region_id: state => state.user.military_region_id,
  user_role: state => state.user.user_role, // 角色权限名称，如ADMIN
  route: state => state.user.route,
  update_time: state => state.user.update_time,
  first_login: state => state.user.first_login,
  // 销售业绩
  contract_customers: state => state.salesmanRepresentativeCustomer.contract_customers,
  contract_amount: state => state.salesmanRepresentativeCustomer.contract_amount,
  loan_amount: state => state.salesmanRepresentativeCustomer.loan_amount,
  call_times: state => state.salesmanRepresentativeCustomer.call_times,
  valid_calls: state => state.salesmanRepresentativeCustomer.valid_calls,
  intention_customers: state => state.salesmanRepresentativeCustomer.intention_customers,
}
export default getters
