package com.dafuweng.controller;


import com.dafuweng.entity.Customer;
import com.dafuweng.entity.User;
import com.dafuweng.service.ContractService;
import com.dafuweng.service.CustomerService;
import com.dafuweng.service.CustomerService.Bar;
import com.dafuweng.service.SalesmanRepresentativeCustomerService;
import com.dafuweng.util.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 客户表 前端控制器
 * </p>
 *
 * @author You.p.j
 * @since 2023-03-14
 */
@RestController
@RequestMapping("/api/customer")
public class CustomerController {
    @Resource
    private CustomerService customerService;
    @Resource
    private ContractService contractService;
    @Resource
    private SalesmanRepresentativeCustomerService salesmanRepresentativeCustomerService;

    // 增删查改
    @GetMapping("/getCustomerBySalesmanId/{id}")
    public Result getCustomerBySalesmanId(@PathVariable("id") Long id) {
        List<Customer> customerList = customerService.getCustomerBySalesmanId(id);

        return Result.ok(customerList);
    }

    @GetMapping("/getCustomerByName")
    public Result getCustomerByName(String name) {
        List<Customer> customerList = customerService.getCustomerByName(name);

        return Result.ok(customerList);
    }

    @PostMapping("/insertCustomer")
    public Result insertCustomer(@RequestBody Customer insertCustomer) {
        if (customerService.insertCustomer(insertCustomer)) {
            return Result.ok().message("创建用户成功！");
        }
        return Result.error().message("创建用户失败！");
    }

    @DeleteMapping("/deleteCustomer/{id}")
    public Result deleteCustomer(@PathVariable("id") Long customerId){
        if (customerService.deleteCustomer(customerId)) {
            return Result.ok().message("删除成功！");
        }
        return Result.error().message("删除失败！");
    }

    @PostMapping("/updateCustomerById/{id}")
    public Result updateUserById(@PathVariable("id") Long id, @RequestBody Customer updatedCustomer) {
        if (customerService.updateCustomer(id, updatedCustomer)) {
            return Result.ok().message("修改成功！");
        }
        return Result.error().message("修改失败");
    }

    @GetMapping("/getBarBasedThisMonthByUserId/{id}")
    public Result getBarBasedThisMonthByUserId(@PathVariable("id") Long id, int monthStep) {
        List<Customer> customerList = customerService.getCustomerBasedThisMonthByUserId(id, monthStep);

        Bar bar = customerService.calculateCustomerNum(customerList);

        return Result.ok(bar);
    }

    @DeleteMapping("/deleteCustomerByCustomerId/{id}")
    public Result deleteCustomerByCustomerId(@PathVariable("id") Long id){
        // 删除用户的同时，也删除其贷款表和映射
        customerService.deleteCustomer(id);
        List<Long> contractNoList = salesmanRepresentativeCustomerService.getContractNoByCustomerId(id);

        for (Long contractNo : contractNoList) {
            contractService.deleteContractByContractNo(contractNo);
            salesmanRepresentativeCustomerService.deleteSalesmanRepresentativeCutsomerByContractNo(contractNo);
        }

        return Result.ok().message("删除成功！");
    }
}

