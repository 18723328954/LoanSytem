package com.dafuweng.controller;


import com.dafuweng.entity.Department;
import com.dafuweng.entity.MilitaryRegion;
import com.dafuweng.entity.SalesmanRepresentativeCustomer;
import com.dafuweng.entity.User;
import com.dafuweng.service.*;
import com.dafuweng.service.UserService.UserTableData;
import com.dafuweng.util.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author You.p.j
 * @since 2023-03-14
 */
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Resource
    private DepartmentService departmentService;
    @Resource
    private UserService userService;
    @Resource
    private SalesmanPerformanceService salesmanPerformanceService;
    @Resource
    private SalesmanRepresentativeCustomerService salesmanRepresentativeCustomerService;
    @Resource
    private ContractService contractService;
    @Resource
    private CustomerService customerService;
    @Resource
    private MilitaryRegionService militaryRegionService;

    @GetMapping("/getUserByName")
    public Result getUserByName(String name) {
        User user = userService.findUserByUserName(name);

        return Result.ok(user);
    }

    // 获取当前时间
    @GetMapping("/getCurrentTime")
    private Result getCurrentDate() {
        return Result.ok(new Date()).message("获取时间成功！");
    }

    /*
    -----------------------------------------
                     增删改查
    -----------------------------------------
     */
    @PostMapping("/updateUserById/{id}")
    public Result updateUserById(@PathVariable("id") Long id, @RequestBody User updatedUser) {
        if (userService.updateUserById(id, updatedUser)) {
            return Result.ok().message("修改成功！");
        }
        return Result.error().message("修改失败");
    }

    @PostMapping("/insertUser")
    public Result insertUser(@RequestBody User insertUser) {
        System.out.println(insertUser);
        if (userService.insertUser(insertUser)) {
            return Result.ok().message("创建用户成功！");
        }
        return Result.error().message("创建用户失败！");
    }

    @DeleteMapping("/deleteUser/{id}")
    public Result deleteUserById(@PathVariable("id") Long id) {
        User user = userService.findUserById(id);

        // 删去销售代表相关的合同、合同映射、业绩、客户表
        if (user.getRoleId() == 1) {
            List<SalesmanRepresentativeCustomer> mapperList = salesmanRepresentativeCustomerService.getAllSalesmanCustomerMapperBySalesmanID(id);
            // 删除业绩
            salesmanPerformanceService.deletePerformanceBySalesmanId(id);
            // 删除合同
            for (SalesmanRepresentativeCustomer mapper : mapperList) {
                Long contractNo = mapper.getContractNo();
                Long customerId = mapper.getCustomerId();
                contractService.deleteContractByContractNo(contractNo);
                customerService.deleteCustomer(customerId); // 此处为删除客户信息，之后可改成移交公海
                salesmanRepresentativeCustomerService.deleteSalesmanRepresentativeCutsomerByContractNo(contractNo);
            }
        }
        userService.deleteUserById(id);

        return Result.ok().message("删除成功");
    }

    @GetMapping("/getUserInfo/{id}")
    public Result getUserInfo(@PathVariable("id") Long id) {
        User user = userService.findUserById(id);

        if (user != null) {
            return Result.ok(user).message("查找成功！");
        }
        return Result.error().message("查找失败，无id为" + id.toString() + "的用户！");
    }

    @GetMapping("/getUserTableData")
    public Result getUserTableData() {
        List<User> userList = userService.getAllUser();
        List<UserTableData> userTableDataList = new ArrayList<>(userList.size());

        for (User user : userList) {
            Long id = user.getId();
            String username = user.getUsername();
            String userRole = user.getUserRole();
            Integer roleId = user.getRoleId();
            String departmentName = "";
            String militaryRegionName = "";

            // 销售经理和销售代表才有部门
            if (roleId == 2 || roleId == 1) {
                Integer departmentId = user.getDepartmentId();
                Department department = departmentService.getDepartmentByDepartmentId((long) departmentId);
                departmentName = department.getName();
            }
            // 销售代表、销售经理、战区主管才有战区
            if (roleId == 2 || roleId == 3 || roleId == 1) {
                Integer militaryRegionId = user.getMilitaryRegionId();
                MilitaryRegion militaryRegion = militaryRegionService.getMilitaryRegionByMilitaryRegionId((long) militaryRegionId);
                militaryRegionName = militaryRegion.getName();
            }

            Boolean sex = user.getSex();
            Integer age = user.getAge();
            BigDecimal salary = user.getSalary();

            userTableDataList.add(new UserTableData(id, username, userRole, sex, age, departmentName, militaryRegionName, salary));
        }

        return Result.ok(userTableDataList);
    }
}

