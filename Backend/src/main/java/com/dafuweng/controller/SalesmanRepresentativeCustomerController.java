package com.dafuweng.controller;


import com.dafuweng.entity.SalesmanRepresentativeCustomer;
import com.dafuweng.service.SalesmanRepresentativeCustomerService;
import com.dafuweng.util.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * <p>
 * 销售员代表客户表 前端控制器
 * </p>
 *
 * @author You.p.j
 * @since 2023-03-14
 */
@RestController
@RequestMapping("/api/salesmanRepresentativeCustomer")
public class SalesmanRepresentativeCustomerController {
    @Resource
    private SalesmanRepresentativeCustomerService representativeCustomerService;

    /**
     * 插入或删除
     *
     * @param salesmanRepresentativeCustomer
     * @return
     */
    @PostMapping("/insertOrUpdateSalesmanRepresentativeCustomer")
    public Result insertOrUpdateSalesmanRepresentativeCustomer(@RequestBody SalesmanRepresentativeCustomer salesmanRepresentativeCustomer) {
        if (representativeCustomerService.insertOrUpdateSalesmanRepresentativeCustomer(salesmanRepresentativeCustomer)) {
            return Result.ok().message("插入成功！");
        }
        return Result.ok().message("更新成功！");
    }

    @PostMapping("/insertSalesmanRepresentativeCustomer")
    public Result insertSalesmanRepresentativeCustomer(@RequestBody SalesmanRepresentativeCustomer salesmanRepresentativeCustomer) {
        if (representativeCustomerService.insertSalesmanRepresentativeCustomer(salesmanRepresentativeCustomer)) {
            return Result.ok().message("插入成功");
        }
        return Result.error().message("插入失败");
    }

    @GetMapping("/getContractNoByCustomerId/{id}")
    public Result getContractNoByCustomerId(@PathVariable("id") Long id) {
        List<Long> no = representativeCustomerService.getContractNoByCustomerId(id);

        return Result.ok(no);
    }

    /**
     * 根据销售代表id获取销售代表和客户的映射表集合
     *
     * @param salesman_id
     * @return
     */
        @GetMapping("/getAllSalesmanCustomerMapperBySalesmanId/{id}")
    public Result getAllSalesmanCustomerMapperBySalesmanId(@PathVariable("id") Long salesman_id) {
        List<SalesmanRepresentativeCustomer> allList = representativeCustomerService.getAllSalesmanCustomerMapperBySalesmanID(salesman_id);

        return Result.ok(allList);
    }

    /**
     * 根据销售代表id返回过去一周的销售代表客户映射表
     *
     * @param salesman_id
     * @return
     */
    @GetMapping("/getWeekSalesmanCustomerMapperBySalesmanId")
    public Result getWeekSalesmanCustomerMapperBySalesmanId(Long salesman_id) {
        List<SalesmanRepresentativeCustomer> accessList = representativeCustomerService.getWeekSalesmanCustomerMapperBySalesmanId(salesman_id);

        return Result.ok(accessList);
    }

    /**
     * 根据销售代表id返回过去一个月的销售代表客户映射表
     *
     * @param salesman_id
     * @return
     */
    @GetMapping("/getMonthSalesmanCustomerMapperBySalesmanId")
    public Result getMonthSalesmanCustomerMapperBySalesmanId(Long salesman_id) {
        List<SalesmanRepresentativeCustomer> accessList = representativeCustomerService.getMonthSalesmanCustomerMapperBySalesmanId(salesman_id);

        return Result.ok(accessList);
    }
}

