package com.dafuweng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dafuweng.dao.SalesmanPerformanceMapper;
import com.dafuweng.entity.SalesmanPerformance;
import com.dafuweng.service.SalesmanPerformanceService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

/**
 * <p>
 * 销售员绩效表 服务实现类
 * </p>
 *
 * @author You.p.j
 * @since 2023-03-14
 */
@Service
@Transactional
public class SalesmanPerformanceServiceImpl extends ServiceImpl<SalesmanPerformanceMapper, SalesmanPerformance> implements SalesmanPerformanceService {
    /**
     * 根据销售代表id查询销售业绩
     *
     * @param saleman_id
     * @return
     */
    @Override
    public SalesmanPerformance getPerformanceBySalemanId(Long saleman_id) {
        QueryWrapper<SalesmanPerformance> queryWrapper = new QueryWrapper<SalesmanPerformance>(); // 条件筛选器
        queryWrapper.eq("salesman_id", saleman_id); // salesman_id == saleman_id

        return baseMapper.selectOne(queryWrapper);
    }

    /**
     * 新增或更新
     *
     * @param salesmanPerformance
     * @return
     */
    @Override
    public boolean insertOrUpdatePerformance(SalesmanPerformance salesmanPerformance) {
        QueryWrapper<SalesmanPerformance> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("salesman_id", salesmanPerformance.getSalesmanId());

        // 更新操作
        if (baseMapper.selectOne(queryWrapper) != null) {
            UpdateWrapper<SalesmanPerformance> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("salesman_id", salesmanPerformance.getSalesmanId());
            SalesmanPerformance updateSalesmanPerformance = baseMapper.selectOne(updateWrapper); // 需要更新的数据
            // 不为默认值则更新数据
            if (salesmanPerformance.getContractAmount() != BigDecimal.valueOf(0)) {
                // 同时更新有效电话数和签约客户数
                updateSalesmanPerformance.setCallTimes(updateSalesmanPerformance.getCallTimes() + 1);
                updateSalesmanPerformance.setContractAmount(updateSalesmanPerformance.getContractAmount().add(salesmanPerformance.getContractAmount()));
                updateSalesmanPerformance.setValidCalls(updateSalesmanPerformance.getValidCalls() + 1);
                updateSalesmanPerformance.setContractCustomers(updateSalesmanPerformance.getContractCustomers() + 1);
            }
            // 更新放款金额
            if (salesmanPerformance.getLoanAmount() != BigDecimal.valueOf(0)) {
                BigDecimal newLoanAmount = salesmanPerformance.getLoanAmount().add(updateSalesmanPerformance.getLoanAmount());
                updateSalesmanPerformance.setLoanAmount(newLoanAmount.min(updateSalesmanPerformance.getContractAmount()));
            }
            // 更新服务费
            if (salesmanPerformance.getServiceFee() != BigDecimal.valueOf(0)){
                BigDecimal newSeriveceFee = salesmanPerformance.getServiceFee().add(updateSalesmanPerformance.getServiceFee());
                updateSalesmanPerformance.setServiceFee(newSeriveceFee);
            }
            if (salesmanPerformance.getCallTimes() != 0)
                updateSalesmanPerformance.setCallTimes(salesmanPerformance.getCallTimes() + updateSalesmanPerformance.getCallTimes());
            if (salesmanPerformance.getIntentionCustomers() != 0)
                updateSalesmanPerformance.setIntentionCustomers(salesmanPerformance.getIntentionCustomers() + updateSalesmanPerformance.getIntentionCustomers());
            if (salesmanPerformance.getInterviewCustomers() != 0)
                updateSalesmanPerformance.setInterviewCustomers(updateSalesmanPerformance.getInterviewCustomers() + salesmanPerformance.getInterviewCustomers());
            // 更新时间
            updateSalesmanPerformance.setUpdateTime(salesmanPerformance.getUpdateTime());
            // 更新数据库
            baseMapper.update(updateSalesmanPerformance, updateWrapper);
            return false;
        }
        // 插入操作
        baseMapper.insert(salesmanPerformance);
        return true;
    }

    @Override
    public boolean deletePerformanceBySalesmanId(Long salesmanId) {
        QueryWrapper<SalesmanPerformance> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("salesman_id", salesmanId);

        return baseMapper.delete(queryWrapper) > 0;
    }
}
