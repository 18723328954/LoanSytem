package com.dafuweng.controller;

import com.dafuweng.entity.Contract;
import com.dafuweng.entity.Customer;
import com.dafuweng.entity.SalesmanRepresentativeCustomer;
import com.dafuweng.entity.User;
import com.dafuweng.service.ContractService;
import com.dafuweng.service.ContractService.ContractTableInfo;
import com.dafuweng.service.ContractService.EconomyTableData;
import com.dafuweng.service.CustomerService;
import com.dafuweng.service.SalesmanRepresentativeCustomerService;
import com.dafuweng.service.UserService;
import com.dafuweng.util.Result;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 合同表 前端控制器
 * </p>
 *
 * @author You.p.j
 * @since 2023-03-14
 */
@RestController
@RequestMapping("/api/contract")
public class ContractController {
    @Resource
    public UserService userService;
    @Resource
    public CustomerService customerService;
    @Resource
    public ContractService contractService;
    @Resource
    public SalesmanRepresentativeCustomerService salesmanRepresentativeCustomerService;

    /**
     * 获取销售代表的合同信息
     *
     * @param id
     * @return
     */
    @GetMapping("/getContractTableData/{id}")
    public Result getContractTableDataBySalesmanId(@PathVariable("id") Long id) {
        List<SalesmanRepresentativeCustomer> salesmanRepresentativeCustomerList = salesmanRepresentativeCustomerService.getAllSalesmanCustomerMapperBySalesmanID(id); // 销售代表客户映射表
        List<ContractTableInfo> contractTableInfoList = new ArrayList<ContractTableInfo>(salesmanRepresentativeCustomerList.size()); // 初始化信息数组

        // 修改信息
        for (int i = 0; i < salesmanRepresentativeCustomerList.size(); i++) {
            Long customerId = salesmanRepresentativeCustomerList.get(i).getCustomerId();
            Long contractNo = salesmanRepresentativeCustomerList.get(i).getContractNo();

            Contract contract = contractService.getContractByContractNo(contractNo); // 合同信息
            Customer customer = customerService.getCustomerByCustomerID(customerId); // 客户信息

            String customerName = customer.getName();
            Integer loanTerm = contract.getLoanTerm();
            BigDecimal contractAmount = contract.getContractAmount();
            BigDecimal loanAmount = contract.getLoanAmount();
            BigDecimal serviceFee = contract.getServiceFee();
            Integer approveStatus = contract.getApproveStatus();
            Date createTime = contract.getCreateTime();
            Date updateTime = contract.getUpdateTime();
            ContractTableInfo contractTableInfo = new ContractTableInfo(contractNo, customerId, customerName, loanTerm, serviceFee, contractAmount, loanAmount, approveStatus, createTime, updateTime);

            contractTableInfoList.add(contractTableInfo);
        }

        return Result.ok(contractTableInfoList);
    }

    @PostMapping("/insertContract")
    public Result insertContract(@RequestBody Contract contract) {
        if (contractService.insertContract(contract)) {
            return Result.ok().message("插入成功！");
        }
        return Result.error().message("插入失败！");
    }

    @PostMapping("/insertOrUpdateContract")
    public Result insertOrUpdateContract(@RequestBody Contract contract) {
        if (contractService.insertOrUpdateContract(contract)) {
            return Result.ok().message("插入成功！");
        }
        return Result.ok().message("更新成功！");
    }

    /**
     * 最后更新的合同
     *
     * @return
     */
    @GetMapping("/getLastContract")
    public Result getLastContract() {
        List<Contract> contractList = contractService.getAllContract();
        Contract lastContract = contractList.stream().max(Comparator.comparing(Contract::getUpdateTime)).orElse(null);

        return Result.ok(lastContract);
    }

    /**
     * 删除contract行，并更新其他模块
     */
    @DeleteMapping("/deleteRow/{no}")
    public Result deleteRow(@PathVariable("no") Long contractNo) {
        // 合同号找映射表，获取客户id
        SalesmanRepresentativeCustomer salesmanRepresentativeCustomer = salesmanRepresentativeCustomerService.getSalesmanRepresentativeCustomerByContractNo(contractNo);
        Long customerId = salesmanRepresentativeCustomer.getCustomerId();
        // 删去合同、映射和客户表的数据
        // 有可能一个客户有多个合同
        contractService.deleteContractByContractNo(contractNo); // 根据合同号删除合同
        salesmanRepresentativeCustomerService.deleteSalesmanRepresentativeCutsomerByContractNo(contractNo);
        // 查找该客户在删除该合同后是否还有合同
        List<Long> customerIdList = salesmanRepresentativeCustomerService.getContractNoByCustomerId(customerId);
        if (customerIdList.isEmpty()) {
            // 没用合同了就可以删除客户了
            customerService.deleteCustomer(customerId);
        }

        return Result.ok().message("删除成功");
    }

    // 根据所有合同no返回EconomyTableData的集合
    @GetMapping("/getEconomyTableData")
    public Result getEconomyTableData() {
        // 初始化数据集合大小
        List<Contract> allContract = contractService.getAllContract();
        List<EconomyTableData> economyTableDataList = new ArrayList<>(allContract.size());

        // 遍历合同，并从映射表、客户表、员工表中获得数据
        for (int i = 0; i < allContract.size(); i++) {
            Contract contract = allContract.get(i);
            SalesmanRepresentativeCustomer salesmanRepresentativeCustomer = salesmanRepresentativeCustomerService.getSalesmanRepresentativeCustomerByContractNo(contract.getContractNo());
            User user = userService.findUserById(salesmanRepresentativeCustomer.getSalesmanId());
            Customer customer = customerService.getCustomerByCustomerID(salesmanRepresentativeCustomer.getCustomerId());

            // 根据以上信息创新EconomyTableData表
            EconomyTableData economyTableData = new EconomyTableData(
                    contract.getContractNo(), customer.getName(), user.getUsername(), contract.getLoanTerm(), contract.getLoanAmount(), contract.getContractAmount(),
                    contract.getApproveStatus(), contract.getCreateTime(), contract.getUpdateTime()
            );
            economyTableDataList.add(economyTableData);
        }
        return Result.ok(economyTableDataList);
    }

    @GetMapping("/getSalesmanIdByContractNo/{id}")
    public Result getSalesmanIdByContractNo(@PathVariable("id") Long contractNo) {
        SalesmanRepresentativeCustomer salesmanRepresentativeCustomer = salesmanRepresentativeCustomerService.getSalesmanRepresentativeCustomerByContractNo(contractNo);

        return Result.ok(salesmanRepresentativeCustomer.getSalesmanId());
    }
}

