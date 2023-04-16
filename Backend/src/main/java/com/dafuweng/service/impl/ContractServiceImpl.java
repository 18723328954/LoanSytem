package com.dafuweng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dafuweng.dao.ContractMapper;
import com.dafuweng.entity.Contract;
import com.dafuweng.service.ContractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * <p>
 * 合同表 服务实现类
 * </p>
 *
 * @author You.p.j
 * @since 2023-03-14
 */
@Service
@Transactional
public class ContractServiceImpl extends ServiceImpl<ContractMapper, Contract> implements ContractService {
//    public SalesmanRepresentativeCustomerService salesmanRepresentativeCustomerService;

    /**
     * 通过合同id查询客户信息
     *
     * @param contractNo 合同编号
     * @return
     */
    @Override
    public Contract getContractByContractNo(Long contractNo) {
        QueryWrapper<Contract> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("contract_no", contractNo);

        return baseMapper.selectOne(queryWrapper);
    }

    /**
     * 新增合同
     *
     * @param contract
     * @return
     */
    @Override
    public boolean insertContract(Contract contract) {
        QueryWrapper<Contract> contractQueryWrapper = new QueryWrapper<>();
        contractQueryWrapper.eq("contract_no", contract.getContractNo());

        if (baseMapper.selectOne(contractQueryWrapper) != null) {
            return false;
        }

        baseMapper.insert(contract);
        return true;
    }

    /**
     * 新增或更新
     *
     * @param contract
     * @return
     */
    @Override
    public boolean insertOrUpdateContract(Contract contract) {
        QueryWrapper<Contract> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("contract_no", contract.getContractNo());

        // 更新操作
        if (baseMapper.selectOne(queryWrapper) != null) {
            UpdateWrapper<Contract> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("contract_no", contract.getContractNo());
            Contract updateContract = baseMapper.selectOne(updateWrapper); // 需要更新的数据
            // 不为默认值则更新数据
            if (!contract.getContractAmount().equals(BigDecimal.valueOf(0.0))) {
                updateContract.setContractAmount(contract.getContractAmount());
            }
            // 更新发放贷款
            if (!contract.getLoanAmount().equals(BigDecimal.valueOf(0.0))){
                BigDecimal newLoanAmount = updateContract.getLoanAmount().add(contract.getLoanAmount());
                updateContract.setLoanAmount(newLoanAmount.min(updateContract.getContractAmount()));
            }

            if (contract.getLoanTerm() != contract.getLoanTerm())
                updateContract.setLoanTerm(contract.getLoanTerm());
            if (contract.getApplyDate() != contract.getApplyDate())
                updateContract.setApplyDate(contract.getApplyDate());
            if (updateContract.getApproveStatus() != contract.getApproveStatus() && contract.getApproveStatus() != -1)
                updateContract.setApproveStatus(contract.getApproveStatus());
            if (contract.getRemark() != "")
                updateContract.setRemark(contract.getRemark());
            if (contract.getApproveId() != updateContract.getApproveId())
                updateContract.setApproveId(updateContract.getApproveId());
            // 更新时间
            updateContract.setUpdateTime(contract.getUpdateTime());
            // 更新数据库
            baseMapper.update(updateContract, updateWrapper);
            return false;
        }
        // 插入操作
        List<Contract> contractList = getAllContract();
        // 若数据库中没有数据时，默认id为1
        if (contractList.isEmpty()) {
            contract.setContractNo(1L);
        } else {
            Long maxId = contractList.stream().mapToLong(Contract::getContractNo).max().orElseThrow(NoSuchElementException::new);
            contract.setContractNo(maxId + 1);
        }
        baseMapper.insert(contract);
        return true;
    }

    @Override
    public List<Contract> getAllContract() {
        QueryWrapper<Contract> queryWrapper = new QueryWrapper<>();

        return baseMapper.selectList(queryWrapper);
    }

    // 按照contractNo删除
    @Override
    public boolean deleteContractByContractNo(Long contractNo) {
        QueryWrapper<Contract> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("contract_no", contractNo);

        return baseMapper.delete(queryWrapper) > 0;
    }
}
