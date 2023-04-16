package com.dafuweng.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dafuweng.entity.SalesmanRepresentativeCustomer;

import java.util.List;

/**
 * <p>
 * 销售员代表客户表 Mapper 接口
 * </p>
 *
 * @author You.p.j
 * @since 2023-03-14
 */
public interface SalesmanRepresentativeCustomerMapper extends BaseMapper<SalesmanRepresentativeCustomer> {
    /**
     * 根据销售代表id查询销售代表和用户的映射表
     *
     * @param salesman_id
     * @return
     */
    public List<SalesmanRepresentativeCustomer> getAllSalesmanCustomerMapperBySalesmanId(Long salesman_id);
}
