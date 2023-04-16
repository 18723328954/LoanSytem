package com.dafuweng.controller;


import com.dafuweng.entity.SalesmanPerformance;
import com.dafuweng.service.SalesmanPerformanceService;
import com.dafuweng.util.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 销售员绩效表 前端控制器
 * </p>
 *
 * @author You.p.j
 * @since 2023-03-14
 */
@RestController
@RequestMapping("/api/salesmanPerformance")
public class SalesmanPerformanceController {
    @Resource
    private SalesmanPerformanceService salesmanPerformanceService;

    @GetMapping("/getSalemanPerformanceBySalesmanId/{id}")
    public Result getSalemanPerformanceBySalesmanId(@PathVariable("id") Long salesman_id) {
        SalesmanPerformance salesmanPerformance = salesmanPerformanceService.getPerformanceBySalemanId(salesman_id);

        return Result.ok(salesmanPerformance);
    }

    @PostMapping("/insertOrUpdatePerformance")
    public Result insertOrUpdatePerformance(@RequestBody SalesmanPerformance salesmanPerformance) {
        if (salesmanPerformanceService.insertOrUpdatePerformance(salesmanPerformance)) {
            return Result.ok().message("插入成功！");
        }
        return Result.ok().message("更新成功！");
    }

    @GetMapping("/exist/{id}")
    public Result exist(@PathVariable("id") Long salesmanId) {
        if (salesmanPerformanceService.getBaseMapper().selectById(salesmanId) != null) {
            return Result.ok(true);
        }
        return Result.ok(false);
    }
}

