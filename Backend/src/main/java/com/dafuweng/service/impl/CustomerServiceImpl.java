package com.dafuweng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dafuweng.dao.CustomerMapper;
import com.dafuweng.entity.Contract;
import com.dafuweng.entity.Customer;
import com.dafuweng.service.CustomerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * <p>
 * 客户表 服务实现类
 * </p>
 *
 * @author You.p.j
 * @since 2023-03-14
 */
@Service
@Transactional
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements CustomerService {
    /**
     * 获取id号销售代表的客户
     *
     * @param id 销售代表id
     * @return
     */
    @Override
    public List<Customer> getCustomerBySalesmanId(Long id) {
        QueryWrapper<Customer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("salesman_id", id);

        return baseMapper.selectList(queryWrapper);
    }

    /**
     * 获取所有customer
     *
     * @return
     */
    @Override
    public List<Customer> getAllCustomer() {
        QueryWrapper<Customer> queryWrapper = new QueryWrapper<>();

        return baseMapper.selectList(queryWrapper);
    }

    /**
     * 按id返回customer的信息
     *
     * @return
     */
    @Override
    public Customer getCustomerByCustomerID(Long id) {
        QueryWrapper<Customer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", id);

        return baseMapper.selectOne(queryWrapper);
    }

    /**
     * 按客户姓名查询
     *
     * @param name
     * @return
     */
    @Override
    public List<Customer> getCustomerByName(String name) {
        QueryWrapper<Customer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", name);

        return baseMapper.selectList(queryWrapper);
    }

    @Override
    public boolean insertCustomer(Customer insertCustomer) {
        QueryWrapper<Customer> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("id", insertCustomer.getId());

        List<Customer> customerList = getAllCustomer();
        // 若数据库中没有数据时，默认id为1
        if (customerList.isEmpty()) {
            insertCustomer.setId(1L);
        } else {
            Long maxId = customerList.stream().mapToLong(Customer::getId).max().orElseThrow(NoSuchElementException::new);
            insertCustomer.setId(maxId + 1);
        }

        // 判断是否系统中已经存在相同的id号
        if (baseMapper.selectOne(queryWrapper) != null) {
            return false;
        }
        baseMapper.insert(insertCustomer); // 添加到数据库中
        return true;
    }

    /**
     * 按客户id删除客户
     *
     * @param customerId
     * @return
     */
    @Override
    public boolean deleteCustomer(Long customerId) {
        return baseMapper.deleteById(customerId) > 0;
    }

    /**
     * 按客户id更新客户
     *
     * @param customerId
     * @return
     */
    @Override
    public boolean updateCustomer(Long customerId, Customer updatedCustomer) {
        UpdateWrapper<Customer> updateWrapper = new UpdateWrapper<Customer>();
        updateWrapper.eq("id", customerId);

        Customer updateCustomer = baseMapper.selectOne(updateWrapper);
        // 把updatedUser中不为null的属性更新到updateWrapper中按id查找的数据中
        if (updateCustomer != null) {
            // 判断需要不为默认值的属性修改
            if (updatedCustomer.getId() != -1) updateCustomer.setId(updatedCustomer.getId());
            if (updatedCustomer.getName() != "") updateCustomer.setName(updatedCustomer.getName());
            if (updatedCustomer.getIdCard() != "") updateCustomer.setIdCard(updatedCustomer.getIdCard());
            if (updatedCustomer.getCompanyName() != "") updateCustomer.setCompanyName(updatedCustomer.getCompanyName());
            if (updatedCustomer.getBusinessLicense() != "") updateCustomer.setBusinessLicense(updatedCustomer.getBusinessLicense());
            if (updatedCustomer.getIntensionScore() != 0) updateCustomer.setIntensionScore(updatedCustomer.getIntensionScore());
            if (updatedCustomer.getSalesmanId() != -1) updateCustomer.setSalesmanId(updatedCustomer.getSalesmanId());
//            updateCustomer.setCreateTime(updatedCustomer.getCreateTime());
            updateCustomer.setUpdateTime(updatedCustomer.getUpdateTime());


            baseMapper.update(updateCustomer, updateWrapper);

            return true;
        }

        return false;

    }

    /**
     * 按用户Id获取某月的客户数，monthSteps表示距离本月第一天时间
     *
     * @param id
     * @param monthSteps
     * @return
     */
    @Override
    public List<Customer> getCustomerBasedThisMonthByUserId(Long id, int monthSteps) {
        LocalDate thisMonth = LocalDate.now().minusMonths(monthSteps); // 需要定位的年月日
        LocalDate nextMonth = thisMonth.plusMonths(1); // 需要定位的的下一个月
        LocalDate firstDay = LocalDate.of(thisMonth.getYear(), thisMonth.getMonth(), 1); // 本月的第一天
        LocalDate lastDay = LocalDate.of(nextMonth.getYear(), nextMonth.getMonth(), 1); // 下个月的第一天

        Date beginDate = Date.from(firstDay.atStartOfDay(ZoneId.systemDefault()).toInstant()); // 本月的第一天零点
        Date endDate = Date.from(lastDay.atStartOfDay(ZoneId.systemDefault()).toInstant()); // 下个月的第一天零点;

        List<Customer> customerList = getCustomerBySalesmanId(id); // 按id获取所有产品记录

        List<Customer> filterList = customerList.stream()
                .filter(customer -> (customer.getUpdateTime().getTime() >= beginDate.getTime() && customer.getUpdateTime().getTime() < endDate.getTime()))
                .collect(Collectors.toList());

        return filterList;
    }

    /**
     * 计算List中的用户数量和活跃用户数量
     *
     * @param customerList
     * @return
     */
    @Override
    public Bar calculateCustomerNum(List<Customer> customerList) {
        Bar bar = new Bar();

        for (Customer customer : customerList) {
            bar.customerNum++;
            bar.activeCustomerNum = customer.getIntensionScore() > 60 ? bar.activeCustomerNum + 1 : bar.activeCustomerNum;
        }

        return bar;
    }

//    // 查询相同用户所办理的所有合同
//    @Override
//    public List<Contract> getContractListByCustomerId(Long customerId) {
//        QueryWrapper<Contract> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq()
//    }
}
