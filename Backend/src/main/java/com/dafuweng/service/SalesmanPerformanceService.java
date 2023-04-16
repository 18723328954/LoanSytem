package com.dafuweng.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dafuweng.entity.SalesmanPerformance;

/**
 * <p>
 * 销售员绩效表 服务类
 * </p>
 *
 * @author You.p.j
 * @since 2023-03-14
 */
public interface SalesmanPerformanceService extends IService<SalesmanPerformance> {
    /**
     * 根据销售代表id查询销售业绩
     *
     * @param saleman_id
     * @return
     */
    public SalesmanPerformance getPerformanceBySalemanId(Long saleman_id);

    public boolean insertOrUpdatePerformance(SalesmanPerformance salesmanPerformance); // 新增或更新

    public boolean deletePerformanceBySalesmanId(Long salesmanId);
}
