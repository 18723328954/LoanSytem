package com.dafuweng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dafuweng.entity.Contract;
import com.dafuweng.entity.Customer;

import java.util.List;

/**
 * <p>
 * 客户表 服务类
 * </p>
 *
 * @author You.p.j
 * @since 2023-03-14
 */
public interface CustomerService extends IService<Customer> {
    // 前端柱状图
    class Bar {
        public Integer customerNum = 0;
        public Integer activeCustomerNum = 0; // 意向>60的客户
    }

    // 增删查改
    public List<Customer> getCustomerBySalesmanId(Long id); // 获取id号销售代表的客户

    public List<Customer> getAllCustomer(); // 获取所有customer

    public Customer getCustomerByCustomerID(Long id); // 按id返回customer的信息

    public List<Customer> getCustomerByName(String name); // 按客户姓名查询

    public boolean insertCustomer(Customer insertCustomer); // 创建用户信息

    public boolean deleteCustomer(Long customerId); // 按客户id删除客户

    public boolean updateCustomer(Long customerId,Customer updatedCustomer); // 按客户id更新客户信息

    public List<Customer> getCustomerBasedThisMonthByUserId(Long id, int monthSteps); // 按用户Id获取某月的客户数，monthSteps表示距离本月第一天时间

    public Bar calculateCustomerNum(List<Customer> customerList); // 计算List中的用户数量和活跃用户数量

//    public List<Contract> getContractListByCustomerId(Long customerId); // 查询相同用户所办理的所有合同

}
