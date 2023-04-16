package com.dafuweng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dafuweng.entity.Contract;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 合同表 服务类
 * </p>
 *
 * @author You.p.j
 * @since 2023-03-14
 */
public interface ContractService extends IService<Contract> {
    // 用于贷款管理的表格项
    @Data
    class ContractTableInfo implements Serializable {
        public Long contractNo = -1L;
        public Long customerId = -1L;
        public String customerName = "";
        public Integer loanTerm = 0;
        public BigDecimal serviceFee = BigDecimal.valueOf(0);
        public BigDecimal contractAmount = BigDecimal.valueOf(0);
        public BigDecimal loanAmount = BigDecimal.valueOf(0);
        public Integer approveStatus = 0;
        public Date createTime = new Date();
        public Date updateTime = new Date();

        public ContractTableInfo() {
        }

        @Override
        public String toString() {
            return "ContractTableInfo{" +
                    "contractNo=" + contractNo +
                    ", customerId=" + customerId +
                    ", customerName='" + customerName + '\'' +
                    ", loanTerm=" + loanTerm +
                    ", contractAmount=" + contractAmount +
                    ", loanAmount=" + loanAmount +
                    ", approveStatus=" + approveStatus +
                    ", createTime=" + createTime +
                    ", updateTime=" + updateTime +
                    '}';
        }

        public ContractTableInfo(Long contractNo, Long customerId, String customerName, Integer loanTerm,BigDecimal serviceFee, BigDecimal contractAmount, BigDecimal loanAmount, Integer approveStatus,
                                 Date createTime,
                                 Date updateTime) {
            this.contractNo = contractNo;
            this.customerId = customerId;
            this.customerName = customerName;
            this.loanTerm = loanTerm;
            this.serviceFee = serviceFee;
            this.contractAmount = contractAmount;
            this.loanAmount = loanAmount;
            this.approveStatus = approveStatus;
            this.createTime = createTime;
            this.updateTime = updateTime;
        }
    }

    // 用于金融审批的表格
    @Data
    class EconomyTableData implements Serializable {
        Long contractNo = -1L;
        String customerName = "";
        String salesmanName = "";
        Integer loanTerm = 0;
        BigDecimal loanAmount = BigDecimal.valueOf(0);
        BigDecimal contractAmount = BigDecimal.valueOf(0.0);
        Integer approveStatus = 0;
        Date createTime = new Date();
        Date updateTime = new Date();

        public EconomyTableData() {
        }

        public EconomyTableData(Long contractNo, String customerName, String salesmanName, Integer loanTerm, BigDecimal loanAmount, BigDecimal contractAmount, Integer approveStatus, Date createTime,
                                Date updateTime) {
            this.contractNo = contractNo;
            this.customerName = customerName;
            this.salesmanName = salesmanName;
            this.loanTerm = loanTerm;
            this.loanAmount = loanAmount;
            this.contractAmount = contractAmount;
            this.approveStatus = approveStatus;
            this.createTime = createTime;
            this.updateTime = updateTime;
        }

        @Override
        public String toString() {
            return "EconomyTableData{" +
                    "contractNo=" + contractNo +
                    ", customerName='" + customerName + '\'' +
                    ", salesmanName='" + salesmanName + '\'' +
                    ", loanTerm=" + loanTerm +
                    ", loanAmount=" + loanAmount +
                    ", contractAmount=" + contractAmount +
                    ", approveStatus=" + approveStatus +
                    ", createTime=" + createTime +
                    ", updateTime=" + updateTime +
                    '}';
        }
    }

    public Contract getContractByContractNo(Long contractNo); // 通过合同id查询客户信息

    public boolean insertContract(Contract contract); // 新增合同

    public boolean insertOrUpdateContract(Contract contract); // 新增或更新

    public List<Contract> getAllContract(); // 获取所有的contract，用于新增id

    public boolean deleteContractByContractNo(Long contractNo); // 按照contractNo删除

//    public List<EconomyTableData> getEconomyTableData(); // 根据所有合同no返回EconomyTableData的集合
}
