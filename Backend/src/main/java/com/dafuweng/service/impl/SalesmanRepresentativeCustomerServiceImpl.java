package com.dafuweng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dafuweng.dao.SalesmanRepresentativeCustomerMapper;
import com.dafuweng.entity.SalesmanRepresentativeCustomer;
import com.dafuweng.service.SalesmanRepresentativeCustomerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * <p>
 * 销售员代表客户表 服务实现类
 * </p>
 *
 * @author You.p.j
 * @since 2023-03-14
 */
@Service
@Transactional
public class SalesmanRepresentativeCustomerServiceImpl extends ServiceImpl<SalesmanRepresentativeCustomerMapper, SalesmanRepresentativeCustomer> implements SalesmanRepresentativeCustomerService {

    /**
     * 插入映射表
     *
     * @param salesmanRepresentativeCustomer
     * @return
     */
    @Override
    public boolean insertSalesmanRepresentativeCustomer(SalesmanRepresentativeCustomer salesmanRepresentativeCustomer) {
        QueryWrapper<SalesmanRepresentativeCustomer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", salesmanRepresentativeCustomer.getId());

        if (baseMapper.selectOne(queryWrapper) != null) {
            return false;
        }
        baseMapper.insert(salesmanRepresentativeCustomer);
        return true;
    }

    /**
     * 插入或更新映射表
     *
     * @param
     * @return
     */
    @Override
    public boolean insertOrUpdateSalesmanRepresentativeCustomer(SalesmanRepresentativeCustomer salesmanRepresentativeCustomer) {
        QueryWrapper<SalesmanRepresentativeCustomer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("contract_no", salesmanRepresentativeCustomer.getContractNo());

        // 更新操作
        if (baseMapper.selectOne(queryWrapper) != null) {
            UpdateWrapper<SalesmanRepresentativeCustomer> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("contract_no", salesmanRepresentativeCustomer.getContractNo());
            SalesmanRepresentativeCustomer updateSalesmanRepresentativeCustomer = baseMapper.selectOne(updateWrapper); // 需要更新的数据
            // 不为默认值则更新数据
            if (updateSalesmanRepresentativeCustomer.getSalesmanId() != salesmanRepresentativeCustomer.getSalesmanId() && salesmanRepresentativeCustomer.getCustomerId() != -1L)
                updateSalesmanRepresentativeCustomer.setSalesmanId(salesmanRepresentativeCustomer.getSalesmanId());
            // 更新时间
            updateSalesmanRepresentativeCustomer.setUpdateTime(salesmanRepresentativeCustomer.getUpdateTime());
            // 更新数据库
            baseMapper.update(updateSalesmanRepresentativeCustomer, updateWrapper);
            return false;
        }
        // 插入操作
        List<SalesmanRepresentativeCustomer> salesmanRepresentativeCustomerList = getAllSalesmanRepresentativeCustomer();
        // 若数据库中没有数据时，默认id为1
        if (salesmanRepresentativeCustomerList.isEmpty()) {
            salesmanRepresentativeCustomer.setId(1L);
        } else {
            Long maxId = salesmanRepresentativeCustomerList.stream().mapToLong(SalesmanRepresentativeCustomer::getId).max().orElseThrow(NoSuchElementException::new);
            salesmanRepresentativeCustomer.setId(maxId + 1);
        }
        baseMapper.insert(salesmanRepresentativeCustomer);
        return true;
    }

    // 根据客户号返回合同号
    @Override
    public List<Long> getContractNoByCustomerId(Long id) {
        QueryWrapper<SalesmanRepresentativeCustomer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("customer_id", id);
        List<SalesmanRepresentativeCustomer> salesmanRepresentativeCustomerList = baseMapper.selectList(queryWrapper);

        if (!salesmanRepresentativeCustomerList.isEmpty()) {
            List<Long> contractNoList = new ArrayList<Long>(salesmanRepresentativeCustomerList.size());

            for (SalesmanRepresentativeCustomer salesmanRepresentativeCustomer : salesmanRepresentativeCustomerList) {
                contractNoList.add(salesmanRepresentativeCustomer.getContractNo());
            }

            return contractNoList;
        }

        return new ArrayList<>(0);
    }

    /**
     * 获取所有映射
     *
     * @return
     */
    @Override
    public List<SalesmanRepresentativeCustomer> getAllSalesmanRepresentativeCustomer() {
        QueryWrapper<SalesmanRepresentativeCustomer> queryWrapper = new QueryWrapper<>();

        return baseMapper.selectList(queryWrapper);
    }

    /**
     * 按照销售代表id查询其销售客户映射表
     *
     * @param salesman_id 销售代表id
     * @return
     */
    @Override
    public List<SalesmanRepresentativeCustomer> getAllSalesmanCustomerMapperBySalesmanID(Long salesman_id) {
        return baseMapper.getAllSalesmanCustomerMapperBySalesmanId(salesman_id);
    }

    /**
     * 根据销售代表id获取过去一个周内所有客户映射表
     *
     * @param salesman_id 销售代表id
     * @return
     */
    @Override
    public List<SalesmanRepresentativeCustomer> getWeekSalesmanCustomerMapperBySalesmanId(Long salesman_id) {
        List<SalesmanRepresentativeCustomer> salesmanRepresentativeCustomerList = getAllSalesmanCustomerMapperBySalesmanID(salesman_id);

        // 选择过去一周内的映射表
        Date currentDate = new Date();
        List<SalesmanRepresentativeCustomer> accessList = salesmanRepresentativeCustomerList.
                stream().
                filter(salesmanRepresentativeCustomer -> (currentDate.getTime() - salesmanRepresentativeCustomer.getCreateTime().getTime()) < 1000 * 60 * 60 * 24 * 7)
                .collect(Collectors.toList());

        return accessList;
    }

    /**
     * 根据销售代表id获取过去一个月内所有客户映射表
     *
     * @param salesman_id 销售代表id
     * @return
     */
    @Override
    public List<SalesmanRepresentativeCustomer> getMonthSalesmanCustomerMapperBySalesmanId(Long salesman_id) {
        List<SalesmanRepresentativeCustomer> salesmanRepresentativeCustomerList = getAllSalesmanCustomerMapperBySalesmanID(salesman_id);

        // 选择过去一周内的映射表
        Date currentDate = new Date();
        List<SalesmanRepresentativeCustomer> accessList = salesmanRepresentativeCustomerList.
                stream().
                filter(salesmanRepresentativeCustomer -> (currentDate.getTime() - salesmanRepresentativeCustomer.getCreateTime().getTime()) < 1000 * 60 * 60 * 24 * 30)
                .collect(Collectors.toList());

        return accessList;
    }

    // 寻找合同号的映射
    @Override
    public SalesmanRepresentativeCustomer getSalesmanRepresentativeCustomerByContractNo(Long contractNo) {
        QueryWrapper<SalesmanRepresentativeCustomer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("contract_no", contractNo);

        return baseMapper.selectOne(queryWrapper);
    }

    // 按合同号删除映射表
    @Override
    public boolean deleteSalesmanRepresentativeCutsomerByContractNo(Long contractNo) {
        QueryWrapper<SalesmanRepresentativeCustomer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("contract_no", contractNo);

        return baseMapper.delete(queryWrapper) > 0;
    }
}
