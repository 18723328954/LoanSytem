package com.dafuweng.service;

import com.dafuweng.entity.Department;
import com.dafuweng.entity.MilitaryRegion;
import com.baomidou.mybatisplus.extension.service.IService;
import lombok.Data;

import java.math.BigDecimal;

/**
 * <p>
 * 战区表 服务类
 * </p>
 *
 * @author You.p.j
 * @since 2023-03-14
 */
public interface MilitaryRegionService extends IService<MilitaryRegion> {
    @Data
    class MilitaryRegionTableData {
        public Long militaryRegionId = -1L;
        public String militaryName = "";
        public Long id = 0L;
        public String userName = "";
        public String userRole = "";

        public MilitaryRegionTableData(Long militaryRegionId, String militaryName, Long id, String userName, String userRole) {
            this.militaryRegionId = militaryRegionId;
            this.militaryName = militaryName;
            this.id = id;
            this.userName = userName;
            this.userRole = userRole;
        }
    }

    @Data
    class MilitaryRegionLineData {
        public BigDecimal contractAmount;
        public BigDecimal loanAmount;
        public String name;

        public MilitaryRegionLineData(BigDecimal contractAmount, BigDecimal loanAmount, String name) {
            this.contractAmount = contractAmount;
            this.loanAmount = loanAmount;
            this.name = name;
        }
    }

    @Data
    class MilitaryRegionCallBarData {
        String name;
        Integer callTimes;
        Integer validCalls;

        public MilitaryRegionCallBarData(String name, Integer callTimes, Integer validCalls) {
            this.name = name;
            this.callTimes = callTimes;
            this.validCalls = validCalls;
        }
    }

    @Data
    class MilitaryRegionCustomerBarData {
        String name;
        Integer interviewCustomers;
        Integer intentionCustomers;

        public MilitaryRegionCustomerBarData(String name, Integer interviewCustomers, Integer intentionCustomers) {
            this.name = name;
            this.interviewCustomers = interviewCustomers;
            this.intentionCustomers = intentionCustomers;
        }
    }

    public MilitaryRegion getMilitaryRegionByMilitaryRegionId(Long militaryRegionId); // 获取部门按照部门id
}
