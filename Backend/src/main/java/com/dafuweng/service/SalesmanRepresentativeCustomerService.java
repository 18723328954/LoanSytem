package com.dafuweng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dafuweng.entity.SalesmanRepresentativeCustomer;

import java.util.List;

/**
 * <p>
 * 销售员代表客户表 服务类
 * </p>
 *
 * @author You.p.j
 * @since 2023-03-14
 */
public interface SalesmanRepresentativeCustomerService extends IService<SalesmanRepresentativeCustomer> {

    public boolean insertSalesmanRepresentativeCustomer(SalesmanRepresentativeCustomer salesmanRepresentativeCustomer); // 插入映射表

    public boolean insertOrUpdateSalesmanRepresentativeCustomer(SalesmanRepresentativeCustomer salesmanRepresentativeCustomer); // 插入或更新映射表

    public List<Long> getContractNoByCustomerId(Long id); // 根据客户号返回合同号

    public List<SalesmanRepresentativeCustomer> getAllSalesmanRepresentativeCustomer(); // 获取所有映射

    public List<SalesmanRepresentativeCustomer> getAllSalesmanCustomerMapperBySalesmanID(Long salesman_id); // 根据销售代表id获取所有客户映射表

    public List<SalesmanRepresentativeCustomer> getWeekSalesmanCustomerMapperBySalesmanId(Long salesman_id); // 根据销售代表id获取过去一周的有所客户映射表

    public List<SalesmanRepresentativeCustomer> getMonthSalesmanCustomerMapperBySalesmanId(Long salesman_id); // 根据销售代表id获取过去一个月内所有客户映射表

    public SalesmanRepresentativeCustomer getSalesmanRepresentativeCustomerByContractNo(Long contractNo); // 寻找合同号的映射

    public boolean deleteSalesmanRepresentativeCutsomerByContractNo(Long contractNo); // 按合同号删除映射表
}
