package com.dafuweng.controller;


import com.dafuweng.entity.Product;
import com.dafuweng.entity.SalesmanRepresentativeCustomer;
import com.dafuweng.service.ProductService;
import com.dafuweng.util.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author You.p.j
 * @since 2023-03-18
 */
@RestController
@RequestMapping("/api/product")
public class ProductController {
    @Resource
    public ProductService productService;


    @GetMapping("/getProductsByUserId/{id}")
    public Result getProductsByUserId(@PathVariable("id") Long id) {
        List<Product> productList = productService.getProductsByUserId(id);

        return Result.ok(productList).message("查询成功！");
    }

    @GetMapping("/getWeekOrMonthProductByUserId/{id}")
    public Result getWeekOrMonthProductByUserId(@PathVariable("id") Long id, boolean isWeek) {
        List<Product> productList = productService.getWeekOrMonthProductsByUserId(id, isWeek);

        return Result.ok(productList).message("查询成功！");
    }

    @GetMapping("/getTotalProductByUserId/{id}")
    public Result getTotalProductByUserId(@PathVariable("id") Long id) {
        List<Product> productList = productService.getProductsByUserId(id);
        productList = productService.mergeSameProductIdInSameSalmesmanId(productList, id);
        System.out.println(productList);
        return Result.ok(productList).message("查询成功！");
    }

    /* -----------------------------------------------------------
     *                        按月寻找产品销量
     *                      用于前端首页界面折线图数据
     * -----------------------------------------------------------
     */
    @GetMapping("/getProductBasedThisMonthByUserId/{id}")
    public Result getProductBasedThisMonthByUserId(@PathVariable("id") Long id, int monthSteps) {
        List<Product> productList = productService.getProductBasedThisMonthByUserId(id, monthSteps);

        return Result.ok(productList).message("查询成功！");
    }

    @GetMapping("/getLocalDateList")
    public Result getLocalDateList(int monthSteps) {
        List<LocalDate> localDateList = productService.getLocalDateList(monthSteps);
        return Result.ok(localDateList).message("成功！");
    }
}

