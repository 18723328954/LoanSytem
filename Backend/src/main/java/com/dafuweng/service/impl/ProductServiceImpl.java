package com.dafuweng.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dafuweng.dao.ProductMapper;
import com.dafuweng.entity.Product;
import com.dafuweng.service.ProductService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author You.p.j
 * @since 2023-03-18
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {
    @Override
    public List<Product> mergeSameProductIdInSameSalmesmanId(List<Product> productList, long id) {
        List<Product> resList = new ArrayList<>();

        // 利用map将accessList中相同的productId的productNum相加
        Map<Integer, Long> productMap = new TreeMap<>();
        for (Product product : productList) {
            int productId = product.getProductId();
            Long productNum = product.getProductNum();
            // 已经存在productId
            if (productMap.containsKey(productId)) {
                productMap.put(productId, productMap.get(productId) + productNum);
            } else {
                productMap.put(productId, productNum); // 不存在productId
            }
        }
        // 将map的key和value添加入新的list
        for (Map.Entry<Integer, Long> entry : productMap.entrySet()) {
            Product addProduct = new Product(id, entry.getKey(), entry.getValue());
            resList.add(addProduct);
        }

        return resList;
    }

    @Override
    public List<Product> getProductsByUserId(Long id) {
        QueryWrapper<Product> queryWrapper = new QueryWrapper<Product>();
        queryWrapper.eq("salesman_id", id);
        queryWrapper.orderBy(true, true, "product_id");

        return baseMapper.selectList(queryWrapper);
    }

    /**
     * 按照销售代表数量获取一周内或一月内的Product
     *
     * @param id     salesman_id
     * @param isWeek true为返回一周，false为返回一个月
     * @return
     */
    @Override
    public List<Product> getWeekOrMonthProductsByUserId(Long id, boolean isWeek) {
        List<Product> resList = new ArrayList<Product>(); // 返回数组
        List<Product> productList = getProductsByUserId(id); // 销售id对应的产品

        // 选择过去一周内的映射表
        Date currentDate = new Date();
        long duration = isWeek ? 1000L * 60 * 60 * 24 * 7 : 1000L * 60 * 60 * 24 * 30;

        List<Product> accessList = productList
                .stream()
                .filter(product -> (currentDate.getTime() - product.getCreateTime().getTime()) < duration)
                .collect(Collectors.toList());

        // 利用map将accessList中相同的productId的productNum相加
        resList = mergeSameProductIdInSameSalmesmanId(accessList, id);

        return resList;
    }

    /* -----------------------------------------------------------
     *                        按月寻找产品销量
     *                      用于前端首页界面折线图数据
     * -----------------------------------------------------------
     */

    /**
     * 按用户Id获取某月的产品效率，monthSteps表示距离本月第一天时间
     *
     * @param id         salesman_id
     * @param monthSteps 距离本月的月份数
     * @return
     */
    @Override
    public List<Product> getProductBasedThisMonthByUserId(Long id, int monthSteps) {
        // 获取现在的年月，本月的意思是距离当前monthSteps的月份
        LocalDate thisMonth = LocalDate.now().minusMonths(monthSteps); // 需要定位的年月日
        LocalDate nextMonth = thisMonth.plusMonths(1); // 需要定位的的下一个月
        LocalDate firstDay = LocalDate.of(thisMonth.getYear(), thisMonth.getMonth(), 1); // 本月的第一天
        LocalDate lastDay = LocalDate.of(nextMonth.getYear(), nextMonth.getMonth(), 1); // 下个月的第一天

        Date beginDate = Date.from(firstDay.atStartOfDay(ZoneId.systemDefault()).toInstant()); // 本月的第一天零点
        Date endDate = Date.from(lastDay.atStartOfDay(ZoneId.systemDefault()).toInstant()); // 下个月的第一天零点;

        List<Product> productList = getProductsByUserId(id); // 按id获取所有产品记录

        List<Product> filterList = productList.stream()
                .filter(product -> (product.getCreateTime().getTime() >= beginDate.getTime() && product.getCreateTime().getTime() < endDate.getTime()))
                .collect(Collectors.toList());

        List<Product> resList = mergeSameProductIdInSameSalmesmanId(filterList, id); // 按productId整合product数据
        // 处理返回为空或者返回的productId种类不是6种的情况
        if (resList.isEmpty()) {
            for (int i = 1; i <= 6; i++) {
                Product tmpProduct = new Product(id, i, 0L);
                resList.add(tmpProduct);
            }
        } else {
            List<Integer> productIdList = new ArrayList<>();

            for (Product product : resList) {
                productIdList.add(product.getProductId());
            }

            // 查找缺失的productId
            for (int i = 1; i <= 6; i++) {
                if (!productIdList.contains(i)) {
                    Product tmpProduct = new Product(id, i, 0L);
                    resList.add(tmpProduct);
                }
            }
        }

        // 按照productId排序
        Collections.sort(resList, new Comparator<Product>() {
            @Override
            public int compare(Product o1, Product o2) {
                return o1.getProductId() - o2.getProductId();
            }
        });

        return resList;
    }

    /**
     * 返回距离当前月份monthSteps内的日期
     *
     * @param monthSteps 与本月的距离
     * @return
     */
    @Override
    public List<LocalDate> getLocalDateList(int monthSteps) {
        List<LocalDate> localDateList = new ArrayList<>();
        LocalDate now = LocalDate.now();

        for (int i = 0; i < monthSteps; i++) {
            localDateList.add(now.minusMonths(i));
        }

        return localDateList;
    }
}
