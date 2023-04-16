package com.dafuweng.controller;


import com.dafuweng.entity.SalesmanPerformance;
import com.dafuweng.entity.User;
import com.dafuweng.service.DepartmentService;
import com.dafuweng.service.DepartmentService.DepartmentCallBarData;
import com.dafuweng.service.DepartmentService.DepartmentCustomerBarData;
import com.dafuweng.service.DepartmentService.DepartmentLineData;
import com.dafuweng.service.DepartmentService.DepartmentTableData;
import com.dafuweng.service.SalesmanPerformanceService;
import com.dafuweng.service.UserService;
import com.dafuweng.util.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 部门表 前端控制器
 * </p>
 *
 * @author You.p.j
 * @since 2023-03-14
 */
@RestController
@RequestMapping("/api/department")
public class DepartmentController {
    @Resource
    public UserService userService;

    @Resource
    public DepartmentService departmentService;

    @Resource
    public SalesmanPerformanceService salesmanPerformanceService;

    @GetMapping("/getDepartmentTableData/{id}")
    public Result getAllUserByDepartmentId(@PathVariable("id") Long departmentId) {
        List<User> userList = userService.getAllUserByDepartmentId(departmentId);
        List<DepartmentTableData> departmentTableDataList = new ArrayList<>(userList.size());

        for (User user : userList) {
            String departmentName = departmentService.getDepartmentByDepartmentId(departmentId).getName();
            Long id = user.getId();
            String userName = user.getUsername();
            String userRole = user.getUserRole();

            departmentTableDataList.add(new DepartmentTableData(departmentId, departmentName, id, userName, userRole));
        }

        return Result.ok(departmentTableDataList);
    }

    @GetMapping("/getDepartmentLineData/{id}")
    public Result getDepartmentLineData(@PathVariable("id") Long departmentId) {
        List<User> userList = userService.getAllUserByDepartmentId(departmentId);
        List<DepartmentLineData> lineData = new ArrayList<>(userList.size());

        for (User user : userList) {
            // 跳过经理
            if (user.getRoleId() == 2 || user.getRoleId() == 4) {
                continue;
            }

            String name = user.getUsername();

            SalesmanPerformance performance = salesmanPerformanceService.getPerformanceBySalemanId(user.getId());
            BigDecimal contractAmount = performance.getContractAmount();
            BigDecimal loanAmount = performance.getLoanAmount();

            lineData.add(new DepartmentLineData(contractAmount, loanAmount, name));
        }

        return Result.ok(lineData);
    }

    @GetMapping("/getDepartmentCallBarData/{id}")
    public Result getDepartmentCallBarData(@PathVariable("id") Long departmentId) {
        List<User> userList = userService.getAllUserByDepartmentId(departmentId);
        List<DepartmentCallBarData> barData = new ArrayList<>(userList.size());

        for (User user : userList) {
            // 跳过经理
            if (user.getRoleId() == 2 || user.getRoleId() == 4) {
                continue;
            }

            SalesmanPerformance performance = salesmanPerformanceService.getPerformanceBySalemanId(user.getId());
            String name = user.getUsername();
            Integer callTimes = performance.getCallTimes();
            Integer validCalls = performance.getValidCalls();

            barData.add(new DepartmentCallBarData(name, callTimes, validCalls));
        }

        return Result.ok(barData);
    }

    @GetMapping("/getDepartmentCustomerBarData/{id}")
    public Result getDepartmentCustomerBarData(@PathVariable("id") Long departmentId) {
        List<User> userList = userService.getAllUserByDepartmentId(departmentId);
        List<DepartmentCustomerBarData> barData = new ArrayList<>(userList.size());

        for (User user : userList) {
            // 跳过经理
            if (user.getRoleId() == 2 || user.getRoleId() == 4) {
                continue;
            }
            SalesmanPerformance performance = salesmanPerformanceService.getPerformanceBySalemanId(user.getId());
            String name = user.getUsername();
            Integer interviewCustomers = performance.getInterviewCustomers();
            Integer intentionCustomers = performance.getIntentionCustomers();
            barData.add(new DepartmentCustomerBarData(name, interviewCustomers, intentionCustomers));
        }

        return Result.ok(barData);
    }
}

