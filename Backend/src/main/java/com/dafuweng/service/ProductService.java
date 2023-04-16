package com.dafuweng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dafuweng.entity.Product;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author You.p.j
 * @since 2023-03-18
 */
public interface ProductService extends IService<Product> {
    public List<Product> getProductsByUserId(Long id); // 按销售代表id获取销售代表的销量

    public List<Product> mergeSameProductIdInSameSalmesmanId(List<Product> productList, long id);// 合并编号为id的销售代表的相同产品数量

    public List<Product> getWeekOrMonthProductsByUserId(Long id, boolean isWeek); // 按周或月获取销售代表的产品销量

    public List<Product> getProductBasedThisMonthByUserId(Long id, int monthSteps); // 按用户Id获取某月的产品效率，monthSteps表示距离本月第一天时间

    public List<LocalDate> getLocalDateList(int monthSteps); // 返回距离当前月份monthSteps内的日期


}
