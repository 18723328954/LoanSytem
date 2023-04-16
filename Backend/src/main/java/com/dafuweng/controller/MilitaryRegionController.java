package com.dafuweng.controller;


import com.dafuweng.entity.SalesmanPerformance;
import com.dafuweng.entity.User;
import com.dafuweng.service.DepartmentService;
import com.dafuweng.service.MilitaryRegionService;
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
 * 战区表 前端控制器
 * </p>
 *
 * @author You.p.j
 * @since 2023-03-14
 */
@RestController
@RequestMapping("/api/militaryRegion")
public class MilitaryRegionController {
    @Resource
    public UserService userService;

    @Resource
    public MilitaryRegionService militaryRegionService;

    @Resource
    public SalesmanPerformanceService salesmanPerformanceService;

    @GetMapping("/getMilitaryTableData/{id}")
    public Result getAllUserByMilitaryRegionId(@PathVariable("id") Long militaryRegionId) {
        List<User> userList = userService.getAllUserByMilitaryRegionId(militaryRegionId);
        List<MilitaryRegionService.MilitaryRegionTableData> militaryTableDataList = new ArrayList<>(userList.size());

        for (User user : userList) {
            String departmentName = militaryRegionService.getMilitaryRegionByMilitaryRegionId(militaryRegionId).getName();
            Long id = user.getId();
            String userName = user.getUsername();
            String userRole = user.getUserRole();

            militaryTableDataList.add(new MilitaryRegionService.MilitaryRegionTableData(militaryRegionId, departmentName, id, userName, userRole));
        }

        return Result.ok(militaryTableDataList);
    }

    @GetMapping("/getMilitaryLineData/{id}")
    public Result getMilitaryLineData(@PathVariable("id") Long militaryRegionId) {
        List<User> userList = userService.getAllUserByMilitaryRegionId(militaryRegionId);
        List<MilitaryRegionService.MilitaryRegionLineData> lineData = new ArrayList<>(userList.size());

        for (User user : userList) {
            // 只要销售代表的业绩
            if (user.getRoleId() == 1) {
                String name = user.getUsername();

                SalesmanPerformance performance = salesmanPerformanceService.getPerformanceBySalemanId(user.getId());
                BigDecimal contractAmount = performance.getContractAmount();
                BigDecimal loanAmount = performance.getLoanAmount();

                lineData.add(new MilitaryRegionService.MilitaryRegionLineData(contractAmount, loanAmount, name));
            }
        }

        return Result.ok(lineData);
    }

    @GetMapping("/getMilitaryCallBarData/{id}")
    public Result getMilitaryCallBarData(@PathVariable("id") Long militaryRegionId) {
        List<User> userList = userService.getAllUserByMilitaryRegionId(militaryRegionId);
        List<MilitaryRegionService.MilitaryRegionCallBarData> barData = new ArrayList<>(userList.size());

        for (User user : userList) {
            if (user.getRoleId() == 1) {
                SalesmanPerformance performance = salesmanPerformanceService.getPerformanceBySalemanId(user.getId());
                String name = user.getUsername();
                Integer callTimes = performance.getCallTimes();
                Integer validCalls = performance.getValidCalls();

                barData.add(new MilitaryRegionService.MilitaryRegionCallBarData(name, callTimes, validCalls));            }
        }

        return Result.ok(barData);
    }

    @GetMapping("/getMilitaryCustomerBarData/{id}")
    public Result getMilitaryCustomerBarData(@PathVariable("id") Long militaryRegionId) {
        List<User> userList = userService.getAllUserByMilitaryRegionId(militaryRegionId);
        List<MilitaryRegionService.MilitaryRegionCustomerBarData> barData = new ArrayList<>(userList.size());

        for (User user : userList) {
            if (user.getRoleId() == 1) {
                SalesmanPerformance performance = salesmanPerformanceService.getPerformanceBySalemanId(user.getId());
                String name = user.getUsername();
                Integer interviewCustomers = performance.getInterviewCustomers();
                Integer intentionCustomers = performance.getIntentionCustomers();
                barData.add(new MilitaryRegionService.MilitaryRegionCustomerBarData(name, interviewCustomers, intentionCustomers));            }
        }

        return Result.ok(barData);
    }
}

