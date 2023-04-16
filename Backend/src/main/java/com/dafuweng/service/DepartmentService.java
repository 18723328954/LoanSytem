package com.dafuweng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dafuweng.entity.Department;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>
 * 部门表 服务类
 * </p>
 *
 * @author You.p.j
 * @since 2023-03-14
 */
public interface DepartmentService extends IService<Department> {
    @Data
    class DepartmentTableData {
        public Long departmentId = -1L;
        public String departmentName = "";
        public Long id = 0L;
        public String userName = "";
        public String userRole = "";

        public DepartmentTableData(Long departmentId, String departmentName, Long id, String userName, String userRole) {
            this.departmentId = departmentId;
            this.departmentName = departmentName;
            this.id = id;
            this.userName = userName;
            this.userRole = userRole;
        }
    }

    @Data
    class DepartmentLineData {
        public BigDecimal contractAmount;
        public BigDecimal loanAmount;
        public String name;

        public DepartmentLineData(BigDecimal contractAmount, BigDecimal loanAmount, String name) {
            this.contractAmount = contractAmount;
            this.loanAmount = loanAmount;
            this.name = name;
        }
    }

    @Data
    class DepartmentCallBarData {
        String name;
        Integer callTimes;
        Integer validCalls;

        public DepartmentCallBarData(String name, Integer callTimes, Integer validCalls) {
            this.name = name;
            this.callTimes = callTimes;
            this.validCalls = validCalls;
        }
    }

    @Data
    class DepartmentCustomerBarData {
        String name;
        Integer interviewCustomers;
        Integer intentionCustomers;

        public DepartmentCustomerBarData(String name, Integer interviewCustomers, Integer intentionCustomers) {
            this.name = name;
            this.interviewCustomers = interviewCustomers;
            this.intentionCustomers = intentionCustomers;
        }
    }

    public Department getDepartmentByDepartmentId(Long departmentId); // 获取部门按照部门id
}
